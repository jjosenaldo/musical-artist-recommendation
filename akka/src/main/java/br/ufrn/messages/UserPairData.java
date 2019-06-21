package br.ufrn.messages;

import java.util.Map;

public class UserPairData {
	private int userOne;
	private int userTwo;
	private Map<Integer, Double> userTwoData;
	private Map<Integer, Double> userOneData;
	private double userOneMod;
	private double userTwoMod;
	private int maxRepos;

	public UserPairData(int userOne, int userTwo, Map<Integer, Double> userOneData, Map<Integer, Double> userTwoData, double userOneMod, double userTwoMod, int maxRepos) {
		this.userOne = userOne;
		this.userTwo = userTwo;
		this.userOneData = userOneData;
		this.userTwoData = userTwoData;
		this.userOneMod = userOneMod;
		this.userTwoMod = userTwoMod;
		this.maxRepos = maxRepos;
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
	
	public double getUserOneMod() {
		return userOneMod;
	}
	
	public double getUserTwoMod() {
		return userTwoMod;
	}
	
	public int getMaxRepos() {
		return maxRepos;
	}
}
