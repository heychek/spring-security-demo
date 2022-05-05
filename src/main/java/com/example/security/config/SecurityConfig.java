package com.example.security.config;

import com.example.security.login.LoginServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

/**
 * 创建于 2022/5/5 01:04
 *
 * @author chek
 */
@RequiredArgsConstructor
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final LoginServiceImpl loginService;
  private final PersistentTokenRepository tokenRepository;

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    // 开启记住我功能 http.rememberMe()
    http.rememberMe()
        // 单位是秒，这里设置的 86400 表示 10 天
        .tokenValiditySeconds(864000)
        .tokenRepository(tokenRepository)
        .userDetailsService(loginService);

    // 用户注销设置
    http.logout()
        .logoutUrl("/logout")
        .logoutSuccessUrl("/index");

    http.formLogin()
        // 配置哪个 url 为登录页面
        .loginPage("/index")
        // 设置哪个是登录的 url
        .loginProcessingUrl("/login")
        // 登录成功之后跳转到哪个 url
        .successForwardUrl("/authsuccess")
        // 登录失败之后跳转到哪个 url
        .failureForwardUrl("/unauth");

    http.authorizeRequests()
        // 表示配置请求路径
        .antMatchers("/static/**", "/index")
        // 指定这些 URL 无需保护
        .permitAll()
        // 需要用户带有 管理员 权限
        .antMatchers("/findAll").hasRole("管理员")
        // 需要主体带有 menu:system 权限
        .antMatchers("/find").hasAnyAuthority("menu:system")
        // 其他请求
        .anyRequest()
        // 需要认证
        .authenticated();

    http.exceptionHandling()
        // 自定义 403 页面，这里 Demo 为了省事和登录失败用的是同一个页面
        .accessDeniedPage("/unauth");

    // 关闭 csrf
    http.csrf().disable();
  }
}
