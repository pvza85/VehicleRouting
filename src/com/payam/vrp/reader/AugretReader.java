package com.payam.vrp.reader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class AugretReader implements Reader 
{
	private String name;
	private String comment;
	private int capacity;
	private int dimension;
	private int[][] nodes;
	private int[] demands;
	private String fileName;
	private int[] depots;
	
	

	public AugretReader(String fileName) 
	{
		super();
		this.fileName = fileName;
		
		read();
	}



	public void read()
	{
		
		try 
		{
			Scanner in = new Scanner(new FileReader(fileName));
			
			//read descriptions
			while(in.hasNext())
			{
				String line = in.nextLine();
				
				if(line.contains("NODE_COORD_SECTION"))
					break;
				
				if(line.contains("NAME"))
					name = line.substring(line.indexOf(':'));
				else if(line.contains("COMMENT"))
					comment = line.substring(line.indexOf(':'));
				else if(line.contains("DIMENSION"))
					dimension = Integer.parseInt(line.substring(line.indexOf(':')));
				else if(line.contains("CAPACITY"))
					capacity = Integer.parseInt(line.substring(line.indexOf(':')));
				else
					System.out.println("Error in Reading File!!!");
			}
			
			
			//read nodes
			nodes = new int[dimension][2];
			int i = 0;
			while(in.hasNext())
			{
				String line = in.nextLine();
				
				if(line.contains("DEMAND_SECTION"))
					break;
				
				String[] t = line.split(" ");
				nodes[i][0] = Integer.parseInt(t[1]);
				nodes[i++][1] = Integer.parseInt(t[2]);
			}
			
			//read demands
			demands = new int[dimension];
			i = 0;
			while(in.hasNext())
			{
				String line = in.nextLine();
				
				if(line.contains("DEPOT_SECTION"))
					break;
				
				String[] t = line.split(" ");
				demands[i++] = Integer.parseInt(t[1]);
			}
			
			//read depots
			while(in.hasNext())
			{
				String line = in.nextLine();
				
				if(line.contains("-1"))
					break;
				
				depots[i] = Integer.parseInt(line)-1;
			}
			System.out.println("everything done well ");
			
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}

}
