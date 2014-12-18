package com.payam.vrp;

import com.payam.vrp.problem.Instance;
import com.payam.vrp.problem.VRPInstance;
import com.payam.vrp.solver.Solver;
import com.payam.vrp.solver.individualsolver.greedysolver.ClarkeWrightGreedySolver;
import com.payam.vrp.solver.individualsolver.greedysolver.NearestNeighborGreedySolver;
import com.payam.vrp.solver.individualsolver.greedysolver.SimpleGreedySolver;

public class MainVRP 
{

	public static Parameters parameters;
	
	public static void main(String[] args) 
	{
		//initialize parameters reading config file
		initialize("");
		
		//making instance from file name

		String fileName = "A-n38-k5.vrp";

		fileName = "input\\" + fileName;
		Instance problem = new VRPInstance(fileName);
		
		//solve problem

		//Solver solver = new SimpleGreedySolver(problem);
		//Solver solver = new NearestNeighborGreedySolver(problem);
		Solver solver = new ClarkeWrightGreedySolver(problem);


		solver.solve();
		solver.printResult();
	}
	
	/**
	 * Initialize parameters to set to the desired values in Parameters class; 
	 * reading from config file.
	 * @param configFile name of config file
	 */
	private static void initialize(String configFile)
	{
		
	}
	
	

}
