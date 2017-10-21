package com.connectz.util.test;

import java.io.File;

import com.connectz.util.StringUtils;

public class MyJetty {
	final static int P = 1;
	final static int D = 2;
	final static int W = 3;
	final static int T = 4;
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		new MyJetty().command(args);
	}

	public String command(String[] args) throws Exception {
		if(args.length==0 || "-h".equalsIgnoreCase(args[0])){
			StringBuilder sb = new StringBuilder();
			sb.append("---------------------------------MyJetty---------------------------------").append("\n\n")
			.append("-h help").append("\n")
			.append("-p port").append("\n")
			.append("-d resource dir").append("\n")
			.append("-w war file or web folder").append("\n")
			.append("-td java.io.tmpdir").append("\n")
			.append("\n");
			System.out.println(sb.toString());
		}else{
			CommandPacket packet = new CommandPacket();
			int cmd        = 0;
			for(String s : args){
			    if("-P".equalsIgnoreCase(s)){
					cmd = P;
					continue;
				}else if("-d".equalsIgnoreCase(s)){
					cmd = D;
					continue;
				}else if("-W".equalsIgnoreCase(s)){
					cmd = W;
					continue;
				}else if("-td".equalsIgnoreCase(s)){
					cmd = T;
					continue;
				}
				switch(cmd){
					case P:{
						packet.setPort(s);;
						break;
					}
					case D:{
						packet.setDir(s);
						break;
					}
					case W:{
						packet.setWar(s);
						break;
					}
					case T:{
						packet.setTmpdir(s);
						break;
					}
					default :{
						break;
					}
				}
			}
			handle(packet);
		}
		return null;
	}
	
	private void handle(CommandPacket packet) throws Exception {
		// TODO Auto-generated method stub
		String port = packet.getPort();
		String dir  = packet.getDir();
		String war  = packet.getWar();
		
		if(!StringUtils.isNumeric(port)){
			System.out.println("port must be a number!");
			return;
		}
		if(packet.getTmpdir()!=null)
			System.setProperty("java.io.tmpdir", new File(System.getProperty("user.dir"),packet.getTmpdir()).getAbsolutePath());
		com.connectz.util.JettyServer js = new com.connectz.util.JettyServer(Integer.valueOf(port));
		if(dir!=null)
			js.setResource_handler(dir, null);
		if(war!=null)
			js.setWebapp("/",null,new File(war));
		js.start();
	}

	class CommandPacket {
		private String port;
		private String dir;
		private String war;
		private String tmpdir;

		public String getDir() {
			return dir;
		}

		public void setDir(String dir) {
			this.dir = dir;
		}

		public String getWar() {
			return war;
		}

		public void setWar(String war) {
			this.war = war;
		}

		public String getPort() {
			return port;
		}

		public void setPort(String port) {
			this.port = port;
		}

		public String getTmpdir() {
			return tmpdir;
		}

		public void setTmpdir(String tmpdir) {
			this.tmpdir = tmpdir;
		}
	}
}
