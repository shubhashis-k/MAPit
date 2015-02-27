package com.mapit.backend;

/**
 * Created by shubhashis on 1/27/2015.
 */
public class Groups {
    private String CreatorMail;
    private String GroupName;
    private String GroupDescription;
    private String GroupPic;
    private String latitude, longitude, location;
    private String permission;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getGroupPic() {
        return GroupPic;
    }

    public void setGroupPic(String groupPic) {
        GroupPic = groupPic;
    }

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

    public String getGroupDescription() {
        return GroupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        GroupDescription = groupDescription;
    }
}
