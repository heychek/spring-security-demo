package com.example.security.handler;

import com.example.security.util.R;
import com.example.security.util.ResponseUtils;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

/**
 * 创建于 2022/5/6 15:16
 *
 * @author chek
 */
@RequiredArgsConstructor
public class TokenLogoutHandler implements LogoutHandler {

  private final TokenManager tokenManager;
  private final RedisTemplate<String, List<String>> redisTemplate;

  @Override
  public void logout(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) {
    String token = request.getHeader("token");
    if (token != null) {
      tokenManager.removeToken(token);
      //清空当前用户缓存中的权限数据
      String userName = tokenManager.getUserNameFromToken(token);
      redisTemplate.delete(userName);
    }
    ResponseUtils.out(response, R.ok());
  }
}
