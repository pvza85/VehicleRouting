package com.payam.vrp.solver.individualsolver.greedysolver;

import java.util.LinkedList;
import java.util.List;

import com.payam.vrp.problem.Instance;

import static com.payam.vrp.Util.*;

/**
 * Simple Greedy algorithm for solving Capacitated VRP
 * it starts from the first customer and assign it to the first vehicle 
 * until the capacity of the vehicle is full.
 * Then continue this process until all customers assigned to vehicles.
 * 
 * @author payam.azad
 *
 */
public class SimpleGreedySolver extends GreedySolver {

	public SimpleGreedySolver(Instance problem) 
	{
		super(problem);
	}
	
	public int[] solve()
	{
		List<Integer> candidSolution = new LinkedList<Integer>();
		
		int vehicleCounter = 0;
		int customerCounter = 1;   //the first customer is depot itself XXX it will not work on multi-depot problems
		int itemCounter = 0;
		int assignedWeight = 0;
		candidSolution.add(0); //set start point to be depot
		while(vehicleCounter < vehicleCount && customerCounter < dimension - 1)
		{
			if(assignedWeight > capacity - demands[customerCounter])
			{
				candidSolution.add(0);  //set start point to be depot for new vehicle and end point depot for previous vehicle
				candidSolution.add(0);
				vehicleCounter++;
				assignedWeight = 0;
				if(vehicleCounter == vehicleCount)
				{
					print("Problem can't be solved");
					break;
				}
			}
			
			assignedWeight += demands[customerCounter];
			candidSolution.add(customerCounter);
			customerCounter++;
		}
		
		candidSolution.add(0); //set end point to be depot
		
		bestSolution = new int[candidSolution.size()];
		for(int i = 0; i < bestSolution.length; i++)
			bestSolution[i] = candidSolution.get(i).intValue();
		
		evaluate();
		
		problem.state = 1;
		
		return bestSolution;
	}

}
