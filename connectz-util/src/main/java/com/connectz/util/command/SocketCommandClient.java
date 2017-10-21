package com.connectz.util.command;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import com.connectz.util.ConnectZ;

public abstract class SocketCommandClient {
	protected Logger logger = Logger.getLogger(getClass());
	private String host;
    private int port;
    private Socket socket;
    private OutputStreamWriter osr;
    private boolean connected;
    
    public SocketCommandClient(String host, int port){
    	this.host = host;
    	this.port = port;
    }
    
    public abstract void response(String[] command);
    public void sendCommand(String[] command) throws IOException {
    	if(command!=null && osr!=null){
    		try{
        		osr.write(mergeCommand(command));
        		osr.flush();
    		}catch(IOException e){
    			connected = false;
    			throw e;
    		}
    	}
    }
    
    public static String mergeCommand(String[] command){
    	StringBuilder sb = new StringBuilder();
    	for(String cmd : command){
    		sb.append(cmd).append(" ");
    	}
    	sb.append("\n");
    	return sb.toString();
    }
    
    public void disconnect(){
    	if (osr!=null){
            try{
            	osr.close();
            }catch(Exception e){}
        }
    	 if (socket!=null){
             try{
             	socket.close();
             }catch(Exception e){}
         }
         connected = false;
    }
    
    public boolean connect(String username, String passwd){
    	try {
    		logger.info(String.format("connecting %s %s", host,port));
    		socket = new Socket(InetAddress.getByName(host),port);
		    Runnable thread = new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						LineNumberReader lin=
			                        new LineNumberReader(new InputStreamReader(socket.getInputStream()));
			            for(String key=lin.readLine();key!=null;key=lin.readLine()){
			            	response(key.split(" "));
			            }
					} catch(Exception e){
	                    System.err.println(e.toString());
	                    
	                }
	                finally{
	                    if (socket!=null){
	                        try{
	                        	socket.close();
	                        }catch(Exception e){}
	                    }
	                    connected = false;
	                }
				}
            	
            };
            ConnectZ.execute(thread);
            osr = new OutputStreamWriter(socket.getOutputStream());
			connected = true;
			logger.info("connected");
			return true;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
		}
    	return false;
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

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}
}
