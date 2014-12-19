package com.payam.vrp.solver.populationbased;

import com.payam.vrp.problem.Instance;
import com.payam.vrp.solver.individualsolver.greedysolver.ClarkeWrightGreedySolver;
import com.payam.vrp.solver.individualsolver.greedysolver.NearestNeighborGreedySolver;
import com.payam.vrp.solver.individualsolver.greedysolver.SimpleGreedySolver;

public class Population 
{
	public Individual[] members;
	public int populationSize;
	public int bestMember;
	public double bestMemberFitness; 
	public double averageFitness;
	public Instance problem;
	
	public Population(Instance problem, int populationSize) 
	{
		super();
		this.problem = problem;
		this.populationSize = populationSize;
		
		this.members = new Individual[populationSize];
	}
	
	/**
	 * evaluate all members of population
	 * using problem.evaluate()
	 * 
	 * @return the best fitness of the population
	 */
	public double evaluate()
	{
		int bestIndex = -1;
		double bestFitness = Double.MAX_VALUE;
		
		for(int i = 0; i < members.length; i++)
		{
			double t = members[i].evaluate();
			if(t < bestFitness)
			{
				bestIndex = i;
				bestFitness = t;
			}
		}
		
		bestMember = bestIndex;
		bestMemberFitness = bestFitness;
		
		return bestFitness;
	}
	
	/**
	 * initialize population using three methods
	 * simple, nearest neighbor, clarkewright
	 */
	public void initialize()
	{
		int[] init1 = (new NearestNeighborGreedySolver(problem)).solve();
		int[] init2 = (new SimpleGreedySolver(problem)).solve();
		int[] init3 = (new ClarkeWrightGreedySolver(problem)).solve();
		
		int i = 0;
		/*for(; i < populationSize/3; i++)
		{
			members[i] = new Individual(init1, problem);
		}
		for(; i < 2 * populationSize/3; i++)
		{
			members[i] = new Individual(init2, problem);
		}*/
		for(; i < populationSize; i++)
		{
			members[i] = new Individual(init3, problem);
		}
	}
	
	public double getAverage()
	{
		double sum = 0;
		for(int i = 0; i < members.length; i++)
		{
			sum += members[i].fitness;
		}
		
		averageFitness = sum / populationSize;
		
		return averageFitness;
	}
	
	public double getBestValue()
	{
		return bestMemberFitness;
	}
	public int[] getBest()
	{
		return members[bestMember].chromosome;
	}
}
