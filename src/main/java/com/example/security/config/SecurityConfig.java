package com.example.security.config;

import com.example.security.filter.TokenAuthFilter;
import com.example.security.filter.TokenLoginFilter;
import com.example.security.handler.DefaultPasswordEncoder;
import com.example.security.handler.TokenLogoutHandler;
import com.example.security.handler.TokenManager;
import com.example.security.handler.UnauthorizedEntryPoint;
import com.example.security.login.LoginServiceImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
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
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final LoginServiceImpl loginService;
  private final PersistentTokenRepository tokenRepository;
  private final TokenManager tokenManager;
  private final DefaultPasswordEncoder passwordEncoder;
  private final RedisTemplate<String, List<String>> redisTemplate;

  /**
   * 设置退出地址和 token，redis 操作地址等配置
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {

    // 添加自己实现逻辑的登录和授权过滤器
    http.addFilter(new TokenLoginFilter(authenticationManager(), tokenManager, redisTemplate))
        .addFilter(new TokenAuthFilter(authenticationManager(), tokenManager, redisTemplate))
        .httpBasic();

    // 开启记住我功能 http.rememberMe()
    http.rememberMe()
        // 单位是秒，这里设置的 86400 表示 10 天
        .tokenValiditySeconds(864000)
        .tokenRepository(tokenRepository)
        .userDetailsService(loginService);

    // 用户注销设置
    http.logout()
        .logoutUrl("/logout")
        .logoutSuccessUrl("/index")
        .addLogoutHandler(new TokenLogoutHandler(tokenManager, redisTemplate));

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
        .antMatchers("/static/**", "/index", "/login")
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
        .accessDeniedPage("/unauth")
        .authenticationEntryPoint(new UnauthorizedEntryPoint());

    // 关闭 csrf
    // http.csrf().disable();
  }

  /**
   * 密码处理
   */
  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(loginService).passwordEncoder(passwordEncoder);
  }
}
