package com.payam.vrp.reader;


/**
 * @author payam.azad
 * An abstract common class that read problem instances from files
 */
public abstract class Reader 
{
	protected String fileName;
	
	public String name;      //Instance name
	public String comment;   //comments written in the instance file
	public int capacity;     //capacity of vehicles
	public int dimension;    //number of  points to visit
	public int[][] nodes;    //coordination of each node
	public int[] demands;    //amount (weight) of demand each customer has
	public int[] depots;     //list of indices of depots
	public int vehicleCount; //number of vehicles for solving problem
	
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
