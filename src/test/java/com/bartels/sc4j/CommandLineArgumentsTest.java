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
    public interface ServerConfiguration {
        
        @DefaultValue("80")
        int serverPort();
        
        @DefaultValue("127.0.0.1")
        String serverHost();
    }
    
    
    
}