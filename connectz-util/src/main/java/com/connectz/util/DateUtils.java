package com.connectz.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	private static final String DefaultFormat = "yyyy-MM-dd HH:mm:ss";
	
	public static String getDate(Date date){
		return getDate(date,DefaultFormat);
	}
	
	public static String getNow(){
		return getDate(new Date(),DefaultFormat);
	}
	
	public static String getDate(Date date, String format){
		if(format==null)format=DefaultFormat;
		SimpleDateFormat f=new SimpleDateFormat(format);
		return f.format(date);
	}
	
	public static Date getDate(String date) throws ParseException{
		return getDate(date,DefaultFormat);
	}
	
	public static Date getDate(String date, String format) throws ParseException{
		if(date==null)
			return null;
		if(format==null)format=DefaultFormat;
		SimpleDateFormat f=new SimpleDateFormat(format);
		return f.parse(date);
	}
}
