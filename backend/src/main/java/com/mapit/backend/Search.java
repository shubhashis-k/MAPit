package com.mapit.backend;

import com.google.appengine.api.datastore.Key;

/**
 * Created by Debashis7 on 1/23/2015.
 */
public class Search {
    private String data;
    private Key key;
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }
}
