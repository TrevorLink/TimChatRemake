package com.yep.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket配置
 * @author HuangSir
 * @date 2022-02-15 16:14
 */
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
