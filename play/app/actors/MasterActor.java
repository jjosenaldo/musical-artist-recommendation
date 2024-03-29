package actors;

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
import messages.AllRecommendationsData;
import messages.AllRecommendationsWithFilterNumberData;
import messages.BestRecommendationsData;
import messages.ClosestUsersData;
import messages.CosData;
import messages.InitMessage;
import messages.InterestsOfOldUsersPlusNewData;
import messages.MaxNumberOfClosestsData;
import messages.PrevUserData;
import messages.SingleUserTasteData;
import messages.UserPairData;
import requests.ArtistRecommendationAggregateRequest;
import requests.CloseDBReuquest;
import requests.CosAggregateRequest;
import requests.PrevDataRequest;

public class MasterActor extends AbstractActor {
	private ActorRef cosAggregateActor = getContext().actorOf(CosAggregateActor.props(), "cos_aggregate");
	private ActorRef artistRecommendationAggregateActor = getContext().actorOf(ArtistRecommendationAggregateActor.props(), "artist_recommendation_aggregate");
	private ActorRef bestRecommendationsActor = getContext().actorOf(KBestRecommendationsActor.props(), "best_recommendation");
	private ActorRef dbActor = getContext().actorOf(DBActor.props(), "db" );
	private ActorRef contextActor;
	
	private InitMessage newUserData;
	private PrevUserData prevUserData;

	private final int maxClosestUsersParam = 10;
	private final int numberOfCosRouteesParam = 10; 

	private int closestUsersParam; 
	private int kParam;	
	private int numberOfArtistRecommendationRouteesParam;
	private int userCountParam;

	private Router cosRouter;
	private Router artistRecommendationRouter;

	@Override
	public Receive createReceive() {
		
		return receiveBuilder()
				.match(InitMessage.class, msg -> {
					contextActor = getSender();
					newUserData = new InitMessage(msg);
					kParam = msg.getMaxRecommendations();
					cosAggregateActor.tell(new MaxNumberOfClosestsData(maxClosestUsersParam), getSelf());
					dbActor.tell(new PrevDataRequest(), getSelf());
				})
				.match(PrevUserData.class, msg -> {
					// System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@ PrevUserData @@@@@@@@@@@@@@@@@@@@@@@@@");
					prevUserData = msg;
					userCountParam = msg.getInterests().size();
					dbActor.tell(new CloseDBReuquest(), getSelf());
					applyCosRoutes(msg);
				})
				.match(CosData.class, msg -> {
					// System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@ CosData @@@@@@@@@@@@@@@@@@@@@@@@@");
					cosAggregateActor.tell(msg, getSelf());
					
					if(--userCountParam == 0) {
						cosAggregateActor.tell(new CosAggregateRequest(), getSelf());
					}
				})
				.match(ClosestUsersData.class, msg -> {
					// System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@ ClosestUsersData @@@@@@@@@@@@@@@@@@@@@@@@@");
					
					applyArtistRecommendationRoutes(msg);
				})
				.match(SingleUserTasteData.class, msg ->{
					// System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@ SingleUserTasteData @@@@@@@@@@@@@@@@@@@@@@@@@");
					artistRecommendationAggregateActor.tell(msg, getSelf());
					
					if(--closestUsersParam == 0) {
						// System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@ Aggregate Time @@@@@@@@@@@@@@@@@@@@@@@@@");
						artistRecommendationAggregateActor.tell(new ArtistRecommendationAggregateRequest(), getSelf());
					}
				})
				.match(AllRecommendationsData.class, msg -> {
					// System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@ AllRecommendationsData @@@@@@@@@@@@@@@@@@@@@@@@@");
					bestRecommendationsActor.tell(
							new AllRecommendationsWithFilterNumberData(msg.getRecommendations(), kParam),
							getSelf());
				})
				.match(BestRecommendationsData.class, msg -> {
					contextActor.tell(msg, getSelf());
				})
				.build();
		
	}
	
	private void initArtistRecommendationRouter() {
		List<Routee> routees = new ArrayList<Routee>();
		for (int i = 0; i < numberOfArtistRecommendationRouteesParam; ++i) {
			ActorRef r = getContext().actorOf(Props.create(ArtistRecommendationActor.class));
			this.getContext().watch(r);
			routees.add(new ActorRefRoutee(r));
		}
		
		artistRecommendationRouter = new Router(new RoundRobinRoutingLogic(), routees);
	}
	
	private void initCosRouter() {
		List<Routee> routees = new ArrayList<Routee>();
		for (int i = 0; i < numberOfCosRouteesParam; i++) {
			ActorRef r = getContext().actorOf(Props.create(CosActor.class));
			this.getContext().watch(r);
			routees.add(new ActorRefRoutee(r));
		}

		cosRouter = new Router(new RoundRobinRoutingLogic(), routees);
	}
	
	private void applyArtistRecommendationRoutes(ClosestUsersData data) {
		List<Integer> closestUsers = data.getClosestUsers();
		
		closestUsersParam = closestUsers.size();
		numberOfArtistRecommendationRouteesParam = Math.min(closestUsersParam, maxClosestUsersParam);
		
		initArtistRecommendationRouter();
		
		for(int closeUser : closestUsers) {
			artistRecommendationRouter.route(
					new InterestsOfOldUsersPlusNewData(prevUserData.getInterests(), 
							newUserData.getUserData(),  closeUser),  
					getSelf());
		}
	}
	
	private void applyCosRoutes(PrevUserData msg) {
		initCosRouter();
		int otherUser;
		for(Map.Entry<Integer, Map<Integer, Double>> interest : msg.getInterests().entrySet()) {
			otherUser = interest.getKey();
			Map<Integer, Double> interestsOtherUser = interest.getValue();
	
			UserPairData pairData = new UserPairData(otherUser, 
					newUserData.getUserData(), 
					interestsOtherUser);
			cosRouter.route(pairData, getSelf());
		}
	}

	public static Props props() {
		return Props.create(MasterActor.class);
	}
}
