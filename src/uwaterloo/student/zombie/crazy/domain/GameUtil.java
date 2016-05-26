package uwaterloo.student.zombie.crazy.domain;

import java.util.Random;

public class GameUtil {
	private static Random randomGenerator = new Random();
	
	/**
	 * a random function that returns true or false based on the 
	 * probability given (in percents).
	 * @param n
	 * @return
	 */
	public static boolean chancePercentage(int n){
		return chancePercentage((float)n);
	}
	
	public static boolean chancePercentage(float n){
		if( n < 0.0  || n > 100.0){
			return false;
		}
		float randomFloatPercentage = randomGenerator.nextFloat() * 100;
		if (n < randomFloatPercentage){
			return false;
		}
		else{
			return true;
		}
	}
	
	/**
	 * a wrapper around the Java.util.Random.nextInt function
	 * @param bound
	 * @return
	 */
	public static int nextInt(int bound) {
		return randomGenerator.nextInt(bound);
	}
}
