package com.connectz.util.command;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.connectz.util.StringUtils;


public class ConsoleSocketCommandClient extends ConsoleCommand {
	final static String HELP = helpAlert();
	SocketCommandClient client;
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ConsoleSocketCommandClient client = new ConsoleSocketCommandClient();
		client.start();
	}
	
	@Override
	public Map<String, String> getKeyMaps() {
		// TODO Auto-generated method stub
		return new HashMap<String,String>(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
				put("open","host");
				put("-pw","password");
				put("help","help");
				put("-u","username");
			}
		};
	}
	
	public CmdPacket getCommandPacket(String[] command){
		ConsoleSocketCommandClientPacket packet = (ConsoleSocketCommandClientPacket) super.getCommandPacket(command);
		packet.setCmd(command[0]);
		return packet;
	}
	
	/**
	 * help
	 * open 127.0.0.1:81 
     *      -u username
     *      -pw password     
	 * exit 
	 * @return
	 */
	public static String helpAlert(){
		StringBuilder sb = new StringBuilder();
		sb
		.append("--------------------------- ConsoleSocketCommandClient ---------------------------\n")
		.append("help \n")
		.append("open 127.0.0.1:81 \n")
		.append("exit \n");
		return sb.toString();
	}

	@Override
	public String help() {
		// TODO Auto-generated method stub
		return HELP;
	}

	@Override
	public String command(CmdPacket command) {
		// TODO Auto-generated method stub
		ConsoleSocketCommandClientPacket packet = (ConsoleSocketCommandClientPacket) command;
		String cmd   = packet.getCmd();
		String host  = packet.getHost();
		String username = packet.getUsername();
		String passwd   = packet.getPasswd();
		
		if("open".equalsIgnoreCase(cmd)){
			if(host==null){
				return "host is null";
			}
			String HOST = host.split(":")[0];
			String port = (host.indexOf(":")!=-1)?host.split(":")[1]:"80";
			if(!StringUtils.isNumeric(port)){
				return "port must be a number";
			}
			if(client!=null && client.isConnected()){
				return "socket have been connected.";
			}
			client = new SocketCommandClient(HOST,Integer.valueOf(port)){

				@Override
				public void response(String[] command) {
					// TODO Auto-generated method stub
					System.out.println(SocketCommandClient.mergeCommand(command));
				}
				
			};
			System.out.println(String.format("connecting %s %s", host,port));
			if(client.connect(username, passwd)){
				System.out.println("connected");
			}
		}else if("exit".equalsIgnoreCase(cmd)){
			System.exit(-1);
		}else if("close".equalsIgnoreCase(cmd) && client!=null && client.isConnected()){
			client.disconnect();
		}else if(client!=null && client.isConnected()){
			try {
				client.sendCommand(packet.getCommands());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.err.println(e.getMessage());
			}
		}
			
		return null;
	}

	@Override
	public CmdPacket getCommandPacketObject() {
		// TODO Auto-generated method stub
		return new ConsoleSocketCommandClientPacket();
	}

}
