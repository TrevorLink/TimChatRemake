package com.yep.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author HuangSir
 * @date 2022-02-14 23:18
 */
@SpringBootApplication
@MapperScan("com.yep.server.mapper")
//@EnableScheduling
public class TimRemakeApplication {
   public static void main(String[] args) {
      SpringApplication.run(TimRemakeApplication.class,args);
   }
}
