package br.ufrn.messages;

import java.util.List;
import java.util.Map;

public class PrevUserData {
	private Map<Integer, Map<Integer, Double>> interests;
	private Map<Integer, Double> mods;
	private List<Integer> userList;
	
	public PrevUserData(Map<Integer, Map<Integer, Double>> interests, Map<Integer, Double> mods, List<Integer> userList) {
		this.interests = interests;
		this.mods = mods;
		this.userList = userList;
	}
	
	public Map<Integer, Map<Integer, Double>> getInterests(){
		return interests;
	}
	
	public Map<Integer, Double> getMods(){
		return mods;
	}
	
	public List<Integer> getUserList(){
		return userList;
	}
}
