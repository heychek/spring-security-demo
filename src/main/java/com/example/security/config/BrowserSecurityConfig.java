package com.example.security.config;

import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

/**
 * 创建于 2022/5/5 23:18
 *
 * @author chek
 */
@RequiredArgsConstructor
@Configuration
public class BrowserSecurityConfig {

  private final DataSource dataSource;

  @Bean
  public PersistentTokenRepository persistentTokenRepository() {
    JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
    // 赋值数据源
    jdbcTokenRepository.setDataSource(dataSource);
    // 自动创建表,第一次执行会创建，以后要执行就要删除掉！
    // jdbcTokenRepository.setCreateTableOnStartup(true);
    return jdbcTokenRepository;
  }
}
