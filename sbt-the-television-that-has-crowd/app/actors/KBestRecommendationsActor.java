package actors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import akka.actor.AbstractActor;
import akka.actor.Props;
import messages.AllRecommendationsWithFilterNumberData;
import messages.BestRecommendationsData;

public class KBestRecommendationsActor extends AbstractActor {
	private List<Integer> bestRecommendations;
	
	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(AllRecommendationsWithFilterNumberData.class, msg -> {
					getSender().tell(new BestRecommendationsData(kBestRecommendations(msg)), getSelf());
				})
				.build();
	}
	
	private List<Integer> kBestRecommendations(AllRecommendationsWithFilterNumberData recs){
		Map<Integer, Double> map = recs.getRecommendations();
		int k = recs.getK();
		Set<Map.Entry<Integer, Double>> entries = map.entrySet();
		int setSize = entries.size();
		List<Map.Entry<Integer, Double>> listOfEntries = new ArrayList<>(setSize);
		listOfEntries.addAll(entries);
		bestRecommendations = new ArrayList<>(k);
		
		for(int i = 0; i < k; ++i) {
			for(int j = 0; j < setSize-1; ++j) {
				if(listOfEntries.get(j).getValue() > listOfEntries.get(j+1).getValue()) {
					Collections.swap(listOfEntries, j, j+1);
				}
			}
			bestRecommendations.add( listOfEntries.get(setSize - i - 1).getKey()  );
		} 
		
		return bestRecommendations;
	}
	
	public static Props props() {
		return Props.create(KBestRecommendationsActor.class);
	}
}
