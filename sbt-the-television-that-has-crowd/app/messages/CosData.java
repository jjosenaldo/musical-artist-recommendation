package messages;

public class CosData {
	private int userOne;
	private int userTwo;
	private double cos;
	
	public CosData(int userOne, int userTwo, double cos) {
		this.userOne = userOne;
		this.userTwo = userTwo;
		this.cos = cos;
	}

	public int getUserOne() {
		return userOne;
	}

	public int getUserTwo() {
		return userTwo;
	}

	public double getCos() {
		return cos;
	}
	
	
}
