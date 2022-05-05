package com.example.security.service;

import com.example.security.model.Menu;
import java.util.List;

/**
 * 创建于 2022/5/5 13:05
 *
 * @author chek
 */
public interface MenuService {

  /**
   * 根据用户 ID 查询可访问的菜单列表
   *
   * @param userId 用户 ID
   * @return 菜单列表
   */
  List<Menu> selectMenusByUserId(long userId);
}



