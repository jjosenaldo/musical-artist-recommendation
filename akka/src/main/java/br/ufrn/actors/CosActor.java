package br.ufrn.actors;

import java.util.Map;

import akka.actor.AbstractActor;
import akka.actor.Props;
import br.ufrn.messages.CosData;
import br.ufrn.messages.UserPairData;

public class CosActor extends AbstractActor{
	@Override
	public Receive createReceive() {
		return receiveBuilder().match(
				UserPairData.class, 
				msg -> { getSender()
						 .tell(calculateCos(msg),
						 getSelf()); }
				).build();
	}
	
	private CosData calculateCos(UserPairData data) {
		Map<Integer, Double> userOneData = data.getUserOneData();
		Map<Integer, Double> userTwoData = data.getUserTwoData();
		Double userOneInterest, userTwoInterest;
		double dotProduct = 0;
		
		// You only need to check the interests of one of the users, since the artists
		// whom the other user listens to that this user doesn't listen to contributes 
		// with 0 to the inner product of the two vectors.
		for(Map.Entry<Integer, Double> interestsUserOne : userOneData.entrySet()) {
			userOneInterest = interestsUserOne.getValue();
			userTwoInterest = userTwoData.get(interestsUserOne.getKey());
			
			if(userTwoInterest != null) {
				dotProduct += userOneInterest * userTwoInterest;
			}
		}
		
		dotProduct = Math.min(1.0, dotProduct); 
		
		return new CosData(data.getUserOne(), data.getUserTwo(), dotProduct);
	}
	
	public static Props props () {
		return Props.create(CosActor.class);
	}
}
