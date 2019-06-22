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
import br.ufrn.messages.AllRecommendationsData;
import br.ufrn.messages.AllRecommendationsWithFilterNumberData;
import br.ufrn.messages.ClosestUsersData;
import br.ufrn.messages.CosData;
import br.ufrn.messages.FilteredRecommendationsData;
import br.ufrn.messages.InterestsOfOldUsersPlusNewData;
import br.ufrn.messages.PrevUserData;
import br.ufrn.messages.SingleUserTasteData;
import br.ufrn.messages.UserData;
import br.ufrn.messages.UserPairData;
import br.ufrn.requests.ArtistRecommendationAggregateRequest;
import br.ufrn.requests.CosAggregateRequest;
import br.ufrn.requests.PrevDataRequest;

public class MasterActor extends AbstractActor {
	private ActorRef prevUserDataActor = getContext().actorOf(PrevUserDataActor.props(), "prev_user_data");
	private ActorRef cosAggregateActor = getContext().actorOf(CosAggregateActor.props(), "cos_aggregate");
	private ActorRef artistRecommendationAggregateActor = getContext().actorOf(ArtistRecommendationAggregateActor.props(), "artist_recommendation_aggregate");
	private ActorRef bestRecommendationsActor = getContext().actorOf(KBestRecommendationsActor.props(), "best_recommendation");
	
	private UserData newUserData;
	private PrevUserData prevUserData;
	
	private final int numberOfCosRouteesParam = 10;
	private int closestUsersParam = 10; 
	private int kParam = 5;
	
	// Needs to be smaller than closestUsersParam
	private final int numberOfArtistRecommendationRouteesParam = 3;
	private int userCount = 100; // TODO: make an actor for this
	

	Router cosRouter;
	{
		List<Routee> routees = new ArrayList<Routee>();
		for (int i = 0; i < numberOfCosRouteesParam; i++) {
			ActorRef r = getContext().actorOf(Props.create(CosActor.class));
			this.getContext().watch(r);
			routees.add(new ActorRefRoutee(r));
		}

		cosRouter = new Router(new RoundRobinRoutingLogic(), routees);
	}
	
	Router artistRecommendationRouter;
	{
		List<Routee> routees = new ArrayList<Routee>();
		for (int i = 0; i < numberOfArtistRecommendationRouteesParam; ++i) {
			ActorRef r = getContext().actorOf(Props.create(ArtistRecommendationActor.class));
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
					prevUserDataActor.tell(new PrevDataRequest(), getSelf());
				})
				.match(PrevUserData.class, msg -> {
					prevUserData = msg;
					applyCosRoutes(msg);
				})
				.match(CosData.class, msg -> {
					cosAggregateActor.tell(msg, getSelf());
					
					if(--userCount == 0) {
						cosAggregateActor.tell(new CosAggregateRequest(), getSelf());
					}
				})
				.match(ClosestUsersData.class, msg -> {
					applyArtistRecommendationRoutes(msg);
				})
				.match(SingleUserTasteData.class, msg ->{
					artistRecommendationAggregateActor.tell(msg, getSelf());
					
					if(--closestUsersParam == 0) {
						artistRecommendationAggregateActor.tell(new ArtistRecommendationAggregateRequest(), getSelf());
					}
				})
				.match(AllRecommendationsData.class, msg -> {
					bestRecommendationsActor.tell(
							new AllRecommendationsWithFilterNumberData(msg.getRecommendations(), kParam),
							getSelf());
				})
				.match(FilteredRecommendationsData.class, msg -> {
					bestRecommendationsActor.forward(msg, getContext());
				})
				.build();
		
	}
	
	private void applyArtistRecommendationRoutes(ClosestUsersData data) {
		int[] closestUsers = data.getClosestUsers();
		
		for(int closeUser : closestUsers) {
			artistRecommendationRouter.route(
					new InterestsOfOldUsersPlusNewData(prevUserData.getInterests(), 
							newUserData.getUserData(),  closeUser),  
					getSelf());
		}
	}
	
	private void applyCosRoutes(PrevUserData msg) {
		int otherUser;
		for(Map.Entry<Integer, Map<Integer, Double>> interest : msg.getInterests().entrySet()) {
			otherUser = interest.getKey();
			Map<Integer, Double> interestsOtherUser = interest.getValue();
	
			UserPairData pairData = new UserPairData(newUserData.getUser(), otherUser, 
					newUserData.getUserData(), 
					interestsOtherUser);
			cosRouter.route(pairData, getSelf());
		}
	}

	public static Props props() {
		return Props.create(MasterActor.class);
	}
}
