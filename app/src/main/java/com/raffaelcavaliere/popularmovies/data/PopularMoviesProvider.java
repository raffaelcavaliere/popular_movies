package com.raffaelcavaliere.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class PopularMoviesProvider extends ContentProvider {

    private final static UriMatcher uriMatcher = buildUriMatcher();
    private MovieDbSQLiteHelper movieDbHelper;

    static final int MOVIE = 100;
    static final int MOVIE_BY_MOVIE_KEY = 101;
    static final int TV = 200;
    static final int TV_BY_ID = 201;
    static final int FILM = 300;
    static final int FILM_BY_ID = 301;
    static final int VIDEO = 400;
    static final int FILM_VIDEO_BY_MOVIE_KEY = 401;
    static final int TV_VIDEO_BY_MOVIE_KEY = 402;
    static final int REVIEW = 500;
    static final int FILM_REVIEW_BY_MOVIE_KEY = 501;
    static final int TV_REVIEW_BY_MOVIE_KEY = 502;

    public PopularMoviesProvider() {

    }

    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieDbContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieDbContract.PATH_MOVIE, MOVIE);
        matcher.addURI(authority, MovieDbContract.PATH_MOVIE + "/#", MOVIE_BY_MOVIE_KEY);
        matcher.addURI(authority, MovieDbContract.PATH_MOVIE + "/" + MovieDbContract.PATH_TV, TV);
        matcher.addURI(authority, MovieDbContract.PATH_MOVIE + "/" + MovieDbContract.PATH_TV + "/#", TV_BY_ID);
        matcher.addURI(authority, MovieDbContract.PATH_MOVIE + "/" + MovieDbContract.PATH_FILM, FILM);
        matcher.addURI(authority, MovieDbContract.PATH_MOVIE + "/" + MovieDbContract.PATH_FILM + "/#", FILM_BY_ID);
        matcher.addURI(authority, MovieDbContract.PATH_VIDEO, VIDEO);
        matcher.addURI(authority, MovieDbContract.PATH_REVIEW, REVIEW);
        matcher.addURI(authority, MovieDbContract.PATH_VIDEO + "/" + MovieDbContract.PATH_FILM + "/#", FILM_VIDEO_BY_MOVIE_KEY);
        matcher.addURI(authority, MovieDbContract.PATH_VIDEO + "/" + MovieDbContract.PATH_TV + "/#", TV_VIDEO_BY_MOVIE_KEY);
        matcher.addURI(authority, MovieDbContract.PATH_REVIEW + "/" + MovieDbContract.PATH_FILM + "/#", FILM_REVIEW_BY_MOVIE_KEY);
        matcher.addURI(authority, MovieDbContract.PATH_REVIEW + "/" + MovieDbContract.PATH_TV + "/#", TV_REVIEW_BY_MOVIE_KEY);

        return matcher;
    }


    @Override
    public boolean onCreate() {
        movieDbHelper = new MovieDbSQLiteHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case MOVIE:
                return MovieDbContract.MovieDbEntry.CONTENT_TYPE;
            case MOVIE_BY_MOVIE_KEY:
                return MovieDbContract.MovieDbEntry.CONTENT_ITEM_TYPE;
            case TV:
                return MovieDbContract.MovieDbEntry.CONTENT_TYPE;
            case TV_BY_ID:
                return MovieDbContract.MovieDbEntry.CONTENT_ITEM_TYPE;
            case FILM:
                return MovieDbContract.MovieDbEntry.CONTENT_TYPE;
            case FILM_BY_ID:
                return MovieDbContract.MovieDbEntry.CONTENT_ITEM_TYPE;
            case VIDEO:
                return MovieDbContract.MovieDbVideoEntry.CONTENT_TYPE;
            case REVIEW:
                return MovieDbContract.MovieDbReviewEntry.CONTENT_TYPE;
            case FILM_VIDEO_BY_MOVIE_KEY:
                return MovieDbContract.MovieDbVideoEntry.CONTENT_TYPE;
            case TV_VIDEO_BY_MOVIE_KEY:
                return MovieDbContract.MovieDbVideoEntry.CONTENT_TYPE;
            case FILM_REVIEW_BY_MOVIE_KEY:
                return MovieDbContract.MovieDbReviewEntry.CONTENT_TYPE;
            case TV_REVIEW_BY_MOVIE_KEY:
                return MovieDbContract.MovieDbReviewEntry.CONTENT_TYPE;
            default:
                return null;
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = movieDbHelper.getWritableDatabase();
        int result;

        switch (uriMatcher.match(uri)) {
            case MOVIE: {
                result = db.delete(MovieDbContract.MovieDbEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case REVIEW: {
                result = db.delete(MovieDbContract.MovieDbReviewEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case VIDEO: {
                result = db.delete(MovieDbContract.MovieDbVideoEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }

        getContext().getContentResolver().notifyChange(uri, null);
        db.close();
        return result;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = movieDbHelper.getWritableDatabase();
        Uri resultUri;

        switch (uriMatcher.match(uri)) {
            case MOVIE: {
                long id = db.insert(MovieDbContract.MovieDbEntry.TABLE_NAME, null, values);
                if (id > 0)
                    resultUri = ContentUris.withAppendedId(uri, id);
                else
                    throw new SQLException("Problem while inserting into uri: " + uri);
                break;
            }
            case REVIEW: {
                long id = db.insert(MovieDbContract.MovieDbReviewEntry.TABLE_NAME, null, values);
                if (id > 0)
                    resultUri =  ContentUris.withAppendedId(uri, id);
                else
                    throw new SQLException("Problem while inserting into uri: " + uri);
                break;
            }
            case VIDEO: {
                long id = db.insert(MovieDbContract.MovieDbVideoEntry.TABLE_NAME, null, values);
                if (id > 0)
                    resultUri = ContentUris.withAppendedId(uri, id);
                else
                    throw new SQLException("Problem while inserting into uri: " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }

        getContext().getContentResolver().notifyChange(uri, null);
        db.close();
        return resultUri;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = movieDbHelper.getReadableDatabase();
        Cursor resultCursor;

        switch (uriMatcher.match(uri)) {
            case MOVIE: {
                resultCursor = db.query(MovieDbContract.MovieDbEntry.TABLE_NAME, null, null, null, null, null, null);
                break;
            }
            case FILM_BY_ID: {
                String id = uri.getPathSegments().get(2);
                resultCursor = db.query(MovieDbContract.MovieDbEntry.TABLE_NAME, null, "format = 'movie' AND id = ?", new String[] { id }, null, null, null);
                break;
            }
            case TV_BY_ID: {
                String id = uri.getPathSegments().get(2);
                resultCursor = db.query(MovieDbContract.MovieDbEntry.TABLE_NAME, null, "format = 'tv' AND id = ?", new String[] { id }, null, null, null);
                break;
            }
            case VIDEO: {
                resultCursor = db.query(MovieDbContract.MovieDbVideoEntry.TABLE_NAME, null, null, null, null, null, null);
                break;
            }
            case REVIEW: {
                resultCursor = db.query(MovieDbContract.MovieDbReviewEntry.TABLE_NAME, null, null, null, null, null, null);
                break;
            }
            case FILM_REVIEW_BY_MOVIE_KEY: {
                String id = uri.getPathSegments().get(2);
                resultCursor = db.query(MovieDbContract.MovieDbReviewEntry.TABLE_NAME, null, "movie_format = 'movie' AND movie_id = ?", new String[] { id }, null, null, null);
                break;
            }
            case FILM_VIDEO_BY_MOVIE_KEY: {
                String id = uri.getPathSegments().get(2);
                resultCursor = db.query(MovieDbContract.MovieDbVideoEntry.TABLE_NAME, null, "movie_format = 'movie' AND movie_id = ?", new String[] { id }, null, null, null);
                break;
            }
            case TV_REVIEW_BY_MOVIE_KEY: {
                String id = uri.getPathSegments().get(2);
                resultCursor = db.query(MovieDbContract.MovieDbReviewEntry.TABLE_NAME, null, "movie_format = 'tv' AND movie_id = ?", new String[] { id }, null, null, null);
                break;
            }
            case TV_VIDEO_BY_MOVIE_KEY: {
                String id = uri.getPathSegments().get(2);
                resultCursor = db.query(MovieDbContract.MovieDbVideoEntry.TABLE_NAME, null, "movie_format = 'tv' AND movie_id = ?", new String[] { id }, null, null, null);
                break;
            }
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }

        return resultCursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
