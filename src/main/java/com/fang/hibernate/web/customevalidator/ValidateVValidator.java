package com.fang.hibernate.web.customevalidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.util.StringUtils;

import com.fang.hibernate.web.customevalidatorannotation.ValidateV;
import com.fang.hibernate.web.dto.ServiceAndVDto;

public class ValidateVValidator implements ConstraintValidator<ValidateV, ServiceAndVDto> {

  private ValidateV validateV;

  @Override
  public void initialize(ValidateV validateV) {
    this.validateV = validateV;
  }

  @Override
  public boolean isValid(ServiceAndVDto object,
                         ConstraintValidatorContext constraintValidatorContext) {
    System.out.println(StringUtils.isEmpty(validateV));
    if (object != null && object.getService() != null && object.getService().equals("fang")) {
      return true;
    }
    constraintValidatorContext.disableDefaultConstraintViolation();
    constraintValidatorContext.buildConstraintViolationWithTemplate("service不正确")
        .addConstraintViolation();
    return false;
  }
}
