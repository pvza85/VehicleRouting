package com.payam.vrp.solver.populationbased;

import com.payam.vrp.problem.Instance;

/**
 * Each individual is a solution to VRP problem that is referenced inside it.
 * These individuals can be used in PopulationBased Solvers.
 * 
 * @author payam.azad
 */
public class Individual 
{
	/**
	 * Permutation of customers to visit, in single depot VRP customer indicated by 0 is depot
	 */
	public int[] chromosome;
	public double fitness = -1;
	public boolean evaluated = false;
	Instance problem;
	
	
	public Individual(int[] chromosome, Instance problem)
	{
		this.chromosome = chromosome;
		this.problem = problem;
		
		evaluate();
	}
	
	/**
	 * evaluate the fitness of a chromosome array in the case that fitness is not evaluated
	 * if fitness evaluated simply it returns it.
	 * complexity of evaluation is O(n)
	 * 
	 * @return fitness of this individual
	 */
	public double evaluate()
	{
		if(evaluated == false || fitness == -1)
			fitness = problem.evaluator.evaluate(chromosome);
		
		//if(validate() != 1)
		//	fitness = Double.MAX_VALUE;
		
		evaluated = true;
		return fitness;
	}
	
	/**
	 * validate if the Individual solution is a valid solution for problem based on:
	 * <ol>
	 * <li>each vehicle does not exceed its capacity</li>
	 * <li>all the customers visited</li>
	 * <li>all the customers visited no more than one time</li>
	 * @return +1: if it is valide, -1: if capacities exceeded -2: if all customers does not visited -3: if some customers visited more than once
	 */
	public int validate()
	{
		int[] customers = new int[problem.dimension];
		
		int routeCounter = 1;
		double sum = 0;
		
		for(int i = 0; i < chromosome.length; i++)
		{
			if(chromosome[i] == 0)
			{
				customers[chromosome[i]] = 1;
				if(sum > problem.capacity)
					return -1;
				sum = 0;
			}
			else
			{
				sum += problem.demands[chromosome[i]];
				customers[chromosome[i]]++;
			}
		}
			
		for(int i = 0; i < customers.length; i++)
		{
			if(customers[i] < 1)
				return -2;
			else if(customers[i] > 1)
				return -3;
		}
		
		return 1;
	}
	
	
	public String toString()
	{
		String res = "";
		int routeCounter = 1;
		for(int i = 0; i < chromosome.length; i++)
		{
			if(chromosome[i] == 0  )
				res = String.format("\n%sroute#%d: ", res, routeCounter++);
			else
				res = String.format("%s %d ", res, chromosome[i]);
		}
		
		return res;
	}
	
	
	public String toSimpleString()
	{
		String res = "[";

		for(int i = 0; i < chromosome.length; i++)
			res = String.format("%s %d", res, chromosome[i]);
		res = String.format("%s ]", res);
		
		return res;
	}
}
