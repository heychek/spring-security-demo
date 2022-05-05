package com.example.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/**
 * @author chek
 */
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@SpringBootApplication
public class SpringSecurityDemoApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringSecurityDemoApplication.class, args);
  }
}
