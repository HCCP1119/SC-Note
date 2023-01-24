package com.note.umc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.note.api.entity.SysRole;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoleMapper extends BaseMapper<SysRole> {
    @Insert("insert into user_role(user_id, role_id) VALUES (#{uid},2)")
    Boolean registerUserRole(final Long uid);
}
