package actors;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import akka.actor.AbstractActor;
import akka.actor.Props;
import messages.InterestsOfOldUsersPlusNewData;
import messages.SingleUserTasteData;

public class ArtistRecommendationActor extends AbstractActor{
	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(InterestsOfOldUsersPlusNewData.class, msg -> {
					getSender().tell(getSingleUserTasteData(msg), getSelf());  
				})
				.build();
	}
	
	private SingleUserTasteData getSingleUserTasteData(InterestsOfOldUsersPlusNewData msg) {
		Map<Integer, Map<Integer, Double>> interests = msg.getInterests();
		Map<Integer, Double> newUserInterests = msg.getNewUserInterests();
		Map<Integer, Double> res = new ConcurrentHashMap<>();
		int closeUser = msg.getCloseUser();
		Map<Integer, Double> interestsOfCloseUser = interests.get(closeUser);
		int artist;
		double userInterest;
		
		for(Map.Entry<Integer, Double> interest : interestsOfCloseUser.entrySet()) {
			artist = interest.getKey();
			userInterest = interest.getValue();

			if(newUserInterests.get(artist) == null)
				res.put(artist, userInterest);
		}
		
		return new SingleUserTasteData(res);
	}
	
	public static Props props() {
		return Props.create(ArtistRecommendationActor.class);
	}
}
