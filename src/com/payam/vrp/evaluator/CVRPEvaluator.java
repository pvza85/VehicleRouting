package com.payam.vrp.evaluator;

public class CVRPEvaluator extends Evaluator 
{
	
	
	public CVRPEvaluator(int capacity, int vehicleCount, int[][] nodes,int[] demands) 
	{
		super(capacity, vehicleCount, nodes, demands);
	}

	public double evaluate(int[] input)
	{
		double res = 0.0;
		
		for(int i = 0; i < input.length-1; i++)
		{
			res += distance(input[i], input[i+1]);
		}
		
		return res;
	}
	
	private double distance(int a, int b)
	{
		double temp = Math.pow((nodes[a][0] - nodes[b][0]), 2) + Math.pow((nodes[a][1] - nodes[b][1]), 2);
		return Math.sqrt(temp);
	}
}
