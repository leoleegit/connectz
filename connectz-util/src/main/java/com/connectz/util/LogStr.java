package com.connectz.util;

import java.util.*;

public class LogStr {
	private String key;
	
	public LogStr(String key){
		this.setKey(key);
	}
	
	public String format(String format, Object... args){
		List<Object> list=new ArrayList<Object>();
		if(args!=null){
			for(Object obj:args){
				if(obj==null)
					obj = "";
				list.add(obj);
			}
		}
		list.add(0,getKey()==null?"":getKey());
		return String.format("(%s)-->" + format, list.toArray());
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
