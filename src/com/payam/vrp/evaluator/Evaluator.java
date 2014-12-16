package com.payam.vrp.evaluator;

public class Evaluator 
{
	public int capacity;   //capacity of vehicles
	public int vehicleCount; //number of vehicles for solving problem
	public int[][] nodes;  //coordination of each node
	public int[] demands;  //amount (weight) of demand each customer has

	

	


	public Evaluator(int capacity, int vehicleCount, int[][] nodes,
			int[] demands) {
		super();
		this.capacity = capacity;
		this.vehicleCount = vehicleCount;
		this.nodes = nodes;
		this.demands = demands;
	}






	public double evaluate(int[] input)
	{
		return 0.0;
	}

}
