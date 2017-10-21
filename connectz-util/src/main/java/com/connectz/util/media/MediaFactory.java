package com.connectz.util.media;

import java.io.File;

public abstract class MediaFactory {
	private static MediaFactory factory;
	public  static MediaFactory instance(){
		return factory;
	}
	
	private String mediaStore;
	private static final String MEDIA_STORE="media.store";
	private static final String DEFAULT_STORE = System.getProperty("user.dir")+File.separator+"mediastroe";
	
	public MediaFactory(){
		setMediaStore(null);
		factory = this;
	}
	
	public MediaFactory(String mediaStore){
		setMediaStore(mediaStore);
		factory = this;
	}
	
	public File getTempFile(String mediaid){
	    File file    = new File(mediaStore,mediaid);
		return file;
	}
	
	public abstract MediaFile getMediaFile(String mediaid);
	public abstract void uploaded(MediaFile mediaFile);
	public abstract String generateMediaId();
	
	private void checkDir(String dir){
		File file = new File(dir);
		if(!file.exists())
			file.mkdirs();
	}

	public String getMediaStore() {
		return mediaStore;
	}

	public void setMediaStore(String mediaStore) {
		if(mediaStore==null)
			mediaStore = System.getProperty(MEDIA_STORE, DEFAULT_STORE);
		this.mediaStore = mediaStore;
		checkDir(mediaStore);
	}
	
	public abstract boolean isSaveFile();
	
}
