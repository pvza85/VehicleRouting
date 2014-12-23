package com.payam.vrp.solver.populationbased;

import com.payam.vrp.problem.Instance;
import com.payam.vrp.solver.Solver;

/**
 * A VRP solver based on population based methods like EHSBA and GA
 * 
 * @author payam.azad
 */
public class PopulationBasedSolver extends Solver 
{
	public Population population;
	public int populationSize;
	public int maxIteration;
	 
	public PopulationBasedSolver(Instance problem) 
	{
		super(problem);
		populationSize = 40;
		maxIteration = 100;
	}
	
	public PopulationBasedSolver(Instance problem, int populationSize, int maxIteration) 
	{
		super(problem);
		this.populationSize = populationSize;
		this.maxIteration = maxIteration;
	}
	
	private void Initialize()
	{
		
	}

}
