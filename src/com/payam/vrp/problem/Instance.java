package com.payam.vrp.problem;

import com.payam.vrp.evaluator.CVRPEvaluator;
import com.payam.vrp.evaluator.Evaluator;
import com.payam.vrp.reader.AugretReader;
import com.payam.vrp.reader.Reader;

/**
 * An instance of VRP problem
 * @author payam.azad
 *
 */
public abstract class Instance 
{
	public Evaluator evaluator;
	
	public String name;    //Instance name
	public String comment; //comments written in the instance file
	public int capacity;   //capacity of vehicles
	public int dimension;  //number of  points to visit
	public int[][] nodes;  //coordination of each node
	public int[] demands;  //amount (weight) of demand each customer has
	public int[] depots;   //list of indices of depots
	public int vehicleCount; //number of vehicles for solving problem
	
	//state of problem
	public int state;   //0: not solved  1: solved
	
	/**
	 * Constructor that take input file name and reader type
	 * @param inputFile
	 * @param type
	 */
	public Instance(String inputFile, Reader.FileType type)
	{
		
		state = 0;  //set to not solved
		
		Reader reader;
		if(type == Reader.FileType.Augret)
			reader = new AugretReader(inputFile);    //call augret reader
		else
			reader = new AugretReader(inputFile);  //XXX temporarily there is no kind of reader
		
		
		reader.read();
		
		this.name = reader.name;
		this.comment = reader.comment;
		this.capacity = reader.capacity;
		this.dimension = reader.dimension;
		this.nodes = reader.nodes;
		this.demands = reader.demands;
		this.depots = reader.depots;
		this.vehicleCount = reader.vehicleCount;
		
		this.evaluator = new CVRPEvaluator(capacity, vehicleCount, nodes, demands);
	}
	
	/**
	 * call default Augret reader as reader
	 * @param inputFile
	 */
	public Instance(String inputFile)
	{
		this(inputFile, Reader.FileType.Augret);
	}
}
