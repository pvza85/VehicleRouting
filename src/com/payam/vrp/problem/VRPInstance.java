package com.payam.vrp.problem;

import com.payam.vrp.reader.Reader.FileType;

public class VRPInstance extends Instance 
{
	//in this class we just consider that we have one depot, the node at index 0
	
	
	public VRPInstance(String inputFile, FileType type) {
		super(inputFile, type);
	}

	public VRPInstance(String inputFile) {
		super(inputFile);
	}	
	
	
}
