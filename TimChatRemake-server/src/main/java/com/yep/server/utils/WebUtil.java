package com.yep.server.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author HuangSir
 * @date 2022-02-15 19:45
 */
public class WebUtil {
   /**
    * 将指定内容渲染到前端
    *
    * @param response
    * @param data
    */
   public static void renderString(HttpServletResponse response, Object data) {
      response.setContentType("application/json;charset=utf-8");
      PrintWriter writer = null;
      try {
         writer = response.getWriter();
         writer.write(new ObjectMapper().writeValueAsString(data));
      } catch (IOException e) {
         e.printStackTrace();
      } finally {
         if (writer != null) {
            writer.flush();
            writer.close();
         }
      }
   }
}
