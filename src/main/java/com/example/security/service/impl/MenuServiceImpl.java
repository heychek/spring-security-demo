package com.example.security.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.security.mapper.MenuMapper;
import com.example.security.mapper.RoleMenuMapper;
import com.example.security.model.Menu;
import com.example.security.model.Role;
import com.example.security.model.RoleMenu;
import com.example.security.service.MenuService;
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
public class MenuServiceImpl implements MenuService {

  @Resource
  private MenuMapper menuMapper;
  @Resource
  private RoleMenuMapper roleMenuMapper;
  @Resource
  private RoleService roleService;

  @Override
  public List<Menu> selectMenusByUserId(long userId) {
    List<Role> roles = roleService.selectRolesByUserId(userId);
    if (roles == null || roles.isEmpty()) {
      return new ArrayList<>();
    }
    List<RoleMenu> roleMenus = roleMenuMapper.selectList(Wrappers.<RoleMenu>lambdaQuery()
        .in(RoleMenu::getRid, roles.stream().map(Role::getId).collect(Collectors.toList())));
    if (roleMenus == null || roleMenus.isEmpty()) {
      return new ArrayList<>();
    }
    return menuMapper.selectList(Wrappers.<Menu>lambdaQuery()
        .in(Menu::getId, roleMenus.stream().map(RoleMenu::getMid).collect(Collectors.toList())));
  }
}



