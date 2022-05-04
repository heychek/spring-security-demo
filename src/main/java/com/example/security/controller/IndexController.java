package com.example.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 创建于 2022/5/5 01:27
 *
 * @author chek
 */
@Controller
public class IndexController {

  @GetMapping("index")
  @ResponseBody
  public String index() {
    return "访问成功";
  }
}
