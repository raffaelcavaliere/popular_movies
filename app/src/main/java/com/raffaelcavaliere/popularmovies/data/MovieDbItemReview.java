package com.raffaelcavaliere.popularmovies.data;

/**
 * Created by raffaelcavaliere on 2015-09-27.
 */

public class MovieDbItemReview {
    public String id, content, author, url;

    public MovieDbItemReview(String _id, String _content, String _author, String _url) {
        this.id = _id;
        this.content = _content;
        this.author = _author;
        this.url = _url;
    }
}
