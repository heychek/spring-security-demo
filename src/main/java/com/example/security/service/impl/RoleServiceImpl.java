package com.example.security.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.security.mapper.RoleMapper;
import com.example.security.mapper.RoleUserMapper;
import com.example.security.model.Role;
import com.example.security.model.RoleUser;
import com.example.security.service.RoleService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 创建于 2022/5/5 13:05
 *
 * @author chek
 */
@Service
public class RoleServiceImpl implements RoleService {

  @Resource
  private RoleMapper roleMapper;
  @Resource
  private RoleUserMapper roleUserMapper;

  @Override
  public List<Role> selectRolesByUserId(long userId) {
    List<RoleUser> roleUsers = roleUserMapper
        .selectList(Wrappers.<RoleUser>lambdaQuery().eq(RoleUser::getUid, userId));
    if (roleUsers == null || roleUsers.isEmpty()) {
      return new ArrayList<>();
    }
    return roleMapper.selectList(Wrappers.<Role>lambdaQuery().
        in(Role::getId, roleUsers.stream().map(RoleUser::getRid).collect(Collectors.toList())));
  }
}

