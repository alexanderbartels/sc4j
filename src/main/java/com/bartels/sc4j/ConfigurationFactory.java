package com.bartels.sc4j;

import java.lang.reflect.Proxy;

import com.bartels.sc4j.file.PropertyFileConfigurationProvider;

public class ConfigurationFactory {

	public ConfigurationFactory() {
		
	}
	
	/**
	 * 
	 * @param clazz interface to create the configuration from
	 * 
	 * @return instance from the given configuration
	 */
	@SuppressWarnings("unchecked")
	public static <T> T create(Class<T> clazz) {
		return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new ProxyListener(loadConfigurationProvider(clazz)));
	}
	
	private static ConfigurationProvider loadConfigurationProvider(Class<?> clazz) {
		ConfigurationProvider providerImpl = null;
		
		Provider provider = clazz.getAnnotation(Provider.class);
		
		if(provider != null) {
			String providerClazz = provider.value();
			try {
				System.out.println("try to load provider impl: " + providerClazz);
				providerImpl = (ConfigurationProvider) Class.forName(providerClazz).newInstance();
			} catch (Exception e) {
				throw new RuntimeException("Unable to load and create provider implementation: " + e.getMessage(), e);
			}
			
		} else {
			System.out.println("no provider annotation found. load default file provider impl");
			providerImpl = new PropertyFileConfigurationProvider();
		}

		providerImpl.loadConfiguration(clazz);
		return providerImpl;
	}
}
