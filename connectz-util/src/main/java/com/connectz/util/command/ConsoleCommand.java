package com.connectz.util.command;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

public abstract class ConsoleCommand extends Cmd{
	private boolean run;
	
	public void start() throws IOException{
		System.out.print(">");
		LineNumberReader lin=
                new LineNumberReader(new InputStreamReader(System.in));
		for(String key=lin.readLine();key!=null;key=lin.readLine()){
			CmdPacket command = getCommandPacket(key.split(" "));
			if(command==null)continue;
			if(command.isHelp() && help()!=null){
				System.out.println(help());
    		}else{
    			String resp = command(command);
            	if(resp!=null){
            		System.out.println(resp);
            	}
    		}
        	System.out.print(">");
        }
	}

	public boolean isRun() {
		return run;
	}

	public void setRun(boolean run) {
		this.run = run;
	}

}
