# 环境搭建

## 初步依赖

创建一个空的项目，创建一个普通的子模块，引入pom依赖

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.5.7</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.yep</groupId>
    <artifactId>TimChatRemake-server</artifactId>
    <version>1.0</version>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-websocket</artifactId>
        </dependency>
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>2.1.3</version>
        </dependency>

        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid-spring-boot-starter</artifactId>
            <version>1.1.10</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

</project>
```

## 配置数据库信息

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/tim_remake
    type: com.alibaba.druid.pool.DruidDataSource
    username: root
    password: 123456
#mapper文件位置配置
mybatis:
  mapper-locations: classpath:mapper/*.xml

```

## 配置扫描静态资源路径

```xml
    <!--配置扫描静态资源路径-->
    <build>
        <resources>
            <resource>
                <directory>src/main/resources</directory>
            </resource>
        </resources>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
```

## JSON公共响应对象

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespBean {
  private Integer status;//状态码
  private String msg;//返回消息
  private Object obj;//返回实体

  public static RespBean build(){
    return new RespBean();
  }

  public static RespBean ok(String msg){
    return new RespBean(200,msg,null);
  }
  public static RespBean ok(String msg,Object obj){
    return new RespBean(200,msg,obj);
  }

  public static RespBean error(String msg){
    return new RespBean(500,msg,null);
  }
  public static RespBean error(String msg,Object obj){
    return new RespBean(500,msg,obj);
  }
  
}
```

# 整合SpringSecurity

## 实现思路

我们这里不去采取自定义的controller，而是自己去使用登录表单的`UsernamePasswordAuthenticationFilter`，针对登录成功和登录失败的处理，我们使用`AuthenticationSuccessHandler`和`AuthenticationFailureHandler`自定义实现类来配置实现

## 实体类实现UserDetails接口

之前Security的依赖已经引入了，我们这边主要是为了实现基于数据库的用户信息security查询

> 所以来一个登录实体类User实现UserDetails接口

```java
package com.yep.server.pojo;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * (User)实体类
 *
 * @author HuangSir
 */
public class User  implements UserDetails {

