package com.alecgorge.android.phishod.phishtracks;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by Alec on 8/4/13.
 */
public class PhishTracksSong implements Serializable {
	private boolean isPlayable;
	private int trackId;
	private String title;
	private int position;
	private double duration;
	private URL fileURL;
	private String filePath;
	private String slug;

	private String showDate;
	private String showLocation;

	private String index;

	private List<PhishTracksSong> tracks;

	public PhishTracksSong (JSONObject obj) throws JSONException, MalformedURLException {
		title = obj.getString("title");
		slug = obj.getString("slug");
		isPlayable = false;

		if(obj.has("id")) {
			isPlayable = true;
			trackId = obj.getInt("id");
			position = obj.getInt("position");
			duration = obj.getDouble("duration") / 1000.0f;
			filePath = obj.getString("file_url");
			fileURL = new URL("http://www.phishtracks.com" + filePath);
		}

		if(obj.has("show")) {
			showDate = obj.getJSONObject("show").getString("show_date");

			String location = obj.getJSONObject("show").getString("location");
			if(location.contains(" - ")) {
				showLocation = location.replace(" - ", "\n");
			}
			else {
				showLocation = location.replace(", ", "\n");
			}
		}
	}

	public String getHumanReadableDuration() {
		long ms = Math.round(getDuration() * 1000.0f);
		int seconds = (int) ((ms / 1000) % 60);
		int minutes = (int) (((ms / 1000) / 60) % 60);
		int hours = (int) ((((ms / 1000) / 60) / 60) % 24);

		String sec, min, hrs;
		if(seconds<10)  sec="0"+seconds;
		else            sec= ""+seconds;
		if(minutes<10)  min="0"+minutes;
		else            min= ""+minutes;
		if(hours<10)    hrs="0"+hours;
		else            hrs= ""+hours;

		if(hours == 0)  return min+":"+sec;
		else    return hrs+":"+min+":"+sec;

	}

	public Uri getUri() {
		return Uri.parse(getFileURL().toString());
	}

	@Override
	public String toString() {
		return getTitle();
	}

	public boolean isPlayable() {
		return isPlayable;
	}

	public void setPlayable(boolean playable) {
		isPlayable = playable;
	}

	public int getTrackId() {
		return trackId;
	}

	public void setTrackId(int trackId) {
		this.trackId = trackId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	public URL getFileURL() {
		return fileURL;
	}

	public void setFileURL(URL fileURL) {
		this.fileURL = fileURL;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getShowDate() {
		return showDate;
	}

	public void setShowDate(String showDate) {
		this.showDate = showDate;
	}

	public String getShowLocation() {
		return showLocation;
	}

	public void setShowLocation(String showLocation) {
		this.showLocation = showLocation;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public List<PhishTracksSong> getTracks() {
		return tracks;
	}

	public void setTracks(List<PhishTracksSong> tracks) {
		this.tracks = tracks;
	}
}
