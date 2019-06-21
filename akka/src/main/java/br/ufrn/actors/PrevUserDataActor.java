package br.ufrn.actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import br.ufrn.messages.PrevUserData;
import br.ufrn.messages.requests.PrevDataRequest;

public class PrevUserDataActor extends AbstractActor{
	@Override
	public Receive createReceive() {	
		return receiveBuilder()
				.match(PrevDataRequest.class, msg -> {
					getSender().tell(new PrevUserData(null), getSelf()); // TODO remove null 
				})
				.build();
		
	}
	
	
	public static Props props () {
		return Props.create(PrevUserDataActor.class);
	}
}
