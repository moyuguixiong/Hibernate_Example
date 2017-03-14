package com.fang.hibernate.web.converter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

/**
 *
 * <p>
 * Description: StringToDateConverter 根据配置决定支持的字符串格式时间和时间戳时间
 * </p>
 *
 * @author jinshilei
 *         2017年2月8日
 * @version 1.0
 *
 */
public class StringToDateConverter implements Converter<String, Date> {

  /**
   * 存储支持的时间格式类型
   */
  private static List<SimpleDateFormat> dateStyleList = new ArrayList<SimpleDateFormat>();

  /**
   * 是否支持时间戳
   */
  private static Boolean isSupportTimestamp = false;

  /**
   * 初始化支持的时间格式类型
   */
  static {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("dateStyleForConverter/dateStyle");
    String isSupportTimestampString = resourceBundle.getString("isSupportTimestamp");
    if (!StringUtils.isEmpty(isSupportTimestampString)
        && "true".equalsIgnoreCase(isSupportTimestampString)) {
      isSupportTimestamp = true;
    }
    String dateStyleString = resourceBundle.getString("dateStyle");
    if (!StringUtils.isEmpty(dateStyleString)) {
      String[] dateStyles = dateStyleString.split(",");
      if (dateStyles.length > 0) {
        for (int i = 0; i < dateStyles.length; i++) {
          SimpleDateFormat dateFormat = new SimpleDateFormat(dateStyles[i]);
          dateStyleList.add(dateFormat);
        }
      }
    }
  }

  @Override
  public Date convert(String source) {
    Date dateResult = null;
    if (!StringUtils.isEmpty(source)) {
      if (dateStyleList.size() > 0) {
        for (SimpleDateFormat simpleDateFormat : dateStyleList) {
          try {
            dateResult = simpleDateFormat.parse(source);
            System.out.println("转换了此格式的字符串时间：" + simpleDateFormat.toPattern());
            return dateResult;
          } catch (Exception ex) {
          }
        }
      }
      if (isSupportTimestamp) {
        try {
          Long timestamp = Long.parseLong(source);
          dateResult = new Date(timestamp);
          System.out.println("转换了时间戳时间：" + source);
        } catch (Exception ex) {
        }
      }
    }
    return dateResult;
  }

}
