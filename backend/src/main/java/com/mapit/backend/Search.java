package com.mapit.backend;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * Created by Debashis7 on 1/23/2015.
 */
public class Search {
    private String data;
    private String key;
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getKey() {
        return key;
    }

    public void setKey(Key key) {

        String keyToString = KeyFactory.keyToString(key);
        this.key = keyToString;
    }
}
