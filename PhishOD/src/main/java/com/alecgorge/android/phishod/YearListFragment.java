package com.alecgorge.android.phishod;

import android.*;
import android.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.alecgorge.android.phishod.phishtracks.PhishTracksAPI;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alec on 8/4/13.
 */
public class YearListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Void> {
	private ArrayAdapter<String> mAdapter;
	private List<String> mItems;
	private LayoutInflater mInflater;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// this is really important in order to save the state across screen
		// configuration changes for example
		setRetainInstance(true);

		mInflater = LayoutInflater.from(getActivity());

		// you only need to instantiate these the first time your fragment is
		// created; then, the method above will do the rest
		if (mAdapter == null) {
			mItems = new ArrayList<String>();
			mAdapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_list_item_1, mItems);
		}

		getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				String year = mItems.get(i);

				Intent intent = new Intent(getActivity(), YearActivity.class);
				intent.putExtra("year", year);
				startActivity(intent);
			}
		});

		// initiate the loader to do the background work
		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public Loader<Void> onCreateLoader(int id, Bundle args) {
		AsyncTaskLoader<Void> loader = new AsyncTaskLoader<Void>(getActivity()) {

			@Override
			public Void loadInBackground() {
				mItems = PhishTracksAPI.sharedInstance().getYears();
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
		mAdapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_list_item_1, mItems);
		setListAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
		setListShown(true);
	}

	@Override
	public void onLoaderReset(Loader<Void> loader) {
	}
}
