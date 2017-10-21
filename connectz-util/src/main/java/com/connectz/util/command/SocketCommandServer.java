package com.connectz.util.command;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import com.connectz.util.Lifecycle;
import com.connectz.util.ConnectZ;

public abstract class SocketCommandServer extends Cmd implements Lifecycle{
    private Logger logger = Logger.getLogger(getClass());
	private String host;
    private int port;
    private ServerSocket socket;
	private Thread listener;
    private boolean run;
    
    public SocketCommandServer(String host, int port){
    	this.host = host;
    	this.port = port;
    }
	
	public void init(){
		listener = new Thread(){
			public void run(){
				startListener();
			}
		};
		listener.start();
	}

	public void destory(){
		stopListener();
		listener.interrupt();
		listener = null;
	}
	
	public boolean syncStart(){
		return false;
	}

	public void startListener() {
		// TODO Auto-generated method stub
		if(port<0){
         	logger.info("port can not be 0 or less than 0");
         	System.out.println("port can not be 0 or less than 0");
            return;
        }
        if(host==null)
         	host = "127.0.0.1";
         
        try {
			socket=new ServerSocket(port,1,InetAddress.getByName(host));
			String str = String.format("host:%s;port:%s", socket.getInetAddress().toString(),socket.getLocalPort());
			logger.info(str);
         	
         	run = true;
         	logger.info(String.format("(%s) listenering...", this.socket.getLocalSocketAddress()));
            while (run){
                try {	
                	final Socket socket=this.socket.accept();
                    logger.info(String.format("(%s) connected", socket.getRemoteSocketAddress()));
                    
                    Runnable thread = new Runnable(){

						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								boolean ssl = false;
								LineNumberReader lin=
					                        new LineNumberReader(new InputStreamReader(socket.getInputStream()));
								OutputStreamWriter osr = new OutputStreamWriter(socket.getOutputStream());
					            for(String key=lin.readLine();key!=null;key=lin.readLine()){
					            	key = key.trim();
					            	logger.info(key);
					            	if(ssl() && !ssl){
					            		CmdPacket command = getCommandPacket(key.split(" "));
					            		if(command==null)continue;
					            		if(command.isHelp() && help()!=null){
					            			osr.write(help());
					            		}else{
					            			ssl = authenticate(command);
						            		if(!ssl){
						            			osr.write("password or username error.\n");
						            			osr.flush();
						            		}else{
						            			osr.write("login success.\n");
						            			osr.flush();
						            		}
					            		}
					            	}else{
					            		CmdPacket command = getCommandPacket(key.split(" "));
					            		if(command==null)continue;
					            		if(command.isHelp() && help()!=null){
					            			osr.write(help());
					            			osr.flush();
					            		}else{
					            			String resp = command(command);
						            		if(resp!=null){
						            			osr.write(resp+"\n");
						            			osr.flush();
						            		}
					            		}
					            	}
					            }
					            logger.info("connection have been closed");
							} catch(Exception e){
								logger.info(String.format("connection(%s) have been closed", socket.getRemoteSocketAddress()));
			                }
			                finally{
			                    if (socket!=null){
			                        try{
			                        	socket.close();
			                        }catch(Exception e){}
			                    }
			                }
						}
                    	
                    };
                    
                    ConnectZ.execute(thread);
                }
                catch(Exception e){
                	logger.error("", e);
                }
            }
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			logger.error("", e);
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("", e);
			e.printStackTrace();
		}
	}

	public void stopListener() {
		// TODO Auto-generated method stub
		run = false;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public ServerSocket getSocket() {
		return socket;
	}

	public void setSocket(ServerSocket socket) {
		this.socket = socket;
	}

	public boolean isRun() {
		return run;
	}

	public void setRun(boolean run) {
		this.run = run;
	}

	public abstract boolean ssl();
	public abstract boolean authenticate(CmdPacket command);
}
