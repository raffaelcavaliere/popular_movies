package com.raffaelcavaliere.popularmovies.data;

/**
 * Created by raffaelcavaliere on 2015-10-06.
 */
import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class MovieDbContract {

    public static final String CONTENT_AUTHORITY = "com.raffaelcavaliere.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIE = "movie";
    public static final String PATH_FILM = "film";
    public static final String PATH_TV = "tv";
    public static final String PATH_VIDEO = "video";
    public static final String PATH_REVIEW = "review";


    public static final class MovieDbVideoEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_VIDEO).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VIDEO;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VIDEO;

        public static final String TABLE_NAME = "video";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_FORMAT = "movie_format";
        public static final String COLUMN_KEY = "key";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_SITE = "site";
        public static final String COLUMN_TYPE = "type";

        public static Uri buildMovieDbVideoFilmUri(long id) {
            return CONTENT_URI.buildUpon().appendPath(PATH_FILM).appendPath(Long.toString(id)).build();
        }

        public static Uri buildMovieDbVideoTvUri(long id) {
            return CONTENT_URI.buildUpon().appendPath(PATH_TV).appendPath(Long.toString(id)).build();
        }
    }


    public static final class MovieDbReviewEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEW).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;

        public static final String TABLE_NAME = "review";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_FORMAT = "movie_format";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_URL = "url";

        public static Uri buildMovieDbReviewFilmUri(long id) {
            return CONTENT_URI.buildUpon().appendPath(PATH_FILM).appendPath(Long.toString(id)).build();
        }

        public static Uri buildMovieDbReviewTvUri(long id) {
            return CONTENT_URI.buildUpon().appendPath(PATH_TV).appendPath(Long.toString(id)).build();
        }
    }


    public static final class MovieDbEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_VOTE_COUNT = "vote_count";
        public static final String COLUMN_FORMAT = "format";

        public static Uri buildMovieDbFilmUri(long id) {
            return CONTENT_URI.buildUpon().appendPath(PATH_FILM).appendPath(Long.toString(id)).build();
        }

        public static Uri buildMovieDbTvUri(long id) {
            return CONTENT_URI.buildUpon().appendPath(PATH_TV).appendPath(Long.toString(id)).build();
        }
    }
}
