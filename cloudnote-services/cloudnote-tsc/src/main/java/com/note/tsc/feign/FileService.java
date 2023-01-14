package com.note.tsc.feign;

import com.note.api.result.R;
import javafx.scene.media.Media;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Component
@FeignClient(value = "cloudnote-file",path = "/file")
public interface FileService {

    @PostMapping(value = "/noteImage",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    R<?> uploadNoteImg(@RequestPart("img") MultipartFile img);

    @PostMapping(value = "/headImage",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    R<?> uploadHeadImg(@RequestPart("img") MultipartFile img,@RequestParam("uid") Long id);
}
