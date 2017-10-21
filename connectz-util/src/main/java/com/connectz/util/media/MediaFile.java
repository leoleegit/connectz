package com.connectz.util.media;

import java.io.File;
import java.io.InputStream;

import com.connectz.util.json.JsonObjectImpl;

public class MediaFile extends JsonObjectImpl {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int idfile;
	private File file;
	private String name;
	private String mediaID;
	private String contentType;
	private String suffix;
	private long size;
	private long createTime;
	private String type;
	private int isdelete;
	private InputStream inputStream;

	public MediaFile() {
		super(null);
		// TODO Auto-generated constructor stub
	}
	
	public MediaFile(String json) {
		super(json);
		// TODO Auto-generated constructor stub
	}

	public void setIdfile(int idfile) {
		this.idfile = idfile;
	}

	public int getIdfile() {
		return idfile;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setMediaID(String mediaID) {
		this.mediaID = mediaID;
	}

	public String getMediaID() {
		return mediaID;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getContentType() {
		return contentType;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public long getSize() {
		return size;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setIsdelete(int isdelete) {
		this.isdelete = isdelete;
	}

	public int getIsdelete() {
		return isdelete;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

}
