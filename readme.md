What is sc4j
============



sc4j is a simple library to read a configuration, for example from a property file.
If you like the way to read a configuration, but your configuration isn't stored in a proeprty file, 
you can write your own 'ConfigurationProvider'. See 'Write your own ConfigurationProvider' below.

Simple Example
==============

Say you would like to read the following property file:

````
#my configuration file

# this is the mail host to send the mails
email.host=smtp@bartels.de

# the port to from the mail host
email.port=20

#authentication 
email.account.user.name=bartels
email.account.user.pwd=bartels
```

Normally we write a class to wrap it:

```java

public class MyConfiguration {
   public static final String EMAIL_HOST = "email.host";
   public static final String EMAIL_PORT = "email.port";
   public static final String EMAIL_ACCOUNT_USER_NAME = "email.account.user.name";
   public static final String EAMIL_ACCOUNT_USER_PWD  = "email.account.user.pwd";
   
   public static final String EMAIL_HOST_DEFAULT_VALUE = "foobar@bartels.de";
   
   private Properties properties;
   
   public MyConfiguration(final String propertyFilePath) {
        // some magic to load the file and the properties;
   }
   
   public String getEmailHost() {
      String host = properties.get(EMAIL_HOST);
      return host == null ? EMAIL_HOST_DEFAULT_VALUE : host;
   }
   
   public int getEmailPort() {
      String  sPort = properties.get(EMAIL_PORT);
      if(sPort != null) {
         int port = Integer.parseInt(sPort);
         return port;
      }
      throw new IllegalStateException("invalid argument");
   }
   
   // getter methods for the other properties
   [...]
}

```

and use the class ...

```java
  [...]
  
  public void configureMailSystem() {
     // load the properties
     MyConfiguration config = new MyConfiguration("MyPropertiesFile.properties");  
     
     // do something with the loaded properties
     config.getEmailHost();
  }
```

With the sc4j lib we only need to define an interface with the properties we would like to read:
(The 'DefaultValue' annotation is currently not implemented)

```java

@PropertyFile("MyPropertiesFile.properties")
public interface MyConfiguration {

   /**
   *
   * @return returns the email.host property if the property is null the default value will returned
   */
   @DefaultValue("foobar@bartels.de")
   String emailHost();

   int emailPort();
   
   /**
   * with the PropertyPath annotions you're able to specify a different property key to read the property
   * @return returns the email.account.user.name property
   */
   @PropertyPath("email.account.user.name")
   String userName();

   @PropertyPath("email.account.user.pwd")
   String userPwd();
}

```

how to use the interface

```java
   public class MyFancyClass {
      // load the properties
      private static final MyConfiguration CONFIG = ConfigurationFactory.create(MyConfiguration.class);
   
      public void configureMailSystem() {
         // do something with the loaded properties
         CONFIG.emailHost();
      }
   }
```