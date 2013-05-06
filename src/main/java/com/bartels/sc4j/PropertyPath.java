package com.bartels.sc4j;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PropertyPath {

	/**
	 * defines a optional property path 
	 * 
	 * @return the optional property path
	 */
	String value();
}
