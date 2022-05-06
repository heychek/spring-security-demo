package com.example.security.handler;

import com.example.security.util.R;
import com.example.security.util.ResponseUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * <p> 无权访问时返回 JSON 格式数据给前端（不配置则为 403 html 页面）
 * <p>
 * 创建于 2022/5/6 18:34
 *
 * @author chek
 */
public class MyAccessDeniedHandler implements AccessDeniedHandler {

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) {
    ResponseUtils.out(response, R.accessDenied());
  }
}
