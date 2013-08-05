package com.alecgorge.android.phishod;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alecgorge.android.phishod.phishtracks.PhishTracksAPI;
import com.alecgorge.android.phishod.phishtracks.PhishTracksShow;

import java.util.List;

/**
 * Created by Alec on 8/4/13.
 */
public class YearActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Void> {
	protected ArrayAdapter<PhishTracksShow> mAdapter;
	protected List<PhishTracksShow> mItems;
	protected ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		setContentView(R.layout.list_layout);
		listView = (ListView)findViewById(R.id.listView);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				PhishTracksShow show = mItems.get(i);

				Intent intent = new Intent(YearActivity.this, ShowActivity.class);
				intent.putExtra("showDate", show.getShowDate());
				startActivity(intent);
			}
		});

		setTitle(getIntent().getStringExtra("year"));

		getSupportLoaderManager().initLoader(0, null, this);
	}

	@Override
	public Loader<Void> onCreateLoader(int id, Bundle args) {
		setProgressBarIndeterminateVisibility(true);

		AsyncTaskLoader<Void> loader = new AsyncTaskLoader<Void>(this) {
			@Override
			public Void loadInBackground() {
				mItems = PhishTracksAPI.sharedInstance().getYear(getIntent().getStringExtra("year"));
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
		mAdapter = new ArrayAdapter<PhishTracksShow>(this,android.R.layout.simple_list_item_2, mItems){
			@Override
			public View getView(int position, View convertView, ViewGroup parent){
				View row;
				if(convertView == null) {
					LayoutInflater inflater = getLayoutInflater();
					row = (View)inflater.inflate(android.R.layout.simple_list_item_2, null);
				}
				else {
					row = (View)convertView;
				}

				PhishTracksShow data = mItems.get(position);

				String metadata = "";
				if(data.isRemastered() || data.isSoundboard()) metadata += " - ";
				if(data.isRemastered()) metadata += "remastered";
				if(data.isRemastered() && data.isSoundboard()) metadata += ", ";
				if(data.isSoundboard()) metadata += "soundboard";

				TextView v = (TextView) row.findViewById(android.R.id.text1);
				v.setText(data.getShowDate() + metadata);
				v = (TextView) row.findViewById(android.R.id.text2);
				v.setText(data.getLocation() + " - " + data.getCity());
				return row;
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
