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
 * representation domain using edge histogram." Parallel Problem Solving from Nature�PPSN VII. 
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
	public int elitismSize = 1;
	
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
		
		logMatrix(matrix, "matrix.csv");
			
        return createNewGeneration(matrix);
		
	}
	
	
	 /*------------------------- New Genereation -------------------------*/
    Population createNewGeneration(int[][] HM)
    {
        int len = input.members[0].chromosome.length; //chromosome length
        double[][] tempMatrix;
        double[] sums;
        
        Population newPop = new Population(problem, 40);   //XXX set population size later
        
        //elitism ********
        int i;
        for(i = 0; i < elitismSize; i++)
        	newPop.members[i] = input.members[input.bestMemberIndex];
        //create each individual in the population 
        for(; i < input.members.length; i++)
        {
            sums = new double[HM.length]; sums[0] = 0;
            tempMatrix = new double[HM.length][HM.length];
            
            //--------------------------------------------------------------------------------------------------------
            Individual indiv = new Individual(input.members[i].chromosome, problem); //new Individual(len, input.problem);    
            //--------------------------------------------------------------------------------------------------------
            
            //create a temporary copy of Histogram Matrix for further changes
            //for(int j = 0; j < len; j++)
                //System.arraycopy(HM[j], 0, tempMatrix[j], 0, len);
            
            double epsilon =  (Bratio * 2 * input.populationSize) / problem.dimension;   //XXX some problems here dimensions is not true
            for(int j = 1; j < HM.length; j++) //XXX not 1
                for(int k = 1; k < HM.length; k++) //XXX not 1
                    tempMatrix[j][k] = (double)HM[j][k]  + epsilon ;   //1 here is acting as B_ratio
            
            //just delete it for better efficiency
            for(int j = 0; j < HM.length; j++)
            	tempMatrix[j][j] = 0;
            
            //XXX this occur because I did not consider 0
            ///solve it ASAP
            logMatrix(tempMatrix, "temp_matrix1.csv");
            
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
            	
                indiv.chromosome[k] = input.members[i].chromosome[k];
                for(int j = 0; j < len; j++)
                {
                	try
                	{
                		tempMatrix[j][indiv.chromosome[k]] = 0;  
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
            
            logMatrix(tempMatrix, "temp_matrix2.csv");
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
                while(a < n && k < len)//XXX put more checks
                {
                	if(k >= len)
                		System.out.print(".");
                	a += tempMatrix[prev][k++];
                }
                    

                indiv.chromosome[j] = --k;   
                
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
            logMatrix(tempMatrix, "temp_matrix3.csv");
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
        int len = input.members[0].chromosome.length+1;
        int[][] HM = new int[len][len];
        
        // a little bit complicated :)
        // count in whole population chromosomes (input.population[i].chromosome[j]) if two numbers e.g 1 and 2 comes after each other
        // add one number to HM[1][2] and also in symetric version to HM[2][1] also % (mod) is to make it work circular means count 1 in end
        // and 2 in the first of each chromosome after each other ;) (it can be change by problem)
        // looking for just selected members to form HM makes it more complicated
        // while we have to just look at selected members instead of population[i] we will use population[selected[i]]
        for(int i = 0; i < selected.length; i++)
        {
            for(int j = 0; j < len-1; j++)   //XXX chromosomeLength is not set
            {
            	int a, b;
            	try
            	{
            		a = input.members[selected[i]].chromosome[j];
            		if(j+1 >= input.members[0].chromosome.length)
            			j = 0;
            		b = input.members[selected[i]].chromosome[j+1];
            		HM[a][b]++;
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
    
    private void logMatrix(int[][] matrix, String fileName)
    {
    	//debug
    	PrintWriter outputFile;
    	try {
    		outputFile = new PrintWriter(fileName);

    		for(int i = 0; i < matrix.length; i++)
    		{
    			int sum = 0;

    			for(int j = 0; j < matrix[0].length; j++)
    			{

    				int ch = (int) ((random.nextGaussian()) * 30 * changeRatio);
    				int temp = matrix[i][j];
    				if(temp > ch)
    					ch = -ch;
    				else
    					ch = ch;
    				//matrix[i][j] += ch;
    				sum += matrix[i][j];

    			}
    			for(int j = 0; j < matrix[0].length; j++)
    			{
    				outputFile.printf("%d,", matrix[i][j]);
    			}

    			outputFile.println();
    		}

    		outputFile.close();
    	} catch (FileNotFoundException e) {
    		e.printStackTrace();
    	}

    	//end debug
    }
    private void logMatrix(double[][] matrix, String fileName)
    {
    	//debug
    	PrintWriter outputFile;
    	try {
    		outputFile = new PrintWriter(fileName);

    		for(int i = 0; i < matrix.length; i++)
    		{

 
    			for(int j = 0; j < matrix[0].length; j++)
    			{
    				outputFile.printf("%f,", matrix[i][j]);
    			}

    			outputFile.println();
    		}

    		outputFile.close();
    	} catch (FileNotFoundException e) {
    		e.printStackTrace();
    	}

    	//end debug
    }
}
