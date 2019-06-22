package br.ufrn.messages;

import java.util.List;

public class BestRecommendationsData {
	private List<Integer> bestRecommendations;
	
	public BestRecommendationsData(List<Integer> bestRecommendations) {
		this.bestRecommendations = bestRecommendations;
	}
	
	public List<Integer> getBestRecommendations(){
		return bestRecommendations;
	}
}
