package com.raffaelcavaliere.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.raffaelcavaliere.popularmovies.data.MovieDbArrayAdapter;
import com.raffaelcavaliere.popularmovies.data.MovieDbContract;
import com.raffaelcavaliere.popularmovies.data.MovieDbItem;
import com.raffaelcavaliere.popularmovies.data.MovieDbItemReview;
import com.raffaelcavaliere.popularmovies.data.MovieDbItemVideo;
import com.squareup.picasso.Picasso;


public abstract class MovieDetailFragment extends Fragment {

    public final static String YOUTUBE_BASE_URL = "http://www.youtube.com/watch";
    protected MovieDbItem movieDbItem;

    public MovieDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_moviedetail, container, false);
        Bundle extras = getArguments();
        String format = extras.getString("format");
        long id = extras.getLong("id", 0);
        String overview = extras.getString("overview");
        String releaseDate = extras.getString("releaseDate");
        String title = extras.getString("title");
        double voteAverage = extras.getDouble("voteAverage", 0d);
        long voteCount = extras.getLong("voteCount", 0);
        double popularity = extras.getDouble("popularity", 0d);
        boolean isFavorite = extras.getBoolean("isFavorite", false);
        String posterPath = extras.getString("posterPath");
        String backdropPath = extras.getString("backdropPath");

        movieDbItem = new MovieDbItem(format, backdropPath, id, title, overview, releaseDate, posterPath, popularity, voteAverage, voteCount);
        movieDbItem.isFavorite = isFavorite;

        TextView text_overview = (TextView) view.findViewById(R.id.movie_detail_overview);
        text_overview.setText(overview);

        TextView text_title = (TextView) view.findViewById(R.id.movie_detail_title);
        text_title.setText(title);

        TextView text_release_date = (TextView) view.findViewById(R.id.movie_detail_release_date);
        text_release_date.setText(releaseDate);

        TextView text_vote_average = (TextView) view.findViewById(R.id.movie_detail_vote_average);
        text_vote_average.setText(String.valueOf(voteAverage) + "/10.0");

        TextView text_vote_count = (TextView) view.findViewById(R.id.movie_detail_vote_count);
        text_vote_count.setText(String.valueOf(voteCount) + " votes");

        ImageView image_poster = (ImageView) view.findViewById(R.id.movie_detail_poster);
        Uri posterUri = Uri.parse(MovieDbArrayAdapter.MOVIE_DB_IMAGE_BASE_URL).buildUpon()
                .appendPath(MovieDbArrayAdapter.MOVIE_DB_LARGE_IMAGE_PATH)
                .appendEncodedPath(posterPath).build();
        Picasso.with(getActivity().getApplicationContext()).load(posterUri.toString()).into(image_poster);

        ImageButton favorite_button = (ImageButton) view.findViewById(R.id.favorite_button);
        if (movieDbItem.isFavorite)
            favorite_button.setImageResource(android.R.drawable.btn_star_big_on);

        favorite_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!movieDbItem.isFavorite) {
                    addToFavorites();
                    addReviewsToFavorites();
                    addVideosToFavorites();
                    movieDbItem.isFavorite = true;
                    Toast.makeText(getActivity(), movieDbItem.title + " was added to your favorites", Toast.LENGTH_SHORT).show();
                } else {
                    removeReviewsFromFavorites();
                    removeVideosFromFavorites();
                    removeFromFavorites();
                    movieDbItem.isFavorite = false;
                    Toast.makeText(getActivity(), movieDbItem.title + " was removed from your favorites", Toast.LENGTH_SHORT).show();
                }
                ((ImageButton) v).setImageResource(movieDbItem.isFavorite ? android.R.drawable.btn_star_big_on : android.R.drawable.btn_star_big_off);
            }
        });

        if (savedInstanceState == null) {
            loadVideos();
            loadReviews();
        }
        else {
            movieDbItem.videos = savedInstanceState.getParcelableArrayList("videos");
            movieDbItem.reviews = savedInstanceState.getParcelableArrayList("reviews");
        }

        LinearLayout video_layout = (LinearLayout) view.findViewById(R.id.list_videos);
        for (int i = 0; i < movieDbItem.videos.size(); i++)
            addVideo(video_layout, movieDbItem.videos.get(i));

        LinearLayout review_layout = (LinearLayout) view.findViewById(R.id.list_reviews);
        for (int i = 0; i < movieDbItem.reviews.size(); i++)
            addReview(review_layout, movieDbItem.reviews.get(i));

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("reviews", movieDbItem.reviews);
        outState.putParcelableArrayList("videos", movieDbItem.videos);
        super.onSaveInstanceState(outState);
    }

    public void notifyReviewDataChanged() {
        View view = getView();
        LinearLayout review_layout = (LinearLayout) view.findViewById(R.id.list_reviews);
        for (int i = 0; i < movieDbItem.reviews.size(); i++)
            addReview(review_layout, movieDbItem.reviews.get(i));
    }

    public void notifyVideoDataChanged() {
        View view = getView();
        LinearLayout video_layout = (LinearLayout) view.findViewById(R.id.list_videos);
        for (int i = 0; i < movieDbItem.videos.size(); i++)
            addVideo(video_layout, movieDbItem.videos.get(i));

    }

    private Uri addToFavorites() {
        ContentValues values = new ContentValues();
        values.put(MovieDbContract.MovieDbEntry.COLUMN_BACKDROP_PATH, movieDbItem.backdropPath);
        values.put(MovieDbContract.MovieDbEntry.COLUMN_FORMAT, movieDbItem.format);
        values.put(MovieDbContract.MovieDbEntry.COLUMN_ID, movieDbItem.id);
        values.put(MovieDbContract.MovieDbEntry.COLUMN_OVERVIEW, movieDbItem.overview);
        values.put(MovieDbContract.MovieDbEntry.COLUMN_POPULARITY, movieDbItem.popularity);
        values.put(MovieDbContract.MovieDbEntry.COLUMN_POSTER_PATH, movieDbItem.posterPath);
        values.put(MovieDbContract.MovieDbEntry.COLUMN_RELEASE_DATE, movieDbItem.releaseDate);
        values.put(MovieDbContract.MovieDbEntry.COLUMN_TITLE, movieDbItem.title);
        values.put(MovieDbContract.MovieDbEntry.COLUMN_VOTE_AVERAGE, movieDbItem.voteAverage);
        values.put(MovieDbContract.MovieDbEntry.COLUMN_VOTE_COUNT, movieDbItem.voteCount);

        return getActivity().getContentResolver().insert(MovieDbContract.MovieDbEntry.CONTENT_URI, values);
    }

    private void addVideosToFavorites() {
        for (int i = 0; i < movieDbItem.videos.size(); i++) {
            MovieDbItemVideo video = movieDbItem.videos.get(i);
            ContentValues values = new ContentValues();
            values.put(MovieDbContract.MovieDbVideoEntry.COLUMN_TYPE, video.type);
            values.put(MovieDbContract.MovieDbVideoEntry.COLUMN_SITE, video.site);
            values.put(MovieDbContract.MovieDbVideoEntry.COLUMN_NAME, video.name);
            values.put(MovieDbContract.MovieDbVideoEntry.COLUMN_KEY, video.key);
            values.put(MovieDbContract.MovieDbVideoEntry.COLUMN_ID, video.id);
            values.put(MovieDbContract.MovieDbVideoEntry.COLUMN_MOVIE_FORMAT, movieDbItem.format);
            values.put(MovieDbContract.MovieDbVideoEntry.COLUMN_MOVIE_ID, movieDbItem.id);
            getActivity().getContentResolver().insert(MovieDbContract.MovieDbVideoEntry.CONTENT_URI, values);
        }
    }

    private void addReviewsToFavorites() {
        for (int i = 0; i < movieDbItem.reviews.size(); i++) {
            MovieDbItemReview review = movieDbItem.reviews.get(i);
            ContentValues values = new ContentValues();
            values.put(MovieDbContract.MovieDbReviewEntry.COLUMN_URL, review.url);
            values.put(MovieDbContract.MovieDbReviewEntry.COLUMN_CONTENT, review.content);
            values.put(MovieDbContract.MovieDbReviewEntry.COLUMN_AUTHOR, review.author);
            values.put(MovieDbContract.MovieDbReviewEntry.COLUMN_ID, review.id);
            values.put(MovieDbContract.MovieDbReviewEntry.COLUMN_MOVIE_FORMAT, movieDbItem.format);
            values.put(MovieDbContract.MovieDbReviewEntry.COLUMN_MOVIE_ID, movieDbItem.id);
            getActivity().getContentResolver().insert(MovieDbContract.MovieDbReviewEntry.CONTENT_URI, values);
        }
    }

    private int removeFromFavorites() {
        return getActivity().getContentResolver().delete(MovieDbContract.MovieDbEntry.CONTENT_URI, "format = ? AND id = ?", new String[] { movieDbItem.format, String.valueOf(movieDbItem.id) });

    }

    private int removeVideosFromFavorites() {
        return getActivity().getContentResolver().delete(MovieDbContract.MovieDbVideoEntry.CONTENT_URI, "movie_format = ? AND movie_id = ?", new String[] { movieDbItem.format, String.valueOf(movieDbItem.id) });

    }

    private int removeReviewsFromFavorites() {
        return getActivity().getContentResolver().delete(MovieDbContract.MovieDbReviewEntry.CONTENT_URI, "movie_format = ? AND movie_id = ?", new String[] { movieDbItem.format, String.valueOf(movieDbItem.id) });

    }

    protected void addVideo(LinearLayout layout, final MovieDbItemVideo video) {

        if (!video.site.equals("YouTube")) // only show YouTube hosted trailers
            return;

        LinearLayout videoLayout = new LinearLayout(getActivity());
        videoLayout.setOrientation(LinearLayout.HORIZONTAL);
        videoLayout.setGravity(Gravity.CENTER_VERTICAL);

        ImageView icon = new ImageView(getActivity());
        icon.setImageResource(R.mipmap.btn_play_youtube);
        videoLayout.addView(icon);

        TextView text = new TextView(getActivity());
        text.setText(video.name);
        text.setTextAppearance(getActivity(), R.style.BlackText);
        videoLayout.addView(text);

        videoLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Uri videoUri = Uri.parse(YOUTUBE_BASE_URL).buildUpon()
                        .appendQueryParameter("v", video.key).build();
                Intent videoIntent = new Intent(Intent.ACTION_VIEW, videoUri);
                if (videoIntent.resolveActivity(getActivity().getPackageManager()) != null)
                    getActivity().startActivity(videoIntent);
            }
        });
        layout.addView(videoLayout);
    }

    protected void addReview(LinearLayout layout, final MovieDbItemReview review) {
        LinearLayout reviewLayout = new LinearLayout(getActivity());
        reviewLayout.setOrientation(LinearLayout.VERTICAL);
        reviewLayout.setPadding(0, 40, 0, 0);

        TextView txtContent = new TextView(getActivity());
        txtContent.setText(review.content);
        txtContent.setTextAppearance(getActivity(), R.style.BlackText_Italic);
        reviewLayout.addView(txtContent);

        TextView txtAuthor = new TextView(getActivity());
        txtAuthor.setTextAppearance(getActivity(), R.style.BlackText);
        txtAuthor.setText(review.author);
        txtAuthor.setGravity(Gravity.END);
        reviewLayout.addView(txtAuthor);

        txtContent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(review.url));
                if (webIntent.resolveActivity(getActivity().getPackageManager()) != null)
                    getActivity().startActivity(webIntent);
            }
        });
        layout.addView(reviewLayout);
    }

    public abstract void loadReviews();
    public abstract void loadVideos();
}
