package com.bartels.sc4j.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

import com.bartels.sc4j.ConfigurationProvider;
import com.bartels.sc4j.util.Converter;

/**
 * {@link ConfigurationProvider} that read the configuration entries from a java standard properties file.
 * 
 * the configuration file will loaded with the {@link Properties} class.
 * 
 * @author bartels
 *
 */
public class PropertyFileConfigurationProvider implements ConfigurationProvider {
	
	//make it final or reusable by different instances
	private Properties properties;
	
	@Override
	public void loadConfiguration(Class<?> clazz) {
		Annotation[] annotations = clazz.getAnnotations();
		if(annotations != null && annotations.length > 0) {
			for(Annotation a : annotations) {
				if(a.annotationType().equals(PropertyFile.class)) {
					this.properties = loadPropertyFile(((PropertyFile)a).value());
				}
			}
		}
		System.out.println("properties: " + properties);
	}
	
	/**
	 * loads the given properties file
	 * @param file
	 */
	private final synchronized Properties loadPropertyFile(final String file) {
		
		// TODO optimize exception handling
		
		File propertyFile = new File(file);
		if(!propertyFile.exists()) {
			URL fileURL = this.getClass().getResource(file);
			if(fileURL != null) {
				propertyFile = loadFileFromURL(fileURL);
			}
		}
		
		if(!propertyFile.exists()) {
			URL fileURL = this.getClass().getClassLoader().getResource(file);
			if(fileURL != null) {
				propertyFile = loadFileFromURL(fileURL);
			}
		}
		
		if(!propertyFile.exists()) {
			throw new RuntimeException("cannot read file: " + file);
		}
		
		Properties props = new Properties();
		try {
			props.load(new FileInputStream(propertyFile));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("cannot read file: " + file);
		}
		
		return props;
	}
	
	/**
	 * loads a file from the given url
	 * @param url
	 * @return
	 */
	private File loadFileFromURL(final URL url) {
		try {
			return new File(url.toURI());
		} catch (URISyntaxException e) {
			throw new RuntimeException("proeprties file not found: " + url);
		}
	}
	
	@Override
	public Object getConfigurationEntry(String configurationKey, Object defaultValue, Method method, Object[] arguments) {
		Object val = properties.get(configurationKey);
		return val != null ? Converter.convert(val.toString(), method.getReturnType()) : defaultValue;
	}
}
