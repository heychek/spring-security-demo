package com.example.security.handler;

import java.nio.charset.StandardCharsets;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

/**
 * 创建于 2022/5/6 14:37
 *
 * @author chek
 */
@Component
public class DefaultPasswordEncoder implements PasswordEncoder {

  public DefaultPasswordEncoder() {
    this(-1);
  }

  /**
   * @param strength the log rounds to use, between 4 and 31
   */
  public DefaultPasswordEncoder(int strength) {
  }

  @Override
  public String encode(CharSequence rawPassword) {
    // DigestUtils 是 spring 自带的加密工具
    return DigestUtils.md5DigestAsHex(rawPassword.toString().getBytes(StandardCharsets.UTF_8));
  }

  @Override
  public boolean matches(CharSequence rawPassword, String encodedPassword) {
    return encodedPassword.equals(encode(rawPassword));
  }
}
