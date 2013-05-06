package com.bartels.sc4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * implements an interface at runtime.
 * 
 * https://blogs.oracle.com/poonam/entry/how_to_implement_an_interface
 * 
 * @author bartels
 *
 */
public class ProxyListener implements InvocationHandler {
	
	private final ConfigurationProvider configurationProvider;
	
	public ProxyListener(final ConfigurationProvider configurationProvider) {
		this.configurationProvider = configurationProvider;
	}
	

	/**
	 * @param m method to get the configuration key for
	 * 
	 * @return the configuration key for the given method
	 */
	private String getConfigurationKey(final Method m) {
		
		// look for a hard coded property path
		PropertyPath path = m.getAnnotation(PropertyPath.class);
		if(path != null && path.value() != null) {
			return path.value();
		}
		
		// if property path was not defined through an annotation, 
		// we use the method name to build it
		StringBuffer buffer = new StringBuffer();
		String methodName = m.getName();
		
		char[] chars = new char[methodName.length()];
		methodName.getChars(0, methodName.length(), chars, 0);
		
		for(char c : chars) {
			if(Character.isUpperCase(c)) {
				buffer.append(".").append(Character.toLowerCase(c));
			} else {
				buffer.append(c);
			}
		}
		
		return buffer.toString();
	}
    
    private String getDefaultValue(final Method method) {
        DefaultValue defaultValue = method.getAnnotation(DefaultValue.class);
        return defaultValue == null ? null : defaultValue.value();
    }
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] arguments) throws Throwable {
		return configurationProvider.getConfigurationEntry(getConfigurationKey(method), getDefaultValue(method), method, arguments);
	}
}
