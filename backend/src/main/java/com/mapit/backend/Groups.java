package com.mapit.backend;

/**
 * Created by shubhashis on 1/27/2015.
 */
public class Groups {
    private String CreatorMail;
    private String GroupName;
    private String latitude;
    private String longitude;

    public String getCreatorMail() {
        return CreatorMail;
    }

    public void setCreatorMail(String creatorMail) {
        CreatorMail = creatorMail;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
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
}
