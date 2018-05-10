package com.manu.springmail.mailutil;

import java.io.File;

public class MailAttachment {
	
	private Byte[] file;
	
	private File fileObjectc;
	
	private String filename;
	
	private boolean isInline;

	/**
	 * @return the file
	 */
	public Byte[] getFile() {
		return file;
	}

	/**
	 * @param file the file to set
	 */
	public void setFile(Byte[] file) {
		this.file = file;
	}

	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	public File getFileObjectc() {
		return fileObjectc;
	}

	public void setFileObjectc(File fileObjectc) {
		this.fileObjectc = fileObjectc;
	}

	public boolean isInline() {
		return isInline;
	}

	public void setInline(boolean isInline) {
		this.isInline = isInline;
	}

}
