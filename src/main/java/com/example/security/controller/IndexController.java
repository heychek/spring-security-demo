package com.example.security.controller;

import com.example.security.model.Users;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 创建于 2022/5/5 01:27
 *
 * @author chek
 */
@Controller
public class IndexController {

  @GetMapping("index")
  public String index() {
    return "login";
  }

  @GetMapping("findAll")
  @ResponseBody
  public String findAll() {
    return "findAll";
  }

  @GetMapping("find")
  @ResponseBody
  public String find() {
    return "find";
  }

  @PostMapping("/authsuccess")
  public String success() {
    return "authsuccess";
  }

  @PostMapping("/unauth")
  public String fail() {
    return "unauth";
  }

  @RequestMapping("/testSecured")
  @ResponseBody
  @Secured({"ROLE_normal", "ROLE_管理员"})
  public String helloUser() {
    return "hello, spring securyty!";
  }

  @RequestMapping("/preAuthorize")
  @ResponseBody
  // @PreAuthorize("hasRole('ROLE_管理员')")
  @PreAuthorize("hasAnyAuthority('menu:system')")
  public String preAuthorize() {
    System.out.println("preAuthorize");
    return "preAuthorize";
  }

  @RequestMapping("/testPostAuthorize")
  @ResponseBody
  @PostAuthorize("hasAnyAuthority('menu:system')")
  public String postAuthorize() {
    System.out.println("postAuthorize");
    return "postAuthorize";
  }

  @RequestMapping("/getTestPreFilter")
  @PreAuthorize("hasRole('ROLE_管理员')")
  @PreFilter(value = "filterObject.id%2==0")
  @ResponseBody
  public List<Users> getTestPreFilter(@RequestBody List<Users> list) {
    list.forEach(t -> System.out.println(t.getId() + "\t" + t.getUsername()));
    return list;
  }

  @RequestMapping("getAll")
  @PreAuthorize("hasRole('ROLE_管理员')")
  @PostFilter("filterObject.username == 'admin1'")
  @ResponseBody
  public List<Users> getAllUser() {
    ArrayList<Users> list = new ArrayList<>();
    list.add(new Users(1L, "admin1", "6666"));
    list.add(new Users(2L, "admin2", "888"));
    return list;
  }
}
