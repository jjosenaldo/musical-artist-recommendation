package models;

public class InitMessageModel {
    private String data;
    private int maxClosestUsers;
    private int maxRecommendations;
    private int user;

    public InitMessageModel(){
        data = "";
        maxClosestUsers = 0;
        maxRecommendations = 0;
        user = -1;
    }
    
    public int getUser() {
        return user;
    }
    
    public String getData() {
        return data;
    }
    
    public int getMaxClosestUsers() {
        return maxClosestUsers;
    }
    
    public int getMaxRecommendations() {
        return maxRecommendations;
    }

    public void setUser(int user){
        this.user = user;
    }

    public void setMaxClosestUsers(int maxClosest){
        this.maxClosestUsers = maxClosest;
    }

    public void setMaxRecommendations(int maxRecommendations){
        this.maxRecommendations = maxRecommendations;
    }

    public void setData(String data){
        this.data = data;
    }
}
