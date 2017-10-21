package com.connectz.util.command;

public class ConsoleSocketCommandClientPacket extends CmdPacket{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ConsoleSocketCommandClientPacket() {
		super(null);
		// TODO Auto-generated constructor stub
	}
	private String cmd;
	private String host;
	private String username;
	private String passwd;
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getCmd() {
		return cmd;
	}
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	@Override
	public boolean isHelp() {
		// TODO Auto-generated method stub
		return "help".equalsIgnoreCase(cmd);
	}
}
