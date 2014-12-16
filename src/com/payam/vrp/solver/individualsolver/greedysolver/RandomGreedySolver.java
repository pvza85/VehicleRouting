package com.payam.vrp.solver.individualsolver.greedysolver;

import com.payam.vrp.problem.Instance;
import static com.payam.vrp.Util.*;

/**
 * a solver that just create a random solution for the problem
 * this problem will be 
 * @author payam.azad
 *
 */
public class RandomGreedySolver extends GreedySolver {

	public RandomGreedySolver(Instance problem) {
		super(problem);
	}

	@Override
	public void solve()
	{
		
	}
}
