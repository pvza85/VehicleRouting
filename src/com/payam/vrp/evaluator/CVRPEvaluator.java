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
		int[] visits = new int[nodes.length];  //array to check if all customers visited just once
		double res = distance(0, input[i]);
		int load = demands[i];
		int next = 0, current = 0;
		for(; i < input.length-1; i++)
		{
			if(input[i] >= nodes.length)
				current = 0;
			else
				current = input[i];
			if(input[i+1] >= nodes.length)
			{
				next = 0;
				load = 0;
			}
			else
				next = input[i+1];
			
			if(next < nodes.length &&  current < nodes.length )
			{
				res += distance(current, next);
				if(load > capacity)
				{
					res += 1000;
					//System.out.println("capacity exceeded!");
				}
			}
			else
			{
				res += 1000;
				System.out.print(".");
			}
			if(next == 0 && current == 0)
			{
				res += 1000;
				//System.out.println("two depot repeated.");
			}
			visits[current]++;
		}
		res += distance(input[next], 0);
		visits[next]++;
		
		
		//check if all customers visited just once
		for(int j = 1; j < nodes.length; j++)
		{
			if(visits[j] > 1)
			{
				res += 1000;
				//System.out.println("more than one visit to: " + j);
			}
			else if(visits[j] == 0)
			{
				res += 1000;
				//System.out.println("no visit to: " + j);
			}
		}
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
