package com.bartels.sc4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.bartels.sc4j.util.Converter;

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
	
	private Map<String, String> defaults;
	
	public ProxyListener(final ConfigurationProvider configurationProvider) {
		this.configurationProvider = configurationProvider;
	}
	
	public ProxyListener(final ConfigurationProvider configurationProvider, final String[] commandlineArgs) {
		this.configurationProvider = configurationProvider;
		this.defaults = parseCommandLineArguments(commandlineArgs);
	}

	/**
	 * parses the array with command line arguments
	 * 
	 * @param args the array with arguments to parse
	 * 
	 * @return a map with the default configuration mapping
	 */
	private Map<String, String> parseCommandLineArguments(final String[] args) {
		// TODO The "parser" should be implemented against an interface to keep it interchangeable and testable
		
		if(args == null) {
			throw new IllegalArgumentException("args should not be null");
		}
		
		Map<String, String> propertyMapping = new HashMap<String, String>();
		
		for(int i = 0; i < args.length; i++) {
			if(args[i].startsWith("--") && (i + 1) < args.length) {
				propertyMapping.put(args[i].substring(2), args[i + 1]);
			}
		}
		return propertyMapping;
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
		String configKey = getConfigurationKey(method);
		
		if(defaults.containsKey(configKey)) {
			return Converter.convert(defaults.get(configKey), method.getReturnType());
		}
		return configurationProvider.getConfigurationEntry(configKey, getDefaultValue(method), method, arguments);
	}
}
