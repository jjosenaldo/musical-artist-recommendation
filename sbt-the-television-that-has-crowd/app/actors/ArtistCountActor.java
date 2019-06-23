package actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import messages.ArtistCount;
import requests.ArtistCountRequest;

public class ArtistCountActor extends AbstractActor{
	// TODO
	private final int ARTIST_COUNT = 100;
	public Receive createReceive() {
		return receiveBuilder() 
				.match(ArtistCountRequest.class, msg -> getSender().tell(new ArtistCount(ARTIST_COUNT), getSelf())
				)
				.build();
	}
	
	public static Props props () {
		return Props.create(ArtistCountActor.class);
	}
}
