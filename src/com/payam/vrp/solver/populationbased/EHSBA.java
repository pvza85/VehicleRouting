package com.payam.vrp.solver.populationbased;

import com.payam.vrp.problem.Instance;

import static com.payam.vrp.Util.*;

/**
 * Implementation of enhanced Edge Histogram Based Algorithm based on(with my enhancements):
 * 
 * Tsutsui, Shigeyoshi. "Probabilistic model-building genetic algorithms in permutation 
 * representation domain using edge histogram." Parallel Problem Solving from Nature—PPSN VII. 
 * Springer Berlin Heidelberg, 2002. 224-233.
 * 
 * 
 * @author payam.azad
 */
public class EHSBA 
{
	public Instance problem;
	public Population input, output;
	
	int[][] matrix;
	
	public EHSBA(Instance problem)
	{
		this.problem = problem;
	}
	
	public Population iterate(Population in)
	{
		input = in;
		
		
		//Population out = null;
		
		int[] selected = selection();
		
		int[][] matrix = createMatrix(selected);
        return createNewGeneration(matrix);
		
	}
	
	
	 /*------------------------- New Genereation -------------------------*/
    Population createNewGeneration(int[][] HM)
    {
        int len = input.members[0].chromosome.length; //chromosome length
        double[][] tempMatrix;
        double[] sums;
        
        Population newPop = new Population(input.problem);
        
        //elitism ********
        newPop.members[0] = input.theBestIndiv();
        //create each individual in the population
        for(int i = input.problem.parameters.eliteNumber; i < input.population.length; i++)
        {
            sums = new double[len];
            tempMatrix = new double[len][len];
            Individual indiv = new Individual(len, input.problem);    //XXX check for a way to not send problem to all 
            //create a temporary copy of Histogram Matrix for further changes
            //for(int j = 0; j < len; j++)
                //System.arraycopy(HM[j], 0, tempMatrix[j], 0, len);
            
            double epsilon =  (input.problem.parameters.Bratio * 2 * input.problem.parameters.populationSize) / input.problem.parameters.chromosomeLength;
            for(int j = 0; j < len; j++)
                for(int k = 0; k < len; k++)
                    tempMatrix[j][k] = (double)HM[j][k]  + epsilon ;   //1 here is acting as B_ratio
            
            //create start and length of changing area
            int startPoint = randInt(len);
            int changeLength = (int) ((random.nextGaussian() + 1) * len * input.problem.parameters.changeRatio);  //connect it to Bratio
            if(changeLength >= len)
                changeLength = len - 1;
            if(changeLength < 2)   //XXX check it
                changeLength = 2;
            
            //normalize none changing points to zero and set new chromosome to prev. one values
            for(int k = ((startPoint + changeLength) % len); k != startPoint; k=((k+1)%len))
            {
                indiv.chromosome[k] = input.members[i].chromosome[k];
                for(int j = 0; j < len; j++)
                    tempMatrix[j][indiv.chromosome[k]] = 0;  
            }
            for(int j = 0; j < len; j++)
                for(int k = 0; k < len; k++)
                    sums[j] += tempMatrix[j][k];
            
            //System.out.println(" ");
            for(int j = startPoint; j != ((startPoint + changeLength)%len); j = ((j+1)%len))
            {

                int prev_index = j-1;//j-1 means the previous permutation member
                if(prev_index < 0)  prev_index += len; 
                int prev = indiv.chromosome[prev_index];
                
                
                if(sums[prev] <= 0)
                {
                    sums[prev] = 1;  //XXX Big Big check
                    //System.out.println("!!!  eee");  //will solve after adding Bratio 
                }
                
                double n = random.nextDouble() * sums[prev]; // random.nextInt(sums[prev]); 
                
                double a = 0; 
                int k = 0;
                while(a <= n && k < len)//XXX put more checks
                    a += tempMatrix[prev][k++];
                k--;
                
                indiv.chromosome[j] = k;
                
                for(int l = 0; l < len; l++)
                {
                    sums[l] -= tempMatrix[l][k];
                    if(sums[l] < 0)
                    {
                        //System.out.println("!!! oo");
                        sums[l] = 0;
                    }
                    tempMatrix[l][k] = 0;
                }
                
                
            }
            //XXX put a check to select new generation
            indiv.evaluate();
            if(indiv.fitness < input.population[i].fitness)
                newPop.population[i] = indiv;
            else if(random.nextDouble() > input.problem.parameters.acceptanceRate)
                newPop.population[i] = indiv;
            else
                newPop.population[i] = input.population[i];
        }
        
        return newPop;
    }
	
	
	/*------------------------ Histogram Matrix -------------------------*/
    protected int[][] createMatrix(int[] selected)
    {
        int len = input.members[0].chromosome.length;
        int[][] HM = new int[len][len];
        
        // a little bit complicated :)
        // count in whole population chromosomes (input.population[i].chromosome[j]) if two numbers e.g 1 and 2 comes after each other
        // add one number to HM[1][2] and also in symetric version to HM[2][1] also % (mod) is to make it work circular means count 1 in end
        // and 2 in the first of each chromosome after each other ;) (it can be change by problem)
        // looking for just selected members to form HM makes it more complicated
        // while we have to just look at selected members instead of population[i] we will use population[selected[i]]
        for(int i = 0; i < selected.length; i++)
        {
            for(int j = 0; j < input.members[0].chromosome.length; j++)   //XXX chromosomeLength is not set
            {
                HM[input.members[selected[i]].chromosome[j]][input.members[selected[i]].chromosome[(j+1) % input.members[0].chromosome.length]]++;
                //symetric matrix
                //HM[input.population[selected[i]].chromosome[(j+1) % input.population[0].chromosome.length]][input.population[selected[i]].chromosome[j]]++;
            }
        }
        
        return HM;
    }
    
    
    
	
	/*---------------------------- Selection -----------------------------*/
    private int[] selection()
    {
        return tournamentSelection2();
        //return noSelection();
    }
    //very temp tournamentSelection with tournemant size of 2
    private int[] tournamentSelection2()
    {
    	int n = input.members[0].chromosome.length;
        int[] selected = new int[n/2];
         
        for(int i = 0; i < selected.length; i++)
        {
            int[] t = new int[2];
            for(int j = 0; j < 2; j++)
                t[j] = randInt(n);
            
            //TODO write it more neat
            //XXX old code conversion
            if(true) //input.problem.parameters.type == input.problem.parameters.type.maximize)
            {
                if(input.members[t[0]].fitness > input.members[t[1]].fitness)   
                    selected[i] = t[0];
                else
                    selected[i] = t[1];     
            }
            //used for minimization
            /*else
            {
                if(input.members[t[0]].fitness < input.members[t[1]].fitness)   
                    selected[i] = t[0];
                else
                    selected[i] = t[1]; 
            }*/
        }
        
        return selected;
    }
    
    private int[] noSelection()
    {
        int[] selected = new int[input.members[0].chromosome.length];
        for(int i= 0; i < selected.length; i++)
            selected[i] = i;
        
        return selected;
    }
}