    private Integer id;
    /**
    * 登录账号
    */
    private String username;
    /**
    * 昵称
    */
    private String nickname;
    /**
    * 密码
    */
    private String password;
    /**
    * 用户头像
    */
    private String userProfile;
    /**
    * 用户状态id
    */
    private Integer userStateId;
    /**
    * 是否可用
    */
    private Boolean isEnabled;
    /**
    * 是否被锁定
    */
    private Boolean isLocked;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    //账号是否未过期
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //账号是否锁定
    @Override
    public boolean isAccountNonLocked() {
        return  !isLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return  isEnabled;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 获取用户拥有的所有角色
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public Integer getUserStateId() {
        return userStateId;
    }

    public void setUserStateId(Integer userStateId) {
        this.userStateId = userStateId;
    }


    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    public void setLocked(Boolean locked) {
        isLocked = locked;
    }
}
```

实现接口的方法，这里需要注意接口重写的isEnabled()和isLocker()方法相当于get方法，**需要把之前自动生成 isEnabled和isLocked属性的get方法删除**

## 基于数据库实现

由于没有引入mybatis——plus，这边还是手写一下，反正也没多少和数据库有关的业务

> Dao

```java
@Repository
public interface UserMapper {
   /**
    * 根据用户名查询用户对象，这个是security要求实现的方法
    * @param username
    * @return
    */
   User loadUserByUsername(String username);
}
```

> Mapper

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.yep.server.mapper.UserMapper">
    <resultMap type="user" id="UserMap">
        <result property="id" column="id" jdbcType="INTEGER"/>
        <result property="username" column="username" jdbcType="VARCHAR"/>
        <result property="nickname" column="nickname" jdbcType="VARCHAR"/>
        <result property="password" column="password" jdbcType="VARCHAR"/>
        <result property="userProfile" column="user_profile" jdbcType="VARCHAR"/>
        <result property="userStateId" column="user_state_id" jdbcType="INTEGER"/>
        <result property="isEnabled" column="is_enabled" jdbcType="OTHER"/>
        <result property="isLocked" column="is_locked" jdbcType="OTHER"/>
    </resultMap>
    <!--根据用户名查询用户 -->
    <select id="loadUserByUsername" resultMap="UserMap">
        select *
        from tim_remake.user
        where username = #{username}
    </select>
</mapper>
```

> Service

由于根据Security的介绍，我们想要实现自定义的用户数据校验，就需要自定义一个UserDetailsService的实现类，来重写其中的`loadUserByUsername`方法

那么我们这边就选择直接让`UserService`这个接口的实现类来同时实现`UserService`和`UserDetailsService`这两个接口

- 接口（这里暂时啥也不用写，因为我们loadUserByUsername不是在这里定义的）

```java
public interface UserService {
}
```

- 实现类

```java
@Service
@Slf4j
public class UserServiceImpl implements UserService,UserDetailsService {
   @Autowired(required = true)
   private UserMapper userMapper;
   @Override
   public User loadUserByUsername(String username) {
      User user = userMapper.loadUserByUsername(username);
      if (user==null) throw  new UsernameNotFoundException("用户不存在！");
      log.debug("查到的用户数据为：{}",user);
      return  user;
   }
}
```

那么我们现在有了service实现类，下一步就是配置他，因为我们这边**并不是用自定义的Controller**，所以配置的方式还是有点不一样的

## SecurityConfig配置

> 验证服务

我们需要让security知道我们自定义的`UserDetailsService`的实现类，就需要配置，这个方法之前都没有使用过，具体可以参照这篇博客

[Spring Security的三个configure方法](https://www.cnblogs.com/woyujiezhen/p/13049979.html)

```java
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
   @Autowired
   private UserDetailsService userDetailsService;

   //调用这个configure方法实现从数据库中获取用户信息，其实就是原先的AuthenticationManager
   @Override
   protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      auth.userDetailsService(userDetailsService);
   }
}
```

注意这边有一个坑，我们自动注入的时候选择的类型不能是UserService，否则会报出参数的类型不匹配的编译错误，而应该是`UserDeatilsService`接口（或者是UserServiceImpl类，这个好像不规范）

> 密码加密

接下来就是进行密码加密器的配置，还是使用Security推荐的BCryptPasswordEncoder，向IOC容器中注入即可

```java
   //密码加密
   @Bean
   PasswordEncoder passwordEncoder(){
      return new BCryptPasswordEncoder();
   }
```

> 配置自定义的校验

```java
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

   @Autowired
   private AuthenticationSuccessHandler successHandler;
   @Autowired
   private AuthenticationFailureHandler failureHandler;
   @Autowired
   private LogoutSuccessHandler logoutSuccessHandler;

   //总体配置
   @Override
   protected void configure(HttpSecurity http) throws Exception {
      http.authorizeRequests()
              .anyRequest()
              .authenticated()
              .and()
              .formLogin()
              .usernameParameter("username")
              .passwordParameter("password")
              .loginPage("/login")
              .loginProcessingUrl("/doLogin")
              .successHandler(successHandler)
              .failureHandler(failureHandler)
              .permitAll()//返回值直接返回
              .and()
              .logout()
              .logoutUrl("/logout")
              .logoutSuccessHandler(logoutSuccessHandler)
              .permitAll()
              .and()
              .csrf().disable().exceptionHandling();
   }
}

```

其中包含了我们认证成功/失败的自定义处理器，以及登出成功的处理器；这边都要求返回JSON对象

- 认证成功的自定义处理器

```java
@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
   //登录成功返回公共JSON
   @Override
   public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
      response.setContentType("application/json;charset=utf-8");
      PrintWriter out=response.getWriter();
      User user=(User) authentication.getPrincipal();
      //密码不要给前端
      user.setPassword(null);
      RespBean respBean = RespBean.ok("登录成功", user);
      String s = new ObjectMapper().writeValueAsString(respBean);
      out.write(s);
      out.flush();
      out.close();
   }
}
```

- 认证失败的自定义处理器

```java
@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {
   @Override
   public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
      response.setContentType("application/json;charset=utf-8");
      PrintWriter out = response.getWriter();
      RespBean error = RespBean.error("登录失败!");
      if (exception instanceof LockedException) {
         error.setMsg("账户已锁定，请联系管理员！");
      } else if (exception instanceof CredentialsExpiredException) {
         error.setMsg("密码已过期，请联系管理员！");
      } else if (exception instanceof AccountExpiredException) {
         error.setMsg("账户已过期，请联系管理员！");
      } else if (exception instanceof DisabledException) {
         error.setMsg("账户被禁用，请联系管理员!");
      } else if (exception instanceof BadCredentialsException) {
         error.setMsg("用户名或密码错误，请重新输入！");
      }
      String s = new ObjectMapper().writeValueAsString(error);
      out.write(s);
      out.flush();
      out.close();
   }
}
```

- 登出成功的自定义处理器

```java
@Component
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {
   @Override
   public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
      response.setContentType("application/json;charset=utf-8");
      PrintWriter out=response.getWriter();
      out.write(new ObjectMapper().writeValueAsString(RespBean.ok("注销成功！")));
      out.flush();
      out.close();
   }
}
```

## 测试回顾

我们现在登录用户的接口已经整合了security实现了自定义从数据库查询用户详细信息，并且完成了接口的认证成功、失败、登出成功的后续自定义处理

> 进行一个测试

但是考虑到数据库中插入的数据的密码都是进行BCrypt加密过的，我们无法解密，这可怎么办捏？

————我们为什么不自己插入测试数据😅😅😅😅😅😅😅😅😅😅😅😅😅😅

![image-20220215101433533](https://gitee.com/TrevorLink666/picgo/raw/master/images/202202151014660.png)

> 使用UsernamePasswordAuthenticationFilter保留节目

不同于我们之前的认证策略，我们这边使用的是另一种认证策略；其实不同的项目每个人的认证方式是不同的，但是其实核心的认证思路都是要按照security的实现思路来的

我们如果沿用UsernamePasswordAuthenticationFilter，就不用自己再写controller了，那之前要写的controller的时候注入的`AuthenticationManager`应该怎么办？——这就被替换成了SecurityConfig里的configure方法

![image-20220215123116914](https://gitee.com/TrevorLink666/picgo/raw/master/images/202202151231013.png)

`configure(AuthenticationManagerBuilder)`用于通过允许AuthenticationProvider容易地添加来建立认证机制，也就是去**自定义一种认证的方式**

比如下方代码，用户的数据不从数据库读取，直接手动赋予写死

```java
AuthenticationManagerBuilder allows 
    public void configure(AuthenticationManagerBuilder auth) {
        auth
            .inMemoryAuthentication()
            .withUser("user")
            .password("password")
            .roles("USER")
        .and()
            .withUser("admin")
            .password("password")
            .roles("ADMIN","USER");
}
```

> 一定要开启@MapperScan！

由于我们这边引入了Mybatis，因此在主启动类上一定要开启@MapperScan扫描，不然就会报出下面的错

![img](https://gitee.com/TrevorLink666/picgo/raw/master/images/202202151247977.png)

# 验证码验证

## 实现思路

由于我们这边是没有使用自定义的登录controller，还是使用security自带的`UsernamePasswordAuthenticationFilter`

因此假如我们想要实现验证码的这个登录附加功能，需要在这个Filter之前再加上一层专门处理验证码验证的Filter

同时另一个方面我们既然要生成验证码，就需要一个特定的`LoginController`去生成登录的验证码；**在生成验证码的同时将验证码信息存入Session中**

- 在这个Filter中主要去做验证码的校验
  - 如果是登录的表单我们才拦截，否则放行
  - 获取到用户传递的验证码参数
  - 将生成验证码时存入Session中的正确答案取出，进行比较
    - 如果有错误就向客户端返回错误的JSON
    - 匹配成功才放行

## 验证码工具类

```java
public class VerificationCode {

  private int width = 100;// 生成验证码图片的宽度
  private int height = 30;// 生成验证码图片的高度
  private String[] fontNames = { "宋体", "楷体", "隶书", "微软雅黑" };
  private Color bgColor = new Color(255, 255, 255);// 定义验证码图片的背景颜色为白色
  private Random random = new Random();
  private String codes = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private String text;// 记录随机字符串

  /**
   * 获取一个随意颜色
   *
   * @return
   */
  private Color randomColor() {
    int red = random.nextInt(150);
    int green = random.nextInt(150);
    int blue = random.nextInt(150);
    return new Color(red, green, blue);
  }

  /**
   * 获取一个随机字体
   *
   * @return
   */
  private Font randomFont() {
    String name = fontNames[random.nextInt(fontNames.length)];
    int style = random.nextInt(4);
    int size = random.nextInt(5) + 24;
    return new Font(name, style, size);
  }

  /**
   * 获取一个随机字符
   *
   * @return
   */
  private char randomChar() {
    return codes.charAt(random.nextInt(codes.length()));
  }

  /**
   * 创建一个空白的BufferedImage对象
   *
   * @return
   */
  private BufferedImage createImage() {
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2 = (Graphics2D) image.getGraphics();
    g2.setColor(bgColor);// 设置验证码图片的背景颜色
    g2.fillRect(0, 0, width, height);
    return image;
  }

  public BufferedImage getImage() {
    BufferedImage image = createImage();
    Graphics2D g2 = (Graphics2D) image.getGraphics();
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < 4; i++) {
      String s = randomChar() + "";
      sb.append(s);
      g2.setColor(randomColor());
      g2.setFont(randomFont());
      float x = i * width * 1.0f / 4;
      g2.drawString(s, x, height - 8);
    }
    this.text = sb.toString();
    drawLine(image);
    return image;
  }

  /**
   * 绘制干扰线
   *
   * @param image
   */
  private void drawLine(BufferedImage image) {
    Graphics2D g2 = (Graphics2D) image.getGraphics();
    int num = 5;
    for (int i = 0; i < num; i++) {
      int x1 = random.nextInt(width);
      int y1 = random.nextInt(height);
      int x2 = random.nextInt(width);
      int y2 = random.nextInt(height);
      g2.setColor(randomColor());
      g2.setStroke(new BasicStroke(1.5f));
      g2.drawLine(x1, y1, x2, y2);
    }
  }

  public String getText() {
    return text;
  }

  public static void output(BufferedImage image, OutputStream out) throws IOException {
    ImageIO.write(image, "JPEG", out);
  }
}
```

## 验证码生成的接口

- 生成验证码，存入验证码答案到Session中

```java
@Controller
@Slf4j
public class LoginController {
   /**
    * 获取登录的验证码图片
    * @param response
    * @param session
    * @throws IOException
    */
   @GetMapping("/verifyCode")
   public void getVerifyCode(HttpServletResponse response, HttpSession session) throws IOException {
      VerificationCode code = new VerificationCode();
      //根据工具类生成验证码
      BufferedImage image = code.getImage();
      String text = code.getText();
      log.debug("生成的验证码为：{}",text);
      //将验证码的答案保存到Session中，如果有就覆盖
      session.setAttribute("verify_code",text);
      VerificationCode.output(image,response.getOutputStream());
   }
}

```

## 校验验证码Filter

在这个Filter中主要去做验证码的校验

- 如果是登录的表单我们才拦截，否则放行
- 获取到用户传递的验证码参数
- 将生成验证码时存入Session中的正确答案取出，进行比较
  - 如果有错误就向客户端返回错误的JSON
  - 匹配成功才放行

```java
@Component
public class VerificationCodeFilter extends GenericFilter {
   @Override
   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
      HttpServletRequest httpServletRequest = (HttpServletRequest) request;
      HttpServletResponse httpServletResponse = (HttpServletResponse) response;
      //如果是登录的表单我们才拦截，否则放行
      if ("POST".equals(httpServletRequest.getMethod()) && "/doLogin".equals(httpServletRequest.getServletPath())) {
         //获取请求参数中的验证码（用户传来的）
         String code = httpServletRequest.getParameter("code");
         //从Session中获取之前登录的时候存储的验证码正确答案
         String verify_code = (String) httpServletRequest.getSession().getAttribute("verify_code");
         httpServletResponse.setContentType("application/json;charset=utf-8");
         PrintWriter writer = httpServletResponse.getWriter();
         try {
            //验证是否相同,如果有错误就向客户端返回错误的JSON
            if (!code.equalsIgnoreCase(verify_code)) {
               //输出json
               writer.write(new ObjectMapper().writeValueAsString(RespBean.error("验证码错误！")));
            } else {
               //匹配成功才放行
               chain.doFilter(request, response);
            }
         } catch (NullPointerException e) {
            writer.write(new ObjectMapper().writeValueAsString(RespBean.error("请求异常，请重新请求！")));
         } finally {
            writer.flush();
            writer.close();
         }
      } else {
         chain.doFilter(request, response);
      }
   }
}
```

## 配置拦截器

拦截器写好了我们需要在SecurityConfig中进行配置，其实和之前我们的自定义JWT的过滤器配置是一样的

```java
   //总体配置
   @Override
   protected void configure(HttpSecurity http) throws Exception {
      //将验证码过滤器添加在用户名密码过滤器的前面
      http.addFilterBefore(verificationCodeFilter, UsernamePasswordAuthenticationFilter.class);
   }
```

这边一定要记得把验证码的获取接口给他放行，不要被Security保护了！

```java
   //忽略"/login","/verifyCode"请求，该请求不需要进入Security的拦截器
   @Override
   public void configure(WebSecurity web) throws Exception {
      web.ignoring().antMatchers("/login", "/verifyCode");
   }

```

## 测试回顾

> 测试

我们现在进行测试，首先向验证码接口发起请求来获取验证码

![image-20220215155359731](https://gitee.com/TrevorLink666/picgo/raw/master/images/202202151553859.png)

- 如果密码输入错误就进到我们自己的认证失败处理器

![image-20220215155444350](https://gitee.com/TrevorLink666/picgo/raw/master/images/202202151554498.png)

- 如果密码输入正确就从`UsernamePasswordAuthenticationFilter`里进到我们自己的认证成功处理器

![image-20220215155558914](https://gitee.com/TrevorLink666/picgo/raw/master/images/202202151555025.png)

> 回顾总结

由于我们不是自定义controller而是选择沿用Security的表单提交默认控制器，那么我们**如果想要额外扩展验证码校验的业务，就需要自己写一个专门处理验证码的拦截器**

# 群聊和系统广播后台实现

## 实现思路

> 聊天记录存到数据库

其实聊天室的聊天主要是由前端来进行聊天，前端写的比较的多，**后端仅仅只是作为一个中间人转发消息**

- 服务端接收到聊天内容就**保存到数据库**
- 前端监听一个消息的通道，收到消息就保存数据到LocalStorage

> 系统广播

后端还应该具有系统广播的功能，为了实现这个需求，我们的思路是：

- 专门注册一个消息代理通道topic来播报系统的欢迎消息和群聊消息
- **当用户登录和登出的时候都应该有系统的广播**——自定义认证成功和登出成功的处理器里去追加；我们的实体类中当前的用户是有一个状态信息，这个就是表示我们当前的用户是否是在线
  - 如果用户认证成功，还需要去数据库中设置当前用户的状态为在线
  - 如果用户登出成功，还需要去数据库中设置当前用户的状态为离线

## 消息实体类

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupMsgContent implements Serializable {
   private static final long serialVersionUID = 980328865610261046L;
   /**
    * 消息内容编号
    */
   private Integer id;
   /**
    * 发送者的编号
    */
   private Integer fromId;
   /**
    * 发送者的昵称
    */
   private String fromName;
   /**
    * 发送者的头像
    */
   private String fromProfile;
   /**
    * 消息发送时间
    */
   @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
   private Date createTime;
   /**
    * 消息内容
    */
   private String content;
   /**
    * 消息类型编号
    */
   private Integer messageTypeId;
}
```

## WebSocket配置

这个配置类十分滴重要，基本上后端聊天室的功能都是在这里配置的；重点主要是分成了两个部分

- 注册Stomp站点，前端通过这个站点连接到我们的后端WebSocket服务
- 配置我们**主动推送**的消息代理域（前端监听这个通道，我们中转后主动从这里推送）

```java
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
   //注册Stomp端点（后端WebSocket的服务地址）
   @Override
   public void registerStompEndpoints(StompEndpointRegistry registry) {
      registry.addEndpoint("/ws/ep")//将地址/ws/ep注册为端点，前端连接了这个端点就可以进行WebSocket通讯
              .setAllowedOrigins("*")//支持任何跨域请求
              .withSockJS();//支持前端用SocketJS连接
   }

   //配置消息代理域，可以配置多个，给用户发消息的通道（前端监听）
   @Override
   public void configureMessageBroker(MessageBrokerRegistry registry) {
      registry.enableSimpleBroker("/topic","/queue");
   }
}

```

如果项目引入了JWT的令牌，则还需要重写`configureClientInboundChannel`这个方法，在这个方法里实现对JWT的认证（详见云E办）

## 聊天controller

这个controller不同于我们普通的controller，不能直接上`@RestController`

```java
@Controller
public class WsController {
   @Autowired
   private SimpMessagingTemplate simpMessagingTemplate;
   @Autowired
   private GroupMsgContentService groupMsgContentService;
   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

   /**
    * 群聊消息的接收和转发
    *
    * @param authentication
    * @param groupMsgContent
    */
   @MessageMapping("/ws/groupChat")
   public void handleGroupMessage(Authentication authentication, GroupMsgContent groupMsgContent) {
      //从Security中获取当前登录的用户信息
      User user = (User) authentication.getPrincipal();
      //设置转发的消息信息
      groupMsgContent.setFromId(user.getId());
      groupMsgContent.setFromName(user.getNickname());
      groupMsgContent.setFromProfile(user.getUserProfile());
      groupMsgContent.setCreateTime(new Date());
      //保存群聊消息到数据库中
      groupMsgContentService.insert(groupMsgContent);
      //转发数据
      simpMessagingTemplate.convertAndSend("/topic/greetings", groupMsgContent);
   }
}
```

在上面的代码中，我们取出当前登录用户的认证信息，之后补充群聊的消息信息，最后转发给前端订阅监听的通道

但是在中转的时候我们还可以存储到数据库中

## 聊天记录存到数据库中

注意到我们的WsController是需要引用GroupMsgContentService的

> Mapper

```java
@Repository
public interface GroupMsgContentMapper {
   int insert(GroupMsgContent groupMsgContent);
}
```

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.yep.server.mapper.GroupMsgContentMapper">

    <!--新增群聊消息记录-->
    <insert id="insert" keyProperty="id" useGeneratedKeys="true">
        insert into tim_remake.group_msg_content(from_id, from_name, from_profile, create_time, content, message_type_id)
        values (#{fromId}, #{fromName}, #{fromProfile}, #{createTime}, #{content}, #{messageTypeId})
    </insert>
</mapper>
```

> Service

- 接口

```java
public interface GroupMsgContentService {
   int insert(GroupMsgContent groupMsgContent);
}
```

- 实现类

```java
@Service
public class GroupMsgContentServiceImpl implements GroupMsgContentService {
   @Autowired
   private GroupMsgContentMapper groupMsgContentMapper;
   @Override
   public int insert(GroupMsgContent groupMsgContent) {
      return groupMsgContentMapper.insert(groupMsgContent);
   }
}
```

## 系统广播实现

> 认证成功的自定义处理器追加

```java
@Component
@Slf4j
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
   @Autowired
   private  UserService userService;
   @Autowired
   private SimpMessagingTemplate simpMessagingTemplate;
   //登录成功返回公共JSON
   @Override
   public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
      response.setContentType("application/json;charset=utf-8");
      PrintWriter out=response.getWriter();
      User user=(User) authentication.getPrincipal();
      log.debug("当前登录的用户信息：{}",user);
      //密码不要给前端
      user.setPassword(null);
      //登录成功后在聊天室里更新用户的状态为在线
      userService.setUserStateToOn(user.getId());
      user.setUserStateId(1);
      //广播系统通知消息
      log.debug("系统消息：欢迎用户【"+user.getNickname()+"】进入聊天室");
      simpMessagingTemplate.convertAndSend("/topic/notification","系统消息：欢迎用户【"+user.getNickname()+"】进入聊天室");
      RespBean respBean = RespBean.ok("登录成功", user);
      String s = new ObjectMapper().writeValueAsString(respBean);
      out.write(s);
      out.flush();
      out.close();
   }
}
```

> 自定义登出成功处理器追加

```java
@Component
@Slf4j
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {
   @Autowired
   private UserService userService;
   @Autowired
   private SimpMessagingTemplate simpleMessagingTemplate;
   @Override
   public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
      User user = (User) authentication.getPrincipal();
      log.debug("当前登录的用户信息：{}",user);
      //用户登出时设置用户的状态为离线
      userService.setUserStateToLeave(user.getId());
      //系统广播
      log.debug("系统消息：用户【"+user.getNickname()+"】润了");
      simpleMessagingTemplate.convertAndSend("/topic/notification","系统消息：用户【"+user.getNickname()+"】润了");
      response.setContentType("application/json;charset=utf-8");
      PrintWriter out=response.getWriter();
      out.write(new ObjectMapper().writeValueAsString(RespBean.ok("注销成功！")));
      out.flush();
      out.close();
   }
}
```

## 测试和回顾

别忘了在我们的Security配置里把WebSocket的请求放行

![image-20220215203439473](https://gitee.com/TrevorLink666/picgo/raw/master/images/202202152034581.png)

# FastDFS

## 简介

> 是什么

FastDFS是一种**分布式文件管理服务**

FastDFS 专为互联网应用量身定做，**解决大容量文件存储问题**，追求高性能和高扩展性，它可以看做是`基于文件的 key/value 存储系统`，key 为文件 ID，value 为文件内容，因此称作分布式文件存储服务更为合适

> 为什么需要FastDFS

传统的企业级开发**对于高并发要求不是很高**，而且数据量可能也不大，在这样的环境下文件管理可能非常容易

而FastDFS可以处理文件大容量存储以及高性能访问带来的问题

> FastDFS系统架构

作为一款分布式文件管理系统，FastDFS 主要包括四个方面的功能：

- 文件存储
- 文件同步
- 文件上传
- 文件下载

FastDFS的系统架构主要可以分成两大部分：Tracker和Storage

![图片](C:/Users/28459/Videos/Desktop/202202161137661.webp)

其中Tracker用来追踪文件，相当于**起到一个索引的作用**；而Storage才是实际存储文件的部分；我们上传的文件最终是保存在Storage里，文件的元数据信息保存在Tracker上；可以**通过Tracker实现对Storage的负载均衡**

在上图我们可以知道，Tracker搭建成了一个集群，而Storage也搭建成了一个集群；

一个`Storage Cluster`可以由多个组构成，不同的组之间不进行通信

一个组又相当于一个小的集群，组由多个 Storage Server 组成，组内的 Storage Server 会通过连接进行文件同步来保证高可用

## 安装

具体可以参见这篇教程

[FastDFS介绍和安装教程](https://mp.weixin.qq.com/s?__biz=MzI1NDY0MTkzNQ==&mid=2247487963&idx=1&sn=8db3c404f1135454f11c5cb1296309c0&chksm=e9c343bbdeb4caad9d2402605188c1cdfd6621563be6fdc49db8c86e32d83919c06cadf957b3&mpshare=1&scene=1&srcid=06063v6GHs4FaBo7LAC9Rt8z&sharer_sharetime=1591459036374&sharer_shareid=f53a915bc3b1fa72db2df3401850d069&key=4444be66eadb67b13bc616f7f56b36656222c21759ebf97def1e43a34b84ef24c4bc9a7ade6ede38796cbcba9824a421fa9943d82d7ea00956e9e1c029f9eb7a04273c6382ce52c71cd127e43bb8d94e&ascene=1&uin=MjM4MjQ4MDEwNg%3D%3D&devicetype=Windows+10+x64&version=62090070&lang=zh_CN&exportkey=A3VbOfyCoEUPN62wWHswzUE%3D&pass_ticket=n9h1mEezwxgSaI3D%2BWD0%2FPTBS6KqdVGSD38sWGfl%2BE%2B6rlXekUjNhW4IXDwmYSlp)

启动了tracker和storage后我们就可以进行图片的上传了，但是一般如果是图片上传，我们还需要一个提供图片访问的接口，最佳的解决方案是`Nginx`，具体的教程可以参见这个博客

[Nginx 极简入门教程](https://mp.weixin.qq.com/s?__biz=MzI1NDY0MTkzNQ==&mid=2247485682&idx=1&sn=19213ba9e2924de455426ff58759e016&scene=21#wechat_redirect)

安装好Nginx了启动

![image-20220216162333674](https://gitee.com/TrevorLink666/picgo/raw/master/images/202202161623762.png)

之后我们整合FastDFS和Nginx，引入**fastdfs-nginx-module**后再次配置完毕重新启动Nginx的时候爆出了错误`nginx: [emerg] bind() to 0.0.0.0:80 failed (98: Address already in use)`

原因好像是我们第一次安装纯净Nginx的时候没有关闭，导致80端口被占用，解决措施：[nginx启动 nginx: [emerg] bind() to 0.0.0.0:80 failed (98: Address already in use)](https://blog.csdn.net/qq_39313596/article/details/115858965)

## 客户端调用

现在我们可以使用Java的客户端进行文件的上传和下载了

> 引入依赖

````xml
        <!--FastDFS-->
        <dependency>
            <groupId>net.oschina.zcx7878</groupId>
            <artifactId>fastdfs-client-java</artifactId>
            <version>1.27.0.0</version>
        </dependency>
````

> 编写配置文件

注意我们默认的Tracker和Storage的配置文件是存放在`/etc/fdfs/`目录下的（如果按照教程来），Java这里的配置文件内容一定要和`tracker.config`和`storage.conf`中保持一致，最主要的就是去配置`storage.config`中的tracker的服务地址和端口，这个在我们的专属配置文件里可以看到

- fastdfs_client.properties

```properties
## fastdfs-client.properties

fastdfs.connect_timeout_in_seconds = 5
fastdfs.network_timeout_in_seconds = 30

fastdfs.charset = UTF-8

fastdfs.http_anti_steal_token = false
fastdfs.http_secret_key = FastDFS1234567890
fastdfs.http_tracker_http_port = 80

#Tracker的地址和端口，都在配置文件里
fastdfs.tracker_servers =192.168.111.135:22122

## Whether to open the connection pool, if not, create a new connection every time
fastdfs.connection_pool.enabled = true

## max_count_per_entry: max connection count per host:port , 0 is not limit
fastdfs.connection_pool.max_count_per_entry = 500

## connections whose the idle time exceeds this time will be closed, unit: second, default value is 3600
fastdfs.connection_pool.max_idle_time = 3600

## Maximum waiting time when the maximum number of connections is reached, unit: millisecond, default value is 1000
fastdfs.connection_pool.max_wait_time_in_ms = 1000
```

> 封装工具类

```java
public class FastDFSUtil {
   private static StorageClient1 client1;
   private  static final Logger logger = LoggerFactory.getLogger(FastDFSUtil.class);
   static{
      try{
         ClientGlobal.initByProperties("fastdfs-client.properties");
         TrackerClient trackerClient=new TrackerClient();
         TrackerServer trackerServer=trackerClient.getConnection();
         client1=new StorageClient1(trackerServer,null);
      } catch (Exception e) {
         logger.debug("文件上传初始化失败！：{}",e.getMessage());
      }
   }
   public static String upload(MultipartFile file) throws IOException, MyException {
      //获取上传过来的文件名
      String oldName=file.getOriginalFilename();
      //返回上传到服务器的路径
      //文件拓展名oldName.substring(oldName.lastIndexOf(".")+1)
         return client1.upload_file1(file.getBytes(),oldName.substring(oldName.lastIndexOf(".")+1),null);
   }
}
```

> 编写Controller

来一个文件上传的controller

```java
@RestController
@Slf4j
public class FileController {
   @Value("${fastdfs.nginx.host}")
   String nginxHost;

   /**
    * 上传文件，返回指定的文件路径
    *
    * @param file
    * @return
    * @throws IOException
    * @throws MyException
    */
   @PostMapping("/file")
   public String uploadFile(MultipartFile file) throws IOException, MyException {
      log.debug("收到的文件是否为空：{}", file == null);
      if (file != null) log.debug("收到的文件名：{}", file.getOriginalFilename());
      String fileId = FastDFSUtil.upload(file);
      return nginxHost + fileId;
   }
}
```

在我们的application.yaml中配置服务器的IP地址`nginxHost`（可以是虚拟机也可以是服务器，用来在controller中调用文件上传工具类拼接结果返回给前端）

```yaml
fastdfs:
  nginx:
    host: http://192.168.111.135/
```

## 测试

我们分别启动tracker和storage的服务（为了方便也是因为没有多余的服务器，我们这边就都部署在同一台服务器上）

之后使用postman测试接口[关于如何测试文件上传的接口我们可以参考这篇](https://www.cnblogs.com/mzq123/p/11516151.html)

之后服务端报错了，报了一个org.csource.common.MyException: getStoreStorage fail, errno code: 2，[具体解决可以看这个博客](https://blog.csdn.net/cdebai/article/details/93916192)；原因可能在于第一次安装的时候启动了tracker和storage，但是关联整合nginx之后没有重新启动storage和tracker，导致无法连接到tracker，重启一下就好了

> 捋一下步骤

首先我们需要启动tracker和storage的服务，在已经安装好配置好nginx模块的情况下输入下面的命令

```bash
service fdfs_trackerd start
service fdfs_storaged start
```

之后启动nginx，nginx的目录在`/usr/local/sbin`下

```bash
cd /usr/local/sbin #cd到nginx录下
./nginx #启动nginx服务
```

之后我们就可以正常访问了

![image-20220217161617408](https://gitee.com/TrevorLink666/picgo/raw/master/images/202202171616773.png)

![image-20220217161622733](https://gitee.com/TrevorLink666/picgo/raw/master/images/202202171616846.png)

# 用户管理实现

管理员实现对聊天室用户的权限管理，主要实现的接口功能如下：

- 用户注册
  - 注册接口
  - 检查用户名是否可用
  - 检查昵称是否可用
- 用户信息
  - 查询单条用户信息
  - 分页查询用户信息
  - 更新用户的锁定状态
  - 删除用户
    - 单一删除
    - 批量删除

这边开始直接引入Mybatis-Plus了，快速开发

![在这里插入图片描述](https://gitee.com/TrevorLink666/picgo/raw/master/images/202202181527190.png)

## 引入Mybatis

> 依赖

```xml
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>3.5.1</version>
        </dependency>
```

> 配置文件

我们要把之前maybatis的修改成mybatis-plus的

```yaml
mybatis-plus:
  mapper-locations: classpath:mapper/*.xml
  type-aliases-package: com.yep.server.pojo
  configuration:
    map-underscore-to-camel-case: true
```

> 分页插件

想要实现分页就要先配置分页插件的拦截器

```java
@Configuration
public class MybatisConfig {
   @Bean
   public MybatisPlusInterceptor mybatisPlusInterceptor() {
      MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
  //创建分页拦截器
      PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor();
  //设置拦截器的各项参数
      // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
      paginationInterceptor.setOverflow(true);
      // 设置最大单页限制数量，默认 500 条，-1 不受限制
      paginationInterceptor.setMaxLimit(500L);
  //把分页拦截器加入到大的拦截器中
      mybatisPlusInterceptor.addInnerInterceptor(paginationInterceptor);
      return mybatisPlusInterceptor;
   }
}
```

## 基本骨架搭建

我们需要搭建基本CRUD的接口骨架

> mapper

```java
@Repository
public interface UserMapper extends BaseMapper<User> {}
```

> Service

```java
public interface UserService  extends IService<User> {}
```

```java
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService,UserDetailsService  {}
```

## 接口实现

> 用户注册

前端传来的不完整，我们需要在controller中补全用户信息后再进行插入

由于用户表的数据主键是自增长的，因此我们需要在实体类中先对Id字段标注一下

![image-20220218095644509](https://gitee.com/TrevorLink666/picgo/raw/master/images/202202180956582.png)

- 注册核心接口

```java
 /**
    * 用户注册
    *
    * @param user
    * @return
    */
   @PostMapping("/register")
   public RespBean addUser(User user) {
      BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
      //对用户密码进行加密后存入数据库
      user.setPassword(encoder.encode(user.getPassword()));
      user.setUserStateId(2);
      user.setEnabled(true);
      user.setLocked(false);
      if (userService.save(user)) return RespBean.ok("注册成功！");
      else return RespBean.error("注册失败！");
   }
```

- 检查用户名是否可用

```java

   /**
    * 检查用户名是否可用
    *
    * @param username
    * @return
    */
   @GetMapping("/checkUsername")
   public Integer checkUsername(@RequestParam("username") String username) {
      QueryWrapper wrapper = new QueryWrapper();
      wrapper.eq("username", username);
      long count = userService.count(wrapper);
      return Math.toIntExact(count);
   }
```

- 检查昵称是否可用

```java
   /**
    * 检查昵称是否可用
    *
    * @param nickname
    * @return
    */
   @GetMapping("/checkNickname")
   public Integer checkNickname(@RequestParam("nickname") String nickname) {
      QueryWrapper wrapper = new QueryWrapper();
      wrapper.eq("nickname", nickname);
      long count = userService.count(wrapper);
      return Math.toIntExact(count);
   }
```

>查询单条用户信息

```java
   /**
    * 查询单条用户信息
    *
    * @param id
    * @return
    */
   @GetMapping("selectOne")
   public User selectOne(Integer id) {
      QueryWrapper wrapper = new QueryWrapper();
      wrapper.eq("id", id);
      return userService.getOne(wrapper);
   }
```

> 分页查询用户信息

```java
   /**
    * 管理员端查询用户的分页信息
    *
    * @param page     当前页数
    * @param size     每页查询的记录
    * @param keyword  关键词，用于搜索筛选
    * @param isLocked 是否锁定，用于搜索筛选
    * @return
    */
   @GetMapping("/")
   public RespPageBean getAllUserByPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                        @RequestParam(value = "size", defaultValue = "10") Integer size,
                                        String keyword, Integer isLocked) {
      log.debug("page:{}",page);
      log.debug("size:{}",size);
      Page<User> pageModel = new Page<>(page, size);
      QueryWrapper<User> wrapper = new QueryWrapper<>();
      if (isLocked != null) wrapper.eq("is_locked", isLocked);
      if (keyword != null) wrapper.like("nickname", keyword);
      Page<User> pageRes = userService.page(pageModel, wrapper);
      List<User> users = pageRes.getRecords();
      long count = userService.count(wrapper);
      return new RespPageBean(count, users);
   }
```

> 更新用户的锁定状态

**注意这里由于引入了Security，用户是否被禁用是被Security所托管的，因此我们需要在调用接口之前传入的实体类中，他的enable属性不可以为null，否则会产生空指针的异常！**

```java
 @PutMapping("/")
   public RespBean changeLockedStatus(@RequestParam("id") Integer id, Boolean isLocked) {
      log.debug("即将修改的用户id:{}",id);
      log.debug("即将修改的isLocked:{}",isLocked);
      User user = new User();
      user.setId(id);
      user.setLocked(isLocked);
      user.setEnabled(true);
      log.debug("即将修改后的用户信息：{}",user);
      boolean update = userService.updateById(user);
      if (update) return RespBean.ok("修改用户状态成功！");
      else return RespBean.error("修改用户状态失败！");
   }
```

> 删除用户

- 单一删除

```java
   @DeleteMapping("/{id}")
   public RespBean deleteUser(@PathVariable Integer id) {
      boolean remove = userService.removeById(id);
      if (remove) return RespBean.ok("删除用户成功！");
      else return RespBean.error("删除用户失败！");
   }
```

- 批量删除

```java
   @DeleteMapping("/")
   public RespBean deleteUsers(Integer[] ids) {
      boolean remove = userService.removeByIds(Arrays.asList(ids));
      if (remove) return RespBean.ok("批量删除用户成功！");
      else return RespBean.error("批量删除用户失败！");
   }
```

注意这里测试接口的时候传入数组需要特殊的处理，具体可以看下面的这两个博客

[@RequestParam,@PathVariable和@RequestBody三者区别 ](https://www.cnblogs.com/chengxiaodi/p/11324611.html)

[使用postman传递数组调试](https://blog.csdn.net/weixin_45156676/article/details/105638465)

[Postman测试接口传入List类型的参数以及数组类型参数](https://blog.csdn.net/qq_41107231/article/details/106981432)

一种是接受JSON格式的数据，这个时候就需要对参数加上`@RequestBody`的注解

另一种是普通的格式，如果未说明都是`x-www-form-urlencoded`格式的数据

![image-20220218144547378](https://gitee.com/TrevorLink666/picgo/raw/master/images/202202181445452.png)

![image-20220218144559381](https://gitee.com/TrevorLink666/picgo/raw/master/images/202202181445483.png)

# 群聊记录管理

这是管理端的另一项功能，管理员可以查看群聊的消息记录

![在这里插入图片描述](https://gitee.com/TrevorLink666/picgo/raw/master/images/202202181527911.png)

主要的功能有：

- 群聊消息查看
  - 查看所有的群聊消息记录
  - 通过消息编号查询单条消息
  - 分页查询
    - 可以根据发送者昵称，消息类型，发送的时间范围进行消息的查询
- 群聊消息删除
  - 单条消息记录删除
  - 多条记录批量删除

## 群聊消息查看

> 所有群聊消息记录查看

```java
@GetMapping("/")
public List<GroupMsgContent> getAllGroupMsgContent() {
   return groupMsgContentService.list();
}
```

> 通过消息编号查询单条消息

```java
   @GetMapping("selectOne")
   public GroupMsgContent selectOne(Integer id) {
      return groupMsgContentService.getById(id);
   }
```

> 根据发送者昵称，消息类型，发送的时间范围进行分页消息的查询

```java
   @GetMapping("/page")
   public RespPageBean getAllGroupMsgContentByPage(@RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "size", defaultValue = "10") Integer size, String nickname, Integer type, Date[] dateScope) {
      Page<GroupMsgContent> pageModel = new Page<>(page, size);
      QueryWrapper<GroupMsgContent> wrapper = new QueryWrapper<>();
      if (nickname != null) wrapper.like("from_name", nickname);
      if (type != null) wrapper.eq("message_type_id", type);
      if (dateScope != null) wrapper.between("create_time", dateScope[0], dateScope[1]);
      Page<GroupMsgContent> res = groupMsgContentService.page(pageModel, wrapper);
      long total = groupMsgContentService.count(wrapper);
      return new RespPageBean(total, res.getRecords());
   }
```

## 群聊消息删除

> 单条消息记录删除

```java
   @DeleteMapping("/{id}")
   public RespBean deleteGroupMsgContentById(@PathVariable("id") Integer id) {
      boolean remove = groupMsgContentService.removeById(id);
      if (remove) return RespBean.ok("删除单条群聊消息成功！");
      else return RespBean.error("删除单条群聊消息失败！");
   }
```

> 多条群聊消息批量删除

```java
   @DeleteMapping("/")
   public RespBean deleteGroupMsgContentByIds(@RequestParam("ids") Integer[] ids) {
      boolean remove = groupMsgContentService.removeByIds(Arrays.asList(ids));
      if (remove) return RespBean.ok("批量删除群聊消息成功！");
      else return RespBean.error("批量删除群聊消息失败！");
   }
```

## 自定义格式转化器

针对分页查询中指定日期查询这个功能，前端传来的是字符串数组，可是我们后端的controller定义的参数是Date类型的数组

![image-20220218193722721](https://gitee.com/TrevorLink666/picgo/raw/master/images/202202181937796.png)

那么我们就需要对这个日期数组进行一个转化，把String类型的转化为Date类型的，用于SQL语句

# 导出聊天记录

## 快速上手

这个其实也是管理员端的一个功能，支持聊天记录的导出，导出为Excel表格

[具体如何快速使用EasyExcel可以参考这个](https://www.yuque.com/easyexcel/doc/write)

> 引入依赖

```xml
 <dependency>
      <groupId>com.alibaba</groupId>
      <artifactId>easyexcel</artifactId>
      <version>2.2.6</version>
</dependency>
```

> 编写实体类

由于我们需要反向写入到excel表格中，因此我们需要对实体类进行特殊处理

官方文档中举了这么一个Demo可以参考

```java
@Getter
@Setter
@EqualsAndHashCode
public class DemoData {
    @ExcelProperty("字符串标题")
    private String string;
    @ExcelProperty("日期标题")
    private Date date;
    @ExcelProperty("数字标题")
    private Double doubleData;
    /**
     * 忽略这个字段
     */
    @ExcelIgnore
    private String ignore;
}
```

```java
    private List<DemoData> data() {
        List<DemoData> list = ListUtils.newArrayList();
        for (int i = 0; i < 10; i++) {
            DemoData data = new DemoData();
            data.setString("字符串" + i);
            data.setDate(new Date());
            data.setDoubleData(0.56);
            list.add(data);
        }
        return list;
    }
```

最后就生成了这么个东西

![img](https://gitee.com/TrevorLink666/picgo/raw/master/images/202202182257780.png)



## 思路分析

由于我们想要把聊天记录导出，那么聊天记录中有的是文字信息，有的是图片信息，如果一定要把图片导出到生成的Excel中，我们应该如何处理捏？

大致上有两种处理，我们这里都去学习一下：

- 定义一个URL转换器，新键一个类`GroupMsgContentData`来**分开处理**普通的文本和图片URL
- 定义一个转化器同时处理图片和文本，如果图片处理出错就当作文本字符串处理

## URL转换器实现

> 新建一个转换Excel专属的实体类

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupMsgContentExcelData {
   @ExcelProperty("消息内容编号")
   private Integer id;
   @ExcelProperty("消息发送者编号")
   private  Integer fromId;
   @ExcelProperty("发消息的人的昵称")
   private String fromName;
   @ExcelIgnore//草 走 忽略！
   private URL fromProfile;
   @ExcelProperty("发送实现")
   @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
   private Date createTime;
   @ExcelProperty(value = {"内容","文本"})
   @ColumnWidth(50)
   private String textContent;
   @ExcelProperty(value = {"内容","图片"})
   @ColumnWidth(50)
   private URL imageContent;
   @ExcelIgnore//这个也要忽略
   private Integer messageTypeId;
}
```

> URL处理转换器

注意这里的转换器不是spring提供的，而是EasyExcel提供的转换器

```java
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.util.IoUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

public class MyUrlImageConverter implements Converter<URL> {
    @Override
    public Class supportJavaTypeKey() {
        return URL.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.IMAGE;
    }

    @Override
    public URL convertToJavaData(CellData cellData, ExcelContentProperty contentProperty,
                                 GlobalConfiguration globalConfiguration) {
        throw new UnsupportedOperationException("Cannot convert images to url.");
    }

    @Override
    public CellData convertToExcelData(URL value, ExcelContentProperty contentProperty,
                                       GlobalConfiguration globalConfiguration) throws IOException {
        InputStream inputStream = null;
        try {
            //开启连接
            URLConnection uc = value.openConnection();
            URL url  = null;
            //获取响应状态
            int statusCode = ((HttpURLConnection) uc).getResponseCode();
            switch (statusCode){
                case 200:
                    inputStream = value.openStream();
                    break;
                case 404:
                    //默认给一个图片
                    url = new URL("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598096095144&di=9a72ad26e83effb9341c711c9818b85f&imgtype=0&src=http%3A%2F%2Fpic.616pic.com%2Fys_bnew_img%2F00%2F11%2F69%2Fj2AjnHspwT.jpg");
                    inputStream = url.openStream();
                    break;
                default :
                    url = new URL("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598096095144&di=9a72ad26e83effb9341c711c9818b85f&imgtype=0&src=http%3A%2F%2Fpic.616pic.com%2Fys_bnew_img%2F00%2F11%2F69%2Fj2AjnHspwT.jpg");
                    inputStream = url.openStream();
                    break;
            }
            byte[] bytes = IoUtils.toByteArray(inputStream);
            byte[] compressBytes = ImgUtil.compressPicForScale(bytes,200, UUID.randomUUID().toString());
            return new CellData(compressBytes);
        }catch (ConnectException exception){
            //捕获下链接异常
            URL url = new URL("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598096095144&di=9a72ad26e83effb9341c711c9818b85f&imgtype=0&src=http%3A%2F%2Fpic.616pic.com%2Fys_bnew_img%2F00%2F11%2F69%2Fj2AjnHspwT.jpg");
            inputStream = url.openStream();
            byte[] bytes = IoUtils.toByteArray(inputStream);
            return new CellData(bytes);
        }catch (FileNotFoundException fileNotFoundException){
            URL url = new URL("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598096095144&di=9a72ad26e83effb9341c711c9818b85f&imgtype=0&src=http%3A%2F%2Fpic.616pic.com%2Fys_bnew_img%2F00%2F11%2F69%2Fj2AjnHspwT.jpg");
            inputStream = url.openStream();
            byte[] bytes = IoUtils.toByteArray(inputStream);
            return new CellData(bytes);
        }finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }
}
```

> 使用Thumbnails压缩图片至指定的大小

- 首先需要引入依赖

```xml
        <dependency>
            <groupId>net.coobird</groupId>
            <artifactId>thumbnailator</artifactId>
            <version>0.4.8</version>
        </dependency>
```

- 之后编写压缩图片的工具类PicUtils

```java
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
 
/**
 * 图片压缩Utils
 *
 * @author worstEzreal
 * @version V1.1.0
 * @date 2018/3/12
 */
public class PicUtils {
 
    private static Logger logger = LoggerFactory.getLogger(PicUtils.class);
 
//    public static void main(String[] args) throws IOException {
//        byte[] bytes = FileUtils.readFileToByteArray(new File("D:\\1.jpg"));
//        long l = System.currentTimeMillis();
//        bytes = PicUtils.compressPicForScale(bytes, 300, "x");// 图片小于300kb
//        System.out.println(System.currentTimeMillis() - l);
//        FileUtils.writeByteArrayToFile(new File("D:\\dd1.jpg"), bytes);
//    }
 
    /**
     * 根据指定大小压缩图片
     *
     * @param imageBytes  源图片字节数组
     * @param desFileSize 指定图片大小，单位kb
     * @param imageId     影像编号
     * @return 压缩质量后的图片字节数组
     */
    public static byte[] compressPicForScale(byte[] imageBytes, long desFileSize, String imageId) {
        if (imageBytes == null || imageBytes.length <= 0 || imageBytes.length < desFileSize * 1024) {
            return imageBytes;
        }
        long srcSize = imageBytes.length;
        double accuracy = getAccuracy(srcSize / 1024);
        try {
            while (imageBytes.length > desFileSize * 1024) {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream(imageBytes.length);
                Thumbnails.of(inputStream)
                        .scale(accuracy)
                        .outputQuality(accuracy)
                        .toOutputStream(outputStream);
                imageBytes = outputStream.toByteArray();
            }
            logger.info("【图片压缩】imageId={} | 图片原大小={}kb | 压缩后大小={}kb",
                    imageId, srcSize / 1024, imageBytes.length / 1024);
        } catch (Exception e) {
            logger.error("【图片压缩】msg=图片压缩失败!", e);
        }
        return imageBytes;
    }
 
    /**
     * 自动调节精度(经验数值)
     *
     * @param size 源图片大小
     * @return 图片压缩质量比
     */
    private static double getAccuracy(long size) {
        double accuracy;
        if (size < 900) {
            accuracy = 0.85;
        } else if (size < 2047) {
            accuracy = 0.6;
        } else if (size < 3275) {
            accuracy = 0.44;
        } else {
            accuracy = 0.4;
        }
        return accuracy;
    }
 
}
```

> 导出文档接口

我们需要给群聊消息暴露一个导出文档的接口

这里用上了stream流函数式编程

```java
@GetMapping("/download")
   public void exportExcel(HttpServletResponse response) throws IOException {
      response.setContentType("application/vnd.ms-excel");
      response.setCharacterEncoding("utf-8");
      //设置文件信息，URLEncoder.encode可以防止中文乱码
      String fileName = URLEncoder.encode("群聊记录", "UTF-8").replaceAll("\\+", "%20");
      response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
      //我们先获取到所有的群聊记录，之后同意转化成为Excel专属的记录实体
      List<GroupMsgContent> groupMsgContents = getAllGroupMsgContent();
      List<GroupMsgContentExcelData> collect = groupMsgContents.stream().map(item -> {
          try {
            return GroupMsgContent.convertToGroupMsgContentExcelData(item);
         } catch (MalformedURLException e) {
            e.printStackTrace();
         }
         return new GroupMsgContentExcelData();
      }).collect(Collectors.toList());
      EasyExcel.write(response.getOutputStream(),GroupMsgContent.class).sheet("sheet1").doWrite(groupMsgContents);
   }
```

> 测试

由于我们数据库里的URL记录本身是之前作者创建的，他把图片放在公网服务器上的文件服务器里，现在访问不了，因此会报404，导致我们的转化器在解析URL类的时候无法进一步进行下去，也无法压缩图片，因此测试不了

## 如果图片处理失败就当作字符串处理

> 定义一个converter同时处理文本内容和图片内容

```java
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.util.IoUtils;
import com.yep.server.utils.PicUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

/**
 * 针对数据库中群聊信息的内容统一处理转换器
 */
public class MyContentConverter implements Converter<String> {
    @Override
    public Class supportJavaTypeKey() {
        return String.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.IMAGE;
    }

    @Override
    public String convertToJavaData(CellData cellData, ExcelContentProperty contentProperty,
                                    GlobalConfiguration globalConfiguration) {
        throw new UnsupportedOperationException("Cannot convert images to string");
    }

    /**
     * 如果图片的URL解析成功，就返回图片，否则就返回的是URL地址字符串
     * @param value
     * @param contentProperty
     * @param globalConfiguration
     * @return
     * @throws IOException
     */
    @Override
    public CellData convertToExcelData(String value, ExcelContentProperty contentProperty,
                                       GlobalConfiguration globalConfiguration) throws IOException {
        if (value.startsWith("http")){
            InputStream inputStream = null;
            URL imageUrl = new URL(value);
            try {
                //开启连接
                URLConnection uc = imageUrl.openConnection();
                URL url  = null;
                //获取响应状态
                int statusCode = ((HttpURLConnection) uc).getResponseCode();
                switch (statusCode){
                    case 200:
                        inputStream = imageUrl.openStream();
                        break;
                    default :
                        //直接当成String处理
                        return new CellData(value);
                }
                byte[] bytes = IoUtils.toByteArray(inputStream);
                //压缩图片
                byte[] compressBytes = PicUtils.compressPicForScale(bytes,200, UUID.randomUUID().toString());
                return new CellData(compressBytes);
            }catch (Exception exception){
                return new CellData(value);
            }finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }

        }
        else{
            return new CellData(value);
        }
    }
}
```

> 在实体类中指定转化器

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupMsgContent implements Serializable {
   private static final long serialVersionUID = 980328865610261046L;
   /**
    * 消息内容编号
    */
   @ExcelProperty("消息内容编号")
   private Integer id;
   /**
    * 发送者的编号
    */
   @ExcelProperty("发送消息者的编号")
   private Integer fromId;
   /**
    * 发送者的昵称
    */
   @ExcelProperty("发送者的昵称")
   private String fromName;
   /**
    * 发送者的头像
    */
   @ExcelIgnore
   private String fromProfile;
   /**
    * 消息发送时间
    */
   @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
   @ExcelProperty("消息发送时间")
   private Date createTime;
   /**
    * 消息内容
    */
   @ExcelProperty(value = "消息发送内容",converter = MyContentConverter.class)
   @ColumnWidth(50)
   private String content;
   /**
    * 消息类型编号
    */
   @ExcelIgnore
   private Integer messageTypeId;
}
```

> 测试

调用接口的时候还是等了好久，debug一下发现好像还是因为我们数据库的消息记录里URL是无法请求的，因此导致程序阻塞了

启动本地虚拟机的FASTDFS服务之后把数据库的图片路径改成可以访问的试一试

修改了数据库中消息的图片的访问路径全部该文本地文件服务器上http://192.168.111.135/group1/M00/00/00/wKhvh2IQYsuAFsnGAAbJ8YhM44I374.png

再次调用接口，等了好久终于导出成功

![image-20220219112748913](https://gitee.com/TrevorLink666/picgo/raw/master/images/202202191127093.png)

## 不对图片进行处理直接当字符串

前面的两种受限于URL是否可以访问的因素，导致如果URL不可以访问会影响用户体验，因此我们这边直接对图片的消息记录不做任何的处理，直接导出

> 消息实体类

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupMsgContent implements Serializable {
   private static final long serialVersionUID = 980328865610261046L;
   /**
    * 消息内容编号
    */
   @ExcelProperty("消息内容编号")
   private Integer id;
   /**
    * 发送者的编号
    */
   @ExcelProperty("发送消息者的编号")
   private Integer fromId;
   /**
    * 发送者的昵称
    */
   @ExcelProperty("发送者的昵称")
   private String fromName;
   /**
    * 发送者的头像
    */
   @ExcelIgnore
   private String fromProfile;
   /**
    * 消息发送时间
    */
   @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
   @ExcelProperty("消息发送时间")
   private Date createTime;
   /**
    * 消息内容
    */
   @ExcelProperty(value = "消息发送内容")
   @ColumnWidth(50)
   private String content;
   /**
    * 消息类型编号
    */
   @ExcelIgnore
   private Integer messageTypeId;
}
```

> 接口

```java
   /**
    * 导出群聊记录为Excel文档
    * 针对聊天内容不进行特殊处理了，图片URL就写入Excel中了，速度太慢了
    * @param response
    */
   @GetMapping("/download")
   public void exportExcel(HttpServletResponse response) throws IOException {
      response.setContentType("application/vnd.ms-excel");
      response.setCharacterEncoding("utf-8");
      //设置文件信息，URLEncoder.encode可以防止中文乱码
      String fileName = URLEncoder.encode("群聊记录", "UTF-8").replaceAll("\\+", "%20");
      response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
      //我们先获取到所有的群聊记录，之后同意转化成为Excel专属的记录实体
      List<GroupMsgContent> groupMsgContents = getAllGroupMsgContent();     EasyExcel.write(response.getOutputStream(),GroupMsgContent.class).sheet("sheet1").doWrite(groupMsgContents);
   }
```

> 测试

这下速度确实非常地快

![image-20220219113342094](https://gitee.com/TrevorLink666/picgo/raw/master/images/202202191133416.png)

# 单聊实现

## 回顾

我们回顾一下之前的多人聊天，其实配置已经配置好了，和我们的单聊是一样的，我们首先最核心的地方就在于我们的`WebSocketConfig`配置类中定义的消息接收站点

```java
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
   //注册Stomp端点（后端WebSocket的服务地址）
   @Override
   public void registerStompEndpoints(StompEndpointRegistry registry) {
      registry.addEndpoint("/ws/ep")//将地址/ws/ep注册为端点，前端连接了这个端点就可以进行WebSocket通讯
              .setAllowedOrigins("*")//支持任何跨域请求
              .withSockJS();//支持前端用SocketJS连接
   }

   //配置消息代理域，可以配置多个，给用户发消息的通道（前端监听）
   @Override
   public void configureMessageBroker(MessageBrokerRegistry registry) {
      registry.enableSimpleBroker("/topic","/queue");
   }
}

```

## 具体功能

单聊这边我们就仅仅只是简单的转发，就不去进行数据库的存储了

在用户登录之后获取除了这个用户之外的所有用户（对他而言这个所有的用户就是好友了）进行聊天

## 代码实现

> 消息实体类

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
  private String from;
  private String to;
  private String content;
  private Date createTime;
  private String fromNickname;
  private String fromUserProfile;
  private Integer messageTypeId;
}
```

> 获取好友列表

```java
@RestController
@RequestMapping("/chat")
public class ChatController {
   @Autowired
   private UserService userService;

   @GetMapping("/users")
   public List<User> getAllUsersWithoutCurrentUser() {
      return userService.getAllUsersWithoutCurrentUser();
   }
}
```

```java
   @Override
   public List<User> getAllUsersWithoutCurrentUser() {
      //获取到当前登录的用户对象
      User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      Integer id = user.getId();
      QueryWrapper<User> wrapper = new QueryWrapper<>();
      wrapper.ne("id",id);
      return userMapper.selectList(wrapper);
   }
```

> 开始聊天

```java
   @MessageMapping("/ws/chat")
   public void handleMessage(Authentication authentication, Message message) {
      User user = (User) authentication.getPrincipal();
      log.debug("当前登录的用户信息：{}", user);
      log.debug("接受到前端传来的单聊消息：{}", message);
      //设置转发的消息信息
      message.setFrom(user.getUsername());
      message.setCreateTime(new Date());
      message.setFromNickname(user.getNickname());
      simpMessagingTemplate.convertAndSendToUser(message.getTo(),"/queue/chat",message);
   }
```

# 邮件验证码

## 场景需求分析

我们这边想要实现的是管理员端如果想要登录进行用户管理和群聊记录管理还有消息记录的导出，还需要在登录的时候接收邮件验证码

![image-20220219204454405](https://gitee.com/TrevorLink666/picgo/raw/master/images/202202192044520.png)

## 快速测试

> 新建模块

![image-20220219214312466](https://gitee.com/TrevorLink666/picgo/raw/master/images/202202192143624.png)

> 引入依赖

```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-mail</artifactId>
        </dependency>
```

> 配置发送服务信息

```properties
#SMTP服务器地址
spring.mail.host=smtp.qq.com
#SMTP服务器端口
spring.main.port=587
#发送者的用户名（邮箱）
spring.mail.username=2845964844@qq.com
#发送方的授权码
spring.mail.password=#申请得到的授权码
#编码格式
spring.mail.default-encoding=UTF-8
spring.mail.properties.mail.smtp.socketFactory.class=javax.net.ssl.SSLSocketFactory
#debug，输出邮件发送的过程
spring.mail.properties.mail.debug=true
```

> 测试类测试

```java
@SpringBootTest
public class MailTest {

   @Autowired
   public JavaMailSender javaMailSender;
   @Value("${spring.mail.username}")
   private  String from;
   //测试邮件发送（只能在本地跑，服务器好像八行）
   @Test
   public  void testMailSend(){
      SimpleMailMessage msg = new SimpleMailMessage();
      //邮件的主题
      msg.setSubject("这是测试邮件主题");
      //邮件的内容
      msg.setText("这是测试邮件内容:\nsecond try");
      //邮件的发送方，对应配置文件中的spring.mail.username
      msg.setFrom(from);
      //邮件发送时间
      msg.setSentDate(new Date());
      //邮件接收方
      msg.setTo("2845964844@qq.com");
      javaMailSender.send(msg);
   }
}
```

![image-20220219214413293](https://gitee.com/TrevorLink666/picgo/raw/master/images/202202192144481.png)

原地TP成功

## 管理端验证码模块实现

客户端界面发送“/admin/mailVerifyCode”请求，即发送随机的4位验证码到特定的邮箱中，并且把这四位验证码添加到当前请求会话(session)中

> 提供管理端登录时获取验证码接口

- Controller，第一个是普通用户登录的图形验证码，可以忽略

```java
@Controller
@Slf4j
public class LoginController {
   @Autowired
   private VerifyCodeService verifyCodeService;

   /**
    * 获取登录的验证码图片
    *
    * @param response
    * @param session
    * @throws IOException
    */
   @GetMapping("/verifyCode")
   public void getVerifyCode(HttpServletResponse response, HttpSession session) throws IOException {
      VerificationCode code = new VerificationCode();
      //根据工具类生成验证码
      BufferedImage image = code.getImage();
      String text = code.getText();
      log.debug("生成的验证码为：{}", text);
      //将验证码的答案保存到Session中，如果有就覆盖
      session.setAttribute("verify_code", text);
      VerificationCode.output(image, response.getOutputStream());
   }

   @GetMapping("/admin/mailVerifyCode")
   public RespBean getAndSendAdminVerifyCodeMail(HttpSession session) {
      //生成随机验证码
      String admin_verifyCode = verifyCodeService.getCode();
      //保存到Session中
      session.setAttribute("admin_verifyCode", admin_verifyCode);
      //根据验证码发送邮件
      verifyCodeService.sendVerifyCodeEmail();
      return  RespBean.ok("邮件发送成功！");
   }
}
```

- Service

这一段后面需要引入RabbitMQ修改，所以看看就好，最后不会这么写的

```java
public interface VerifyCodeService {
   String getCode();

   void sendVerifyCodeEmail();

}
```

```java
public void sendVerifyCodeEmail(){
   //邮件内容
    SimpleMailMessage msg = new SimpleMailMessage();
    msg.setSubject("微言聊天室管理端验证码验证");
    msg.setText("本次登录的验证码："+code);
    msg.setFrom("1258398543@qq.com");
    msg.setSentDate(new Date());
    msg.setTo("jinhaihuang824@aliyun.com");
    //保存验证码到本次会话
    session.setAttribute("mail_verify_code",code.toString());
    //发送到邮箱
    try {
      javaMailSender.send(msg);
      return RespBean.ok("验证码已发送到邮箱，请注意查看！");
    }catch (Exception e){
      e.printStackTrace();
      return RespBean.error("服务器出错啦！请稍后重试！");
    }
}
```

> 多个Security配置类

那么由于我们引入了Security，并且没有自定义登录的接口，其实也挺方便的，这个时候就需要**自定义多个SecurityConfigAdapter对象**，这里定义一个包含多个自定义Security配置类的大类

首先在大类中使用`@EnableWebSecurity`开启WebSecurity

之后定义两个静态内部类，分别针对管理员和普通用户的登录进行不同的security配置，两者都主要是分成以下的部分

- 在配置类中重写参数为`AuthenticationManagerBuilder`的configure方法来实现从数据库中获取登录信息，需要Admin和User实体类实现UserDetails接口，Service去实现UserDetailsService接口去重写`loadUserByUsername`方法；之后只要通过`auth.userDetailsService`注入Service的Bean即可
- 在配置类中重写参数为`WebSecurity`的configure方法来设置Security拦截请求的路径
- 在配置类中重写参数为`HttpSecurity`的configure方法来设置有关登录登出暴露的接口，已经登录成功/失败或者是登出成功的处理器，同时也可以配置Security过滤器链的位置关系

```java
@EnableWebSecurity
public class MultipleSecurityConfig {
   //虽然有两种不同的用户认证策略但是还是少不了密码加密捏
   @Bean
   public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }
   @Configuration
   @Order(1)
   public static class AdminSecurityConfig extends WebSecurityConfigurerAdapter{
      @Autowired
      AdminServiceImpl adminService;
      @Autowired
      VerificationCodeFilter verificationCodeFilter;
      @Autowired
      MyAuthenticationFailureHandler myAuthenticationFailureHandler;
      @Autowired
      MyLogoutSuccessHandler myLogoutSuccessHandler;
      @Autowired
      AdminAuthenticationSuccessHandler adminAuthenticationSuccessHandler;

      //管理员登录的用户名和密码验证服务
      @Override
      protected void configure(AuthenticationManagerBuilder auth) throws Exception {
         auth.userDetailsService(adminService);
      }

      //忽略"/login","/verifyCode"请求，该请求不需要进入Security的拦截器
      @Override
      public void configure(WebSecurity web) throws Exception {
         web.ignoring().antMatchers("/css/**","/fonts/**","/img/**","/js/**","/favicon.ico","/index.html","/admin/login","/admin/mailVerifyCode");
      }
      //http请求验证和处理规则，响应处理的配置
      @Override
      protected void configure(HttpSecurity http) throws Exception {
         //将验证码过滤器添加在用户名密码过滤器的前面
         http.addFilterBefore(verificationCodeFilter, UsernamePasswordAuthenticationFilter.class);
         http.antMatcher("/admin/**").authorizeRequests()
                 .anyRequest().authenticated()
                 .and()
                 .formLogin()
                 .usernameParameter("username")
                 .passwordParameter("password")
                 .loginPage("/admin/login")
                 .loginProcessingUrl("/admin/doLogin")
                 //成功处理
                 .successHandler(adminAuthenticationSuccessHandler)
                 //失败处理
                 .failureHandler(myAuthenticationFailureHandler)
                 .permitAll()//返回值直接返回
                 .and()
                 //登出处理
                 .logout()
                 .logoutUrl("/admin/logout")
                 .logoutSuccessHandler(myLogoutSuccessHandler)
                 .permitAll()
                 .and()
                 .csrf().disable()//关闭csrf防御方便调试
                 //没有认证时，在这里处理结果，不进行重定向到login页
                 .exceptionHandling().authenticationEntryPoint(new AuthenticationEntryPoint() {
            @Override
            public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
               httpServletResponse.setStatus(401);
            }
         });
      }
   }
   @Configuration
   @Order(2)
   public static class UserSecurityConfig extends WebSecurityConfigurerAdapter {
      @Autowired
      private UserServiceImpl userService;
      @Autowired
      private  VerificationCodeFilter verificationCodeFilter;
      @Autowired
      private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
      @Autowired
      private MyAuthenticationFailureHandler myAuthenticationFailureHandler;
      @Autowired
      private  MyLogoutSuccessHandler myLogoutSuccessHandler;

      //验证服务，设置从数据库中读取普通用户信息
      @Override
      protected void configure(AuthenticationManagerBuilder auth) throws Exception {
         auth.userDetailsService(userService);
      }


      //忽略"/login","/verifyCode"请求，该请求不需要进入Security的拦截器
      @Override
      public void configure(WebSecurity web) throws Exception {
         web.ignoring().antMatchers("/login","/verifyCode","/file","/user/register","/user/checkUsername","/user/checkNickname");
      }
      //登录验证
      @Override
      protected void configure(HttpSecurity http) throws Exception {
         //将验证码过滤器添加在用户名密码过滤器的前面
         http.addFilterBefore(verificationCodeFilter, UsernamePasswordAuthenticationFilter.class);
         http.authorizeRequests()
                 .anyRequest().authenticated()
                 .and()
                 .formLogin()
                 .usernameParameter("username")
                 .passwordParameter("password")
                 .loginPage("/login")
                 .loginProcessingUrl("/doLogin")
                 //成功处理
                 .successHandler(myAuthenticationSuccessHandler)
                 //失败处理
                 .failureHandler(myAuthenticationFailureHandler)
                 .permitAll()//返回值直接返回
                 .and()
                 //登出处理
                 .logout()
                 .logoutUrl("/logout")
                 .logoutSuccessHandler(myLogoutSuccessHandler)
                 .permitAll()
                 .and()
                 .csrf().disable()//关闭csrf防御方便调试
                 //没有认证时，在这里处理结果，不进行重定向到login页
                 .exceptionHandling().authenticationEntryPoint(new AuthenticationEntryPoint() {
            @Override
            public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
               httpServletResponse.setStatus(401);
            }
         });
      }
   }
}
```

> 配置拦截器

配置类种需要验证码拦截器，注意我们这里需要**配置两种身份拦截器**，一个是普通用户登录的时候进行图形验证码的验证，另一个是管理端登录的邮件验证码验证

由于我们之前对图形验证码已经处理过了，现在只需要额外判断一下即可（注意我们Security中的匹配是普通用户的登录接口是`/doLogin`，而管理员登录才是`/admin/doLogin`

```java
@Component
public class VerificationCodeFilter extends GenericFilter {
   @Override
   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
      HttpServletRequest httpServletRequest = (HttpServletRequest) request;
      HttpServletResponse httpServletResponse = (HttpServletResponse) response;
      //如果是登录的表单我们才拦截，否则放行
      if ("POST".equals(httpServletRequest.getMethod()) && "/doLogin".equals(httpServletRequest.getServletPath())) {
         //获取请求参数中的验证码（用户传来的）
         String code = httpServletRequest.getParameter("code");
         //从Session中获取之前登录的时候存储的验证码正确答案
         String verify_code = (String) httpServletRequest.getSession().getAttribute("verify_code");
         httpServletResponse.setContentType("application/json;charset=utf-8");
         PrintWriter writer = httpServletResponse.getWriter();
         try {
            //验证是否相同,如果有错误就向客户端返回错误的JSON
            if (!code.equalsIgnoreCase(verify_code)) {
               //输出json
               writer.write(new ObjectMapper().writeValueAsString(RespBean.error("验证码错误！")));
            } else {
               //匹配成功才放行
               chain.doFilter(request, response);
            }
         } catch (NullPointerException e) {
            writer.write(new ObjectMapper().writeValueAsString(RespBean.error("请求异常，请重新请求！")));
         } finally {
            writer.flush();
            writer.close();
         }
      } else if ("POST".equals(httpServletRequest.getMethod())&&"/admin/doLogin".equals(httpServletRequest.getServletPath())){
         //获取输入的验证码
         String mailCode = request.getParameter("mailCode");
         //获取session中保存的验证码
         String verifyCode = ((String) httpServletRequest.getSession().getAttribute("admin_verifyCode"));
         //构建响应输出流
         response.setContentType("application/json;charset=utf-8");
         PrintWriter printWriter =response.getWriter();
         try {
            if(!mailCode.equals(verifyCode)){
               printWriter.write(new ObjectMapper().writeValueAsString(RespBean.error("验证码错误！")));
            }else {
               chain.doFilter(request,response);
            }
         }catch (NullPointerException e){
            printWriter.write(new ObjectMapper().writeValueAsString(RespBean.error("请求异常，请重新请求！")));
         }finally {
            printWriter.flush();
            printWriter.close();
         }
      }
      else {
         chain.doFilter(request,response);
      }
   }
}
```

> 补充有关Admin的MVC层业务

- Controller

```java
@RestController
@RequestMapping("admin")
public class AdminController {
   @Autowired
   private AdminService adminService;

   @RequestMapping("/selectOne")
   public Admin selectOne(Integer id){
      return adminService.getById(id);
   }
}
```

- Service

```java
public interface AdminService extends IService<Admin> {}
```

```java
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin>implements AdminService, UserDetailsService {
   @Autowired
   private AdminMapper adminMapper;

   /**
    * 使用Security从数据库中获取Admin对象
    * @param username
    * @return
    * @throws UsernameNotFoundException
    */
   @Override
   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      QueryWrapper<Admin> wrapper = new QueryWrapper<>();
      wrapper.eq("username",username);
      Admin admin = adminMapper.selectOne(wrapper);
      if(admin==null) throw  new UsernameNotFoundException("该管理员不存在！");
      return  admin;
   }
}
```

- Mapper

```java
@Repository
public interface AdminMapper extends BaseMapper<Admin> {}
```

# 引入RabbitMQ确保邮件投递的可靠实现

## 前置知识

使用RabbitMQ作为邮件发送的中间件来保证消息可靠性的实现

首先需要了解RabbitMQ整个消息投递的路径：

`Producer->Broker Cluster->Exchange->Queue->Consumer`

其中：

- Message从Producer到Broker Cluster则会返回一个confirmCallback

- Message从Exchange到Queue则会返回一个returnCallback

在程序运行过程中可能造成消息丢失的情况：

1.在消息发送到交换机过程中出错，交换机不存在。

2.消息发送到交换机，但是找不到对应的队列

3.生产者调用convertAndSend方法报错，如MQ服务器宕机，消费端找不到适用的方法处理生产者发送的消息。

4.消息成功发送到交换机和绑定的队列，但是在消息消费过程中产生了异常，消息不能成功消费。

对于上述情况的解决方案:

- 生产者：开启confirm模式
  - confirmCallBack：消息从生产者到达`exchange`时返回`ack`，消息未到达`exchange`返回`nack`
  - returnCallBack：消息进入`exchange`但未进入`queue`时会被调用
- 开启RabbitMQ持久化
  - 持久化队列以及持久化消息，需要**声明队列的时候指定durable属性来持久化队列**；发送消息的时候指定消息的属性来持久化消息
- 消费者：关闭RabbitMQ自动ACK

## 需求分析

我们这里希望的需求是**用户可以发送对系统的反馈**，那么我们这部分可以把他拿来和邮件验证码一起来实现，都让放入消息队列中处理

同时为了确保消息百分之百可以准确传输，我们这边需要在生产者增加定时任务，轮询查询数据库中异常状态的任务，重新发送

## 数据库表设计

> 消息反馈表

![image-20220221104011607](https://gitee.com/TrevorLink666/picgo/raw/master/images/202202211040685.png)

![image-20220221104117327](https://gitee.com/TrevorLink666/picgo/raw/master/images/202202211041390.png)

>邮件发送日志表

用于记录MQ消息发送的日志

![image-20220221104040636](https://gitee.com/TrevorLink666/picgo/raw/master/images/202202211040743.png)

![image-20220221104133892](https://gitee.com/TrevorLink666/picgo/raw/master/images/202202211041954.png)

## 业务框架搭建

针对用户的反馈以及管理员接收邮件的任务，我们单独交给消息队列来处理，起到应用解耦的作用

> 实体类

- 用户消息反馈实体类

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Feedback implements Serializable {
   private static final long serialVersionUID = 550979088670747783L;

   private String id;

   private String userId;

   private String username;

   private String nickname;

   private String content;

}
```

- 消息队列发送日志表实体类

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailSendLog implements Serializable {
   private static final long serialVersionUID = 740872026109078508L;

   private String msgId;
   /**
    * 1:反馈，2:验证码
    */
   private Integer contentType;

   private String content;

   private Integer status;

   private String routeKey;

   private String exchange;

   private Integer count;

   private Date tryTime;

   private Date createTime;

   private Date updateTime;
}
```

- 邮件投递状态的实体类（常量类）

```java
public class MailConstants {
    /**
     * 消息投递中
     */
    public static final  Integer DELIVERING=0;
    /**
     * 消息投递成功
     */
    public static final  Integer SUCCESS=1;
    /**
     * 消息投递失败
     */
    public static final  Integer FAILURE=2;
    /**
     * 最大重试次数
     */
    public static final  Integer MAX_TRY_COUNT=3;
    /**
     * 消息超时时间
     */
    public static final  Integer MEG_TIMEOUT=1;
    /**
     * 消息类型为反馈
     */
    public static final Integer FEEDBACK_TYPE=1;
    /**
     * 消息类型为验证码
     */
    public static final Integer VERIFY_CODE_TYPE=2;
}
```

> Mapper

```java
@Repository
public interface FeedbackMapper extends BaseMapper<Feedback> {}

@Repository
public interface MailSendLogMapper extends BaseMapper<MailSendLog> {}
```

> Service

- 接口

```java
public interface FeedbackService extends IService<Feedback> {}

public interface MailSendLogService extends IService<MailSendLog> {}
```

- 实现类

```java
@Service
public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper, Feedback> implements FeedbackService {}

@Service
public class MailSendLogServiceImpl extends ServiceImpl<MailSendLogMapper, MailSendLog> implements MailSendLogService {}
```

> Controller

```java
@RestController
@RequestMapping("mail")
public class MailController {
   @Autowired
   private FeedbackService feedbackService;

   @RequestMapping("feedback")
   public RespBean sendFeedbackToMail(@RequestBody Feedback feedback){
         feedback.sendFeedbackToMail(feedback);
         return RespBean.ok("邮件已经发送给管理员！感谢宁的反馈！");
   }
}
```

## 消息生产者

#### RabbitMQTemplate配置

> 首先我们需要更改消息生产者开启confirm模式

```properties
#将消息确认投递到交换机的确认类型改为交互
spring.rabbitmq.publisher-returns=true
spring.rabbitmq.publisher-confirm-type=correlated
```

> 自定义RabbitMQTemplate来处理消息的发送

自定义confirmCallback和returnCallback的方法实现，这里直接使用Lambda表达式来处理，确实效率高

```java
@Configuration
@Slf4j
public class RabbitMQConfig {
   @Autowired
   private CachingConnectionFactory cachingConnectionFactory;
   @Autowired
   private MailSendLogService mailSendLogService;
   @Bean
   public RabbitTemplate rabbitTemplate(){
      RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
      //设置rabbitTemplate中消息从生产者到达exchange的回调函数
      rabbitTemplate.setConfirmCallback((data,ack,cause)->{
         String msgId = data.getId();
         if(ack){
            log.info(msgId+"消息发送到交换机成功！");
            //修改数据库中的邮件日志记录，消息发送成功就设置status为1
            MailSendLog log = new MailSendLog();
            log.setMsgId(msgId);
            log.setStatus(MailConstants.SUCCESS);
            mailSendLogService.updateById(log);
         }else{
            log.error("消息投递到交换机失败！");
         }
      });
      rabbitTemplate.setReturnCallback((msg,repCode,repText,exchange,routingKey)->{
         log.error("{}---消息从交换机投递到队列中失败！具体原因：{}",msg.getBody(),repText);
         log.error("发生错误的交换机：{}发生错误的路由key：{}",exchange,routingKey);
      });
      return rabbitTemplate;
   }
}
```

### 开启定时任务

> 增加注解@EnableScheduling

如果我们想要开始Spring的定时任务，就需要在启动类上增加注解`@EnableScheduling`

![image-20220221193455629](https://gitee.com/TrevorLink666/picgo/raw/master/images/202202211935759.png)

> 建立定时任务

建立定时任务，查询表中不能成功发送的消息，进行再次发送

```java
@Component
public class MailSendTask {
   @Autowired
   private MailSendLogService mailSendLogService;
   @Autowired
   private RabbitTemplate rabbitTemplate;
   @Scheduled(cron = "0/10 * * * * ?")
   public void mailSendTask(){
      //TODO 这里每十秒钟就查询一次日志表，可以考虑使用视图和索引提高查询的效率
      //①获取到日志表中所有未正常投递的日志
      QueryWrapper<MailSendLog> wrapper = new QueryWrapper<>();
      wrapper.eq("status", MailConstants.DELIVERING);
      List<MailSendLog> sendLogs = mailSendLogService.list(wrapper);
      UpdateWrapper<MailSendLog> updateWrapper = new UpdateWrapper<>();
      //ForEach遍历集合
      sendLogs.forEach(mailSendLog -> {
         //②如果超过了最大尝试发送的次数还是发不出去就更新这条发送的日志的发送状态为发送失败！
         if (mailSendLog.getCount()>MailConstants.MAX_TRY_COUNT){
            updateWrapper.eq("msgId",mailSendLog.getMsgId());
            MailSendLog temp = new MailSendLog();
            temp.setStatus(MailConstants.FAILURE);
            mailSendLogService.update(temp, updateWrapper);
         }else{
            //③更新消息投递的尝试次数和时间
            updateWrapper.eq("msgId",mailSendLog.getMsgId());
            MailSendLog temp = new MailSendLog();
            temp.setTryTime(new Date());
            temp.setCount(mailSendLog.getCount()+1);
            mailSendLogService.update(temp,updateWrapper);
            //④获取到消息并再次投递
               String message = mailSendLog.getContent();
               rabbitTemplate.convertAndSend(mailSendLog.getExchange(),mailSendLog.getRouteKey(),message,new CorrelationData(mailSendLog.getMsgId()));
         }
      });
   }
}
```

### 反馈邮件

> 作为生产者把反馈信息发送到消息队列中

我们上面的MailController只允许用户发送反馈邮件，那具体实现还是看Service，我们还没写

- 首先设置反馈邮件存储在数据库的UUDI，之后插入到反馈邮件表的记录中
- 之后我们封装基本属性存储到`mail_send_log`的日志中
- 最后作为生产者使用`rabbitTemplate`投递消息到消息队列中

```java
@Service
public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper, Feedback> implements FeedbackService {
   @Autowired
   private FeedbackMapper feedbackMapper;

   @Autowired
   RabbitTemplate rabbitTemplate;

   @Value("${mail.exchange:mail-exchange}")
   private String mailExchange;

   @Value("${mail.queue.feedback:mail-queue-feedback}")
   private String mailQueueFeedback;

   @Value("${mail.route.feedback:mail-route-feedback}")
   private String mailRouteFeedback;

   @Autowired
   private MailSendLogService mailSendLogService;

   @Override
   public void sendFeedbackToMail(Feedback feedback) {
      //①设置反馈的ID编号为随机的UUID
      feedback.setId(UUID.randomUUID().toString());
          //向反馈表中存储数据
      feedbackMapper.insert(feedback);
      String json = JsonUtil.parseToString(feedback);
      //②添加消息发送日志的数据库记录
      String msgId=UUID.randomUUID().toString();
      MailSendLog sendLog = new MailSendLog();
      sendLog.setMsgId(msgId);
      sendLog.setContent(json);
      sendLog.setContentType(MailConstants.FEEDBACK_TYPE);
      sendLog.setCount(1);
      sendLog.setCreateTime(new Date());
      sendLog.setUpdateTime(new Date());
         //设置超过一分钟后开始重试
      sendLog.setTryTime(new Date(System.currentTimeMillis()+1000*60*MailConstants.MEG_TIMEOUT));
      sendLog.setExchange(mailExchange);
      sendLog.setRouteKey(mailRouteFeedback);
      sendLog.setStatus(MailConstants.DELIVERING);
          //新增消息发送的日志记录
      mailSendLogService.save(sendLog);
      //③投递消息
      rabbitTemplate.convertAndSend(mailExchange,mailRouteFeedback,json,new CorrelationData(msgId));
   }
}
```

### 验证码邮件

我们之前说的验证码邮件的发送其实还没有完成，主要也是需要保存到`mail_send_log`的日志中后再发送到消息队列中

```java
@Service
public class VerifyCodeServiceImpl implements VerifyCodeService {
   @Autowired
   RabbitTemplate rabbitTemplate;

   @Autowired
   MailSendLogService mailSendLogService;

   @Value("${mail.exchange:mail-exchange}")
   private String mailExchange;

   @Value("${mail.route.verifyCode:mail-route-verifyCode}")
   private String mailRouteVerifyCode;

   /**
    * 管理端登录随机生成四位验证码
    * @return
    */
   @Override
   public String getCode() {
      //获取随机的四个数字
      StringBuilder code = new StringBuilder();
      for (int i = 0; i < 4; i++) {
         code.append(new Random().nextInt(10));
      }
      return code.toString();
   }

   @Override
   public void sendVerifyCodeEmail(String code) {
      //①仙布着急去把验证码作为内容直接发送到消息队列中，先添加消息发送日志记录到数据库中
      String msgId = UUID.randomUUID().toString();
      MailSendLog mailSendLog = new MailSendLog();
      mailSendLog.setMsgId(msgId);
      mailSendLog.setContent(code);
      mailSendLog.setContentType(MailConstants.VERIFY_CODE_TYPE);
      mailSendLog.setCount(1);
      mailSendLog.setCreateTime(new Date());
      mailSendLog.setTryTime(new Date(System.currentTimeMillis()+1000*10*MailConstants.MEG_TIMEOUT));
      mailSendLog.setUpdateTime(new Date());
      mailSendLog.setExchange(mailExchange);
      mailSendLog.setRouteKey(mailRouteVerifyCode);
      mailSendLog.setStatus(MailConstants.DELIVERING);
      mailSendLogService.save(mailSendLog);
      //②生产者发送消息到消息队列，内容就是传入的验证码，前面这么做都是为了确保消息可靠性插入数据库的日志记录
      rabbitTemplate.convertAndSend(mailExchange,mailRouteVerifyCode,code,new CorrelationData(msgId));
   }
}
```

## 消息消费者

我们这里另一个模块TimChatRemake-mail就是用来作为消息的消费者来处理队列中的消息

这边由于只有两种的业务，一个是反馈内容，另一个是验证码；因此直接使用RabbitMQ的Routing路由模型即可，使用的交换机为`DirectExchange`

### 配置队列并绑定交换机

首先我们需要配置分别用于处理验证码和反馈消息的消息队列，然后绑定一个唯一的交换机

```java
@Configuration
public class RabbitMQConfig {
   @Value("${mail.exchange:mail-exchange}")
   private String mailExchange;

   @Value("${mail.queue.verifyCode:mail-queue-verifyCode}")
   private String mailQueueVerifyCode;

   @Value("${mail.route.verifyCode:mail-route-verifyCode}")
   private String mailRouteVerifyCode;

   @Value("${mail.queue.feedback:mail-queue-feedback}")
   private String mailQueueFeedback;

   @Value("${mail.route.feedback:mail-route-feedback}")
   private String mailRouteFeedback;


   @Bean
   DirectExchange mailExchange(){
      return new DirectExchange(mailExchange,true,false);
   }

   /**
    * 验证码消息队列
    * @return
    */
   @Bean
   Queue mailQueueVerifyCode(){
      //定义持久化队列
      return new Queue(mailQueueVerifyCode,true);
   }

   /**
    * 验证码队列绑定交换机并指定路由key
    * @return
    */
   @Bean
   Binding mailQueueVerifyCodeBinding(){
      return BindingBuilder.bind(mailQueueVerifyCode()).to(mailExchange()).with(mailRouteVerifyCode);
   }
   @Bean
   Queue mailQueueFeedback(){
      //定义持久化队列
      return new Queue(mailQueueFeedback,true);
   }
   /**
   反馈队列和交换机绑定并指定路由key
    */
   @Bean
   Binding mailQueueFeedbackBinding(){
      return BindingBuilder.bind(mailQueueFeedback()).to(mailExchange()).with(mailRouteFeedback);
   }
}
```

### 关闭消息自动ACK

同时我们还需要在生产者这里关闭消息自动确认，改为手动确认

```yaml
spring:
  rabbitmq:
    #开启手动确认是否消息消费成功
    listener:
      simple:
        acknowledge-mode: manual
        prefetch: 100
```

### 消息反馈接收

> 存在的问题

- 可能在业务流程中处理完了该条消息，但是后续的代码（和消费者无关）出现了异常导致，消息消费失败，消息回到队列中；那么消费者之后再处理的时候就会重复消费，业务重复处理
- 消费者在消费的时候如果进程挂掉了，那么消息还没消费完毕就没了，造成消息的丢失

> 如何解决

- 针对第一个问题，我们可以使用Redis的Hash作为缓存；如果消费成功了，就在Redis中以当前消息的ID为key，放入到哈希中；每次消费者在消费之前都去redis中看看能不能获取的到；如果能获取到就说明之前处理过了，就不要重复处理了
- 针对第二个问题，我们**让消费者手动确认消息**；这样如果消费者挂掉了，没有自动确认，会触发消息回到消息队列中

这才是最重要的部分，重点在于消费者消费成功**手动确认消息处理**：`channel.basicAck(tag,true)`，而消费者消费消息失败手动确认消息处理：`channel.backNack(tag,false,true)`

> 代码实现

```java
@Component
@Slf4j
public class FeedbackReceiver {
   @Autowired
   private JavaMailSender javaMailSender;
   @Autowired
   private RedisTemplate redisTemplate;
   /**
    * 监听反馈消息的消息队列
    */
   @RabbitListener(queues ="${mail.queue.feedback:mail-queue-feedback}")
   public void getFeedbackMessage(Message message, Channel channel) throws IOException {
      //获取到消息队列中的消息内容
      String s = message.getPayload().toString();
      MessageHeaders headers = message.getHeaders();
      Long tag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
      String msgId = headers.get("spring_returned_message_correlation").toString();
      log.info("【{}】---正在处理的消息",msgId);
      //如果此条消息已经在Redis中存在
      if(redisTemplate.opsForHash().entries("mail_log").containsKey(msgId)){
         //消费消息
         channel.basicAck(tag,true);
         log.info("消息id{}存在重复消费",msgId);
         return;
      }
      try {
         //将消息内容反序列化为Feedback实体
         Feedback feedback = JsonUtil.parseToObject(s, Feedback.class);
         log.info("即将发送的反馈消息内容：{}",feedback.getContent());
         SimpleMailMessage mailMessage = new SimpleMailMessage();
         mailMessage.setSubject("来自用户的意见反馈");
         //拼接邮件信息
         StringBuilder formatMessage = new StringBuilder();
         formatMessage.append("用户编号："+feedback.getUserId()+"\n");
         formatMessage.append("用户名："+feedback.getUsername()+"\n");
         formatMessage.append("用户昵称："+feedback.getNickname()+"\n");
         formatMessage.append("反馈内容："+feedback.getContent());
         log.info("即将发送的邮件信息：{}",formatMessage);
         //设置邮件消息
         mailMessage.setText(formatMessage.toString());
         mailMessage.setFrom("2845964844@qq.com");
         mailMessage.setTo("2845964844@qq.com");
         mailMessage.setSentDate(new Date());
         javaMailSender.send(mailMessage);
         //邮件发送完毕更新Redis的Hash
         redisTemplate.opsForHash().put("mail_log",msgId,feedback.getContent());
         //手动确认消息处理完毕
         channel.basicAck(tag,true);
      }catch (IOException e) {
         //发生异常就手动确认消息消费失败，返回队列中
         channel.basicNack(tag,false,true);
         log.info("消息【{}】重新返回消息队列中",msgId);
         e.printStackTrace();
      }
   }
}
```

### 邮件消息接收

和消息反馈一样，我们的邮件信息也是去使用redis来查询是否已经存在数据，如果有就不要重复消费了，之后我们发送验证码邮件

```java
@Component
@Slf4j
public class VerifyCodeReceiver {
   @Autowired
   private RedisTemplate redisTemplate;
   @Autowired
   private JavaMailSender javaMailSender;
   @RabbitListener(queues = "${mail.queue.verifyCode:mail-queue-verifyCode}")
   public void receiveVerifyCode(Message message, Channel channel) throws IOException {
      //获得队列中的消息，其实就是验证码
      String code = message.getPayload().toString();
      //获取消息头中的标签信息
      MessageHeaders headers = message.getHeaders();
      Long tag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
      //获取消息ID
      String msgId = headers.get("spring_returned_message_correlation").toString();
      log.info("正在处理的消息【{}】",msgId);
      //查看redis中是否存在当前消息
      if(redisTemplate.opsForHash().entries("mail_log").containsKey(msgId)){
         //手动确认消息被消费
         channel.basicAck(tag,true);
         log.info("消息【{}】被重复消费",msgId);
         return;
      }
      SimpleMailMessage msg = new SimpleMailMessage();
      msg.setSubject("管理员端登录验证码");
      msg.setText("本次登录的验证码："+code);
      msg.setFrom("2845964844@qq.com");
      msg.setSentDate(new Date());
      msg.setTo("2845964844@qq.com");
      javaMailSender.send(msg);
   }
}
```

# Emoji处理

## 需求分析

前端聊天需要发送Emoji图像，针对Emoji的处理我们后端是直接将其转化为Unicode处理编码

## 代码实现

> 引入依赖

```xml
        <!--Emoji-->
        <dependency>
            <groupId>com.github.binarywang</groupId>
            <artifactId>java-emoji-converter</artifactId>
            <version>0.1.1</version>
        </dependency>
```

> 在群聊中进行处理

```java
   @MessageMapping("/ws/groupChat")
   public void handleGroupMessage(Authentication authentication, GroupMsgContent groupMsgContent) {
      //从Security中获取当前登录的用户信息
      User user = (User) authentication.getPrincipal();
      log.debug("当前登录的用户信息：{}", user);
      log.debug("接受到前端传来的群聊消息：{}", groupMsgContent);
      //设置Emoji内容,转换成unicode编码
      groupMsgContent.setContent(emojiConverter.toHtml(groupMsgContent.getContent()));
      //设置转发的消息信息
      groupMsgContent.setFromId(user.getId());
      groupMsgContent.setFromName(user.getNickname());
      groupMsgContent.setFromProfile(user.getUserProfile());
      groupMsgContent.setCreateTime(new Date());
      //保存群聊消息到数据库中
      log.debug("即将保存的群聊消息：{}", groupMsgContent);
      groupMsgContentService.insert(groupMsgContent);
      //转发数据
      simpMessagingTemplate.convertAndSend("/topic/greetings", groupMsgContent);
   }
```

# 回顾总结

## StringUtils

在开源项目和别人的源码中大量地使用上了StringUtils等Spring封装的工具类，专门用来操作String的工具类，直接用即可

[Spring的StringUtils工具类](https://blog.csdn.net/milife2013/article/details/8057417)

主要用到的有：

- hasText
- hasLength

## @RequestParam、@RequestBody的区别

具体可以看这一篇博客

[@RequestBody和@RequestParam区别](https://cloud.tencent.com/developer/article/1497766?from=article.detail.1332866)

> @RequestParam

这个注解专门用来处理`Content-Type`为application/x-www-form-urlencoded编码的内容（值得注意的是：Http协议中，**默认传递的参数就是**application/x-www-form-urlencoded类型

RequestParam接收的参数是来自requestHeader中也就是**请求头**中的数据

RequestParam可以用于`GET`,`POST`,`DELETE`等请求

对于这个注解，如果是散装的参数，MVC默认加或者是不加都是会自动生效的，本质其实是获取request.getParameter的参数然后封装；但是这里我们是一个实体类，加了就会报错`Bad Request`

- 测试

`不加注解`

![image-20220224164157016](https://gitee.com/TrevorLink666/picgo/raw/master/images/202202241642301.png)

![image-20220224164225826](https://gitee.com/TrevorLink666/picgo/raw/master/images/202202241642013.png)

`加上注解`

![image-20220224165934424](https://gitee.com/TrevorLink666/picgo/raw/master/images/202202241659622.png)

![image-20220224170001430](https://gitee.com/TrevorLink666/picgo/raw/master/images/202202241700635.png)

- 最佳实践

@RequestParam其实主要是用于散装的请求数据，如果接口的参数**是一整个实体的话**不要加这个注解才可以正常封装；因为只有不加这个注解，最终的参数处理器才为`ServletModelAttributeMethodProcessor`（主要是把HttpServletRequest中的表单参数封装到MutablePropertyValues实例中，再通过参数类型实例化(通过**构造反射**创建User实例)，反射匹配属性进行值的填充）

- 适用场景
  - @RequestParam可以修饰作为GET/POST请求的参数,并且可以指定参数的名称，但是一般适用于散装的数据
  - @RequestParam有三个配置参数：
    - `required` 表示是否必须，默认为 `true`，必须
    - `defaultValue` 可设置请求参数的**默认值**
    - `value` 为接收url的参数名（相当于指定key值）
- 缺点
  - **不支持批量插入数据**，假如我们现在有一个需求是批量向数据库中插入数据吗，那么就不可以额这样了，就算是**不用注解也只是一次只能插入一条数据**

> @RequestBody

所以这就引出了我们的@RequestBody注解

不同于@RequestParam，恰恰相反Body支持的请求格式不是`application/x-www-form-urlencoded`或者是`application/form-data`，而是`application/json`格式的数据

- 测试

![image-20220224215236593](https://gitee.com/TrevorLink666/picgo/raw/master/images/202202242152987.png)

![img](https://gitee.com/TrevorLink666/picgo/raw/master/images/202202242152635.png)

- 最佳实践

使用注解@RequestBody可以将body里面所有的json数据传到后端，后端再进行解析

> 结合GET和POST进行总结

GET

- 不支持@RequestBody
- 支持@RequeParam

![image-20220224215903854](https://gitee.com/TrevorLink666/picgo/raw/master/images/202202242159069.png)

POST

- 都支持

---

> 结合两种注解

![image-20220224220102310](https://gitee.com/TrevorLink666/picgo/raw/master/images/202202242201545.png)

@RequestParam本质其实就是请求的参数，**只会考虑URL后面？的参数内容** 以及 `application/x-www-form-urlencoded`类型的内容

@RequestBody本质其实就是接收JSON格式的**请求参数**

> 结合content-type总结

![image-20220224220125162](https://gitee.com/TrevorLink666/picgo/raw/master/images/202202242201384.png)

## form-data、x-www-form-urlencoded的区别以及POSTMAN调试的参数格式

> 两者的区别

 [form-data与x-www-form-urlencoded的区别)](https://www.cnblogs.com/wbl001/p/12050751.html)

**application/x-www-form-urlencoded** 是最简单的表单提交格式，原生的form表单，不设置默认就是这个属性

这也是常见的post请求方式，一般用来**上传文件**，各大服务器的支持也比较好。所以我们使用表单 **上传文件** 时，必须让\<form>表单的enctype属性值为 multipart/form-data

> POSTMAN

form-data可以上传文件，也可以处理键值对数据；由于有boundary隔离，所以multipart/form-data既可以上传文件，也可以上传键值对，它采用了键值对的方式，所以可以上传多个文件

而x-www-form-urlencoded即application/x-www-from-urlencoded，将表单内的数据转换为Key-Value；键值对都是通过&间隔分开的

## EasyExcel

> 需求分析

针对项目中**导出群聊聊天记录**的功能，使用EasyExcel实现快速将数据库中的记录导出为Excel记录

由于发送的聊天记录中存在图片文件，如果直接导出的话亲测速度巨慢，所以最终决定导出图片只导出在文件服务器的资源地址，加快响应速度

> 实现

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupMsgContent implements Serializable {
   private static final long serialVersionUID = 980328865610261046L;
   /**
    * 消息内容编号
    */
   @ExcelProperty("消息内容编号")
   private Integer id;
   /**
    * 发送者的编号
    */
   @ExcelProperty("发送消息者的编号")
   private Integer fromId;
   /**
    * 发送者的昵称
    */
   @ExcelProperty("发送者的昵称")
   private String fromName;
   /**
    * 发送者的头像
    */
   @ExcelIgnore
   private String fromProfile;
   /**
    * 消息发送时间
    */
   @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
   @ExcelProperty("消息发送时间")
   private Date createTime;
   /**
    * 消息内容
    */
   @ExcelProperty(value = "消息发送内容")
   @ColumnWidth(50)
   private String content;
   /**
    * 消息类型编号
    */
   @ExcelIgnore
   private Integer messageTypeId;
}
```

```java
   /**
    * 导出群聊记录为Excel文档
    * 针对聊天内容不进行特殊处理了，图片URL就写入Excel中了，速度太慢了
    * @param response
    */
   @GetMapping("/download")
   public void exportExcel(HttpServletResponse response) throws IOException {
      response.setContentType("application/vnd.ms-excel");
      response.setCharacterEncoding("utf-8");
      //设置文件信息，URLEncoder.encode可以防止中文乱码
      String fileName = URLEncoder.encode("群聊记录", "UTF-8").replaceAll("\\+", "%20");
      response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
      //我们先获取到所有的群聊记录，之后同意转化成为Excel专属的记录实体
      List<GroupMsgContent> groupMsgContents = getAllGroupMsgContent();     EasyExcel.write(response.getOutputStream(),GroupMsgContent.class).sheet("sheet1").doWrite(groupMsgContents);
   }
```

![image-20220219113342094](https://gitee.com/TrevorLink666/picgo/raw/master/images/202202261637994.png)

## Stream函数

Java8新增的新特性，针对函数式编程的优化，主要更方便地处理集合中元素的对象

## SpringBoot自定义Converter

### 前置知识

这里可以结合上面[获取请求参数](#@RequestParam、@RequestBody的区别)的地方看一下这个博客

[Spring MVC 接收请求参数所有方式总结！](https://cloud.tencent.com/developer/article/1646894)

关于参数类型中存在日期类型属性(例如java.util.Date、java.sql.Date、java.time.LocalDate、java.time.LocalDateTime)，**解析的时候一般需要自定义实现的逻辑实现String->日期类型的转换**

这个时候就需要用到我们Spring的Converter类型转化器了

### 需求分析

在群聊记录的查看里，现在有一个需求是管理员可以**根据不同的时间段来查询群聊记录的分页数据**

前端饿了么UI组件中发送到后端是用String字符串数组，但是我们实体类型应该是Date类型，这样设计才可以不用转换数据类型直接去数据库中查询

```java
if (dateScope != null) wrapper.between("create_time", dateScope[0], dateScope[1]);
```

### 代码实现

我们只需要向容器中注入一个Converter的实现类，自定义转换的规则即可对接收到的参数进行转化

```java
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
```

![image-20220226164157727](https://gitee.com/TrevorLink666/picgo/raw/master/images/202202261641827.png)

## 图片压缩

在这个项目中还学习到针对图片压缩需求的处理，主要是结合导出为Excel的需求；图片经过压缩后写出excel文档的效率会更高

## URL类处理

## RabbitMQ消息可靠性的设计

## SpringBoot的定时轮询任务

## 前端部署

[(7条消息) Failed at the node-sass@4.14.1 postinstall script. npm ERR! This is probably not a problem with npm._与秃顶斗争！的博客-CSDN博客](https://blog.csdn.net/weixin_41940690/article/details/106977906)

[(7条消息) 解决：windows下npm安装的模块执行报错：无法将“cnpm”项识别为 cmdlet、函数、脚本文件或可运行程序的名称等一系列问题_lwwzyy-CSDN博客](https://blog.csdn.net/qq_40638006/article/details/84067298)
