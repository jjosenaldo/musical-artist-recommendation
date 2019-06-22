package br.ufrn.actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import br.ufrn.messages.ClosestUsersData;
import br.ufrn.messages.CosData;
import br.ufrn.requests.CosAggregateRequest;

public class CosAggregateActor extends AbstractActor{
	private int user;
	private int[] closestUsers;
	private double[] closestValues;
	// TODO: this should be in the Master actor
	private final int numberOfClosestsParam = 10;
	
	public CosAggregateActor() {
		super();
		this.closestUsers = new int[numberOfClosestsParam];
		this.closestValues = new double[numberOfClosestsParam];
		
		for(int i = 0; i < numberOfClosestsParam; ++i) {
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
				.match(CosAggregateRequest.class, msg -> getSender().tell(new ClosestUsersData(user, closestUsers), getSelf()))
				.build();
	}
	
	public static Props props () {
		return Props.create(CosAggregateActor.class);
	}
	
	public void updateBest(CosData data) {
		double cos = data.getCos();
		int otherUser = data.getUserTwo();
		
		for(int i = 0; i < numberOfClosestsParam; ++i) {
			if(cos > closestValues[i]) {
				for(int j = numberOfClosestsParam-1; j > i; --j) {
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
