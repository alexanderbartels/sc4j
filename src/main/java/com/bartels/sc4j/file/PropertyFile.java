package com.bartels.sc4j.file;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to say the framework where to find the properties file 
 * 
 * @author bartels
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PropertyFile {

	/**
	 * defines the file name or path from the properties file to read.
	 * 
	 * @return path to the properties file to read
	 */
	String value() default "sc4j.properties";
	
}
