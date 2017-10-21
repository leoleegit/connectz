package com.connectz.util.json;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

public class FileConfig  extends JsonObjectImpl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FileConfig(String json) {
		super(json);
		// TODO Auto-generated constructor stub
	}

	public FileConfig(File file) {
		super(null);
		// TODO Auto-generated constructor stub
		if(file.exists()){
			try {
				FileInputStream is = new FileInputStream(file);
				initialized(is);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				logger.error("", e);
			}
		}else{
			logger.info("can not found file:"+file.getAbsolutePath());
		}
	}
	
	public FileConfig(InputStream is) {
		super(null);
		// TODO Auto-generated constructor stub
		initialized(is);
	}
	
	
	protected void initialized(InputStream is){
		// TODO Auto-generated constructor stub
		if(is!=null){
			try {
				StringBuilder sb = new StringBuilder();
				LineNumberReader lin=
	                        new LineNumberReader(new InputStreamReader(is));
		        for(String str = lin.readLine();str!=null;str = lin.readLine()){
		        	if(!str.trim().startsWith("#")){
		        		sb.append(str);
		        	}
		        }
		        is.close();  
		        json2Object(sb.toString(),this);

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				logger.error("", e);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error("", e);
			}
		}else{
			logger.info("InputStream is Null");
		}
	}
}
