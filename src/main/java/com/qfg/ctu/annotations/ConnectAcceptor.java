package com.qfg.ctu.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by rbtq on 8/2/16.
 */
@Retention(RUNTIME)
@Target(METHOD)

public @interface ConnectAcceptor {
}
