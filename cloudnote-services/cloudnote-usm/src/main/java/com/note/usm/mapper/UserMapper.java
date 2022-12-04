package com.note.usm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.note.api.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * UserMapper
 *
 * @date 2022/11/29 22:12
 **/
@Mapper
public interface UserMapper extends BaseMapper<SysUser> {
    SysUser findByUsername(String username);
}
