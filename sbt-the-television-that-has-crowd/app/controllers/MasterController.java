package controllers;

import actors.MasterActor;
import akka.actor.*;
import static akka.pattern.Patterns.ask;
import akka.pattern.Patterns;
import akka.util.Timeout;
import java.util.Map;
import javax.inject.*;
import messages.BestRecommendationsData;
import messages.InitMessage;
import models.InitMessageModel;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.*;
import scala.compat.java8.FutureConverters;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;
import utils.Parser;

public class MasterController extends Controller{
    private Form<InitMessageModel> initMessageForm;

    @Inject
    public MasterController (FormFactory formFactory) {
        this.initMessageForm = formFactory.form(InitMessageModel.class);
    }

    public Result index() {
        return ok(views.html.request_recommendation.render(initMessageForm));
    }

    public Result getRecommendations(Http.Request request){
        // Data sent in form
        InitMessageModel initMessageModel = initMessageForm.bindFromRequest(request).get();
        Map<Integer, Double> dataMap = Parser.mapFromString(initMessageModel.getData());
        
        Timeout timeout = new Timeout((FiniteDuration) Duration.create("10 seconds"));
        ActorSystem actorSystem = ActorSystem.create("recommender");
        ActorRef masterActor = actorSystem.actorOf(MasterActor.props());

        Future<Object> future = Patterns.ask(masterActor, new InitMessage(initMessageModel.getUser(), dataMap, initMessageModel.getMaxClosestUsers(), initMessageModel.getMaxRecommendations()),timeout );
        // System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@ Created Future object @@@@@@@@@@@@@@@@@@@@@@@@@");

        try {
            BestRecommendationsData result = (BestRecommendationsData) Await.result(future, Duration.create("10 seconds"));
            return ok(views.html.recommendations.render(result.getBestRecommendations()));
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            actorSystem.terminate();
        }

        return null;
    }
}