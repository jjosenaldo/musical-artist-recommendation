package br.ufrn.messages;

public class ModData {
	private int user;
	private double data;
	
	public ModData(int user, double data) {
		this.user = user;
		this.data = data;
	}
	
	public int getUser() {
		return this.user;
	}
	
	public double getData() {
		return data;
	}
}
