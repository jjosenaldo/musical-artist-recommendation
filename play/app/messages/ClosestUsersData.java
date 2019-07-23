package messages;

import java.util.List;

public class ClosestUsersData {
	private int user;
	private List<Integer> closestUsers;
	
	public ClosestUsersData(int user, List<Integer> closestUsers) {
		this.user = user;
		this.closestUsers = closestUsers;
	}
	
	public int getUser() {
		return user;
	}
	
	public List<Integer> getClosestUsers() {
		return closestUsers;
	}
}
