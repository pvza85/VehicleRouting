package com.payam.vrp.reader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AugretReader extends Reader 
{
	private String name;
	private String comment;
	private int capacity;
	private int dimension;
	private int[][] nodes;
	private int[] demands;
	private int[] depots;
	
	

	public AugretReader(String fileName) 
	{
		super(fileName);
		
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
					name = line.substring(line.indexOf(':')+1);
				else if(line.contains("COMMENT"))
					comment = line.substring(line.indexOf(':')+1);
				else if(line.contains("DIMENSION"))
					dimension = Integer.parseInt(line.substring(line.indexOf(':')+1).trim());
				else if(line.contains("CAPACITY"))
					capacity = Integer.parseInt(line.substring(line.indexOf(':')+1).trim());
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
				nodes[i][0] = Integer.parseInt(t[2]);
				nodes[i++][1] = Integer.parseInt(t[3]);
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
			List<Integer> depotList = new ArrayList<Integer>();
			while(in.hasNext())
			{
				String line = in.nextLine();
				
				if(line.contains("-1"))
					break;
				depotList.add(Integer.parseInt(line.trim())-1);
			}
			depots = new int[depotList.size()];
			for(i = 0; i < depotList.size(); i++)
				depots[i] = (int) depotList.get(i);
			System.out.println("everything done well ");
			
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}

}
