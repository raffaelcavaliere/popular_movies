package com.raffaelcavaliere.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

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
 * Created by raffaelcavaliere on 2015-09-23.
 */
public class FetchMovieDbTask extends AsyncTask<FetchMovieDbTask.FetchMovieDbTaskParams,Void,FetchMovieDbTask.MovieDbItem[]> {

    final String MOVIE_DB_BASE_URL = "http://api.themoviedb.org/3/";
    final String MOVIE_DB_API_KEY = "3086d73199a86d247ea72fdfdf26aac1";

    public final static String MOVIE_DB_SEARCH = "search";
    public final static String MOVIE_DB_FIND = "find";
    public final static String MOVIE_DB_DISCOVER = "discover";
    public final static String MOVIE_DB_MOVIE = "movie";
    public final static String MOVIE_DB_TV = "tv";
    public final static String MOVIE_DB_POPULARITY_SORT = "popularity.desc";
    public final static String MOVIE_DB_RATING_SORT = "vote_average.desc";

    public class FetchMovieDbTaskParams {
        public String type = MOVIE_DB_DISCOVER;
        public String format = MOVIE_DB_MOVIE;
        public String sort = MOVIE_DB_POPULARITY_SORT;

        public FetchMovieDbTaskParams(String _type, String _format, String _sort) {
            this.type = _type;
            this.format = _format;
            this.sort = _sort;
        }
    }

    public class MovieDbItem {
        public String backdropPath;
        public long id;
        public String title;
        public String overview;
        public String releaseDate;
        public String posterPath;
        public double popularity;
        public double voteAverage;
        public long voteCount;

        public MovieDbItem (String _backdropPath, long _id, String _title, String _overview, String _releaseDate, String _posterPath, double _popularity, double _voteAverage, long _voteCount) {
            this.backdropPath = _backdropPath;
            this.id = _id;
            this.title = _title;
            this.overview = _overview;
            this.releaseDate = _releaseDate;
            this.posterPath = _posterPath;
            this.popularity = _popularity;
            this.voteAverage = _voteAverage;
            this.voteCount = _voteCount;
        }
    }

    private MovieDbItem[] getMovieDbDataFromJson(String movieDbJsonString, String format)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String MDB_RESULTS = "results";
        final String MDB_BACKDROP_PATH = "backdrop_path";
        final String MDB_ID = "id";
        final String MDB_TITLE = format.equals(MOVIE_DB_TV) ? "name" : "title";
        final String MDB_OVERVIEW = "overview";
        final String MDB_RELEASE_DATE = format.equals(MOVIE_DB_TV) ? "first_air_date" : "release_date";
        final String MDB_POSTER_PATH = "poster_path";
        final String MDB_POPULARITY = "popularity";
        final String MDB_VOTE_AVERAGE = "vote_average";
        final String MDB_VOTE_COUNT = "vote_count";

        JSONObject movieDbJson = new JSONObject(movieDbJsonString);
        JSONArray items = movieDbJson.getJSONArray(MDB_RESULTS);

        MovieDbItem[] movieDbData = new MovieDbItem[items.length()];

        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            String backdropPath = item.getString(MDB_BACKDROP_PATH);
            long id = item.getLong(MDB_ID);
            String title = item.getString(MDB_TITLE);
            String overview = item.getString(MDB_OVERVIEW);
            String releaseDate = item.getString(MDB_RELEASE_DATE);
            String posterPath = item.getString(MDB_POSTER_PATH);
            double popularity = item.getDouble(MDB_POPULARITY);
            double voteAverage = item.getDouble(MDB_VOTE_AVERAGE);
            long voteCount = item.getLong(MDB_VOTE_COUNT);
            movieDbData[i] = new MovieDbItem(backdropPath, id, title, overview, releaseDate, posterPath, popularity, voteAverage, voteCount);
        }

        return movieDbData;
    }

    protected MovieDbItem[] doInBackground(FetchMovieDbTaskParams... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String movieDbJsonString = null;

        if (params.length <= 0)
            return null;

        try {
            Uri uri = Uri.parse(MOVIE_DB_BASE_URL).buildUpon()
                    .appendPath(params[0].type)
                    .appendPath(params[0].format)
                    .appendQueryParameter("sort_by", params[0].sort)
                    .appendQueryParameter("vote_count.gte", "50") //this is to eliminate high-rated films with very few votes
                    .appendQueryParameter("api_key", MOVIE_DB_API_KEY).build();
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
        } finally{
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

        MovieDbItem[] result = null;
        try {
            Log.i("Popular Movies App", movieDbJsonString);
            result = getMovieDbDataFromJson(movieDbJsonString, params[0].format);
        } catch (JSONException ex) {
            Log.e("Popular Movies App", ex.getMessage());
        }

        return result;
    }

}
