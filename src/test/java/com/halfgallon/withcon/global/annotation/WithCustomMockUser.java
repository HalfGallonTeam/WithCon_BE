package com.halfgallon.withcon.global.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithCustomMockUserSecurityContextFactory.class)
public @interface WithCustomMockUser {

  long id() default 1L;

  String username() default "username";

  String password() default "password";
}
