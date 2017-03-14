package com.fang.hibernate.web.customevalidatorannotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.fang.hibernate.web.customevalidator.ValidateVValidator;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Inherited
@Constraint(validatedBy = ValidateVValidator.class)
public @interface ValidateV {

  String message() default "错误";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
