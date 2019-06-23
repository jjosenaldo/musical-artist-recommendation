package actors;

import akka.actor.*;
import messages.*;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Iterator;
import java.util.Map;


public class DbActor extends AbstractActor {

    private Cluster cluster;
	  private Session session;
    public DbActor() {
  		  cluster = Cluster.builder().addContactPoint("127.0.0.1").withCredentials("cassandra", "cassandra").build();
        Metadata metadata = cluster.getMetadata();
        session = cluster.connect("recommendation");
        System.out.printf("@@@@@@@Connected to cluster: %s\n",
                metadata.getClusterName());
  	}

    public String queryUser(Integer i) {
        return getAllUsers().get(i).toString(); 
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

    static public Props props() {
      return Props.create(DbActor.class);
    }

		@Override
		public Receive createReceive() {
			return receiveBuilder()
                  .match(Integer.class, s -> {
                    sender().tell(queryUser(s), self());
                  })
									.build();
		}

}
