package com.example.security.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 创建于 2022/5/7 00:56
 *
 * @author chek
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestJsonUtils {

  public static String getPostRequestJsonString(HttpServletRequest request) {
    BufferedReader br;
    StringBuilder sb;
    String jsonString;
    try {
      br = new BufferedReader(new InputStreamReader(
          request.getInputStream()));
      String line;
      sb = new StringBuilder();
      while ((line = br.readLine()) != null) {
        sb.append(line);
      }
      jsonString = URLDecoder.decode(sb.toString(), StandardCharsets.UTF_8.name());
      jsonString = jsonString.substring(jsonString.indexOf("{"));
    } catch (IOException e) {
      throw new RuntimeException("参数不正确，请传入正确的 JSON 参数");
    }
    return jsonString;
  }
}
