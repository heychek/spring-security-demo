package com.example.security.util;

import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 创建于 2022/5/6 14:31
 *
 * @author chek
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenManager {

  private final static long TOKEN_EXPIRATION = 24 * 60 * 60 * 1000;
  private final static String TOKEN_SIGN_KEY = "123456";

  public static String createToken(String username) {
    return Jwts.builder().setSubject(username)
        .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION))
        .signWith(SignatureAlgorithm.HS512, TOKEN_SIGN_KEY)
        .compressWith(CompressionCodecs.GZIP).compact();
  }

  public static String getUserNameFromToken(String token) {
    return Jwts.parser().setSigningKey(TOKEN_SIGN_KEY).parseClaimsJws(token).getBody().getSubject();
  }

  public static void removeToken(String token) {
    // jwt token 无需删除，客户端清除即可
  }
}
