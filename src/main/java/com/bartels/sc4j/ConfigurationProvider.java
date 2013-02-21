package com.bartels.sc4j;

import java.lang.reflect.Method;

/**
 * interface for specific configuration provider implementations
 * 
 * @author bartels
 *
 */
public interface ConfigurationProvider {

	/**
	 * load the configuration
	 * 
	 * @param clazz the user specific configuration interface
	 */
	void loadConfiguration(final Class<?> clazz);
	
	/**
	 * called if a specific configuration entry was requested
	 * 
	 * @param configurationKey the configuration key from the property that was requested
	 * @param defaultValue the default value that should be used if no property for the given key was found
	 * @param method the method from the user specific configuration interface that was called
	 * @param arguments array of arguments from the given method
	 * 
	 * @return requested configuration entry
	 */
	Object getConfigurationEntry(final String configurationKey, final String defaultValue, final Method method, final Object[] arguments);
	
}
