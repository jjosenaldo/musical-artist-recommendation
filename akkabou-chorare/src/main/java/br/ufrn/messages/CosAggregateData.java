package br.ufrn.messages;

public class CosAggregateData {
	private int user;
	private int[] closestUsers;
	
	public CosAggregateData(int user, int[] closestUsers) {
		this.user = user;
		this.closestUsers = closestUsers;
	}
	
	public int getUser() {
		return user;
	}
	
	public int[] getClosestUsers() {
		return closestUsers;
	}
}
