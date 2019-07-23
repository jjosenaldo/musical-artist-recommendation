package messages;

import java.util.Map;

public class InterestsOfOldUsersPlusNewData {
	private Map<Integer, Double> newUserInterests;
	private Map<Integer, Map<Integer, Double>> interests;
	private int closeUser;
	
	public InterestsOfOldUsersPlusNewData(Map<Integer, Map<Integer, Double>> interests,
			Map<Integer, Double> newUserInterests, int closeUser) {
		this.newUserInterests = newUserInterests;
		this.interests = interests;
		this.closeUser = closeUser;
	}
	
	public int getCloseUser() {
		return closeUser;
	}
	
	public Map<Integer, Double> getNewUserInterests(){
		return newUserInterests;
	}
	
	public Map<Integer, Map<Integer, Double>> getInterests(){
		return interests;
	}
}
