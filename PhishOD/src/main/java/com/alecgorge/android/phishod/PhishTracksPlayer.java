package com.alecgorge.android.phishod;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;

import com.alecgorge.android.phishod.phishtracks.PhishTracksShow;
import com.alecgorge.android.phishod.phishtracks.PhishTracksSong;

import java.util.List;

/**
 * Created by Alec on 8/5/13.
 */
public class PhishTracksPlayer {
	static PhishTracksPlayer inst = null;
	public static PhishTracksPlayer sharedInstance() {
		if(inst == null) inst = new PhishTracksPlayer();

		return inst;
	}

	List<PhishTracksSong> playlist;
	private PhishTracksShow show;
	int index = 0;

	public PhishTracksPlayer setPlaylist(List<PhishTracksSong> p) {
		playlist = p;
		return this;
	}

	public PhishTracksPlayer setIndex(int index) {
		this.index = index;
		return this;
	}

	public int getIndex() {
		return index;
	}

	public List<PhishTracksSong> getPlaylist() {
		return playlist;
	}

	public PhishTracksSong getCurrentSong() {
		return playlist.get(getIndex());
	}

	public PhishTracksSong getNextSong() {
		if(getIndex() + 1 < playlist.size())
			setIndex(getIndex() + 1);

		return getCurrentSong();
	}

	public PhishTracksSong getPreviousSong() {
		if(getIndex() - 1 >= 0)
			setIndex(getIndex() - 1);

		return getCurrentSong();
	}

	public PhishTracksShow getShow() {
		return show;
	}

	public void setShow(PhishTracksShow show) {
		this.show = show;
	}
}
