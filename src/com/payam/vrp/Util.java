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
	/**
	 * static imported from {@link Util} class by myself.
	 */
	public static double Bratio = 0.3;
	/**
	 * static imported from {@link Util} class by myself.
	 */
	public static double changeRatio = 0.1;
	/**
	 * static imported from {@link Util} class by myself.
	 */
	public static int elitismSize = 1;
	/**
	 * static imported from {@link Util} class by myself.
	 */
	public static double acceptanceRate = 0.8;
	
	public static double mutuationRate = 1;
	
	public static int changeFrequency = 10;
	
	
	public static Random random = new Random(System.currentTimeMillis());
	
	
	/**
	 * static imported from {@link Util} class by myself.
	 * will print result in different outputs, include console or a file.
	 * @param str
	 */
	public static void print(String str)
	{
		System.out.println(str);
	}
	
	/**
	 * static imported from {@link Util} class by myself.
	 * create a random double number in range (0, 1)
	 * @return random number
	 */
	public static double randDouble()
	{
		return random.nextDouble();
	}
	
	/**
	 * static imported from {@link Util} class by myself.
	 * create a random int number in range (a, b)
	 * @return random integer
	 */
	public static int randInt(int a, int b)
	{
		return 0;
	}
	
	/**
	 * static imported from {@link Util} class by myself.
	 * create a random int number in range (0, b) 
	 * @return random integer
	 */
	public static int randInt(int b)
	{
		return random.nextInt(b);
	}
}
