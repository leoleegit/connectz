package org.connectz.com.demos.test;

import java.io.File;


public class Jetty1 {
	 private static String HOME = System.getProperty("user.dir");
	 private static String PATH= HOME+File.separator+"src/main/webapp";
	 public static void main( String[] args ) throws Exception {
		 int port = 82;

		 com.connectz.util.JettyServer js = new com.connectz.util.JettyServer(port);
		 js.setResource_handler(PATH, null);
		 js.setWebapp("/",null, new File(PATH));
		 js.start();
	 }
}

