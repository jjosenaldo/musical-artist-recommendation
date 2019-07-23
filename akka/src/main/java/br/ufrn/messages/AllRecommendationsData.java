package br.ufrn.messages;

import java.util.Map;

public class AllRecommendationsData {
	private Map<Integer, Double> recommendations;
	
	public AllRecommendationsData(Map<Integer, Double> recommendations) {
		this.recommendations = recommendations;
	}
	
	public Map<Integer, Double> getRecommendations(){
		return recommendations;
	}
}
