package com.example.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 创建于 2022/5/5 01:04
 *
 * @author chek
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // 表单登录
    http.formLogin()
        // 认证配置
        .and().authorizeRequests()
        // 任何请求
        .anyRequest()
        // 都需要身份验证
        .authenticated();
  }
}
