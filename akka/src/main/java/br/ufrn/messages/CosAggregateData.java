package br.ufrn.messages;

import java.util.Map;

public class CosAggregateData {
	private int user;
	private Map<Integer, Double> cosines;
	private int[] closestUsers;
	
	public CosAggregateData(int user, Map<Integer, Double> cosines, int[] closestUsers) {
		this.user = user;
		this.cosines = cosines;
		this.closestUsers = closestUsers;
	}
	
	public int getUser() {
		return user;
	}
	
	public Map<Integer, Double> getCosines(){
		return cosines;
	}
	
	public int[] getClosestUsers() {
		return closestUsers;
	}
}
