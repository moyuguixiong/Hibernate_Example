package com.fang.hibernate.web.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

/**
 *
 * <p>
 * Description: StringTrimConverter
 * </p>
 *
 * @author jinshilei
 *         2017年2月8日
 * @version 1.0
 *
 */
public class StringTrimConverter implements Converter<String, String> {

  @Override
  public String convert(String source) {
    if (source != null) {
      source = source.trim();
      if (!StringUtils.isEmpty(source)) {
        return source;
      }
    }
    return null;
  }

}
