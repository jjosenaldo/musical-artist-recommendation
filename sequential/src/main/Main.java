package main;

import java.util.Map;

import utils.IOUtils;

public class Main {
	public static void main(String[] args) {
		int totalRepos = 0;
		Map<Integer, Map<Integer, Double>> res = IOUtils.getInterests();
		for(Map.Entry<Integer,Map<Integer, Double>> i : res.entrySet()) {
			totalRepos += i.getValue().size();
		}
		System.out.println(res);
		System.out.println(totalRepos);
	}
}
