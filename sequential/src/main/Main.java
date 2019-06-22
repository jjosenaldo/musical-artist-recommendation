package main;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
	public static void main(String[] args) {
		Map<Integer, Double> newInterests = new ConcurrentHashMap<>();
		newInterests.put(500, 0.3);
		newInterests.put(520, 0.3);
		newInterests.put(540, 0.4);
		Recommender rec = new Recommender(5, 10);
		for(int i : rec.recommend(newInterests)) {
			System.out.println(i);
		}
	}
}
