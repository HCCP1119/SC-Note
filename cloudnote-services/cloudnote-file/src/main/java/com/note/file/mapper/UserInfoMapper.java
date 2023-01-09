package com.note.file.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserInfoMapper {
    @Update("update user_info set headImage=#{headImage},disk=#{disk} where id=#{id}")
    void setHeadImg(String headImage,String disk,Long id);

    @Select("select disk from user_info where id=#{id}")
    String getImgDisk(Long id);
}
