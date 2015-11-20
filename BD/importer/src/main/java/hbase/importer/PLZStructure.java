package hbase.importer;

import org.json.simple.JSONArray;

public class PLZStructure {

	private String id;
	private String city;
	private JSONArray loc;
	private String pop;
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

	public String getPop() {
		return pop;
	}

	public void setPop(String pop) {
		this.pop = pop;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
