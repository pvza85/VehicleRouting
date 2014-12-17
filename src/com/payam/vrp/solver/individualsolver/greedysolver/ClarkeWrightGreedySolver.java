package com.payam.vrp.solver.individualsolver.greedysolver;

import com.payam.vrp.problem.Instance;

/**
 * implementation of Clarke Wright Algorithm presented at:
 * 
 * G. Clarke and J. Wright “Scheduling of vehicles from a central depot to a number of delivery points”, 
 * Operations Research, 12 #4, 568-581, 1964.
 * 
 * This algorithm is using savings and preduce pretty nice results for VRP
 * 
 * @author payam.azad
 *
 */
public class ClarkeWrightGreedySolver extends GreedySolver 
{

	public ClarkeWrightGreedySolver(Instance problem) 
	{
		super(problem);
	}

}
