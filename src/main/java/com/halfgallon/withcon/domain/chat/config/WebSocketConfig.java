package com.halfgallon.withcon.domain.chat.config;

import com.halfgallon.withcon.domain.chat.config.Handler.StompErrorHandler;
import com.halfgallon.withcon.domain.chat.config.Handler.StompPreHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  private final StompPreHandler stompPreHandler;
  private final StompErrorHandler stompErrorHandler;

  @Value("${relay.host}")
  private String relayHost;

  @Value("${relay.port}")
  private int relayPort;

  @Value("${relay.clientLogin}")
  private String clientLogin;

  @Value("${relay.clientPasscode}")
  private String clientPasscode;

  @Value("${relay.systemLogin}")
  private String systemLogin;

  @Value("${relay.systemPasscode}")
  private String systemPasscode;

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    registry.enableStompBrokerRelay("/exchange")
        .setRelayHost(relayHost)
        .setRelayPort(relayPort)
        .setClientLogin(clientLogin)
        .setClientPasscode(clientPasscode)
        .setSystemLogin(systemLogin)
        .setSystemPasscode(systemPasscode)
        .setSystemHeartbeatSendInterval(10000)
        .setSystemHeartbeatReceiveInterval(10000);

    registry.setApplicationDestinationPrefixes("/app");
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
    registry.setErrorHandler(stompErrorHandler);
  }

  @Override
  public void configureClientInboundChannel(ChannelRegistration registration) {
    registration.interceptors(stompPreHandler);
  }

  @Override
  public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
    registry.setMessageSizeLimit(50 * 1024 * 1024);
    registry.setSendTimeLimit(20 * 10000);
    registry.setSendBufferSizeLimit(3 * 512 * 1024);
  }

  @Bean
  public ServletServerContainerFactoryBean servletServerContainerFactoryBean(){
    ServletServerContainerFactoryBean servletServerContainerFactoryBean = new ServletServerContainerFactoryBean();
    servletServerContainerFactoryBean.setMaxTextMessageBufferSize(2048 * 2048);
    servletServerContainerFactoryBean.setMaxBinaryMessageBufferSize(2048 * 2048);
    return servletServerContainerFactoryBean;
  }

}
