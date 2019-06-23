package messages;

import java.util.Map;

public class PrevUserData {
	private Map<Integer, Map<Integer, Double>> interests;
	
	public PrevUserData(Map<Integer, Map<Integer, Double>> interests) {
		this.interests = interests;
	}
	
	public Map<Integer, Map<Integer, Double>> getInterests(){
		return interests;
	}
}
