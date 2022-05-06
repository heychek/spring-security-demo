package com.example.security.filter;

import com.example.security.handler.TokenManager;
import com.example.security.login.SecurityUser;
import com.example.security.model.Users;
import com.example.security.util.R;
import com.example.security.util.ResponseUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * @author chek
 */
@RequiredArgsConstructor
public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {

  private final TokenManager tokenManager;
  private final RedisTemplate<String, List<String>> redisTemplate;
  private final AuthenticationManager authenticationManager;

  public TokenLoginFilter(AuthenticationManager authenticationManager, TokenManager tokenManager,
      RedisTemplate<String, List<String>> redisTemplate) {
    this.authenticationManager = authenticationManager;
    this.tokenManager = tokenManager;
    this.redisTemplate = redisTemplate;
    this.setPostOnly(false);
    this.setRequiresAuthenticationRequestMatcher(
        new AntPathRequestMatcher("/admin/acl/login", "POST"));
  }

  /**
   * 获取表单提交用户名和密码
   */
  @SneakyThrows
  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException {
    //获取表单提交数据
    Users user = new ObjectMapper().readValue(request.getInputStream(), Users.class);
    return authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            user.getUsername(), user.getPassword(), new ArrayList<>()
        )
    );
  }

  /**
   * 认证成功调用的方法
   */
  @Override
  protected void successfulAuthentication(HttpServletRequest request,
      HttpServletResponse response, FilterChain chain, Authentication authResult) {
    //认证成功，得到认证成功之后用户信息
    SecurityUser user = (SecurityUser) authResult.getPrincipal();
    //根据用户名生成token
    String token = tokenManager.createToken(user.getCurrentUserInfo().getUsername());
    //把用户名称和用户权限列表放到redis
    redisTemplate.opsForValue()
        .set(user.getCurrentUserInfo().getUsername(), user.getPermissionValueList());
    //返回token
    ResponseUtils.out(response, R.ok().data("token", token));
  }

  /**
   * 认证失败调用的方法
   */
  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request,
      HttpServletResponse response, AuthenticationException failed) {
    ResponseUtils.out(response, R.error());
  }
}
