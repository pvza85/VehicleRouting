package com.payam.vrp;

import java.io.File;

import com.payam.vrp.problem.Instance;
import com.payam.vrp.problem.VRPInstance;
import com.payam.vrp.solver.Solver;
import com.payam.vrp.solver.individualsolver.greedysolver.ClarkeWrightGreedySolver;
import com.payam.vrp.solver.populationbased.EHSBASolver;

public class MainVRP 
{

	public static Parameters parameters;
	
	public static void main(String[] args) 
	{
		//initialize parameters reading config file
		initialize("");
		
		//making instance from file name

		String str = "A-n32-k5.vrp";
		String fileName = "input//" + str;
		
		
		Instance problem = new VRPInstance(fileName);
		System.out.println(problem.comment);
		//solve problem

		//Solver solver2 = new SimpleGreedySolver(problem);
		//Solver solver1 = new NearestNeighborGreedySolver(problem);
		Solver solver = new ClarkeWrightGreedySolver(problem);
		//Solver solver = new EHSBASolver(problem);
		solver.solve();
		//solver.printResult();
		
		//solver = new ClarkeWrightGreedySolver(problem);
		Solver solver3 = new EHSBASolver(problem);
		solver3.solve();
		//solver1.printResult();
		
		System.out.println("finished.");
		
		
		/*File folder = new File("input");
		File[] listOfFiles = folder.listFiles();
		
		for(int i = 0; i < listOfFiles.length; i++)
		{
		
			String str = listOfFiles[i].getName();
			System.out.println(str);
			if(listOfFiles[i].isFile() && str.substring(0, 1).equalsIgnoreCase("A"))
				fileName = "input\\" + str;
			else
				continue;

			//fileName = "input\\" + fileName;
			Instance problem = new VRPInstance(fileName);
			System.out.println(problem.comment);
			//solve problem
	
			//Solver solver = new SimpleGreedySolver(problem);
			//Solver solver = new NearestNeighborGreedySolver(problem);
			Solver solver = new ClarkeWrightGreedySolver(problem);
			//Solver solver = new EHSBASolver(problem);
			solver.solve();
			solver.printResult();	

		}*/

		
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
