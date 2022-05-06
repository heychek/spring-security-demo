package com.example.security.handler;

import com.example.security.util.R;
import com.example.security.util.ResponseUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * <p> 登录成功返回的 JSON 格式数据给前端（否则为 html）
 * <p>
 * 创建于 2022/5/6 18:10
 *
 * @author chek
 */
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) {
    ResponseUtils.out(response, R.ok());
  }
}
