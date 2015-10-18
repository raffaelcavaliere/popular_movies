package com.raffaelcavaliere.popularmovies.data;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

/**
 * Created by raffaelcavaliere on 2015-10-17.
 */
public class MovieDbContentObserver extends ContentObserver {

    public MovieDbContentObserver(Handler handler) {
        super(handler);
    }

    @Override
    public void onChange(boolean selfChange) {
        this.onChange(selfChange, null);
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        Log.i("CHANGE", Boolean.toString(selfChange) + " " + uri.toString());
    }
}