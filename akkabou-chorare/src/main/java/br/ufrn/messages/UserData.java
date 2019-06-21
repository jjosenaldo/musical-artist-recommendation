package br.ufrn.messages;

import java.util.Map;

public class UserData {
	// Map<Artist, ListeningCoefficient>
	private Map<Integer, Double> data;
	private int user;
	
	public UserData(UserData another) {
		this(another.user, another.data);
	}
	
	public UserData(int user, Map<Integer, Double> data) {
		this.user = user;
		this.data = data;
	}
	
	public int getUser() {
		return user;
	}
	
	public Map<Integer, Double> getUserData() {
		return data;
	}
}
