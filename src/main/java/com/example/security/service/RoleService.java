package com.example.security.service;

import com.example.security.model.Role;
import java.util.List;

/**
 * 创建于 2022/5/5 13:05
 *
 * @author chek
 */
public interface RoleService {

  /**
   * 根据用户 ID 查询用户角色列表
   *
   * @param userId 用户 ID
   * @return 用户对应的角色列表
   */
  List<Role> selectRolesByUserId(long userId);
}



