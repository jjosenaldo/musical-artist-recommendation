package main;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
	public static void main(String[] args) {
		Map<Integer, Double> newInterests = new ConcurrentHashMap<>();
		newInterests.put(1, 0.5);
		newInterests.put(2, 0.3);
		newInterests.put(3, 0.2);
		Recommender rec = new Recommender(100);
		for(int i : rec.recommend(newInterests)) {
			System.out.println(i);
		}
	}
}
