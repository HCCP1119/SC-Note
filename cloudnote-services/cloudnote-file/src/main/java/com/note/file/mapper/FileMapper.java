package com.note.file.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.note.file.entity.File;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FileMapper extends BaseMapper<File> {

}
