package com.connectz.util.json;

import org.json.JSONException;
import org.json.JSONObject;

import com.connectz.util.CoderUtils;
import com.connectz.util.StringUtils;

public class MyJSONObject extends JSONObject{
	
	public MyJSONObject(){
		super();
	}
	
	public MyJSONObject(String json) throws JSONException{
		super(json);
	}
	
	public String toJson() throws JSONException{
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		java.util.Iterator<?> it = super.keys();
		if(it!=null){
			for(;it.hasNext();){
				String name  = (String) it.next();
				String value = super.getString(name);
				if (value != null
						&& (!(value instanceof String) || StringUtils
								.notNull((String) value))) {
					value = encode((String) value);
					sb.append("\"").append(name).append("\"")
							.append(":").append(value).append(",");
				}
			}
		}
		if (sb.lastIndexOf(",") == sb.length() - 1)
			sb.deleteCharAt(sb.length() - 1);
		sb.append("}");
		return sb.toString();
	}
	
	public String encode(String str) {
		// TODO Auto-generated method stub
		return CoderUtils.encode(str);
	}
}
