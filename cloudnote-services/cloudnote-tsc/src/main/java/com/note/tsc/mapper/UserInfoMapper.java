package com.note.tsc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.note.tsc.entity.SysUserView;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserInfoMapper extends BaseMapper<SysUserView> {

    @Update("update user_info set introduce=#{introduce} where id=#{id}")
    void setIntroduce(String introduce,Long id);
}
