package com.payam.vrp.solver.individualsolver.greedysolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.payam.vrp.problem.Instance;

/**
 * implementation of Parallel Clarke Wright Algorithm presented at:
 * 
 * G. Clarke and J. Wright “Scheduling of vehicles from a central depot to a number of delivery points”, 
 * Operations Research, 12 #4, 568-581, 1964.
 * 
 * This algorithm is using savings and preduce pretty nice results for VRP
 * 
 * It is time wasting O(n^3)
 * 
 * XXX need to introcuce it's time complexity
 * XXX need heave optmization and re-write
 * 
 * @author payam.azad
 *
 */
public class ClarkeWrightGreedySolver extends GreedySolver 
{
	double[][] saves;  //a matrix that hold saves; saves[i][j] is the amount that we save if we connect i to j
	List<Saving> savesList;
	int[] state;       //state of each node 0:not assigned  1:assigned to a end of route   2:assined inside a route
	List<Rout> routes;
	
	public ClarkeWrightGreedySolver(Instance problem) 
	{
		super(problem);
		
		
		//create initial routes of 0 -> i -> 0
		routes = new LinkedList<Rout>();
		for(int i = 1; i < dimension; i++)
			routes.add(new Rout(i));
	}


	/**
	 * @return an array of customers to visit as a solution.
	 */
	public int[] solve()
	{
		saves = constructSaves();

		//start merging saves
		for(int i = 0; i < savesList.size(); i++)
		{
			Saving currentSave = savesList.get(i);
			
			int[] res = findRoutes(currentSave.i, currentSave.j);
			if(res == null)
				continue;
			
			mergeRoutes(res[0], res[1]);
			
			//if(routes.size() == vehicleCount)
				//break;
		}
		
		List<Integer> candidSolution = new LinkedList<Integer>();
		for(int i = 0; i < routes.size(); i++)
		{
			candidSolution.addAll(routes.get(i).path);
		}
		
		//System.out.println(candidSolution.toString());
		bestSolution = new int[dimension + vehicleCount - 2];
		boolean depotVisited = false;
		int dep = problem.dimension;
		for(int i = 0, j = 0; i < candidSolution.size(); i++)
		{
			int n = candidSolution.get(i).intValue();
			
			if(!depotVisited && n == 0)
				depotVisited = true;
			else
			{
				if(j < bestSolution.length)
					if(n != 0)
						bestSolution[j] = n;
					else
						bestSolution[j] = ++dep;
				else
				{
					System.out.println("****Error****Error****Error****Error****Error*****Error****Error****Error****Error****Error*****");
					System.out.println(candidSolution.toString());
				}
				j++;
				depotVisited = false;
			}
		}
		
		evaluate();
		
		problem.state = 1;
		
		return bestSolution;
	}
	
	/**
	 * construct saves using s[i][j] = c[i][0] + c[0][j] - c[i][j]
	 * it returns save matrix also a sorted list of saves
	 *  
	 * @return a matrix of saves
	 */
	private double[][] constructSaves()
	{
		savesList = new LinkedList<Saving>();
		
		double [][] res = new double[dimension][dimension];
		
		
		//XXX i:0->dim   j:i->dim is enough
		for(int i = 0 ; i < dimension; i++)
		{
			for(int j = 0; j < dimension; j++)
			{
				if(i != j)
				{
					res[i][j] = evaluator.distance(i, 0) + evaluator.distance(0, j) - evaluator.distance(i, j);
					savesList.add(new Saving(i, j, res[i][j]));
				}
			}
		}
		/*for(int i = 1 ; i < dimension; i++)
		{
			for(int j = i+1; j < dimension; j++)
			{
				res[i][j] = evaluator.distance(i, 0) + evaluator.distance(0, j) - evaluator.distance(i, j);
				savesList.add(new Saving(i, j, res[i][j]));
				
			}
		}*/
		
		Collections.sort(savesList);
		
		return res;
	}

	//XXX have to optimize using arrays
	private boolean ifSavingPossible(int a, int b)
	{
		return false;
	}
	
	// saving
	private class Saving implements Comparable<Saving>
	{
		public int i, j;
		double save;
		public Saving(int i, int j, double save) {
			super();
			this.i = i;
			this.j = j;
			this.save = save;
		}
		@Override
		public int compareTo(Saving arg0) 
		{
			return Double.compare(arg0.save, this.save);
		}
		
	}
	
	private class Rout
	{
		List<Integer> path;
		int demand;
		
		public Rout(int a)
		{
			path = new ArrayList<Integer>(3);
			path.add(0);
			path.add(a);
			path.add(0);
			
			demand = demands[a];
		}
		
		public Rout(Rout a, Rout b)
		{
			List<Integer> temp = a.path.subList(0, a.path.size()-1);
			temp.addAll(b.path.subList(1, b.path.size()));
			this.path = temp;
			
			this.demand = a.demand + b.demand;
		}

		@Override
		public String toString() {
			return "Rout [path=" + path + "]\n";
		}
		
		
	}
	
	/**
	 * find two rout that start with 0->i and another ends with j->0
	 * if it is feasiible (demand is less than capacity)
	 * 
	 * @return return index to these route at routes list
	 */
	private int[] findRoutes(int i, int j)
	{
		int[] res = new int[2];
		res[0] = res[1] = -1;
		
		for(int counter = 0; counter < routes.size(); counter++)
		{
			// XXX remember make it more optimize
			//find route with ...i0 and 0j...
			if(routes.get(counter).path.get(routes.get(counter).path.size()-1) == 0 && routes.get(counter).path.get(routes.get(counter).path.size()-2) == i)
				res[0] = counter;
			else if(routes.get(counter).path.get(0) == 0 && routes.get(counter).path.get(1) == j)
				res[1] = counter; 
		}
		
		if(res[0] == -1 || res[1] == -1 )
			res = null;
		else if(routes.get(res[0]).demand + routes.get(res[1]).demand > capacity)
			res = null;
		
		return res;
	}
	
	/**
	 * merge to routes at index a and be and add it to the routes list
	 * and then remove routes a and be from this list
	 */
	private void mergeRoutes(int a, int b)
	{
		Rout r = new Rout(routes.get(a), routes.get(b));
		routes.add(r);
		if(a > b)
		{
			routes.remove(a);
			routes.remove(b);
		}
		else
		{
			routes.remove(b);
			routes.remove(a);
		}
	}
}
