package com.raffaelcavaliere.popularmovies.data;

/**
 * Created by raffaelcavaliere on 2015-09-27.
 */

public class MovieDbItemVideo {
    public String key, name, id, site, type;

    public MovieDbItemVideo(String _key, String _name, String _id, String _site, String _type) {
        this.key = _key;
        this.name = _name;
        this.id = _id;
        this.site = _site;
        this.type = _type;
    }
}
