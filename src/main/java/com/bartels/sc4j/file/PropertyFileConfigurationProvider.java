package com.bartels.sc4j.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private static final Logger logger = LoggerFactory.getLogger(PropertyFileConfigurationProvider.class);
	
	private Properties properties;
	
	
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
	
	private File loadFileFromURL(final URL url) {
		try {
			return new File(url.toURI());
		} catch (URISyntaxException e) {
			throw new RuntimeException("properties file not found: " + url);
		}
	}
	
	@Override
	public Object getConfigurationEntry(String configurationKey, String defaultValue, Method method, Object[] arguments) {
		Object val = properties.get(configurationKey);
		
		if(val != null) {
			return Converter.convert(val.toString(), method.getReturnType());
		} else {
			// return the default value, because no entry was found in the properties file
			return Converter.convert(defaultValue, method.getReturnType());
		}
	}
	
	@Override
	public void loadConfiguration(Class<?> clazz) {
		PropertyFile propertyFile = clazz.getAnnotation(PropertyFile.class);
		
		if(propertyFile == null) {
			throw new IllegalStateException("Missing @PropertyFile annotation");
		}
		
		this.properties = loadPropertyFile(propertyFile.value());
		logger.debug("Properties loaded. Path: {}, Properties: {}", propertyFile.value(), properties);
	}
}
