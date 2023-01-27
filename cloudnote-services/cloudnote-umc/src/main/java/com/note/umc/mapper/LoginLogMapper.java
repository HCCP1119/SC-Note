package com.note.umc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.note.umc.entity.UserLoginLogs;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface LoginLogMapper extends BaseMapper<UserLoginLogs> {

}
