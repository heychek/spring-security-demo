package com.example.security.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author chek
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseUtils {

  public static void out(HttpServletResponse response, R r) {
    ObjectMapper mapper = new ObjectMapper();
    response.setStatus(HttpStatus.OK.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    // 这里需要设置下编码，否则将使返回的中文乱码
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());
    try {
      mapper.writeValue(response.getWriter(), r);
    } catch (IOException e) {
      throw new RuntimeException("返回信息失败", e);
    }
  }
}
