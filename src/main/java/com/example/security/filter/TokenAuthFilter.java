package com.example.security.filter;

import com.example.security.util.TokenManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * @author chek
 */
public class TokenAuthFilter extends BasicAuthenticationFilter {

  private final RedisTemplate<String, List<String>> redisTemplate;

  public TokenAuthFilter(AuthenticationManager authenticationManager,
      RedisTemplate<String, List<String>> redisTemplate) {
    super(authenticationManager);
    this.redisTemplate = redisTemplate;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain) throws IOException, ServletException {
    //获取当前认证成功用户权限信息
    UsernamePasswordAuthenticationToken authRequest = getAuthentication(request);
    //判断如果有权限信息，放到权限上下文中
    if (authRequest != null) {
      SecurityContextHolder.getContext().setAuthentication(authRequest);
    }
    chain.doFilter(request, response);
  }

  private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
    // 从 header 获取 token
    String token = request.getHeader("Authorization");
    if (token == null) {
      return null;
    }
    // 从 token 获取用户名
    String username = TokenManager.getUserNameFromToken(token);
    // 从 redis 获取对应权限列表
    List<String> permissionValueList = redisTemplate.opsForValue().get(username);
    Collection<GrantedAuthority> authority = new ArrayList<>();
    if (permissionValueList != null) {
      for (String permissionValue : permissionValueList) {
        SimpleGrantedAuthority auth = new SimpleGrantedAuthority(permissionValue);
        authority.add(auth);
      }
    }
    return new UsernamePasswordAuthenticationToken(username, token, authority);
  }
}
