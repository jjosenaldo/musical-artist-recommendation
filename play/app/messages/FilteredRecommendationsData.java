package messages;

import java.util.List;

public class FilteredRecommendationsData {
	private List<Integer> recommendations;
	
	public FilteredRecommendationsData(List<Integer> recommendations) {
		this.recommendations = recommendations;
	}
	
	public List<Integer> getRecommendations(){
		return recommendations;
	}
}
