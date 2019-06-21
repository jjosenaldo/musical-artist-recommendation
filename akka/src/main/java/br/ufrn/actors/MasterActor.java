package br.ufrn.actors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.routing.ActorRefRoutee;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;
import br.ufrn.messages.CosAggregateData;
import br.ufrn.messages.CosData;
import br.ufrn.messages.ModData;
import br.ufrn.messages.PrevUserData;
import br.ufrn.messages.UserData;
import br.ufrn.messages.UserPairData;
import br.ufrn.messages.requests.CosAggregateRequest;
import br.ufrn.messages.requests.PrevDataRequest;

public class MasterActor extends AbstractActor {
	private ActorRef modActor = getContext().actorOf(ModActor.props(), "mod");
	private ActorRef prevUserDataActor = getContext().actorOf(PrevUserDataActor.props(), "prev_user_data");
	private ActorRef cosAggregateActor = getContext().actorOf(CosAggregateActor.props(), "cos_aggregate");
	private UserData newUserData;
	private double newUserMod;
	private final int numberOfRoutees = 10;// TODO: config this
	private int maxArtists = 10; // TODO: make actor for this
	private int userCount = 10; // TODO: make actor for this

	Router cosRouter;
	{
		List<Routee> routees = new ArrayList<Routee>();
		for (int i = 0; i < numberOfRoutees; i++) {
			ActorRef r = getContext().actorOf(Props.create(CosActor.class));
			this.getContext().watch(r);
			routees.add(new ActorRefRoutee(r));
		}

		cosRouter = new Router(new RoundRobinRoutingLogic(), routees);

	}

	@Override
	public Receive createReceive() {
		
		return receiveBuilder()
				.match(UserData.class, msg -> {
					newUserData = new UserData(msg);
					modActor.tell(msg, getSelf());
				})
				.match(ModData.class, msg -> {
					newUserMod = msg.getData();
					prevUserDataActor.tell(new PrevDataRequest(), getSelf());
				})
				.match(PrevUserData.class, msg -> {
					applyRoutes(msg);
				})
				.match(CosData.class, msg -> {
					cosAggregateActor.tell(msg, getSelf());
					
					if(--userCount == 0) {
						cosAggregateActor.tell(new CosAggregateRequest(), getSelf());
					}
				})
				.match(CosAggregateData.class, msg -> {
					printMean(msg);
				})
				.build();
		
	}
	
	private void applyRoutes(PrevUserData msg) {
		for(Integer userTwo : msg.getUserList()) {
			Map<Integer, Double> interestsUserTwo = msg.getInterests().get(userTwo);
			Double userTwoMod = msg.getMods().get(userTwo);
			
			if(interestsUserTwo != null) {
				UserPairData pairData = new UserPairData(newUserData.getUser(), userTwo, newUserData.getUserData(), 
						interestsUserTwo, newUserMod, userTwoMod, maxArtists);
				cosRouter.route(pairData, getSelf());
			}
		}
	}
	
	private void printMean(CosAggregateData msg) {
		for(int user : msg.getClosestUsers()) {
			System.out.println(user);
		}
	}

	public static Props props() {
		return Props.create(MasterActor.class);
	}
}