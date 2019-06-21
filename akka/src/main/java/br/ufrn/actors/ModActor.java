package br.ufrn.actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import br.ufrn.messages.ModData;
import br.ufrn.messages.UserData;

public class ModActor extends AbstractActor{
	@Override
	public Receive createReceive() {
		return receiveBuilder().match(
					UserData.class, 
					msg -> { getSender()
							 .tell(calculateMod(msg),
							 getSelf()); }
				)
				.build();
	}
	
	private ModData calculateMod(UserData userData) {
		double result = 0;
		
		for (Double coeffCount: userData.getUserData().values()) {
		    result += coeffCount * coeffCount; 
		}
		
		return new ModData(userData.getUser(), Math.min(Math.sqrt(result), 1.0));
	}
	
	public static Props props () {
		return Props.create(ModActor.class);
	}
}
