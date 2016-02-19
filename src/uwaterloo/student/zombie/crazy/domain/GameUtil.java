package uwaterloo.student.zombie.crazy.domain;

import java.util.Random;

public class GameUtil {
	private static Random randomGenerator = new Random();
	
	public boolean chancePercentage(int n){
		return chancePercentage((float)n);
	}
	
	public boolean chancePercentage(float n){
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
}
