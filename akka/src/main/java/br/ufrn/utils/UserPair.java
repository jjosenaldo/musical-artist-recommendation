package br.ufrn.utils;

import java.util.Objects;

public class UserPair {
	private int userOne;
	private int userTwo;
	
	public UserPair(int userOne, int userTwo) {
		this.userOne = userOne;
		this.userTwo = userTwo;
	}
	
	public int getUserOne() {
		return userOne;
	}
	
	public int getUserTwo() {
		return userTwo;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o == this)
			return true;
		
		if(!(o instanceof UserPair))
			return false;
		
		UserPair anotherPos = (UserPair) o;
		
		if(anotherPos.userOne != userOne)
			return false;
		
		return anotherPos.userTwo == userTwo;
}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(userOne, userTwo);
	}
}
