package br.ufrn.actors;

import java.util.ArrayList;
import java.util.List;

import akka.actor.AbstractActor;
import akka.actor.Props;
import br.ufrn.messages.ClosestUsersData;
import br.ufrn.messages.CosData;
import br.ufrn.messages.MaxNumberOfClosestsData;
import br.ufrn.requests.CosAggregateRequest;

public class CosAggregateActor extends AbstractActor{
	private int user;
	private int[] closestUsers;
	private double[] closestValues;
	private int numberOfClosestsParam;
	
	
	private void initArrays(int size) {
		numberOfClosestsParam = size;
		this.closestUsers = new int[numberOfClosestsParam];
		this.closestValues = new double[numberOfClosestsParam];
		
		for(int i = 0; i < numberOfClosestsParam; ++i) {
			closestUsers[i] = -1;
			closestValues[i] = -1;
		}
	}
	
	public Receive createReceive() {
		return receiveBuilder() 
				.match(MaxNumberOfClosestsData.class, msg ->{
					initArrays(msg.getMaxClosests());
				})
				.match(CosData.class, msg -> {   
						updateBest(msg);
					}
				)
				.match(CosAggregateRequest.class, msg -> {
					List<Integer> res = new ArrayList<>(closestUsers.length);
					for(int i : closestUsers) if(i != -1) res.add(i); 
					getSender().tell(new ClosestUsersData(user, res), getSelf());
				})
				.build();
	}
	
	public static Props props () {
		return Props.create(CosAggregateActor.class);
	}
	
	public void updateBest(CosData data) {
		double cos = data.getCos();
		int otherUser = data.getUserTwo();
		
		if(cos == 0.0) {
			return;
		}
		
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
