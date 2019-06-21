package br.ufrn.actors;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import akka.actor.AbstractActor;
import akka.actor.Props;
import br.ufrn.messages.CosAggregateData;
import br.ufrn.messages.CosData;
import br.ufrn.messages.requests.CosAggregateRequest;

public class CosAggregateActor extends AbstractActor{
	private Map<Integer, Double> data;
	private int user;
	private int[] closestUsers;
	private double[] closestValues;
	private final int numberOfClosests = 3;
	
	public CosAggregateActor() {
		super();
		this.closestUsers = new int[numberOfClosests];
		this.closestValues = new double[numberOfClosests];
		this.data = new ConcurrentHashMap<Integer, Double>();
	}
	
	public Receive createReceive() {
		return receiveBuilder() 
				.match(CosData.class, msg -> {   
						user = msg.getUserOne();
						Integer user2 = msg.getUserTwo();
						double cos = msg.getCos();
						updateBest(user2, cos);
						data.put(user2, cos);
					}
				)
				.match(CosAggregateRequest.class, msg -> getSender().tell(new CosAggregateData(user, data, closestUsers), getSelf()))
				.build();
	}
	
	public static Props props () {
		return Props.create(CosAggregateActor.class);
	}
	
	public void updateBest(int otherUser, double cos) {
		for(int i = 0; i < numberOfClosests;++i) {
			if(cos < closestValues[i]) {
				for(int j = i+1; j < numberOfClosests; ++j) {
					closestValues[j] = closestValues[j-1];
					closestUsers[j] = closestUsers[j-1];
				}
				closestValues[i] = cos;
				closestUsers[i] = otherUser;
				break;
			}
		}
	}
	
}
