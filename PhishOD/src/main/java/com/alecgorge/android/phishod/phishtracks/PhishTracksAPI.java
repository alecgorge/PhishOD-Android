package com.alecgorge.android.phishod.phishtracks;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alec on 8/4/13.
 */
public class PhishTracksAPI {
	final static String BASE_URL = "http://www.phishtracks.com";
	static PhishTracksAPI inst = null;
	public static PhishTracksAPI sharedInstance() {
		if(inst == null) inst = new PhishTracksAPI();
		return inst;
	}

	public List<String> getYears() {
		List<String> r = new ArrayList<String>();
		try {
			JSONObject obj = (JSONObject)get("/");
			JSONArray arr = obj.getJSONArray("years");
			for(int i = 0; i < arr.length(); i++) {
				r.add(arr.getString(i));
			}
		}
		catch (JSONException e){
			e.printStackTrace();
		}
		return r;
	}

	public List<PhishTracksSong> getSongs() {
		List<PhishTracksSong> r = new ArrayList<PhishTracksSong>();
		try {
			JSONArray arr = (JSONArray)get("/songs");
			for(int i = 0; i < arr.length(); i++) {
				r.add(new PhishTracksSong(arr.getJSONObject(i)));
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return r;
	}

	public List<PhishTracksShow> getYear(String year) {
		List<PhishTracksShow> r = new ArrayList<PhishTracksShow>();
		try {
			JSONArray arr = (JSONArray)get("/shows?year=" + year);
			for(int i = 0; i < arr.length(); i++) {
				r.add(new PhishTracksShow(arr.getJSONObject(i)));
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return r;
	}

	public PhishTracksShow getShow(String showDate) {
		try {
			JSONObject o = (JSONObject)get("/shows/" + showDate);
			return new PhishTracksShow(o);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Object get(String url)	{
		HttpClient httpclient = new DefaultHttpClient();

		// Prepare a request object
		HttpGet httpget = new HttpGet(BASE_URL + url);
		httpget.addHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		httpget.addHeader("X-Requested-With", "XMLHttpRequest");

		// Execute the request
		HttpResponse response;
		try {
			response = httpclient.execute(httpget);

			// Get hold of the response entity
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				// A Simple JSON Response Read
				InputStream instream = entity.getContent();
				String result = convertStreamToString(instream);

				Object json;
				if(result.startsWith("[")) {
					json = new JSONArray(result);
				}
				else {
					json = new JSONObject(result);
				}

				instream.close();

				return json;
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	String convertStreamToString(InputStream is) {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine()
         * method. We iterate until the BufferedReader return null which means
         * there's no more data to read. Each line will appended to a StringBuilder
         * and returned as String.
         */
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}
