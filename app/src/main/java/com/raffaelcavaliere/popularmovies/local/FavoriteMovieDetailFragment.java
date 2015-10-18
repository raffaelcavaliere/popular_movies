package com.raffaelcavaliere.popularmovies.local;

import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.raffaelcavaliere.popularmovies.MovieDetailFragment;
import com.raffaelcavaliere.popularmovies.R;
import com.raffaelcavaliere.popularmovies.data.MovieDbContract;
import com.raffaelcavaliere.popularmovies.data.MovieDbItem;
import com.raffaelcavaliere.popularmovies.data.MovieDbItemReview;
import com.raffaelcavaliere.popularmovies.data.MovieDbItemVideo;

import java.util.ArrayList;


public class FavoriteMovieDetailFragment extends MovieDetailFragment {

    public void loadMovieData(final View view) {

        LinearLayout video_layout = (LinearLayout) view.findViewById(R.id.list_videos);

        Cursor c;
        if (movieDbItem.format.equals("tv"))
            c = getActivity().getContentResolver().query(MovieDbContract.MovieDbVideoEntry.buildMovieDbVideoTvUri(movieDbItem.id), null, null, null, null);
        else
            c = getActivity().getContentResolver().query(MovieDbContract.MovieDbVideoEntry.buildMovieDbVideoFilmUri(movieDbItem.id), null, null, null, null);

        movieDbItem.videos.clear();

        while (c.moveToNext()) {
            String id = c.getString(c.getColumnIndex("id"));
            String key = c.getString(c.getColumnIndex("key"));
            String name = c.getString(c.getColumnIndex("name"));
            String site = c.getString(c.getColumnIndex("site"));
            String type = c.getString(c.getColumnIndex("type"));
            MovieDbItemVideo video = new MovieDbItemVideo(key, name, id, site, type);
            movieDbItem.videos.add(video);
        }
        c.close();

        for(int i = 0; i < movieDbItem.videos.size(); i++)
            addVideo(video_layout, movieDbItem.videos.get(i));


        LinearLayout review_layout = (LinearLayout) view.findViewById(R.id.list_reviews);

       if (movieDbItem.format.equals("tv"))
            c = getActivity().getContentResolver().query(MovieDbContract.MovieDbReviewEntry.buildMovieDbReviewTvUri(movieDbItem.id), null, null, null, null);
        else
        c = getActivity().getContentResolver().query(MovieDbContract.MovieDbReviewEntry.buildMovieDbReviewFilmUri(movieDbItem.id), null, null, null, null);

        movieDbItem.reviews.clear();

        while (c.moveToNext()) {
            String id = c.getString(c.getColumnIndex("id"));
            String author = c.getString(c.getColumnIndex("author"));
            String content = c.getString(c.getColumnIndex("content"));
            String url = c.getString(c.getColumnIndex("url"));
            MovieDbItemReview review = new MovieDbItemReview(id, content, author, url);
            movieDbItem.reviews.add(review);
        }
        c.close();

        for (int i = 0; i < movieDbItem.reviews.size(); i++)
            addReview(review_layout, movieDbItem.reviews.get(i));

    }
}
