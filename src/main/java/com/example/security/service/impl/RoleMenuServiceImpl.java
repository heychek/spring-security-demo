package com.example.security.service.impl;

import com.example.security.mapper.RoleMenuMapper;
import com.example.security.service.RoleMenuService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 创建于 2022/5/5 13:05
 *
 * @author chek
 */
@Service
public class RoleMenuServiceImpl implements RoleMenuService {

  @Resource
  private RoleMenuMapper roleMenuMapper;

}



