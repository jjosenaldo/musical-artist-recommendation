package br.ufrn.main;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.pattern.Patterns;
import akka.util.Timeout;
import br.ufrn.actors.MasterActor;
import br.ufrn.messages.UserData;
import br.ufrn.requests.ResultRequest;
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
    public static void main( String[] args )
    {
    	Timeout timeout = new Timeout((FiniteDuration) Duration.create("5 seconds"));
    	
        ActorSystem system = ActorSystem.create("recommenderSystem");
        ActorRef master = system.actorOf(MasterActor.props(), "master");
        Map<Integer, Double> newInterests = new ConcurrentHashMap<>();
        newInterests.put(500, 0.3);
		newInterests.put(520, 0.3);
		newInterests.put(540, 0.4);
        
		// 5000: id of the new user
        master.tell(new UserData(5000, newInterests), master);
        
        try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        
        Future<Object> future = Patterns.ask(master, new ResultRequest(),timeout );
        try {
			String result = (String) Await.result(future, Duration.create("5 seconds"));
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}

       
    }
}
