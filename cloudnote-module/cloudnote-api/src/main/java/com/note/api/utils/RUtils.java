package com.note.api.utils;

/**
 * 响应工具类
 *
 * @date 2022/11/28 16:41
 **/

import com.fasterxml.jackson.databind.ObjectMapper;
import com.note.api.result.R;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RUtils {
    private static final ObjectMapper mapper = new ObjectMapper();
    public RUtils() {

    }
    public static <T> void toResponse(R<T> r, final HttpServletResponse response)
            throws IOException{
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(r.getCode());
        response.getWriter().write(mapper.valueToTree(r).toString());
    }
}
