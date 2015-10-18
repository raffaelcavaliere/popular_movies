package com.raffaelcavaliere.popularmovies;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.raffaelcavaliere.popularmovies.data.MovieDbArrayAdapter;
import com.raffaelcavaliere.popularmovies.data.MovieDbContract;
import com.raffaelcavaliere.popularmovies.data.MovieDbItem;
import com.raffaelcavaliere.popularmovies.remote.FetchMovieDetailActivity;

import java.util.ArrayList;

public abstract class MovieGridFragment extends Fragment implements AbsListView.OnItemClickListener, SharedPreferences.OnSharedPreferenceChangeListener {

    protected ArrayList<MovieDbItem> movieDbData =  new ArrayList<>();
    protected OnFragmentInteractionListener mListener;
    protected GridView mListView;
    protected MovieDbArrayAdapter mAdapter;
    SharedPreferences preferences;

    public MovieGridFragment() {
        this.setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_moviegrid, container, false);
        mAdapter = new MovieDbArrayAdapter(getActivity(), R.layout.moviegrid_item, movieDbData);
        // Set the adapter
        mListView = (GridView) view.findViewById(R.id.movieGrid);
        mListView.setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        loadMovieData();

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        preferences.registerOnSharedPreferenceChangeListener(this);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        preferences.unregisterOnSharedPreferenceChangeListener(this);
        preferences = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MovieDbItem item = movieDbData.get(position);

        Uri u = MovieDbContract.MovieDbEntry.CONTENT_URI.buildUpon()
                .appendPath(item.format.equals("movie") ? "film" : item.format)
                .appendPath(String.valueOf(item.id)).build();
        Cursor c = getActivity().getContentResolver().query(u, null, null, null, null, null);
        item.isFavorite = c.getCount() >= 1;
        c.close();

        boolean startActivity = true;

        if (null != mListener) {
            if (mListener.onFragmentInteraction(item))
                startActivity = false;
        }

        if (startActivity) {
            Intent intent = getDetailIntent();
            intent.putExtra("overview", item.overview);
            intent.putExtra("posterPath", item.posterPath);
            intent.putExtra("backdropPath", item.backdropPath);
            intent.putExtra("releaseDate", item.releaseDate);
            intent.putExtra("title", item.title);
            intent.putExtra("id", item.id);
            intent.putExtra("popularity", item.popularity);
            intent.putExtra("voteAverage", item.voteAverage);
            intent.putExtra("voteCount", item.voteCount);
            intent.putExtra("format", item.format);
            intent.putExtra("isFavorite", item.isFavorite);
            getActivity().startActivity(intent);
        }
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        boolean onFragmentInteraction(MovieDbItem item);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        loadMovieData();
    }

    public abstract void loadMovieData();

    public abstract Intent getDetailIntent();
}
