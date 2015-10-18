package com.raffaelcavaliere.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.raffaelcavaliere.popularmovies.data.MovieDbContract;

/**
 * Created by raffaelcavaliere on 2015-10-06.
 */
public class MovieDbSQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 11;
    static final String DATABASE_NAME = "popularmovies.db";

    public MovieDbSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieDbContract.MovieDbEntry.TABLE_NAME + " (" +
                MovieDbContract.MovieDbEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL, " +
                MovieDbContract.MovieDbEntry.COLUMN_FORMAT + " TEXT NOT NULL, " +
                MovieDbContract.MovieDbEntry.COLUMN_ID + " INTEGER NOT NULL, " +
                MovieDbContract.MovieDbEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MovieDbContract.MovieDbEntry.COLUMN_POPULARITY + " REAL NOT NULL, " +
                MovieDbContract.MovieDbEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                MovieDbContract.MovieDbEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieDbContract.MovieDbEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieDbContract.MovieDbEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, " +
                MovieDbContract.MovieDbEntry.COLUMN_VOTE_COUNT + " INTEGER NOT NULL, " +
                " PRIMARY KEY (" + MovieDbContract.MovieDbEntry.COLUMN_ID + ", " + MovieDbContract.MovieDbEntry.COLUMN_FORMAT + ")" +
                ");";

        final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE " + MovieDbContract.MovieDbReviewEntry.TABLE_NAME + " (" +
                MovieDbContract.MovieDbReviewEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                MovieDbContract.MovieDbReviewEntry.COLUMN_MOVIE_FORMAT + " TEXT NOT NULL, " +
                MovieDbContract.MovieDbReviewEntry.COLUMN_AUTHOR + " TEXT NOT NULL, " +
                MovieDbContract.MovieDbReviewEntry.COLUMN_CONTENT + " TEXT NOT NULL, " +
                MovieDbContract.MovieDbReviewEntry.COLUMN_ID + " TEXT NOT NULL, " +
                MovieDbContract.MovieDbReviewEntry.COLUMN_URL + " TEXT NOT NULL, " +
                " PRIMARY KEY (" + MovieDbContract.MovieDbReviewEntry.COLUMN_ID + ", " + MovieDbContract.MovieDbReviewEntry.COLUMN_MOVIE_ID + ", " + MovieDbContract.MovieDbReviewEntry.COLUMN_MOVIE_FORMAT + "), " +
                " FOREIGN KEY (" + MovieDbContract.MovieDbReviewEntry.COLUMN_MOVIE_ID + ", " + MovieDbContract.MovieDbReviewEntry.COLUMN_MOVIE_FORMAT + ") REFERENCES " +
                MovieDbContract.MovieDbEntry.TABLE_NAME + " (" + MovieDbContract.MovieDbEntry.COLUMN_ID + ", " + MovieDbContract.MovieDbEntry.COLUMN_FORMAT + ")" +
                ");";

        final String SQL_CREATE_VIDEO_TABLE = "CREATE TABLE " + MovieDbContract.MovieDbVideoEntry.TABLE_NAME + " (" +
                MovieDbContract.MovieDbVideoEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL," +
                MovieDbContract.MovieDbVideoEntry.COLUMN_MOVIE_FORMAT + " TEXT NOT NULL, " +
                MovieDbContract.MovieDbVideoEntry.COLUMN_ID + " TEXT NOT NULL, " +
                MovieDbContract.MovieDbVideoEntry.COLUMN_KEY + " TEXT NOT NULL, " +
                MovieDbContract.MovieDbVideoEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                MovieDbContract.MovieDbVideoEntry.COLUMN_SITE + " TEXT NOT NULL, " +
                MovieDbContract.MovieDbVideoEntry.COLUMN_TYPE + " TEXT NOT NULL, " +
                " PRIMARY KEY (" + MovieDbContract.MovieDbVideoEntry.COLUMN_ID + ", " + MovieDbContract.MovieDbVideoEntry.COLUMN_MOVIE_ID + ", " + MovieDbContract.MovieDbVideoEntry.COLUMN_MOVIE_FORMAT + "), " +
                " FOREIGN KEY (" + MovieDbContract.MovieDbVideoEntry.COLUMN_MOVIE_ID + ", " + MovieDbContract.MovieDbVideoEntry.COLUMN_MOVIE_FORMAT + ") REFERENCES " +
                MovieDbContract.MovieDbEntry.TABLE_NAME + " (" + MovieDbContract.MovieDbEntry.COLUMN_ID + ", " + MovieDbContract.MovieDbEntry.COLUMN_FORMAT + ")" +
                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_REVIEW_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_VIDEO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieDbContract.MovieDbVideoEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieDbContract.MovieDbReviewEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieDbContract.MovieDbEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
