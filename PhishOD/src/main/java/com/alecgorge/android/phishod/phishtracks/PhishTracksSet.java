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
public class PhishTracksSet {
	private String title;
	private List<PhishTracksSong> tracks;

	public PhishTracksSet(JSONObject o) throws JSONException, MalformedURLException {
		title = o.getString("title");
		tracks = new ArrayList<PhishTracksSong>();

		JSONArray arr = o.getJSONArray("tracks");
		for(int i = 0; i < arr.length(); i++) {
			tracks.add(new PhishTracksSong(arr.getJSONObject(i)));
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<PhishTracksSong> getTracks() {
		return tracks;
	}

	public void setTracks(List<PhishTracksSong> tracks) {
		this.tracks = tracks;
	}
}
