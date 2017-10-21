package com.connectz.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;


public class ReflectUtils {
	protected static Logger logger = Logger.getLogger(ReflectUtils.class);
	
	public static java.lang.reflect.Method getMethod(Class<?> c, String name){
		Method   _method  = null;
		for(Method m:c.getMethods()){ 
			if(m.getName().equalsIgnoreCase(name)){
				_method = m;
				break;
			}
		}
		return _method;
	}
	
	public static java.lang.reflect.Method getMethod(Class<?> c, String name, Class<?>[] parameterTypes){
		Method   _method  = null;
		for(Method m:c.getMethods()){ 
			if(m.getName().equalsIgnoreCase(name) && equals(parameterTypes,m.getParameterTypes())){
				_method = m;
				break;
			}
		}
		return _method;
	}
	
	public static boolean equals(Class<?>[] parameterTypes1, Class<?>[] parameterTypes2){
		if(parameterTypes1.length != parameterTypes2.length)
			return false;
		for(int i=0;i<parameterTypes2.length;i++){
			if(!parameterTypes1[0].getName().equals(parameterTypes2[0].getName()))
				return false;
		}
		return true;
	}
	
	public static boolean hasConstructor(Class<?> obj, Class<?>... types){
		Constructor<?>[] cs = obj.getConstructors();  
		if(types==null)
			types = new Class<?>[]{};
		for(Constructor<?> c : cs){
			Class<?>[] cls = c.getParameterTypes();
			if(cls.length == types.length){
				if(cls.length == 0)
					return true;
				for(int i=0; i<cls.length; i++){
					if(cls[i].getName().equals(types[i].getName()))
						return true;
				}
			}
		}
		return false;
	}
	
	public static Class<?> loadClass(String clazz, ClassLoader class_loader){
		if(class_loader==null)
			class_loader = ReflectUtils.class.getClassLoader();
		try {
			Class<?> c = class_loader.loadClass(clazz);
			return c;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			logger.error("",e);
		}
		return null;
	}
	
	public static Object newInstance(String clazz, ClassLoader class_loader){
		if(class_loader==null)
			class_loader = ReflectUtils.class.getClassLoader();
		Object object = null;
		try {
			Class<?> c = class_loader.loadClass(clazz);
			object     =  c.newInstance();
			return object;
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			logger.error("",e);
		}
		return object;
	}
	
	public static Object newInstance(Class<?> class_, Object[] objs) {
	    try {
	        Class<?>[] par = getClazzs(objs);
	        Constructor<?> con = class_.getConstructor(par);
	        return con.newInstance(objs);
	    }
	    catch (SecurityException e) {
	    	logger.error("",e);
	    }
	    catch (IllegalArgumentException e) {
	    	logger.error("",e);
	    }
	    catch (NoSuchMethodException e) {
	    	logger.error("",e);
	    }
	    catch (InstantiationException e) {
	    	logger.error("",e);
	    }
	    catch (IllegalAccessException e) {
	    	logger.error("",e);
	    }
	    catch (InvocationTargetException e) {
	      logger.error("",e);
	    }
	    return null;
    }
	
	public static Class<?>[] getClazzs(Object... objs){
		if(objs==null){
			Class<?>[] clazzs = new Class<?>[]{};
			return clazzs;
		}
		Class<?>[] clazzs = new Class<?>[objs.length];
		for(int i=0; i<objs.length; i++){
			clazzs[i] = objs[i].getClass();
		}
		return clazzs;
	}
}
