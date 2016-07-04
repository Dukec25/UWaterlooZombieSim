package uwaterloo.student.zombie.crazy.domain;

import java.util.List;
import java.util.Random;

public class GameUtil
{
    private static Random randomGenerator = new Random();

    /**
     * a random function that returns true or false based on the probability
     * given (in percents).
     *
     * @param n
     * @return
     */
    public static boolean chancePercentage(int n)
    {
        return chancePercentage((float) n);
    }

    public static boolean chancePercentage(float n)
    {
        if (n < 0.0 || n > 100.0) {
            return false;
        }
        float randomFloatPercentage = randomGenerator.nextFloat() * 100;
        if (n < randomFloatPercentage) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Receives a list of non-negative weights representing probability and
     * returns the index of the randomly chosen weight.
     * @param weights list of weights representing probability
     * @return the index of the randomly chosen weight; or -1 if
     */
    public static int randomlyChooseByWeight(List<Integer> weights)
    {
        if (weights.isEmpty()) { return -1; }

        // sanity-checking
        if (!weights.stream().allMatch(integer -> integer >= 0))
        {
            throw new IllegalArgumentException("randomlyChooseByWeight() received negative weight!");
        }

        int weightSum = weights.stream().mapToInt(Integer::intValue).sum();
        final int decisionPosition = GameUtil.nextInt(weightSum) + 1;

        int decisionPositionRemaining = decisionPosition;
        int i = 0;
        while (decisionPositionRemaining > weights.get(i))
        {
            decisionPositionRemaining -= weights.get(i);
            i++;
        }
        return i;
    }

    /**
     * a wrapper around the Java.util.Random.nextInt function
     *
     * @param bound
     * @return
     */
    public static int nextInt(int bound)
    {
        return randomGenerator.nextInt(bound);
    }
}
