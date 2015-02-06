package com.mapit.backend;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * Created by shubhashis on 1/23/2015.
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

    @Override
    public boolean equals(Object object){
        if(object instanceof Search){
            Search compare = (Search) object;

            if(compare.getData().equals(this.getData()) && compare.getKey().equals(this.getKey()))
                return true;
            return false;
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return 1107001;
    }
}
