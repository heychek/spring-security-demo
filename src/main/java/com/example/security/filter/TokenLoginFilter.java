package com.example.security.filter;

import cn.hutool.json.JSONUtil;
import com.example.security.util.TokenManager;
import com.example.security.model.Users;
import com.example.security.util.RequestJsonUtils;
import com.example.security.util.R;
import com.example.security.util.ResponseUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author chek
 */
public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {

  private final RedisTemplate<String, List<String>> redisTemplate;
  private final AuthenticationManager authenticationManager;
  private final boolean postOnly;

  public TokenLoginFilter(AuthenticationManager authenticationManager,
      RedisTemplate<String, List<String>> redisTemplate) {
    this.authenticationManager = authenticationManager;
    this.redisTemplate = redisTemplate;
    this.postOnly = true;
    this.setRequiresAuthenticationRequestMatcher(
        new AntPathRequestMatcher("/login", "POST"));
  }

  /**
   * 获取表单提交用户名和密码
   */
  @SneakyThrows
  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException {
    if (this.postOnly && !request.getMethod().equals(RequestMethod.POST.name())) {
      throw new AuthenticationServiceException(
          "Authentication method not supported: " + request.getMethod());
    }
    //  在这里取消原有的通过 request parameter 形式获取用户名密码的方法, 替换为 json 数据格式的获取
    Users user = JSONUtil
        .toBean(RequestJsonUtils.getPostRequestJsonString(request), Users.class);
    UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
        user.getUsername(), user.getPassword(), new ArrayList<>());
    // Allow subclasses to set the "details" property
    setDetails(request, authRequest);
    return authenticationManager.authenticate(authRequest);
  }

  /**
   * 认证成功调用的方法
   */
  @Override
  protected void successfulAuthentication(HttpServletRequest request,
      HttpServletResponse response, FilterChain chain, Authentication authResult) {
    //认证成功，得到认证成功之后用户信息
    User user = (User) authResult.getPrincipal();
    //根据用户名生成token
    String token = TokenManager.createToken(user.getUsername());
    //把用户名称和用户权限列表放到redis
    redisTemplate.opsForValue()
        .set(user.getUsername(), new ArrayList<>(
            user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(
                Collectors.toList())));
    //返回token
    ResponseUtils.out(response, R.ok().data("Authorization", token));
  }

  /**
   * 认证失败调用的方法
   */
  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request,
      HttpServletResponse response, AuthenticationException failed) {
    ResponseUtils.out(response, R.unauth());
  }
}
