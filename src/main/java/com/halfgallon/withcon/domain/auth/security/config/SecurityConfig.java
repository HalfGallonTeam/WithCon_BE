package com.halfgallon.withcon.domain.auth.security.config;

import static com.halfgallon.withcon.domain.auth.constant.AuthConstant.REFRESH_TOKEN_COOKIE_NAME;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.halfgallon.withcon.domain.auth.client.OAuth2Client;
import com.halfgallon.withcon.domain.auth.manager.JwtManager;
import com.halfgallon.withcon.domain.auth.repository.AccessTokenRepository;
import com.halfgallon.withcon.domain.auth.repository.RefreshTokenRepository;
import com.halfgallon.withcon.domain.auth.security.filter.JwtAuthenticationFilter;
import com.halfgallon.withcon.domain.auth.security.filter.LoginFilter;
import com.halfgallon.withcon.domain.auth.security.filter.OAuth2LoginFilter;
import com.halfgallon.withcon.domain.auth.security.handler.CustomLogoutSuccessHandler;
import com.halfgallon.withcon.domain.auth.security.handler.LoginFailureHandler;
import com.halfgallon.withcon.domain.auth.security.handler.LoginSuccessHandler;
import com.halfgallon.withcon.domain.auth.security.handler.TokenClearingLogoutHandler;
import com.halfgallon.withcon.domain.auth.security.provider.OAuth2LoginProvider;
import com.halfgallon.withcon.domain.auth.security.service.CustomUserDetailsService;
import com.halfgallon.withcon.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtManager jwtManager;
  private final ObjectMapper objectMapper;
  private final OAuth2Client oAuth2Client;
  private final MemberRepository memberRepository;
  private final AccessTokenRepository accessTokenRepository;
  private final RefreshTokenRepository refreshTokenRepository;

  private static final AntPathRequestMatcher LOGIN_ANT_PATH_REQUEST_MATCHER =
      new AntPathRequestMatcher("/auth/login", "POST");

  private static final AntPathRequestMatcher LOGOUT_ANT_PATH_REQUEST_MATCHER =
      new AntPathRequestMatcher("/auth/logout", "POST");

  private static final AntPathRequestMatcher OAUTH2_LOGIN_ANT_PATH_REQUEST_MATCHER =
      new AntPathRequestMatcher("/auth/oauth2/login", "POST");

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) -> web.ignoring()
        .requestMatchers("/favicon.ico")
        .requestMatchers("/error");
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(
            (configurer) -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .logout(
            (configurer) -> configurer
                .logoutRequestMatcher(LOGOUT_ANT_PATH_REQUEST_MATCHER)
                .deleteCookies(REFRESH_TOKEN_COOKIE_NAME)
                .addLogoutHandler(
                    new TokenClearingLogoutHandler(accessTokenRepository, refreshTokenRepository))
                .logoutSuccessHandler(new CustomLogoutSuccessHandler()))
        .authorizeHttpRequests((request) -> request
            .requestMatchers(LOGOUT_ANT_PATH_REQUEST_MATCHER).hasRole("USER")
            .anyRequest().permitAll()
        )
        .addFilterBefore(
            new JwtAuthenticationFilter(memberRepository, accessTokenRepository),
            LogoutFilter.class)
        .addFilterBefore(loginFilter(), UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(oAuth2LoginFilter(), UsernamePasswordAuthenticationFilter.class)
    ;

    return http.build();
  }

  @Bean
  public OAuth2LoginFilter oAuth2LoginFilter() {
    OAuth2LoginFilter filter = new OAuth2LoginFilter(
        OAUTH2_LOGIN_ANT_PATH_REQUEST_MATCHER, authenticationManager(), objectMapper
    );
    filter.setAuthenticationSuccessHandler(
        new LoginSuccessHandler(jwtManager, accessTokenRepository, refreshTokenRepository));
    filter.setAuthenticationFailureHandler(new LoginFailureHandler(objectMapper));

    return filter;
  }

  @Bean
  public LoginFilter loginFilter() {
    LoginFilter filter = new LoginFilter(
        LOGIN_ANT_PATH_REQUEST_MATCHER, authenticationManager(), objectMapper);
    filter.setAuthenticationSuccessHandler(
        new LoginSuccessHandler(jwtManager, accessTokenRepository, refreshTokenRepository));
    filter.setAuthenticationFailureHandler(new LoginFailureHandler(objectMapper));
    return filter;
  }

  @Bean
  public AuthenticationManager authenticationManager() {
    DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
    daoAuthenticationProvider.setUserDetailsService(userDetailsService());
    daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

    OAuth2LoginProvider oAuth2LoginProvider = new OAuth2LoginProvider(oAuth2Client,
        memberRepository);
    return new ProviderManager(daoAuthenticationProvider, oAuth2LoginProvider);
  }

  @Bean
  public UserDetailsService userDetailsService() {
    return new CustomUserDetailsService(memberRepository);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
