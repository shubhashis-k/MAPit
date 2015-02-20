package com.mapit.backend;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import java.util.Date;

/**
 * Created by shubhashis on 1/29/2015.
 */
public class StatusData {
    private String groupKey, status, personMail, latitude, longitude, kind, statusKey;
    private String personName;
    private String statusPhoto;
    private String profilePic;
    private Date publishDate;

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getStatusPhoto() {
        return statusPhoto;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public void setStatusPhoto(String statusPhoto) {
        this.statusPhoto = statusPhoto;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPersonMail() {
        return personMail;
    }

    public void setPersonMail(String personMail) {
        this.personMail = personMail;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getStatusKey() {
        return statusKey;
    }

    public void setStatusKey(Key key) {
        String keyToString = KeyFactory.keyToString(key);
        this.statusKey = keyToString;
    }
}
