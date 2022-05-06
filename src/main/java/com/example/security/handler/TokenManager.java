package com.example.security.handler;

import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.stereotype.Component;

/**
 * 创建于 2022/5/6 14:31
 *
 * @author chek
 */
@Component
public class TokenManager {

  private final long tokenExpiration = 24 * 60 * 60 * 1000;
  private final String tokenSignKey = "123456";

  public String createToken(String username) {
    return Jwts.builder().setSubject(username)
        .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
        .signWith(SignatureAlgorithm.HS512, tokenSignKey)
        .compressWith(CompressionCodecs.GZIP).compact();
  }

  public String getUserNameFromToken(String token) {
    return Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token).getBody().getSubject();
  }

  public void removeToken(String token) {
    // jwt token 无需删除，客户端清除即可
  }
}
