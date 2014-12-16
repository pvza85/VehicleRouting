package com.payam.vrp.solver;

import com.payam.vrp.problem.Instance;

/**
 * Solver class that have an instance of VRP problem and solve it.
 * 
 * @author payam.azad
 */
public class Solver 
{
	public Instance problem;
	public int[] bestSolution;   //contain best solution array
	public double bestFitness;   //cost of best solution
	
	public Solver(Instance problem)
	{
		
	}
	
	public void solve()
	{
		
	}
	
	public String toString()
	{
		if(problem.state == 0)
			return "not solved yet";
		return "";
	}
	
	public void printResult()
	{
		if(problem.state == 0)
			System.out.println("not solved yet");
	}
}
