package com.example.security.service;

import com.example.security.model.Users;

/**
 * 创建于 2022/5/5 13:05
 *
 * @author chek
 */
public interface UsersService {

  /**
   * 根据用户名获取用户信息
   *
   * @param username 用户名
   * @return 用户实体
   */
  Users selectUsersByUsername(String username);
}



