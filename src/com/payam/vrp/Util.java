package com.payam.vrp;

import java.util.Random;


/**
 * This class contain those functions that is used frequently in our program.
 * It may help change some common methods easily and compare them and will lead to more
 * robust and optimize code.
 * @author payam.azad
 */

public class Util 
{
	public static double Bratio;
	public static double changeRatio;
	public static int elitismSize;
	public static double acceptanceRate;
	
	
	public static Random random = new Random(System.currentTimeMillis());
	
	
	/**
	 * will print result in different outputs, include console or a file.
	 * @param str
	 */
	public static void print(String str)
	{
		System.out.println(str);
	}
	
	/**
	 * create a random double number in range (0, 1)
	 * @return random number
	 */
	public static double randDouble()
	{
		return random.nextDouble();
	}
	
	/**
	 * create a random int number in range (a, b)
	 * @return random integer
	 */
	public static int randInt(int a, int b)
	{
		return 0;
	}
	
	/**
	 * create a random int number in range (0, b)
	 * @return random integer
	 */
	public static int randInt(int b)
	{
		return random.nextInt(b);
	}
}
