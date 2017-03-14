package com.fang.hibernate.web.messageconverter;

import java.io.IOException;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

public class MyMessageConverter extends AbstractHttpMessageConverter<String> {

  @Override
  protected String readInternal(Class<? extends String> clazz, HttpInputMessage httpInputMessage)
      throws IOException, HttpMessageNotReadableException {
    return null;
  }

  @Override
  protected boolean supports(Class<?> clazz) {
    return false;
  }

  @Override
  protected void writeInternal(String str, HttpOutputMessage httpOutputMessage) throws IOException,
      HttpMessageNotWritableException {

  }

}
