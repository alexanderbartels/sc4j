package com.bartels.sc4j;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DefaultValue {
	
	/**
	 * defines the default value, that will be returned if no value is found by the value provider
	 * 
	 * @return the default value
	 */
    String value();
}
