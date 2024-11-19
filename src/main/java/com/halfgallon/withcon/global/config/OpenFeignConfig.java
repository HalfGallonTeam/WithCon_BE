package com.halfgallon.withcon.global.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients("com.halfgallon.withcon")
public class OpenFeignConfig {

}
