package com.payam.vrp.solver;

import com.payam.vrp.evaluator.CVRPEvaluator;
import com.payam.vrp.evaluator.Evaluator;
import com.payam.vrp.problem.Instance;

import static com.payam.vrp.Util.*;

/**
 * Solver class that have an instance of VRP problem and solve it.
 * 
 * @author payam.azad
 */
public class Solver 
{
	public Instance problem;
	public int[] bestSolution;   //contain best solution array
	public double bestFitness;   //cost of best solution
	
	public String name;    //Instance name
	public String comment; //comments written in the instance file
	public int capacity;   //capacity of vehicles
	public int dimension;  //number of  points to visit
	public int[][] nodes;  //coordination of each node
	public int[] demands;  //amount (weight) of demand each customer has
	public int[] depots;   //list of indices of depots
	public int vehicleCount; //number of vehicles for solving problem
	
	public Evaluator evaluator;
	
	public Solver(Instance problem)
	{
		this.problem = problem;
		
		this.name = problem.name;
		this.comment = problem.comment;
		this.capacity = problem.capacity;
		this.dimension = problem.dimension;
		this.nodes = problem.nodes;
		this.demands = problem.demands;
		this.depots = problem.depots;
		this.vehicleCount = problem.vehicleCount;
		this.evaluator = problem.evaluator;
	}
	
	public int[] solve()
	{
		return bestSolution;
	}
	
	public String toString()
	{
		String res = "";
		
		if(problem.state == 0)
			res = "not solved yet";
		else
		{
			int routeCounter = 1;
			boolean newRoute = true;
			for(int i = 0; i < bestSolution.length; i++)
			{
				if(newRoute)
				{
					res = String.format("%s\nroute#%d: ", res, routeCounter++);
					newRoute = false;
				}
				
				if(bestSolution[i] == 0  )
					newRoute = true;
				else
					res = String.format("%s %d ", res, bestSolution[i]);
			}
		}
		
		return res;
	}
	
	
	public void printResult()
	{
		print("The best result is: " + bestFitness);
		print(this.toString());
		/*if(problem.state == 0)
			print("not solved yet");
		else if(problem.state == 1)
		{
			print("The best result is: " + bestFitness);
			
			int counter = 1;
			boolean depotSeen = true;
			for(int i = 0; i < bestSolution.length; i++)
			{
				if(bestSolution[i] == 0)
				{
					if(depotSeen)
					{
						System.out.printf("\nroute#%d: ", counter++);
						depotSeen = false;
					}
					else
						depotSeen = true;
				}
				System.out.printf("%d   ", bestSolution[i]+1);
						
					
			}
		}*/
	}
	
	public double evaluate()
	{
		bestFitness = evaluator.evaluate(bestSolution);
		
		return bestFitness ;
	}
}
