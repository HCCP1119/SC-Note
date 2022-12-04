package com.note.usm.security;


import com.note.api.entity.SysRole;
import com.note.api.entity.SysUser;
import com.note.usm.mapper.UserMapper;
import com.note.web.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @date 2022/11/29 23:05
 **/
@RequiredArgsConstructor
public class UserDetailManager implements UserDetailsService {

    private final UserMapper userMapper;
    private final RedisUtils redisServer;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = userMapper.findByUsername(username);
        if (user==null){
            throw new UsernameNotFoundException("用户不存在");
        }
        Set<SysRole> roles = user.getRoles();
        List<String> roleList = roles.stream().map(SysRole::getName).collect(Collectors.toList());
        List<String> permissions = roles.stream().flatMap(sysRole -> sysRole.getPermissions().stream()).distinct().collect(Collectors.toList());

        if(redisServer.hasKey(username+RedisUtils.ROLE_SUF)){
            redisServer.deleteObject(username+RedisUtils.ROLE_SUF);
        }
        if(redisServer.hasKey(username+RedisUtils.PERMISSION_SUF)){
            redisServer.deleteObject(username+RedisUtils.PERMISSION_SUF);
        }

        redisServer.setList(username+RedisUtils.ROLE_SUF,roleList);
        redisServer.setList(username+RedisUtils.PERMISSION_SUF,permissions);
        return new com.note.web.details.UserDetails(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getStatus(),
                roleList,
                permissions
        );
    }
}
