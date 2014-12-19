package com.payam.vrp.solver.populationbased;

import com.payam.vrp.problem.Instance;

public class Individual 
{
	public int[] chromosome;
	public double fitness;
	Instance problem;
	
	public Individual(int[] chromosome, Instance problem)
	{
		this.chromosome = chromosome;
		this.problem = problem;
		
		evaluate();
	}
	
	public double evaluate()
	{
		fitness = problem.evaluator.evaluate(chromosome);
		return fitness;
	}
}
