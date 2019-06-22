package br.ufrn.messages;

public class ClosestUsersData {
	private int user;
	private int[] closestUsers;
	
	public ClosestUsersData(int user, int[] closestUsers) {
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
