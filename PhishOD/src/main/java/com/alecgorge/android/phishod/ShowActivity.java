package com.alecgorge.android.phishod;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alecgorge.android.phishod.phishtracks.PhishTracksAPI;
import com.alecgorge.android.phishod.phishtracks.PhishTracksSet;
import com.alecgorge.android.phishod.phishtracks.PhishTracksShow;
import com.alecgorge.android.phishod.phishtracks.PhishTracksSong;
import com.alecgorge.android.phishod.player.MusicService;
import com.applidium.headerlistview.HeaderListView;
import com.applidium.headerlistview.SectionAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alec on 8/4/13.
 */
public class ShowActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Void> {
	protected SectionAdapter mAdapter;
	protected PhishTracksShow show;
	protected HeaderListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		setContentView(R.layout.sectioned_list_layout);
		listView = (HeaderListView)findViewById(R.id.headerListView);
		listView.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				if(i <= 4) return;

				i -= 4;

				int count = 0;
				int sectionCount = 1 ;
				int index = i;
				PhishTracksSong song = null;
				List<PhishTracksSong> flatSongs = new ArrayList<PhishTracksSong>();
				for(PhishTracksSet set : show.getSets()) {
					for(PhishTracksSong s : set.getTracks()) {
						if(count == i) {
							index = i - sectionCount;
						}
						count++;

						flatSongs.add(s);
					}

					count++;
					sectionCount++;
				}

				PhishTracksPlayer.sharedInstance().setPlaylist(flatSongs).setIndex(index).setShow(show);

				Intent in = new Intent(MusicService.ACTION_URL);
				in.setData(PhishTracksPlayer.sharedInstance().getCurrentSong().getUri());

				startService(in);
			}
		});

		setTitle(getIntent().getStringExtra("showDate"));

		getSupportLoaderManager().initLoader(0, null, this);
	}

	@Override
	public Loader<Void> onCreateLoader(int id, Bundle args) {
		setProgressBarIndeterminateVisibility(true);

		AsyncTaskLoader<Void> loader = new AsyncTaskLoader<Void>(this) {
			@Override
			public Void loadInBackground() {
				show = PhishTracksAPI.sharedInstance().getShow(getIntent().getStringExtra("showDate"));
				return null;
			}
		};
		// somehow the AsyncTaskLoader doesn't want to start its job without
		// calling this method
		loader.forceLoad();
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Void> loader, Void result) {
		// add the new item and let the adapter know in order to refresh the
		// views
		mAdapter = new SectionAdapter() {
			@Override
			public int numberOfSections() {
				return 1 + show.getSets().size();
			}

			@Override
			public int numberOfRows(int section) {
				if(section == 0) {
					return 3;
				}
				else {
					return show.getSets().get(section - 1).getTracks().size();
				}
			}

			@Override
			public Object getRowItem(int section, int row) {
				return null;
			}

			@Override
			public boolean hasSectionHeaderView(int section) {
				return true;
			}

			@Override
			public int getSectionHeaderViewTypeCount() {
				return 1;
			}

			@Override
			public int getSectionHeaderItemViewType(int section) {
				return 0;
			}

			@Override
			public View getRowView(int section, int row, View convertView, ViewGroup parent) {
				if (convertView == null) {
					LayoutInflater inflater = getLayoutInflater();
					convertView = (View)inflater.inflate(android.R.layout.simple_list_item_2, null);
				}
				TextView textLabel = (TextView) convertView.findViewById(android.R.id.text1);
				TextView detailTextLabel = (TextView) convertView.findViewById(android.R.id.text2);

				if(section == 0) {
					if(row == 0) {
						textLabel.setText("Features");

						String metadata = (show.isRemastered() || show.isSoundboard()) ? "": "None";
						if(show.isRemastered()) metadata += "Remastered";
						if(show.isRemastered() && show.isSoundboard()) metadata += ", ";
						if(show.isSoundboard()) metadata += "Soundboard";

						detailTextLabel.setText(metadata);
					}
					else if(row == 1) {
						textLabel.setText("City");
						detailTextLabel.setText(show.getCity());
					}
					else if(row == 2) {
						textLabel.setText("Venue");
						detailTextLabel.setText(show.getLocation());
					}
				}
				else {
					PhishTracksSet set = show.getSets().get(section - 1);
					PhishTracksSong song = set.getTracks().get(row);

					textLabel.setText(song.getTitle());
 					detailTextLabel.setText(song.getHumanReadableDuration());
				}

				return convertView;
			}

			@Override
			public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
				if (convertView == null) {
					convertView = (TextView) getLayoutInflater().inflate(getResources().getLayout(android.R.layout.simple_list_item_1), null);
				}

				TextView textLabel = (TextView)convertView;
				textLabel.setBackgroundColor(getResources().getColor(android.R.color.background_dark));

				if(section == 0) {
					textLabel.setText("Concert Info");
				}
				else {
					PhishTracksSet set = show.getSets().get(section - 1);
					textLabel.setText(set.getTitle());
				}

				return convertView;
			}
		};

		listView.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
		setProgressBarIndeterminateVisibility(false);
	}

	@Override
	public void onLoaderReset(Loader<Void> loader) {
	}
}

