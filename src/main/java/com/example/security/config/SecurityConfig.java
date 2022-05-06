package com.example.security.config;

import com.example.security.filter.TokenAuthFilter;
import com.example.security.filter.TokenLoginFilter;
import com.example.security.handler.AuthFailHandler;
import com.example.security.handler.AuthSuccessHandler;
import com.example.security.handler.MyAccessDeniedHandler;
import com.example.security.handler.TokenLogoutHandler;
import com.example.security.handler.UnauthEntryPoint;
import com.example.security.login.LoginServiceImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
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
  private final PasswordEncoder passwordEncoder;
  private final RedisTemplate<String, List<String>> redisTemplate;

  /**
   * 设置退出地址和 token，redis 操作地址等配置
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http.sessionManagement()
        // 使用 JWT，关闭 session
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    // 添加自己实现逻辑的登录和授权过滤器
    http
        // 用重写的 Filter 替换掉原有的 UsernamePasswordAuthenticationFilter 实现使用 Json 数据也可以登陆
        .addFilterAt(new TokenLoginFilter(authenticationManager(), redisTemplate),
            UsernamePasswordAuthenticationFilter.class)
        // 设置执行其他工作前的 Filter(最重要的验证 JWT)
        .addFilterBefore(new TokenAuthFilter(authenticationManager(), redisTemplate),
            UsernamePasswordAuthenticationFilter.class);

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
        // 用户注销返回 JSON 格式数据给前端（不配置则为 html）
        .addLogoutHandler(new TokenLogoutHandler(redisTemplate));

    http.formLogin()
        // 配置哪个 url 为登录页面
        .loginPage("/index")
        // 设置哪个是登录的 url
        .loginProcessingUrl("/login")
        // 登录成功之后跳转到哪个 url
        .successForwardUrl("/authsuccess")
        // 登录失败之后跳转到哪个 url
        .failureForwardUrl("/unauth")
        // 登录成功返回 JSON 格式数据给前端（不配置则为 html）
        .successHandler(new AuthSuccessHandler())
        // 登录失败返回 JSON 格式数据给前端（不配置则为 html）
        .failureHandler(new AuthFailHandler());

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
        // 未授权返回 JSON 格式数据给前端（不配置则为 html）
        .authenticationEntryPoint(new UnauthEntryPoint())
        // 无权访问时返回 JSON 格式数据给前端（不配置则为 html）
        .accessDeniedHandler(new MyAccessDeniedHandler());

    // 关闭 csrf
    http.csrf().disable();
  }

  /**
   * 密码处理
   */
  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    // 加入自定义的安全认证
    auth.userDetailsService(loginService).passwordEncoder(passwordEncoder);
  }
}
