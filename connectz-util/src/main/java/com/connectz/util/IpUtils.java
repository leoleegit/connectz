package com.connectz.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IpUtils {
	public static String getLocalIp() throws UnknownHostException{
		 String ip = InetAddress.getLocalHost().getHostAddress();  
		 return ip;
	}
}
