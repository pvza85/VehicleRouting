package com.payam.vrp.problem;

import com.payam.vrp.evaluator.CVRPEvaluator;
import com.payam.vrp.evaluator.Evaluator;
import com.payam.vrp.reader.AugretReader;
import com.payam.vrp.reader.Reader;

import static com.payam.vrp.Util.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	public int a, b;
	public double optimal = 0; //784;
	
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
		
		Pattern pattern = Pattern.compile("Optimal value: [0-9]+");
		Pattern innerPattern = Pattern.compile("[0-9]+");
		Matcher matcher = pattern.matcher(comment);

		if(matcher.find())
		{
			String inner = matcher.group();
			Matcher innerMatcher = innerPattern.matcher(inner);
			if(innerMatcher.find())
				optimal = Double.parseDouble(innerMatcher.group());
		}
		System.out.println("Optimal Value yet is: " + optimal);
	}
	
	/**
	 * call default Augret reader as reader
	 * @param inputFile
	 */
	public Instance(String inputFile)
	{
		this(inputFile, Reader.FileType.Augret);
	}
	public void change()
	{
		a = random.nextInt(nodes.length);
		b = random.nextInt(nodes.length);
		
		int[] temp = nodes[a];
		nodes[a] = nodes[b];
		nodes[b] = temp;
		
		int tempd = demands[a];
		demands[a] = demands[b];
		demands[b] = tempd;
		
	}
	public void revertChange()
	{
		int[] temp = nodes[a];
		nodes[a] = nodes[b];
		nodes[b] = temp;
		
		int tempd = demands[a];
		demands[a] = demands[b];
		demands[b] = tempd;
		
	}
}
