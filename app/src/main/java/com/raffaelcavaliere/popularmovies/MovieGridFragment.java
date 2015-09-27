package com.raffaelcavaliere.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MovieGridFragment extends Fragment implements AbsListView.OnItemClickListener, SharedPreferences.OnSharedPreferenceChangeListener {

    ArrayList<FetchMovieDbTask.MovieDbItem> movieDbData = new ArrayList<FetchMovieDbTask.MovieDbItem>();
    private OnFragmentInteractionListener mListener;
    private GridView mListView;
    private MovieDbArrayAdapter mAdapter;
    private SharedPreferences preferences;

    public MovieGridFragment() {
        this.setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        preferences.registerOnSharedPreferenceChangeListener(this);
        refreshMovieList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_moviegrid, container, false);
        mAdapter = new MovieDbArrayAdapter(getActivity(), R.layout.moviegrid_item, movieDbData);
        // Set the adapter
        mListView = (GridView) view.findViewById(R.id.movieGrid);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            refreshMovieList();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
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
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        refreshMovieList();
    }

    public void refreshMovieList() {
        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (!isConnected) {
            Toast.makeText(getActivity(),"No internet connection !", Toast.LENGTH_LONG).show();
            return;
        }

        FetchMovieDbTask f = new FetchMovieDbTask() {
            @Override
            protected void onPostExecute(MovieDbItem[] result) {
                super.onPostExecute(result);
                movieDbData.clear();
                for (int i = 0; i < result.length; i++)
                    movieDbData.add(result[i]);
                mAdapter.notifyDataSetChanged();
            }
        };
        String pref_format = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("pref_format", FetchMovieDbTask.MOVIE_DB_MOVIE);
        String pref_sort = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("pref_sort", FetchMovieDbTask.MOVIE_DB_POPULARITY_SORT);
        FetchMovieDbTask.FetchMovieDbTaskParams params =
                f.new FetchMovieDbTaskParams(FetchMovieDbTask.MOVIE_DB_DISCOVER, pref_format, pref_sort);
        f.execute(params);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction(movieDbData.get(position).title);
        }
        Intent intent = new Intent(getActivity().getApplicationContext(), DetailActivity.class);
        FetchMovieDbTask.MovieDbItem item = movieDbData.get(position);
        intent.putExtra("overview", item.overview);
        intent.putExtra("posterPath", item.posterPath);
        intent.putExtra("releaseDate", item.releaseDate);
        intent.putExtra("title", item.title);
        intent.putExtra("id", item.id);
        intent.putExtra("popularity", item.popularity);
        intent.putExtra("voteAverage", item.voteAverage);
        intent.putExtra("voteCount", item.voteCount);
        getActivity().startActivity(intent);
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
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

}
