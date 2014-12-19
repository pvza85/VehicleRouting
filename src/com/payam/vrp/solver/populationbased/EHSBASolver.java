package com.payam.vrp.solver.populationbased;

import com.payam.vrp.problem.Instance;
import com.payam.vrp.solver.individualsolver.greedysolver.ClarkeWrightGreedySolver;
import com.payam.vrp.solver.individualsolver.greedysolver.NearestNeighborGreedySolver;
import com.payam.vrp.solver.individualsolver.greedysolver.SimpleGreedySolver;

import static com.payam.vrp.Util.*;

/**
 * EHSBA implementation_temp
 * 
 * @author payam.azad
 *
 */
public class EHSBASolver extends PopulationBasedSolver 
{
	
	public EHSBASolver(Instance problem) 
	{
		this(problem, 40, 100);		
	}
	
	
	//XXX better to do these stuff at parent class
	public EHSBASolver(Instance problem, int populationSize, int maxIteration) 
	{
		super(problem);
		this.populationSize = populationSize;
		this.maxIteration = maxIteration;
		population = new Population(problem, populationSize);
		
		population.initialize();
		population.evaluate();
		bestSolution = population.getBest();
		bestFitness = population.bestMemberFitness;
		print("At initialization the best solution's fitness is: " + population.getBestValue());
		print("At initialization the average solution's fitness is: " + population.getAverage());
	}
	
	public int[] Solve()
	{
		return population.getBest();
	}

}
