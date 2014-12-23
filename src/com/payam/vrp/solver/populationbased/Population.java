package com.payam.vrp.solver.populationbased;

import com.payam.vrp.problem.Instance;
import com.payam.vrp.solver.individualsolver.greedysolver.ClarkeWrightGreedySolver;
import com.payam.vrp.solver.individualsolver.greedysolver.NearestNeighborGreedySolver;
import com.payam.vrp.solver.individualsolver.greedysolver.SimpleGreedySolver;

/**
 * A group (population) of {@link Individual}s.
 * We can evaluate best member, average fitness and...
 * @author payam.azad
 */
public class Population 
{
	/**
	 * an array of {@link Individual}s
	 */
	public Individual[] members;
	public int populationSize;
	public int chromosomeSize;
	public int bestMemberIndex;      //index of best member in members array
	public double bestMemberFitness; //fitness of best member
	public double averageFitness;    
	public Instance problem; 
	
	public Population(Instance problem, int populationSize) 
	{
		super();
		this.problem = problem;
		this.populationSize = populationSize;
		this.chromosomeSize = problem.dimension + problem.vehicleCount;
		
		this.members = new Individual[populationSize];
	}
	
	/**
	 * evaluate all members of population
	 * using {@link Individual#evaluate()}
	 * and find the best member
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
		
		bestMemberIndex = bestIndex;
		bestMemberFitness = bestFitness;
		
		return bestFitness;
	}
	
	/**
	 * initialize population using three methods
	 * simple, nearest neighbor, clarkewright
	 */
	public void initialize()
	{
		//int[] init1 = (new NearestNeighborGreedySolver(problem)).solve();
		//int[] init2 = (new SimpleGreedySolver(problem)).solve();
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
	
	/**
	 * @return average fitness of all members of population
	 */
	public double getAverage()
	{
		double average = 0.0;
		for(int i = 0; i < members.length; i++)
		{
			average += members[i].fitness / populationSize;
		}
		
		averageFitness = average;
		
		return averageFitness;
	}
	
	/**
	 * @return fitness of best member in population
	 */
	public double getBestValue()
	{
		return bestMemberFitness;
	}
	
	/**
	 * @return chromosome of the best member as an array
	 */
	public int[] getBestChromosome()
	{
		return members[bestMemberIndex].chromosome;
	}
	
	/**
	 * @return best member of population as Individual
	 */
	public Individual getBestIndividual()
	{
		return members[bestMemberIndex];
	}
	
	/**
	 * get population in a matrix for further Linear Algebera uses.
	 * @return two dimensional matrix of solutions; one solution in each row
	 */
	public int[][] getMatrix()
	{
		int[][] res = new int[populationSize][chromosomeSize];
		
		for(int i = 0; i < populationSize; i++)
			res[i] = members[i].chromosome;
		
		return res;
	}
}
