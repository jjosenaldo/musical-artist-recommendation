package actors;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import akka.actor.AbstractActor;
import akka.actor.Props;
import messages.AllRecommendationsData;
import messages.SingleUserTasteData;
import requests.ArtistRecommendationAggregateRequest;

public class ArtistRecommendationAggregateActor extends AbstractActor{
	private Map<Integer, Double> totalRecommendations;
	
	public ArtistRecommendationAggregateActor() {
		super();
		totalRecommendations = new ConcurrentHashMap<>();
	}
	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(SingleUserTasteData.class, msg -> {
					update(msg.getTaste());
				})
				.match(ArtistRecommendationAggregateRequest.class, msg ->{
					getSender().tell(new AllRecommendationsData(totalRecommendations), getSelf());
				})
				.build();
	}
	
	private void update(Map<Integer, Double> taste) {
		// System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@ ArtistRecommendationAggregateActor.update @@@@@@@@@@@@@@@@@@@@@@@@@");
		int artist;
		Double userInterest;
		for(Map.Entry<Integer, Double> tasteEntry : taste.entrySet()) {
			artist = tasteEntry.getKey();
			userInterest = tasteEntry.getValue();
			Double oldInterestRec =  totalRecommendations.getOrDefault(artist, 0d);
			totalRecommendations.put(artist, oldInterestRec + userInterest);
		}	
	}
	
	public static Props props () {
		return Props.create(ArtistRecommendationAggregateActor.class);
	}
}
