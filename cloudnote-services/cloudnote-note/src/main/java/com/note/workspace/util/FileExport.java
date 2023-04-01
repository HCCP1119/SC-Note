package com.note.workspace.util;


import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;


import java.nio.charset.StandardCharsets;

/**
 * 笔记导出
 *
 * @date 2023/4/1 17:04
 **/
public class FileExport {

    public static ResponseEntity<byte[]> html2md(String title,String html) {
        String md = FlexmarkHtmlConverter.builder().build().convert(html).replaceAll("<br />","");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", title + ".md");
        return new ResponseEntity<>(md.getBytes(StandardCharsets.UTF_8), headers, HttpStatus.OK);
    }
}
