package br.ufrn.main;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.pattern.Patterns;
import akka.util.Timeout;
import br.ufrn.actors.MasterActor;
import br.ufrn.messages.BestRecommendationsData;
import br.ufrn.messages.InitMessage;
import br.ufrn.messages.NewUserData;
import br.ufrn.utils.IOUtils;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

/**
 * Hello world!
 *
 */
public class App 
{
	public static void insertStuffInCassandra() {
		ActorSystem system = ActorSystem.create("recommenderSystem");
		ActorRef master = system.actorOf(MasterActor.props(), "master");
		Map<Integer, Map<Integer, Double>> data = IOUtils.getInterests();
		
		for(Map.Entry<Integer, Map<Integer, Double>> entry : data.entrySet()) {
			master.tell(new NewUserData(entry.getValue(), entry.getKey()), master);
		}
	}
	
    public static void main( String[] args )
    {
//    	insertStuffInCassandra();
    	Timeout timeout = new Timeout((FiniteDuration) Duration.create("10 seconds"));
    	
        ActorSystem system = ActorSystem.create("recommenderSystem");
        ActorRef master = system.actorOf(MasterActor.props(), "master");
        Map<Integer, Double> newInterests = new ConcurrentHashMap<>();
        newInterests.put(500, 0.3);
		newInterests.put(520, 0.3);
		newInterests.put(540, 0.4);
        
        Future<Object> future = Patterns.ask(master, new InitMessage(5000, newInterests, 8, 2),timeout );
        try {
			BestRecommendationsData result = (BestRecommendationsData) Await.result(future, Duration.create("10 seconds"));

            return ok(views.html.recommendations.render(result.getBestRecommendations()));
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			system.terminate();
		}
		
		
        return null;
       
    }
}
