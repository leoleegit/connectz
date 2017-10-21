package com.connectz.util;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.log4j.Logger;

public class SystemClassLoader extends URLClassLoader{
	 private Logger logger = Logger.getLogger(this.getClass());
	 public SystemClassLoader() {
	        super(new URL[] {}, findParentClassLoader());
	 }
	 
	 @SuppressWarnings("deprecation")
	public void addDirectory(File directory) throws MalformedURLException{
		 File[] jars = null;
		 if(directory.isDirectory()){
			 // Add lib directory to classpath.
	         File libDir = new File(directory, "lib");
	         jars = libDir.listFiles(new FilenameFilter() {
	             public boolean accept(File dir, String name) {
	                 return name.endsWith(".jar") || name.endsWith(".zip");
	             }
	         });
		 }else if(directory.getName().endsWith(".jar")){
			 jars = new File[]{directory};
		 }
        
        if (jars != null) {
            for (int i = 0; i < jars.length; i++) {
                if (jars[i] != null && jars[i].isFile()) {
                    addURL(jars[i].toURL());  
                    logger.info("path:"+jars[i].toURL());
                }
            }
        }
	 }
	 
	 public void addURLFile(URL file) {
	        addURL(file);
	 }
   /**
    * Locates the best parent class loader based on context.
    *
    * @return the best parent classloader to use.
    */
   private static ClassLoader findParentClassLoader() {
       ClassLoader parent = SystemClassLoader.class.getClassLoader();
       return parent;
   }
}