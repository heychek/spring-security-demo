package com.example.security.util;

import lombok.AccessLevel;
import lombok.Data;
import java.util.HashMap;
import java.util.Map;
import lombok.NoArgsConstructor;

/**
 * 统一返回结果的类
 */
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class R {

  private Boolean success;

  private Integer code;

  private String message;

  private Map<String, Object> data = new HashMap<>();

  public static R ok() {
    R r = new R();
    r.setSuccess(true);
    r.setCode(20000);
    r.setMessage("访问成功");
    return r;
  }

  public static R unauth() {
    R r = new R();
    r.setSuccess(false);
    r.setCode(20001);
    r.setMessage("您未登录，访问失败");
    return r;
  }

  public static R authfail() {
    R r = new R();
    r.setSuccess(false);
    r.setCode(20002);
    r.setMessage("登录失败");
    return r;
  }

  public static R accessDenied() {
    R r = new R();
    r.setSuccess(false);
    r.setCode(20003);
    r.setMessage("无权访问");
    return r;
  }

  public R data(String key, Object value) {
    this.data.put(key, value);
    return this;
  }

  public R data(Map<String, Object> map) {
    this.setData(map);
    return this;
  }
}
