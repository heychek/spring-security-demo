package com.example.security.login;

import com.example.security.model.Users;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author chek
 */
@Data
@NoArgsConstructor
public class SecurityUser implements UserDetails {

  /**
   * 当前登录用户
   */
  private transient Users currentUserInfo;

  /**
   * 当前权限
   */
  private List<String> permissionValueList;

  public SecurityUser(Users user) {
    if (user != null) {
      this.currentUserInfo = user;
    }
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Collection<GrantedAuthority> authorities = new ArrayList<>();
    for (String permissionValue : permissionValueList) {
      if (permissionValue != null && !permissionValue.trim().isEmpty()) {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(permissionValue);
        authorities.add(authority);
      }
    }
    return authorities;
  }

  @Override
  public String getPassword() {
    return currentUserInfo.getPassword();
  }

  @Override
  public String getUsername() {
    return currentUserInfo.getUsername();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}

