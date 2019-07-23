package messages;

import java.util.Map;

public class AllRecommendationsWithFilterNumberData {
	private Map<Integer, Double> recommendations;
	private int k;
	
	public AllRecommendationsWithFilterNumberData(Map<Integer, Double> recommendations, int k) {
		this.k = k;
		this.recommendations = recommendations;
	}
	
	public int getK() {
		return k;
	}
	
	public Map<Integer, Double> getRecommendations(){
		return recommendations;
	}
}
