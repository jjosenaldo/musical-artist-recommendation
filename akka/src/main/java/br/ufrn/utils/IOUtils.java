package br.ufrn.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class IOUtils {
	public static Map<Integer, Map<Integer, Double>> getInterests(String path){
		Integer user, artist;
		Double interest;
		Map<Integer, Map<Integer, Double>> res = new ConcurrentHashMap<Integer, Map<Integer,Double>>();
		Map<Integer, Double> userMap = new ConcurrentHashMap<Integer, Double>();
		int prevUser = -1;
		
		try (BufferedReader reader = new BufferedReader(new FileReader(path)))
		{
			String[] splitted_line;
			String line;

			while ((line = reader.readLine()) != null)
			{
				splitted_line = line.split(" ");
				user = Integer.parseInt((splitted_line[0]));
				artist = Integer.parseInt((splitted_line[1]));
				interest = Double.parseDouble(splitted_line[2]);
				
				if(user != prevUser && prevUser != -1) {
					res.put(prevUser, userMap);
					userMap = new ConcurrentHashMap<Integer, Double>();
				}
				
				userMap.put(artist, interest);
				
				prevUser = user;
			}
			
			res.put(prevUser, userMap);
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return res;
	}
}

