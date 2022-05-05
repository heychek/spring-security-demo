package com.example.security.login;

import com.example.security.model.Menu;
import com.example.security.model.Role;
import com.example.security.model.Users;
import com.example.security.service.MenuService;
import com.example.security.service.RoleService;
import com.example.security.service.UsersService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 创建于 2022/5/5 11:10
 *
 * @author chek
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class LoginServiceImpl implements UserDetailsService {

  private final UsersService usersService;
  private final RoleService roleService;
  private final MenuService menuService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    Users users = usersService.selectUsersByUsername(username);
    // 获取用户角色列表和菜单列表
    List<Role> roles = roleService.selectRolesByUserId(users.getId());
    List<Menu> menus = menuService.selectMenusByUserId(users.getId());

    // 声明权限集合
    List<GrantedAuthority> auths = new ArrayList<>();
    // 处理角色
    if (roles != null && !roles.isEmpty()) {
      String prefix = "ROLE_";
      auths.addAll(roles.stream().map(Role::getName).map(name
          -> new SimpleGrantedAuthority(prefix + name)).collect(Collectors.toList())
      );
    }
    // 处理权限
    if (menus != null && !menus.isEmpty()) {
      auths.addAll(menus.stream().map(Menu::getPermission)
          .map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
    }
    // 将用户角色，权限，添加到当前用户
    return new User(users.getUsername(), users.getPassword(), auths);
  }
}
