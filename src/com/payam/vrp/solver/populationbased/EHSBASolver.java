package com.payam.vrp.solver.populationbased;

import java.util.LinkedList;

import org.apache.log4j.Logger;

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
	
	static Logger logger = Logger.getLogger(EHSBASolver.class);
	
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
		bestSolution = population.getBestChromosome();
		bestFitness = population.bestMemberFitness;
		print("At initialization the best solution's fitness is: " + population.getBestValue());
		//print("At initialization the average solution's fitness is: " + population.getAverage());
	}
	
	@Override
	public int[] solve()
	{
		int maxGeneration = 1000;
		
		EHSBA ehsba = new EHSBA(problem);
		//Population
		double[] results = new double[maxGeneration];
		boolean flag = false;
		double sum = 0.0;
		for(int i = 0; i < maxGeneration; i++)
		{
			if(!flag && (i+1) % changeFrequency == 0)
			{
				population.problem.change();
				//System.out.println("c");
				flag = !flag;
			}
			else if(flag && (i+1) % changeFrequency == 0)
			{
				population.problem.revertChange();
				//System.out.println("r");
				flag = !flag;
			}
			population = ehsba.iterate(population);
			results[i] = population.getBestValue();
			
			//System.out.println(results[i]);
			
			sum += (results[i] - population.problem.optimal);
			logger.debug(String.format("1; %d; %f; %f; %f", i, sum / i, population.bestMemberFitness, population.bestMemberFitness));
		}
		
		this.bestFitness = population.bestMemberFitness;
		this.bestSolution = population.getBestChromosome();
		
		System.out.println("offline error is: " + sum);
		return population.getBestChromosome();
	}

}
