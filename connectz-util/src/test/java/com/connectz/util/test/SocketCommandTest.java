package com.connectz.util.test;

import java.io.IOException;
import java.util.Map;

import com.connectz.util.command.CmdPacket;
import com.connectz.util.command.SocketCommandServer;

public class SocketCommandTest extends SocketCommandServer{

	public SocketCommandTest(String host, int port) {
		super(host, port);
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		new SocketCommandTest("127.0.0.1",82).init();
		//System.in.read();
	}

	@Override
	public boolean ssl() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String help() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean authenticate(CmdPacket command) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String command(CmdPacket packet) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CmdPacket getCommandPacket(String[] command) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CmdPacket getCommandPacketObject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getKeyMaps() {
		// TODO Auto-generated method stub
		return null;
	}
}
