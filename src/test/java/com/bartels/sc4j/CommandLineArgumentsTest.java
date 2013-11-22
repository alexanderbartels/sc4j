package com.bartels.sc4j;

import com.bartels.sc4j.file.PropertyFile;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for using command line arguments to override properties
 */
public class CommandLineArgumentsTest extends TestCase {
    
    // configuration interface to test
    @PropertyFile("sc4j.properties")
    public interface ServerConfiguration {
        
        @DefaultValue("80")
        int serverPort();
        
        @DefaultValue("127.0.0.1")
        String serverHost();
    }
    

	public CommandLineArgumentsTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(CommandLineArgumentsTest.class);
	}
	
	public void testCompleteOverride() {
	    String[] cmdArgs = new String[]{
	        "--server.port", "5432",
	        "--server.host", "localhost"
	    };
	    ServerConfiguration config = ConfigurationFactory.create(ServerConfiguration.class, cmdArgs);
	    
	    assertNotNull(config);
	    
	    assertEquals(5432, config.serverPort());
	    assertEquals("localhost", config.serverHost());
	}
	
	public void testDefaults() {
	    String[] cmdArgs = new String[]{
	        "--some.prop", "foo",
	        "--some.other.prop", "bar"
	    };
	    ServerConfiguration config = ConfigurationFactory.create(ServerConfiguration.class, cmdArgs);
	    
	    assertNotNull(config);
	    
	    assertEquals(80, config.serverPort());
	    assertEquals("127.0.0.1", config.serverHost());
	}
}