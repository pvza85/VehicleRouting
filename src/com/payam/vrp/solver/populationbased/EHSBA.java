package com.payam.vrp.solver.populationbased;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.logging.Logger;

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
	//final static Logger logger = Logger.getLogger(EHSBA.class);
	
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
		//for(int i = 0; i < input.)
		
		int[] selected = selection();
		
		int[][] matrix = createMatrix(selected);
		

		//debug
		PrintWriter outputFile;
		try {
			outputFile = new PrintWriter("matrix");
			
			for(int i = 0; i < matrix.length; i++)
			{
				int sum = 0;
				
				for(int j = 0; j < matrix[0].length; j++)
				{
					
					int ch = (int) ((random.nextGaussian()) * 30 * changeRatio); 
	        		int temp = matrix[i][j];
	        		if(temp > ch)
	        			ch =  -ch;
	        		else
	        			ch =  ch;
					matrix[i][j] += ch;
					sum += matrix[i][j];
					
				}
				for(int j = 0; j < matrix[0].length; j++)
				{
					outputFile.printf("%6.2f", (float)matrix[i][j]/sum );
				}
				
				outputFile.println();
			}
			
			outputFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		//end debug
		
		
        return createNewGeneration(matrix);
		
	}
	
	
	 /*------------------------- New Genereation -------------------------*/
    Population createNewGeneration(int[][] HM)
    {
        int len = input.members[0].chromosome.length+2; //chromosome length
        double[][] tempMatrix;
        double[] sums;
        
        Population newPop = new Population(problem, 40);   //XXX set population size later
        
        //elitism ********
        newPop.members[0] = input.members[input.bestMemberIndex];
        //create each individual in the population 
        for(int i = 1; i < input.members.length; i++)
        {
            sums = new double[len];
            tempMatrix = new double[len][len];
            
            //--------------------------------------------------------------------------------------------------------
            Individual indiv = new Individual(input.members[i].chromosome, problem); //new Individual(len, input.problem);    
            //--------------------------------------------------------------------------------------------------------
            
            //create a temporary copy of Histogram Matrix for further changes
            //for(int j = 0; j < len; j++)
                //System.arraycopy(HM[j], 0, tempMatrix[j], 0, len);
            
            double epsilon =  (Bratio * 2 * input.populationSize) / problem.dimension;   //XXX some problems here dimensions is not true
            for(int j = 0; j < len; j++)
                for(int k = 0; k < len; k++)
                    tempMatrix[j][k] = (double)HM[j][k]  + epsilon ;   //1 here is acting as B_ratio
            
            //create start and length of changing area
            int startPoint = randInt(len);
            int changeLength = (int) ((random.nextGaussian() + 1) * len * changeRatio);  //connect it to Bratio
            if(changeLength >= len)
                changeLength = len - 1;
            if(changeLength < 2)   //XXX check it
                changeLength = 2;
            
            //normalize none changing points to zero and set new chromosome to prev. one values
            for(int k = ((startPoint + changeLength) % len); k != startPoint; k=((k+1)%len))
            {
            	int t1 = k;
            	int t2 = i;
            	if(t1 > 31)
            		t1=t1-2;
            	if(t2 > 31)
            		t2 = t2 -2;
            	
                indiv.chromosome[t1] = input.members[t2].chromosome[t1];
                for(int j = 0; j < len; j++)
                {
                	try
                	{
                		tempMatrix[j][indiv.chromosome[t1]] = 0;  
                	}
                	catch(Exception e)
                	{
                		/*System.out.println("j: " + j);
                		System.out.println("indiv.chromosome[k]: " +  indiv.chromosome[t1]);
                		System.out.println("tempMatrix.length: " + tempMatrix.length);
                		System.out.println("tempMatrix[0].length: " + tempMatrix[0].length);*/
                	}
                }
            }
            for(int j = 0; j < len; j++)
                for(int k = 0; k < len; k++)
                    sums[j] += tempMatrix[j][k];
            
            //System.out.println(" ");
            for(int j = startPoint; j != ((startPoint + changeLength)%len); j = ((j+1)%len))
            {

                int prev_index = j-1;//j-1 means the previous permutation member
                if(prev_index < 0)  prev_index += len; 
                int t = prev_index;
                if(t > 31)
                	t = t -2;
                int prev = indiv.chromosome[t];
                
                
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
                int t3 = --k;
                if(t3 > 31)
                	t3= t3-2;
                int t4 = j;
                if(t4 > 31)
                	t4 = t4 - 2;
                indiv.chromosome[t4] = t3;
                
                for(int l = 0; l < len; l++)
                {
                    sums[l] -= tempMatrix[l][t3];
                    if(sums[l] < 0)
                    {
                        //System.out.println("!!! oo");
                        sums[l] = 0;
                    }
                    tempMatrix[l][t3] = 0;
                }
                
                
            }
            //XXX put a check to select new generation
            indiv.evaluate();
            if(indiv.fitness < input.members[i].fitness)
                newPop.members[i] = indiv;
            else if(random.nextDouble() > acceptanceRate)
                newPop.members[i] = indiv;
            else
                newPop.members[i] = input.members[i];
        }
        
        return newPop;
    }
	
	
	/*------------------------ Histogram Matrix -------------------------*/
    protected int[][] createMatrix(int[] selected)
    {
        int len = input.members[0].chromosome.length+2;
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
            	try
            	{
            		HM[input.members[selected[i]].chromosome[j]][input.members[selected[i]].chromosome[(j+1) % input.members[0].chromosome.length]]++;
            	}
            	catch(Exception e)
            	{
            		System.out.println("input.members[i].chromosome[j]: " + input.members[i].chromosome[j]);
            		System.out.println("input.members[selected[i]].chromosome[(j+1) % input.members[0].chromosome.length: " + input.members[selected[i]].chromosome[(j+1) % input.members[0].chromosome.length ]);
            	}
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
