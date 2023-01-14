package com.note.file.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 *
 * @date 2023/1/13 20:03
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemoveImg {
    private List<String> removeImgList;
}
