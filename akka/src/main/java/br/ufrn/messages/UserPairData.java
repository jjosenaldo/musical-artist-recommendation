package br.ufrn.messages;

import java.util.Map;

public class UserPairData {
	private int userOne;
	private int userTwo;
	private Map<Integer, Double> userTwoData;
	private Map<Integer, Double> userOneData;

	public UserPairData(int userOne, int userTwo, Map<Integer, Double> userOneData, Map<Integer, Double> userTwoData) {
		this.userOne = userOne;
		this.userTwo = userTwo;
		this.userOneData = userOneData;
		this.userTwoData = userTwoData;
	}
	
	public int getUserOne() {
		return userOne;
	}
	
	public int getUserTwo() {
		return userTwo;
	}
	
	public Map<Integer, Double> getUserOneData(){
		return userOneData;
	}
	
	public Map<Integer, Double> getUserTwoData(){
		return userTwoData;
	}
}
