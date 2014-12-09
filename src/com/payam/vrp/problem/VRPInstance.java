package com.payam.vrp.problem;

public class VRPInstance extends Instance 
{
	String fileName;
	int[][] nodes;  //first node is the only depot
	int[][] distances;  //distances between nodes
	
	
	
	
	public VRPInstance(String fileName) 
	{
		super();
		this.fileName = fileName;
		
		
	}
	
	
}
