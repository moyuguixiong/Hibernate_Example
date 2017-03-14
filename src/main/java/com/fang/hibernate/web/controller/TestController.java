package com.fang.hibernate.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fang.hibernate.po.SysUser;
import com.fang.hibernate.web.dto.InModel;
import com.fang.hibernate.web.dto.ServiceAndVDto;
import com.fang.hibernate.web.validationgroup.ValidateForUser;

@Controller
public class TestController {

  class Test2 {

  }

  @RequestMapping(value = "/test")
  public String test(Model model, @Validated(value = ValidateForUser.class) InModel inModel,
                     BindingResult bindingResult,
                     @Validated(value = ValidateForUser.class) ServiceAndVDto serviceAndVDto,
                     BindingResult bindingResult2) {

    if (bindingResult.hasErrors()) {
      List<ObjectError> errors = bindingResult.getAllErrors();
      for (ObjectError objectError : errors) {
        System.out.println(objectError.getDefaultMessage());
      }
      model.addAttribute("errors", errors);
    }
    return "default";
  }

  @RequestMapping(value = "/backJson", produces = {"application/xml;charset=utf-8"})
  public @ResponseBody SysUser backJson() {
    SysUser sysUser = new SysUser();
    sysUser.setUserId("1");
    sysUser.setAge(30);
    sysUser.setBirthday(new Date());
    sysUser.setGender(true);
    sysUser.setUserName("战三");
    return sysUser;
  }
}

class Test {

}
