package models;

public class InitMessageModel {
    private String data;
    private int maxRecommendations;

    public InitMessageModel(){
        data = "";
        maxRecommendations = 0;
    }
    
    public String getData() {
        return data;
    }
    
    public int getMaxRecommendations() {
        return maxRecommendations;
    }

    public void setMaxRecommendations(int maxRecommendations){
        this.maxRecommendations = maxRecommendations;
    }

    public void setData(String data){
        this.data = data;
    }
}
