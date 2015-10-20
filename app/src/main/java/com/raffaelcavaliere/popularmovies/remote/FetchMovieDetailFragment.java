package com.raffaelcavaliere.popularmovies.remote;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.raffaelcavaliere.popularmovies.MovieDetailFragment;
import com.raffaelcavaliere.popularmovies.data.MovieDbItemReview;
import com.raffaelcavaliere.popularmovies.data.MovieDbItemVideo;
import com.raffaelcavaliere.popularmovies.R;

import java.util.ArrayList;


public class FetchMovieDetailFragment extends MovieDetailFragment {

    public void loadVideos() {
        FetchMovieDbItemTask f_videos = new FetchMovieDbItemTask() {
            @Override
            protected void onPostExecute(Object[] result) {
                super.onPostExecute(result);
                movieDbItem.videos.clear();
                for (int i = 0; i < result.length; i++) {
                    movieDbItem.videos.add((MovieDbItemVideo) result[i]);
                }
                notifyVideoDataChanged();
            }
        };
        FetchMovieDbItemTask.FetchMovieDbItemTaskParams p_videos =
                f_videos.new FetchMovieDbItemTaskParams(movieDbItem.id, movieDbItem.format, FetchMovieDbItemTask.MOVIE_DB_ITEM_VIDEOS);
        f_videos.execute(p_videos);
    }

    public void loadReviews() {
        if (movieDbItem.format.equals(FetchMovieDbTask.MOVIE_DB_MOVIE)) {
            FetchMovieDbItemTask f_reviews = new FetchMovieDbItemTask() {
                @Override
                protected void onPostExecute(Object[] result) {
                super.onPostExecute(result);
                movieDbItem.reviews.clear();
                for (int i = 0; i < result.length; i++) {
                    movieDbItem.reviews.add((MovieDbItemReview) result[i]);
                }
                notifyReviewDataChanged();
                }
            };
            FetchMovieDbItemTask.FetchMovieDbItemTaskParams p_reviews =
                    f_reviews.new FetchMovieDbItemTaskParams(movieDbItem.id, movieDbItem.format, FetchMovieDbItemTask.MOVIE_DB_ITEM_REVIEWS);
            f_reviews.execute(p_reviews);
        }
    }
}
