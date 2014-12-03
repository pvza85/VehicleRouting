package com.payam.vrp.reader;

public abstract class Reader 
{
	protected String fileName;
	public enum FileType
	{
		Augret
	}
	
	public Reader(String fileName)
	{
		this.fileName = fileName;
	}
	public abstract void read();
}
