package com.example.security.handler;

import com.example.security.util.R;
import com.example.security.util.ResponseUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 * 创建于 2022/5/6 18:08
 *
 * @author chek
 */
public class AuthFailHandler implements AuthenticationFailureHandler {

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) {
    ResponseUtils.out(response, R.authfail());
  }
}
