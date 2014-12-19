package com.payam.vrp;

import com.payam.vrp.problem.Instance;
import com.payam.vrp.problem.VRPInstance;
import com.payam.vrp.solver.Solver;
import com.payam.vrp.solver.populationbased.EHSBASolver;

public class MainVRP 
{

	public static Parameters parameters;
	
	public static void main(String[] args) 
	{
		//initialize parameters reading config file
		initialize("");
		
		//making instance from file name

		String fileName = "A-n32-k5.vrp";

		fileName = "input\\" + fileName;
		Instance problem = new VRPInstance(fileName);
		
		//solve problem

		//Solver solver = new SimpleGreedySolver(problem);
		//Solver solver = new NearestNeighborGreedySolver(problem);
		//Solver solver = new ClarkeWrightGreedySolver(problem);
		Solver solver = new EHSBASolver(problem);


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
