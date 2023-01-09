package com.note.file.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.note.file.entity.File;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileMapper extends BaseMapper<File> {
}
