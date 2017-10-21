package com.connectz.util.packet;

public interface Packet {
	public int getType();
	public String  getID();
	public  void   setID(String id);
	public String  toString();
	public boolean isKeepAlive();
}
