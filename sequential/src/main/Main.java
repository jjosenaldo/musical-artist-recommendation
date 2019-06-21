package main;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
	public static void main(String[] args) {
		Map<Integer, Double> newInterests = new ConcurrentHashMap<>();
		newInterests.put(0, 0.3);
		newInterests.put(1, 0.3);
		newInterests.put(2, 0.9);
		Recommender rec = new Recommender();
		rec.recommend(newInterests);
	}
}
