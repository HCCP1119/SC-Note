package com.note.usm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.note.api.entity.SysUser;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.ibatis.annotations.Mapper;

/**
 * UserMapper
 *
 * @date 2022/11/29 22:12
 **/
@Mapper
public interface UserMapper extends BaseMapper<SysUser> {
    SysUser findByUsernameOrEmail(String username,String email);

    Boolean hasEmail(String email,String username);
}
