package com.payam.vrp.solver.individualsolver.greedysolver;

import java.util.LinkedList;
import java.util.List;

import com.payam.vrp.problem.Instance;

import static com.payam.vrp.Util.*;


/**
 * this class find a solution for VRP using Nearest Neighbor algorithm
 * It is pretty fast and give reasonable results to start.
 * The complexity is O(x^3) ! actually it is not that much good now
 * 
 * XXX a little change will lead to O(n*log(n)) from O(n^3).. huraaay
 * @author payam.azad
 *
 */
public class NearestNeighborGreedySolver extends GreedySolver 
{


	List<Integer> candidSolution = new LinkedList<Integer>();
	
	public NearestNeighborGreedySolver(Instance problem) 
	{
		super(problem);
	}
	
	public void solve()
	{
		
		
		int vehicleCounter = 0;
		int customerCounter = 1;   //the first customer is depot itself XXX it will not work on multi-depot problems
		int itemCounter = 0;
		int assignedWeight = 0;
		candidSolution.add(0); //set start point to be depot
		
		while(vehicleCounter < vehicleCount && customerCounter < dimension - 1)
		{
			int itr = findNearestNeighbor(candidSolution.get(candidSolution.size()-1));
			if(assignedWeight > capacity - demands[itr])
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
			
			itr = findNearestNeighbor(candidSolution.get(candidSolution.size()-1));
			assignedWeight += demands[itr];
			customerCounter++;
			candidSolution.add(itr);
		}
		candidSolution.add(0);  
		
		bestSolution = new int[candidSolution.size()];
		for(int i = 0; i < bestSolution.length; i++)
			bestSolution[i] = candidSolution.get(i).intValue();
		
		evaluate();
		
		problem.state = 1;
	}
	

	
	/**
	 * find a nearest neighbor to a that is not added to list
	 * 
	 * @param a   index of a node that we wish to find it's neighbor
	 * @return    nearest neighbor for node[a]
	 */
	private int findNearestNeighbor(int a)
	{
		double bestDistance = Double.MAX_VALUE;
		int best = 0;
		//XXX I have to do something to reduce the complexity here to O(n) it is O(n^2)
		//XXX add an array to hold a variable for each node, if it is added or not => O(n) :D

		for(int i = 0; i < dimension; i++)
		{
			if(!candidSolution.contains(i))
			{
				double t = evaluator.distance(a, i);
				if(t < bestDistance)
				{
					bestDistance = t;
					best = i;
				}
			}
		}
		
		return best;
	}

}
