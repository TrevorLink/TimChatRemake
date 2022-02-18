package com.yep.server.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 将前端传入的字符串参数转化为日期类型Date
 * @author HuangSir
 * @date 2022-02-18 19:39
 */
@Component
public class StringToDateConverter implements Converter<String, Date> {
   SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

   @Override
   public Date convert(String s) {
      Date res = null;
      if (!StringUtils.isEmpty(s)) {
         try {
            res = simpleDateFormat.parse(s);
         } catch (ParseException e) {
            e.printStackTrace();
         }
      }
      return res;
   }
}
