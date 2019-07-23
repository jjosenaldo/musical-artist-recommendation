package messages;

import java.util.Map;

public class InitMessage {
	// Map<Artist, ListeningCoefficient>
	private Map<Integer, Double> data;
	private int maxRecommendations;
	
	public InitMessage(InitMessage another) {
		this(another.data, another.maxRecommendations);
	}
	
	public InitMessage(Map<Integer, Double> data, int maxRecommendations) {
		this.data = data;
		this.maxRecommendations = maxRecommendations;
	}
	
	public Map<Integer, Double> getUserData() {
		return data;
	}
	
	public int getMaxRecommendations() {
		return maxRecommendations;
	}
}
