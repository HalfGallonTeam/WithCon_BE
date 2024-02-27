package com.halfgallon.withcon.domain.chat.config;

import com.halfgallon.withcon.domain.chat.config.Handler.StompPreHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  private final StompPreHandler stompPreHandler;

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
        .setSystemHeartbeatSendInterval(4000)
        .setSystemHeartbeatReceiveInterval(4000);

    registry.setApplicationDestinationPrefixes("/app");
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
  }

  @Override
  public void configureClientInboundChannel(ChannelRegistration registration) {
    registration.interceptors(stompPreHandler);
  }

}
