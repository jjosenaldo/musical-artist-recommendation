package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import utils.IOUtils;

public class Recommender {
	private Map<Integer, Map<Integer, Double>> interests;
	
	public Recommender() {
		interests = IOUtils.getInterests();
	}
	
	public List<Integer> recommend(Map<Integer, Double> newUserInterests) {
		int[] closest = closestUsers(newUserInterests, 4);
		return recommendedArtists(closest, 4, newUserInterests);
		
	}
	
	private List<Integer> recommendedArtists(int[] closest, int numRecomendations, Map<Integer, Double> newUserInterests) {
		Map<Integer, Double> allRecs = new ConcurrentHashMap<>();
		int artist;
		double userInterest;
		Map<Integer, Double> interestsOfCloseUser;
		
		for(int close : closest) {
			interestsOfCloseUser = interests.get(close);
			
			for(Map.Entry<Integer, Double> interest : interestsOfCloseUser.entrySet()) {
				artist = interest.getKey();
				userInterest = interest.getValue();
				
				if(newUserInterests.get(artist) == null) {
					Double oldInterestRec =  allRecs.getOrDefault(artist, 0d);
					allRecs.put(artist, oldInterestRec + userInterest);
				}
			}
		}
		
		return kBest(allRecs, numRecomendations);
	}
	
	private List<Integer> kBest(Map<Integer, Double> map, int k) {
		Set<Map.Entry<Integer, Double>> entries = map.entrySet();
		int setSize = entries.size();
		List<Map.Entry<Integer, Double>> listOfEntries = new ArrayList<>(setSize);
		listOfEntries.addAll(entries);
		List<Integer> res = new ArrayList<>(k);
		
		for(int i = 0; i < k; ++i) {
			for(int j = 0; j < setSize-1; ++j) {
				if(listOfEntries.get(j).getValue() > listOfEntries.get(j+1).getValue()) {
					Collections.swap(listOfEntries, j, j+1);
				}
			}
			res.add( listOfEntries.get(setSize - i - 1).getKey()  );
		}
		
		return res;
	}
	
	private int[] closestUsers(Map<Integer, Double> newUserInterests, int param) {
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
