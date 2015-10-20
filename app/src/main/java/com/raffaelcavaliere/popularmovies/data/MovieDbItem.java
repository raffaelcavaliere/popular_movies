package com.raffaelcavaliere.popularmovies.data;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PersistableBundle;

import java.util.ArrayList;

/**
 * Created by raffaelcavaliere on 2015-09-27.
 */
public class MovieDbItem implements Parcelable {
    public String backdropPath;
    public long id;
    public String title;
    public String overview;
    public String releaseDate;
    public String posterPath;
    public double popularity;
    public double voteAverage;
    public long voteCount;
    public ArrayList<MovieDbItemVideo> videos;
    public ArrayList<MovieDbItemReview> reviews;
    public String format;
    public boolean isFavorite = false;

    public MovieDbItem (String _format, String _backdropPath, long _id, String _title, String _overview, String _releaseDate, String _posterPath, double _popularity, double _voteAverage, long _voteCount) {
        this.backdropPath = _backdropPath;
        this.id = _id;
        this.title = _title;
        this.overview = _overview;
        this.releaseDate = _releaseDate;
        this.posterPath = _posterPath;
        this.popularity = _popularity;
        this.voteAverage = _voteAverage;
        this.voteCount = _voteCount;
        this.format = _format;
        this.videos = new ArrayList<>();
        this.reviews = new ArrayList<>();
    }

    private MovieDbItem(Parcel in) {
        Bundle bundle = in.readBundle();
        this.backdropPath = bundle.getString("backdropPath");
        this.id = bundle.getLong("id");
        this.title = bundle.getString("title");
        this.overview = bundle.getString("overview");
        this.releaseDate = bundle.getString("releaseDate");
        this.posterPath = bundle.getString("posterPath");
        this.popularity = bundle.getDouble("popularity");
        this.voteAverage = bundle.getDouble("voteAverage");
        this.voteCount = bundle.getLong("voteCount");
        this.format = bundle.getString("format");
        this.videos = new ArrayList<>();
        this.reviews = new ArrayList<>();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        Bundle bundle = new Bundle();
        bundle.putString("backdropPath", backdropPath);
        bundle.putLong("id", id);
        bundle.putString("title", title);
        bundle.putString("overview", overview);
        bundle.putString("releaseDate", releaseDate);
        bundle.putString("posterPath", posterPath);
        bundle.putDouble("popularity", popularity);
        bundle.putDouble("voteAverage", voteAverage);
        bundle.putLong("voteCount", voteCount);
        bundle.putString("format", format);
        out.writeBundle(bundle);
    }

    public static final Parcelable.Creator<MovieDbItem> CREATOR = new Parcelable.Creator<MovieDbItem>() {
        public MovieDbItem createFromParcel(Parcel in) {
            return new MovieDbItem(in);
        }

        public MovieDbItem[] newArray(int size) {
            return new MovieDbItem[size];
        }
    };
}
