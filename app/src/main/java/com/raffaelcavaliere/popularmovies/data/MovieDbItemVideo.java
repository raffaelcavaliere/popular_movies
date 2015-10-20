package com.raffaelcavaliere.popularmovies.data;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by raffaelcavaliere on 2015-09-27.
 */

public class MovieDbItemVideo implements Parcelable {
    public String key, name, id, site, type;

    public MovieDbItemVideo(String _key, String _name, String _id, String _site, String _type) {
        this.key = _key;
        this.name = _name;
        this.id = _id;
        this.site = _site;
        this.type = _type;
    }

    private MovieDbItemVideo(Parcel in) {
        Bundle bundle = in.readBundle();
        this.id = bundle.getString("id");
        this.name = bundle.getString("name");
        this.key = bundle.getString("key");
        this.site = bundle.getString("site");
        this.type = bundle.getString("type");
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("name", name);
        bundle.putString("key", key);
        bundle.putString("site", site);
        bundle.putString("type", type);
        out.writeBundle(bundle);
    }

    public static final Parcelable.Creator<MovieDbItemVideo> CREATOR = new Parcelable.Creator<MovieDbItemVideo>() {
        public MovieDbItemVideo createFromParcel(Parcel in) {
            return new MovieDbItemVideo(in);
        }

        public MovieDbItemVideo[] newArray(int size) {
            return new MovieDbItemVideo[size];
        }
    };
}
