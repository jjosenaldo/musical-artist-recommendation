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
		double userOneMod = data.getUserOneMod();
		double userTwoMod = data.getUserTwoMod();
		int maxRepos = data.getMaxRepos();
		Double userOneInterest, userTwoInterest;
		double dotProduct = 0;
		
		for(int i = 0; i < maxRepos; ++i) {
			userOneInterest = userOneData.get(i);
			userTwoInterest = userTwoData.get(i);
			
			if(userOneInterest != null && userTwoInterest != null) {
				dotProduct += userOneInterest * userTwoInterest;
			}
		}
		
		double result = Math.min(1.0, dotProduct / userOneMod / userTwoMod); 
		
		return new CosData(data.getUserOne(), data.getUserTwo(), result);
	}
	
	public static Props props () {
		return Props.create(CosActor.class);
	}
}
