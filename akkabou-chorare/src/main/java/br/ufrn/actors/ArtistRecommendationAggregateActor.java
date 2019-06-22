package br.ufrn.actors;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import akka.actor.AbstractActor;
import akka.actor.Props;
import br.ufrn.messages.AllRecommendationsData;
import br.ufrn.messages.SingleUserTasteData;
import br.ufrn.requests.ArtistRecommendationAggregateRequest;

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
