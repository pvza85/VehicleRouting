package com.payam.vrp.evaluator;

public class CVRPEvaluator extends Evaluator 
{
	
	
	public CVRPEvaluator(int capacity, int vehicleCount, int[][] nodes,int[] demands) 
	{
		super(capacity, vehicleCount, nodes, demands);
	}

	public double evaluate(int[] input)
	{
		int i = 0;
		double res = distance(0, input[i]);
		
		for(; i < input.length-1; i++)
		{
			int next, current;
			if(input[i] > nodes.length)
				current = 0;
			else
				current = input[i];
			if(input[i+1] > nodes.length)
				next = 0;
			else
				next = input[i+1];
			
			if(next < nodes.length &&  current < nodes.length )
				res += distance(current, next);
			else
			{
				res += 1000;
				System.out.print(".");
			}
		}
		res += distance(input[i], 0);
		
		return res;
	}
	
	public double distance(int a, int b)
	{
		double temp;
		try
		{

			 temp = Math.pow((nodes[a][0] - nodes[b][0]), 2) + Math.pow((nodes[a][1] - nodes[b][1]), 2);
		}
		catch(Exception e){ temp = 100; }
		return Math.sqrt(temp);
	}
}
