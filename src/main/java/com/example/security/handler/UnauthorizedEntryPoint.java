package com.example.security.handler;

import com.example.security.util.R;
import com.example.security.util.ResponseUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * 创建于 2022/5/6 15:24
 *
 * @author chek
 */
public class UnauthorizedEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) {
    ResponseUtils.out(response, R.error());
  }
}
