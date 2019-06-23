package messages;

import java.util.Map;

public class NewUserData {
	private Map<Integer, Double> interests;
	private int user;
	
	public NewUserData(Map<Integer, Double> interests, int user) {
		this.interests = interests;
		this.user = user;
	}
	
	public Map<Integer, Double> getInterests(){
		return interests;
	}
	
	public int getUser() {
		return user;
	}
}
