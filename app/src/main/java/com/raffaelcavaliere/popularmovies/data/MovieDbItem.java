package com.raffaelcavaliere.popularmovies.data;

import java.util.ArrayList;

/**
 * Created by raffaelcavaliere on 2015-09-27.
 */
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
}
