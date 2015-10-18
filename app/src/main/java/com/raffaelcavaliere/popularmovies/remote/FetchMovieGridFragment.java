package com.raffaelcavaliere.popularmovies.remote;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.raffaelcavaliere.popularmovies.data.MovieDbItem;
import com.raffaelcavaliere.popularmovies.MovieGridFragment;
import com.raffaelcavaliere.popularmovies.R;

public class FetchMovieGridFragment extends MovieGridFragment  {

    private int pageCount = 0;

    public FetchMovieGridFragment() { this.setHasOptionsMenu(true); }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            loadMovieData();
        }

        if (id == R.id.action_load_more) {
            appendMovieData();
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean checkConnectivity() {
        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (!isConnected) {
            Toast.makeText(getActivity(),"No internet connection !", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public void fetchMovieDb(final boolean clear) {
        FetchMovieDbTask f = new FetchMovieDbTask() {
            @Override
            protected void onPostExecute(MovieDbItem[] result) {
                super.onPostExecute(result);

                if (clear)
                    movieDbData.clear();

                for (int i = 0; i < result.length; i++)
                    movieDbData.add(result[i]);
                mAdapter.notifyDataSetChanged();
            }
        };
        String pref_format = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("pref_format", FetchMovieDbTask.MOVIE_DB_MOVIE);
        String pref_sort = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("pref_sort", FetchMovieDbTask.MOVIE_DB_POPULARITY_SORT);
        if (clear)
            pageCount = 1;
        else
            pageCount++;
        if (pageCount <= 1000) {
            FetchMovieDbTask.FetchMovieDbTaskParams params =
                    f.new FetchMovieDbTaskParams(FetchMovieDbTask.MOVIE_DB_DISCOVER, pref_format, pref_sort, pageCount);
            f.execute(params);
        }
    }

    public void loadMovieData() {
        if (checkConnectivity())
            fetchMovieDb(true);
    }

    public void appendMovieData() {
        if (checkConnectivity())
            fetchMovieDb(false);
    }

    public Intent getDetailIntent() {
        return new Intent(getActivity().getApplicationContext(), FetchMovieDetailActivity.class);
    }
}
