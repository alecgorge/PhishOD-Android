package com.alecgorge.android.phishod.phishtracks;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alec on 8/4/13.
 */
public class PhishTracksShow {
	private int showId;
	private String location;
	private String city;
	private boolean isRemastered;
	private boolean isSoundboard;
	private String showDate;
	private boolean hasSetsLoaded;
	private List<PhishTracksSet> sets;

	public PhishTracksShow(JSONObject o) throws JSONException, MalformedURLException {
		String[] locParts = o.getString("location").split(" - ");

		showId = o.getInt("id");
		location = locParts.length > 0 ? locParts[0] : "";
		city = locParts.length > 1 ? locParts[1] : "";
		isRemastered = o.getBoolean("remastered");
		isSoundboard = o.getBoolean("sbd");
		showDate = o.getString("show_date");
		hasSetsLoaded = false;
		sets = new ArrayList<PhishTracksSet>();

		if(o.has("sets")) {
			hasSetsLoaded = true;
			JSONArray arr = o.getJSONArray("sets");
			for(int i = 0; i < arr.length(); i++) {
				sets.add(new PhishTracksSet(arr.getJSONObject(i)));
			}
		}
	}

	public String getAlbum() {
		return String.format("%s - %s - %s", getShowDate(), getCity(), getLocation());
	}

	public int getShowId() {
		return showId;
	}

	public void setShowId(int showId) {
		this.showId = showId;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public boolean isRemastered() {
		return isRemastered;
	}

	public void setRemastered(boolean remastered) {
		isRemastered = remastered;
	}

	public boolean isSoundboard() {
		return isSoundboard;
	}

	public void setSoundboard(boolean soundboard) {
		isSoundboard = soundboard;
	}

	public String getShowDate() {
		return showDate;
	}

	public void setShowDate(String showDate) {
		this.showDate = showDate;
	}

	public boolean isHasSetsLoaded() {
		return hasSetsLoaded;
	}

	public void setHasSetsLoaded(boolean hasSetsLoaded) {
		this.hasSetsLoaded = hasSetsLoaded;
	}

	public List<PhishTracksSet> getSets() {
		return sets;
	}

	public void setSets(List<PhishTracksSet> sets) {
		this.sets = sets;
	}
}
