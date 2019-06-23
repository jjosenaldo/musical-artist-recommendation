package messages;

import java.util.Map;

public class InitMessage {
	// Map<Artist, ListeningCoefficient>
	private Map<Integer, Double> data;
	private int maxClosestUsers;
	private int maxRecommendations;
	private int user;
	
	public InitMessage(InitMessage another) {
		this(another.user, another.data, another.maxClosestUsers, another.maxRecommendations);
	}
	
	public InitMessage(int user, Map<Integer, Double> data, int maxClosestUsers, int maxRecommendations) {
		this.user = user;
		this.data = data;
		this.maxClosestUsers = maxClosestUsers;
		this.maxRecommendations = maxRecommendations;
	}
	
	public int getUser() {
		return user;
	}
	
	public Map<Integer, Double> getUserData() {
		return data;
	}
	
	public int getMaxClosestUsers() {
		return maxClosestUsers;
	}
	
	public int getMaxRecommendations() {
		return maxRecommendations;
	}
}
