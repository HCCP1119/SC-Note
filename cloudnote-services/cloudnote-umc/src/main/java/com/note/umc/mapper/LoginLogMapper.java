package com.note.umc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.note.umc.entity.UserLoginLogs;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LoginLogMapper extends BaseMapper<UserLoginLogs> {

    @Select("select count(user_login_logs.ip=#{ip} or null) from user_login_logs")
    Boolean checkIp(String ip);
}
