package com.yep.server;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author HuangSir
 * @date 2022-02-15 0:23
 */
@SpringBootTest
public class MainTest {
   BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
   @Test
   public void testPassword(){
//      System.out.println(passwordEncoder.matches("123", "$10$oE39aG10kB/rFu2vQeCJTu/V/v4n6DRR0f8WyXRiAYvBpmadoOBE."));
//      System.out.println(passwordEncoder.encode("123456"));
      System.out.println(passwordEncoder.encode("admin"));
   }
}
