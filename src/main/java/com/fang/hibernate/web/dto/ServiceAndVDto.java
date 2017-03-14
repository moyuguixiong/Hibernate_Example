package com.fang.hibernate.web.dto;

import com.fang.hibernate.web.customevalidatorannotation.ValidateV;
import com.fang.hibernate.web.validationgroup.ValidateForUser;

@ValidateV(groups = {ValidateForUser.class})
public class ServiceAndVDto {

  private String service;

  private String v;

  public String getService() {
    return service;
  }

  public void setService(String service) {
    this.service = service;
  }

  public String getV() {
    return v;
  }

  public void setV(String v) {
    this.v = v;
  }
}
