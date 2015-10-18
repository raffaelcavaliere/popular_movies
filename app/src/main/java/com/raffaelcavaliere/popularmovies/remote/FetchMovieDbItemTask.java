package com.raffaelcavaliere.popularmovies.remote;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.raffaelcavaliere.popularmovies.data.MovieDbItemReview;
import com.raffaelcavaliere.popularmovies.data.MovieDbItemVideo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by raffaelcavaliere on 2015-09-27.
 */
public class FetchMovieDbItemTask extends AsyncTask<FetchMovieDbItemTask.FetchMovieDbItemTaskParams,Void,Object[]> {

    public final static String MOVIE_DB_ITEM_MOVIE = "movie";
    public final static String MOVIE_DB_ITEM_TV = "tv";
    public final static String MOVIE_DB_ITEM_REVIEWS = "reviews";
    public final static String MOVIE_DB_ITEM_VIDEOS = "videos";

    public class FetchMovieDbItemTaskParams {
        public String format = MOVIE_DB_ITEM_MOVIE;
        public String action = MOVIE_DB_ITEM_REVIEWS;
        public long id;

        public FetchMovieDbItemTaskParams(long _id, String _format, String _action) {
            this.id = _id;
            this.format = _format;
            this.action = _action;
        }
    }

    private MovieDbItemVideo[] getMovieDbItemVideosFromJson(String movieDbJsonString)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String MDB_RESULTS = "results";
        final String MDB_KEY = "key";
        final String MDB_NAME = "name";
        final String MDB_ID = "id";
        final String MDB_SITE = "site";
        final String MDB_TYPE = "type";

        JSONObject movieDbItemJson = new JSONObject(movieDbJsonString);
        JSONArray items = movieDbItemJson.getJSONArray(MDB_RESULTS);

        MovieDbItemVideo[] results = new MovieDbItemVideo[items.length()];

        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            String key = item.getString(MDB_KEY);
            String name = item.getString(MDB_NAME);
            String id = item.getString(MDB_ID);
            String site = item.getString(MDB_SITE);
            String type = item.getString(MDB_TYPE);
            results[i] = new MovieDbItemVideo(key, name, id, site, type);
        }

        return results;
    }

    private MovieDbItemReview[] getMovieDbItemReviewsFromJson(String movieDbJsonString)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String MDB_RESULTS = "results";
        final String MDB_ID = "id";
        final String MDB_CONTENT = "content";
        final String MDB_AUTHOR = "author";
        final String MDB_URL = "url";

        JSONObject movieDbItemJson = new JSONObject(movieDbJsonString);
        JSONArray items = movieDbItemJson.getJSONArray(MDB_RESULTS);

        MovieDbItemReview[] results = new MovieDbItemReview[items.length()];

        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            String id = item.getString(MDB_ID);
            String content = item.getString(MDB_CONTENT);
            String author = item.getString(MDB_AUTHOR);
            String url = item.getString(MDB_URL);
            results[i] = new MovieDbItemReview(id, content, author, url);
        }

        return results;
    }

    protected Object[] doInBackground(FetchMovieDbItemTaskParams... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String movieDbJsonString = null;

        if (params.length <= 0)
            return null;

        try {
            Uri uri = Uri.parse(FetchMovieDbTask.MOVIE_DB_BASE_URL).buildUpon()
                    .appendPath(params[0].format)
                    .appendPath(String.valueOf(params[0].id))
                    .appendPath(params[0].action)
                    .appendQueryParameter("api_key", FetchMovieDbTask.MOVIE_DB_API_KEY).build();
            URL url = new URL(uri.toString());
            Log.i("URL", uri.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            movieDbJsonString = buffer.toString();
        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }

        Object[] result = null;
        try {
            Log.i("Popular Movies App", movieDbJsonString);
            result = params[0].action.equals(MOVIE_DB_ITEM_VIDEOS) ? getMovieDbItemVideosFromJson(movieDbJsonString) : getMovieDbItemReviewsFromJson(movieDbJsonString);
        } catch (JSONException ex) {
            Log.e("Popular Movies App", ex.getMessage());
        }

        return result;
    }
}
