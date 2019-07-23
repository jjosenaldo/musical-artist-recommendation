package br.ufrn.actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import br.ufrn.messages.PrevUserData;
import br.ufrn.requests.PrevDataRequest;
import br.ufrn.utils.IOUtils;

public class PrevUserDataActor extends AbstractActor{
	@Override
	public Receive createReceive() {	
		return receiveBuilder()
				.match(PrevDataRequest.class, msg -> {
					getSender().tell(new PrevUserData(IOUtils.getInterests()), getSelf());  
				})
				.build();
		
	}
	
	public static Props props () {
		return Props.create(PrevUserDataActor.class);
	}
}
