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
		
		PropertyPath path = m.getAnnotation(PropertyPath.class);
		if(path != null && path.value() != null) {
			return path.value();
		}
		
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
		
		System.out.println("config key: " + buffer.toString());
		
		return buffer.toString();
		
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] arguments) throws Throwable {
		return configurationProvider.getConfigurationEntry(getConfigurationKey(method), null, method, arguments);
	}
	
	/**
	 * prints about some information about the method that was invoked
	 * 
	 * @param proxy
	 * @param method
	 * @param arguments
	 */
	protected void debugMethodInvoke(Object proxy, Method method, Object[] arguments) {
		System.out.println("Proxy: " + proxy.getClass().getSimpleName());
		try {
            System.out.print("Begin method: "+ method.getName() + "( ");
            
            if(arguments != null) {
            	for(int i=0; i < arguments.length; i++) {
            		if(i > 0) {
            			System.out.print(", ");
            		}
            		System.out.print(" " + arguments[i].toString());
            	}
            }
            System.out.println(" )");
		} catch(Exception e) {
			e.printStackTrace();
			// TODO create a specific exception
			throw new RuntimeException("unexpected invocation exception: " + e.getMessage());
		}
	}
}
