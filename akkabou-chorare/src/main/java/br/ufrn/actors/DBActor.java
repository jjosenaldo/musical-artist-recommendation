package br.ufrn.actors;

import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentHashMap;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import akka.actor.AbstractActor;
import akka.actor.Props;
import br.ufrn.messages.NewUserData;
import br.ufrn.messages.PrevUserData;
import br.ufrn.requests.PrevDataRequest;

public class DBActor extends AbstractActor{
	private Cluster cluster;
	private Session session;
	
	public DBActor() {
		connect();
	}
	
	public Map<Integer, Map<Integer, Double>> getAllUsers(){
		 String query = "SELECT * FROM User;";
		 ResultSet rs = session.execute(query);
		 int user;
		 Map<Integer, Double> interests;
		 Map<Integer, Map<Integer, Double>> result = new ConcurrentHashMap<Integer, Map<Integer,Double>>();
		 
		 for(Row r : rs) {
			 user = r.getInt("id");
			 interests = r.getMap("artists", Integer.class, Double.class);
			 result.put(user, interests);
		 }
		 
		 return result;
		 
	}
	
	public void insertUser(NewUserData newUser) {
		Map<Integer, Double> interests = newUser.getInterests();
		
		StringBuilder sb = new StringBuilder("INSERT INTO ")
			      .append("User").append("(id, artists) ")
			      .append("VALUES (").append(newUser.getUser())
			      .append(", ");
		
		StringJoiner sj = new StringJoiner(",",  "{", "}");
		
		for(Map.Entry<Integer, Double> entry : interests.entrySet()) {
			sj.add(String.valueOf(entry.getKey()) + ":" + String.valueOf(entry.getValue()));
		}
		
		sb = sb.append(sj).append(");");
		System.out.println("message = " + sb.toString());
		session.execute(sb.toString());
		
		System.out.println("executed!");
	}
	
	public void connect() {

		cluster = Cluster.builder().addContactPoint("127.0.0.1").withCredentials("cassandra", "cassandra").build();
		Metadata metadata = cluster.getMetadata();
		session = cluster.connect("recommendation");
		System.out.printf("@@@@@@@Connected to cluster: %s\n",
				metadata.getClusterName());
	}
	
	public void close() {
        session.close();
        cluster.close();
    }
	
	
	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(NewUserData.class, msg -> {
					System.out.println("received ");
					insertUser(msg);
				})
				.match(PrevDataRequest.class, msg -> {
					getSender().tell(new PrevUserData(getAllUsers()), getSelf());
				})
				.build();
	}		
	public static Props props () {
		return Props.create(DBActor.class);
	}
}
