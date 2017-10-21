package com.connectz.util.command;

import java.util.Map;

import com.connectz.util.json.JsonObject;

public abstract class Cmd {
	public CmdPacket getCommandPacket(String[] command){
		String json = getKeyMaps() != null ? JsonObject.lineToJson(getKeyMaps(), command)
				: null;
		CmdPacket packet =  getCommandPacketObject();
		if(packet!=null){
			packet.json2Object(json, packet);
			packet.setCommands(command);
			return packet;
		}
		return null;
	}

	public abstract String command(CmdPacket packet);

	public abstract String help();
	
	public abstract CmdPacket getCommandPacketObject();
	
	public abstract Map<String, String> getKeyMaps();
}
