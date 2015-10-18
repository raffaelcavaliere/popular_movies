package com.raffaelcavaliere.popularmovies.local;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.raffaelcavaliere.popularmovies.data.MovieDbItem;
import com.raffaelcavaliere.popularmovies.MovieGridFragment;
import com.raffaelcavaliere.popularmovies.data.MovieDbContract;
import com.raffaelcavaliere.popularmovies.data.MovieDbContentObserver;
import com.raffaelcavaliere.popularmovies.remote.FetchMovieDetailActivity;

import java.util.ArrayList;

public class FavoriteMovieGridFragment extends MovieGridFragment {

    private MovieDbContentObserver contentObserver;

    class MovieDbContentObserver extends ContentObserver {

        public MovieDbContentObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            this.onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            loadMovieData();
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        contentObserver = new MovieDbContentObserver(new Handler());
        getActivity().getContentResolver().registerContentObserver(MovieDbContract.MovieDbEntry.CONTENT_URI, true, contentObserver);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().getContentResolver().unregisterContentObserver(contentObserver);
    }

    public void loadMovieData() {
        movieDbData.clear();
        Cursor c = getActivity().getContentResolver().query(MovieDbContract.MovieDbEntry.CONTENT_URI, null, null, null, null);
        while(c.moveToNext()) {
            MovieDbItem item = new MovieDbItem(c.getString(c.getColumnIndex("format")),
                                               c.getString(c.getColumnIndex("backdrop_path")),
                                               c.getInt(c.getColumnIndex("id")),
                                               c.getString(c.getColumnIndex("title")),
                                               c.getString(c.getColumnIndex("overview")),
                                               c.getString(c.getColumnIndex("release_date")),
                                               c.getString(c.getColumnIndex("poster_path")),
                                               c.getDouble(c.getColumnIndex("popularity")),
                                               c.getDouble(c.getColumnIndex("vote_average")),
                                               c.getInt(c.getColumnIndex("vote_count")));
        movieDbData.add(item);
        }
        c.close();
    }


    public Intent getDetailIntent() {
        return new Intent(getActivity().getApplicationContext(), FavoriteMovieDetailActivity.class);
    }
}
