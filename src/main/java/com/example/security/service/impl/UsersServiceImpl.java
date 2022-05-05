package com.example.security.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.security.mapper.UsersMapper;
import com.example.security.model.Users;
import com.example.security.service.UsersService;
import javax.annotation.Resource;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 创建于 2022/5/5 13:05
 *
 * @author chek
 */
@Service
public class UsersServiceImpl implements UsersService {

  @Resource
  private UsersMapper usersMapper;

  @Override
  public Users selectUsersByUsername(String username) {
    if (username == null || "".equals(username.trim())) {
      throw new UsernameNotFoundException("用户名不存在！");
    }
    return usersMapper
        .selectOne(Wrappers.<Users>lambdaQuery().eq(Users::getUsername, username));
  }
}



