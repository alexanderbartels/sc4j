package com.bartels.sc4j;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Provider {

	/**
	 * defines the provider implementation to load the properties
	 * 
	 * @return full classified class name from the provider implementation to use
	 */
	String value();
	
}
