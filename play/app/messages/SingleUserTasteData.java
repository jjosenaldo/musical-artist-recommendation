package messages;

import java.util.Map;

public class SingleUserTasteData {
	private Map<Integer, Double> taste;
	
	public SingleUserTasteData(Map<Integer, Double> taste) {
		this.taste = taste;
	}
	
	public Map<Integer, Double> getTaste(){
		return taste;
	}
}
