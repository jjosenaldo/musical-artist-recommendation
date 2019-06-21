package main;

import java.util.Map;

import utils.IOUtils;

public class Recommender {
	private Map<Integer, Map<Integer, Double>> interests;
	
	public Recommender() {
		interests = IOUtils.getInterests();
	}
	
	public void recommend(Map<Integer, Double> newUserInterests) {
		int[] closest = closest(newUserInterests); 
		for(int i : closest) System.out.println(i);
	}
	
	private int[] closest(Map<Integer, Double> newUserInterests) {
		int param = 3;
		int[] closestUsers = new int[param];
		double[] closestValues = new double[param];
		double prod;
		Double like, likeOther;
		
		for(int i = 0; i < param; ++i) closestValues[i] = -1;
		for(int i = 0; i < param; ++i) closestUsers[i] = -1;
		
		for(Map.Entry<Integer, Map<Integer, Double>> e : interests.entrySet()) {
			prod = 0;
			
			for(Map.Entry<Integer, Double> interest : e.getValue().entrySet()) {
				like = newUserInterests.get(interest.getKey());
				likeOther = interest.getValue();
				
				if(like != null && likeOther != null)
					prod += like*likeOther;
			}
			
			prod = Math.min(1.0, prod);
			
			for(int i = 0; i < param; ++i) {
				if(prod > closestValues[i]) {
					for(int j = param-1; j > i; --j) {
						closestValues[j] =closestValues[j-1];
						closestUsers[j] =closestUsers[j-1];
					}
					closestValues[i]=prod;
					closestUsers[i]=e.getKey();
					break;
				}
			}
		}
		
		return closestUsers;
	}
}
