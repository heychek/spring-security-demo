package com.example.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 创建于 2022/5/5 13:29
 *
 * @author chek
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Users {

  private Long id;

  private String username;

  private String password;
}