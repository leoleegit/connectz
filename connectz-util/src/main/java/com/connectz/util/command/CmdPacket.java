package com.connectz.util.command;

import com.connectz.util.json.JsonObjectImpl;

public abstract class CmdPacket extends JsonObjectImpl {
	private String[] commands;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CmdPacket(String json) {
		super(json);
		// TODO Auto-generated constructor stub
	}

	public abstract boolean isHelp();

	public String[] getCommands() {
		return commands;
	}

	public void setCommands(String[] commands) {
		this.commands = commands;
	}
}
