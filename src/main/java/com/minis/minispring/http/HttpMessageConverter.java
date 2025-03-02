package com.minis.minispring.http;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 让Controller返回给前端的字符流数据可以进行格式转换
 */
public interface HttpMessageConverter {
    void write(Object obj, HttpServletResponse response) throws IOException;
}
