package com.connectz.util.media;

import com.connectz.util.RandomUtils;

public class DefaultMediaFactory extends MediaFactory {


	public DefaultMediaFactory(String mediaStore) {
		super(mediaStore);
		// TODO Auto-generated constructor stub
	}
	
	

	@Override
	public MediaFile getMediaFile(String mediaid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void uploaded(MediaFile mediaFile) {
		// TODO Auto-generated method stub

	}

	@Override
	public String generateMediaId() {
		// TODO Auto-generated method stub
		return RandomUtils.generateRandom(20);
	}



	@Override
	public boolean isSaveFile() {
		// TODO Auto-generated method stub
		return true;
	}

}
