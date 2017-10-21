package com.connectz.util;

import java.util.HashMap;
import java.util.Map;

import com.connectz.util.CoderUtils;
import com.connectz.util.json.JsonObject;

public class StringUtilsTest {
	private static Map<String,String> keyMaps = new HashMap<String,String>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("open","host");
			put("-pw","password");
			put("-p","port");
			put("-n","name");
		}
	};
	public static void main(String[] args) {
//		String username = "leo@protelnet.com";
//		System.out.println(StringUtils.getUid(username));
//		System.out.println(StringUtils.getDomain(username));
//		System.out.println(StringUtils.getUsername(StringUtils.getUid(username), StringUtils.getDomain(username)));
//		
//		LogStr log = StringUtils.generateLogStr(null);
		String str = "Linux%20x86_64";
		System.out.println(CoderUtils.decode(str));
		
		System.out.println(JsonObject.lineToJson(keyMaps, new String[]{"open","127.0.0.1","-p","80","-pw","leo","-n","leo.com"}));
	}
}
