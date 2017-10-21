package com.connectz.util.json;
import com.connectz.util.CoderUtils;

public class JsonObjectImpl extends JsonObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public JsonObjectImpl(String json){
		super(json);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setListObject(String name, Object args) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String toJson(String name, Object o1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setObject(String name, Object args) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String encode(String str) {
		// TODO Auto-generated method stub
		return CoderUtils.encode(str);
	}

	@Override
	public String decode(String str) {
		// TODO Auto-generated method stub
		return CoderUtils.decode(str);
	}

}
