package br.ufrn.actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import br.ufrn.messages.CosAggregateData;
import br.ufrn.messages.CosData;
import br.ufrn.requests.CosAggregateRequest;

public class CosAggregateActor extends AbstractActor{
	private int user;
	private int[] closestUsers;
	private double[] closestValues;
	private final int numberOfClosests = 3;
	
	public CosAggregateActor() {
		super();
		this.closestUsers = new int[numberOfClosests];
		this.closestValues = new double[numberOfClosests];
		
		for(int i = 0; i < numberOfClosests; ++i) {
			closestUsers[i] = -1;
			closestValues[i] = -1;
		}
	}
	
	public Receive createReceive() {
		return receiveBuilder() 
				.match(CosData.class, msg -> {   
						updateBest(msg);
					}
				)
				.match(CosAggregateRequest.class, msg -> getSender().tell(new CosAggregateData(user, closestUsers), getSelf()))
				.build();
	}
	
	public static Props props () {
		return Props.create(CosAggregateActor.class);
	}
	
	public void updateBest(CosData data) {
		double cos = data.getCos();
		int otherUser = data.getUserTwo();
		
		for(int i = 0; i < numberOfClosests; ++i) {
			if(cos > closestValues[i]) {
				for(int j = numberOfClosests-1; j > i; --j) {
					closestValues[j] =closestValues[j-1];
					closestUsers[j] =closestUsers[j-1];
				}
				closestValues[i]=cos;
				closestUsers[i]=otherUser;
				break;
			}
		}
	}
	
}
