package random;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Random;

public class RandomManager {
	private static final int RANDOM_ARRAY_SIZE = 50;
	private static HashMap<RandomKey, Random> randomMap = new HashMap<RandomKey, Random>();
	private static int[] randomArray = new int[RANDOM_ARRAY_SIZE];

	public void initialize(int experimentNumber) {
		makeRandom(experimentNumber);
		
		randomMap.put(RandomKey.AGENT_RANDOM, new Random(randomArray[0]));
		randomMap.put(RandomKey.TASK_RANDOM, new Random(randomArray[1]));
		randomMap.put(RandomKey.DEADLINE_RANDOM, new Random(randomArray[2]));
		randomMap.put(RandomKey.AGENT_MAP_RANDOM, new Random(randomArray[3]));
		randomMap.put(RandomKey.DEADLINE_SORT_RANDOM, new Random(randomArray[4]));
		randomMap.put(RandomKey.REQUIRE_SORT_RANDOM, new Random(randomArray[5]));
		randomMap.put(RandomKey.POISSON_RANDOM, new Random(randomArray[6]));
		randomMap.put(RandomKey.POISSON_RANDOM_2, new Random(randomArray[7]));
		randomMap.put(RandomKey.SORT_RANDOM_1, new Random(randomArray[8]));
		randomMap.put(RandomKey.SORT_RANDOM_2, new Random(randomArray[9]));
		randomMap.put(RandomKey.SORT_RANDOM_3, new Random(randomArray[10]));
		randomMap.put(RandomKey.SORT_RANDOM_4, new Random(randomArray[11]));
		randomMap.put(RandomKey.EPSILON_GREEDY_RANDOM_1, new Random(randomArray[12]));
		randomMap.put(RandomKey.EPSILON_GREEDY_RANDOM_2, new Random(randomArray[13]));
		randomMap.put(RandomKey.EPSILON_GREEDY_RANDOM_3, new Random(randomArray[14]));
		randomMap.put(RandomKey.SHUFFLE_RANDOM_1, new Random(randomArray[15]));
		randomMap.put(RandomKey.SHUFFLE_RANDOM_2, new Random(randomArray[16]));
		randomMap.put(RandomKey.SELECT_RANDOM_1, new Random(randomArray[17]));
		randomMap.put(RandomKey.SELECT_RANDOM_2, new Random(randomArray[18]));
		randomMap.put(RandomKey.SELECT_RANDOM_3, new Random(randomArray[19]));
		randomMap.put(RandomKey.SELECT_RANDOM_4, new Random(randomArray[20]));
		randomMap.put(RandomKey.SELECT_RANDOM_5, new Random(randomArray[21]));
		randomMap.put(RandomKey.REQUIRE_RANDOM, new Random(randomArray[22]));
		randomMap.put(RandomKey.GREEDY_RANDOM, new Random(randomArray[23]));
	}
	
	private static void makeRandom(int experimentNumber) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream("prime/prime" + experimentNumber + ".txt")));
			for(int i = 0; i < randomArray.length; i++){
				String str = br.readLine();
				randomArray[i] = Integer.parseInt(str);
			}
			br.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Random getRandom(RandomKey key) {
		return randomMap.get(key);
	}
}
