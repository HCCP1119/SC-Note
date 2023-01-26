package com.note.umc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.note.api.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * UserMapper
 *
 * @date 2022/11/29 22:12
 **/
@Mapper
public interface UserMapper extends BaseMapper<SysUser> {
    SysUser findByUsernameOrEmail(String username,String email);

    SysUser findById(Long uid);

    Boolean hasEmail(String email,String username);

    @Update("update sys_user set nickname=#{nickname} where id=#{id}")
    void setNickname(String nickname,Long id);

    @Update("update sys_user set password=#{password} where id=#{id}")
    void resetPassword(String password,Long id);
}
