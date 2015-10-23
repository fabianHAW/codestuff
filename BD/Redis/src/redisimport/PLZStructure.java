package src.redisimport;

import org.json.simple.JSONArray;

public class PLZStructure {

	private String id;
	private String city;
	private JSONArray loc;
	private Long pop;
	private String state;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public JSONArray getLoc() {
		return loc;
	}

	public void setLoc(JSONArray loc) {
		this.loc = loc;
	}

	public Long getPop() {
		return pop;
	}

	public void setPop(Long pop) {
		this.pop = pop;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
