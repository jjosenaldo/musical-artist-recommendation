package br.ufrn.reducemap;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.pattern.Patterns;
import akka.util.Timeout;
import br.ufrn.actors.MasterActor;
import br.ufrn.messages.Result;
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
    	
        System.out.println( "Hello World!" );
        ActorSystem system = ActorSystem.create("recommenderSystem");
        ActorRef master = system.actorOf(MasterActor.props(), "master");
        
        master.tell("fui na casa de maria comprar bolo", master);

        
        try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        
        Future<Object> future = Patterns.ask(master, new Result(),timeout );
        try {
			String result = (String) Await.result(future, Duration.create("5 seconds"));
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
