package com.note.umc.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.session.TokenSign;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.note.api.entity.SysUser;
import com.note.api.result.R;
import com.note.umc.entity.*;
import com.note.umc.feign.CodeMailService;
import com.note.umc.mapper.LoginLogMapper;
import com.note.umc.mapper.RoleMapper;
import com.note.umc.mapper.UserInfoMapper;
import com.note.umc.mapper.UserMapper;
import com.note.web.utils.RedisUtils;
import com.note.web.utils.SaTokenUtils;
import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.RequiredArgsConstructor;
import net.dreamlu.mica.ip2region.core.Ip2regionSearcher;
import net.dreamlu.mica.ip2region.core.IpInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 验证控制器
 *
 * @date 2023/1/23 15:59
 **/
@RestController
@CrossOrigin
@RequiredArgsConstructor
public class AuthController {
    private final UserMapper userMapper;
    private final UserInfoMapper userInfo;
    private final RoleMapper roleMapper;
    private final LoginLogMapper loginLogMapper;
    private final RedisUtils redisServer;
    private final CodeMailService codeMailService;
    private final Ip2regionSearcher ip2regionSearcher;

    @PostMapping("login")
    public R<?> login(@RequestBody LoginBody loginUser) {
        SysUser user = userMapper.findByUsernameOrEmail(loginUser.getUsername(), loginUser.getUsername());
        if (user == null) {
            return R.fail("用户名不存在");
        }
        if (BCrypt.checkpw(loginUser.getPassword(), user.getPassword())) {
            String header = SaHolder.getRequest().getHeader("User-Agent");
            String ip = SaHolder.getRequest().getHeader("Host").substring(0, (SaHolder.getRequest().getHeader("Host")).lastIndexOf(":"));
            String address = Objects.requireNonNull(ip2regionSearcher.memorySearch(ip)).getAddress();
            UserAgent userAgent = UserAgent.parseUserAgentString(header);
            String type = userAgent.getBrowser().getName().substring(0, (userAgent.getBrowser().getName()).lastIndexOf(" "));
            String system = userAgent.getOperatingSystem().getName().substring(0, (userAgent.getOperatingSystem().getName()).lastIndexOf(" "));
            DeviceType device = userAgent.getOperatingSystem().getDeviceType();
            StpUtil.login(user.getId());
            SaTokenUtils.setLoginUser(user);
            String token = StpUtil.getTokenValue();
            String tokenSuf = token.substring(token.lastIndexOf(".") + 1);
            UserLoginLogs log = new UserLoginLogs(
                    null,
                    tokenSuf,
                    new Date(StpUtil.getTokenSession().getCreateTime()),
                    DeviceType.COMPUTER.equals(device) ? "PC" : "PHONE",
                    system,
                    type,
                    ip,
                    address,
                    1,
                    SaTokenUtils.getLoginUserId()
            );
            loginLogMapper.insert(log);
            SaHolder.getResponse().addHeader("Authorization", token);
            SaHolder.getResponse().addHeader("Access-Control-Expose-Headers", "Authorization");
            return R.ok(user.getId(), "登录成功");
        }
        return R.fail("用户名或密码错误");
    }

    @PostMapping("/register/{email}")
    public R<?> sendRegisterCode(@PathVariable("email") final String email, final HttpServletRequest request) {
        if (redisServer.hasKey(request.getRemoteAddr() + "_COUNT")) {
            Integer count = redisServer.getObject(request.getRemoteAddr() + "_COUNT");
            if (count > 5) {
                return R.fail("发送频繁，请稍后再试");
            }
        }
        Boolean isSend = codeMailService.sendCode(email, request.getRemoteAddr());
        if (isSend) {
            return R.ok("发送成功");
        }
        return R.fail("发送失败，请稍后再试");
    }

    @PostMapping("register")
    public R<?> apiRegister(@RequestBody final RegisterBody user) {
        String verifyCode = redisServer.getObject(user.getEmail() + RedisUtils.MAILCODE_SUF);
        if (user.getAuthCode().isEmpty() || !user.getAuthCode().equals(verifyCode)) {
            return R.fail("无效验证码");
        }
        if (userMapper.hasEmail(user.getEmail(), user.getUsername())) {
            return R.fail("该邮箱或用户名已注册");
        }
        final SysUser sysUser = new SysUser(
                user.getEmail(),
                user.getNickname(),
                user.getUsername(),
                BCrypt.hashpw(user.getPassword())
        );
        userMapper.insert(sysUser);
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.select("id").eq("username", user.getUsername());
        roleMapper.registerUserRole(userMapper.selectOne(wrapper).getId());
        SysUserView userView = new SysUserView(userMapper.selectOne(wrapper).getId(),null,null);
        userInfo.insert(userView);
        return R.ok("注册成功");
    }

    @PostMapping("logout")
    public R<?> logout() {
        StpUtil.logout();
        return R.ok("退出成功");
    }

    @GetMapping("/register/verify")
    public R<?> verify(@RequestParam String account) {
        SysUser user = userMapper.findByUsernameOrEmail(account, account);
        if (user != null) {
            return R.ok(user.getEmail(), "success");
        }
        return R.fail("用户不存在");
    }

    @PostMapping("/register/reset")
    public R<?> reset(@RequestBody ResetPasswordBody formBody) {
        String verifyCode = redisServer.getObject(formBody.getEmail() + RedisUtils.MAILCODE_SUF);
        if (formBody.getAuthCode().isEmpty() || !formBody.getAuthCode().equals(verifyCode)) {
            return R.fail("验证码错误");
        }
        UpdateWrapper<SysUser> wrapper = new UpdateWrapper<>();
        wrapper.eq("email", formBody.getEmail()).set("password", BCrypt.hashpw(formBody.getNewPassword()));
        userMapper.update(null, wrapper);
        return R.ok("success");
    }
}
