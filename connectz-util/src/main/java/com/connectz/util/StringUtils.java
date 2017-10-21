package com.connectz.util;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
	
	public static LogStr generateLogStr(String key){
		return new LogStr(key);
	}
	
	public static boolean notNull(String str) {
	   return (str != null) && (!"".equals(str));
	}
	
    public static boolean isNumeric(String str){ 
		if(str==null)return false;
	    Pattern pattern = Pattern.compile("[0-9]*"); 
	    Matcher isNum = pattern.matcher(str);
	    if( !isNum.matches() ){
	        return false; 
	    } 
	    return true; 
    }
    
    public static String getUsername(String uid, String domain){
    	return String.format("%s@%s", uid, domain);
    }
    
    public static String getUid(String username){
    	if(username==null)
    		return null;
    	if(username.indexOf("@")!=-1)
    		return username.substring(0, username.lastIndexOf("@"));
    	return username;
    }
    
    public static String getDomain(String username){
    	if(username==null)
    		return null;
    	if(username.indexOf("@")!=-1 && (username.lastIndexOf("@")+1)<=username.length())
    		return username.substring(username.lastIndexOf("@")+1);
    	return username;
    }
    
    public static String generateSessionID(){
    	return RandomUtils.generateRandom(DateUtils.getDate(new Date(), "yyyyMMdd"),6);
    }
    
    public static String generatePacketID(){
    	return RandomUtils.generateRandom(DateUtils.getDate(new Date(), "yyyyMMdd"),8);
    }
}
