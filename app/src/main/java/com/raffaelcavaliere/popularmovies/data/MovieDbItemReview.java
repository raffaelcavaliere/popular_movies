package com.raffaelcavaliere.popularmovies.data;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by raffaelcavaliere on 2015-09-27.
 */

public class MovieDbItemReview implements Parcelable {
    public String id, content, author, url;

    public MovieDbItemReview(String _id, String _content, String _author, String _url) {
        this.id = _id;
        this.content = _content;
        this.author = _author;
        this.url = _url;
    }

    private MovieDbItemReview(Parcel in) {
        Bundle bundle = in.readBundle();
        this.id = bundle.getString("id");
        this.content = bundle.getString("title");
        this.author = bundle.getString("overview");
        this.url = bundle.getString("releaseDate");
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("content", content);
        bundle.putString("author", author);
        bundle.putString("url", url);
        out.writeBundle(bundle);
    }

    public static final Parcelable.Creator<MovieDbItemReview> CREATOR = new Parcelable.Creator<MovieDbItemReview>() {
        public MovieDbItemReview createFromParcel(Parcel in) {
            return new MovieDbItemReview(in);
        }

        public MovieDbItemReview[] newArray(int size) {
            return new MovieDbItemReview[size];
        }
    };
}
