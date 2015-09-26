package com.raffaelcavaliere.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_moviedetail, container, false);
        Intent intent = getActivity().getIntent();
        String overview = intent.getStringExtra("overview");
        String releaseDate = intent.getStringExtra("releaseDate");
        String title = intent.getStringExtra("title");
        double voteAverage = intent.getDoubleExtra("voteAverage", 0d);
        long voteCount = intent.getLongExtra("voteCount", 0);
        String posterPath = intent.getStringExtra("posterPath");
        Uri posterUri = Uri.parse(MovieDbArrayAdapter.MOVIE_DB_IMAGE_BASE_URL).buildUpon()
                .appendPath(MovieDbArrayAdapter.MOVIE_DB_LARGE_IMAGE_PATH)
                .appendEncodedPath(posterPath).build();

        TextView text_overview = (TextView) view.findViewById(R.id.movie_detail_overview);
        TextView text_title = (TextView) view.findViewById(R.id.movie_detail_title);
        TextView text_release_date = (TextView) view.findViewById(R.id.movie_detail_release_date);
        TextView text_vote_average = (TextView) view.findViewById(R.id.movie_detail_vote_average);
        TextView text_vote_count = (TextView) view.findViewById(R.id.movie_detail_vote_count);
        ImageView image_poster = (ImageView) view.findViewById(R.id.movie_detail_poster);

        text_overview.setText(overview);
        text_title.setText(title);
        text_release_date.setText(releaseDate);
        text_vote_average.setText(String.valueOf(voteAverage) + "/10.0");
        text_vote_count.setText(String.valueOf(voteCount) + " votes");

        Picasso.with(getActivity().getApplicationContext()).load(posterUri.toString()).into(image_poster);

        return view;
    }
}
