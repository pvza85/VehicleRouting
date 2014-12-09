package com.payam.vrp.reader;


/**
 * @author payam.azad
 * An abstract common class that read problem instances from files
 */
public abstract class Reader 
{
	protected String fileName;
	
	/**
	 * different types of files
	 * @author payam.azad
	 *
	 */
	public enum FileType
	{
		Augret
	}
	/**
	 * read from file
	 * @param fileName input file name. This file have to be inside input folder by default or provide local address
	 */
	public Reader(String fileName)
	{
		this.fileName = fileName;
	}
	public abstract void read();
}
