package com.mapit.backend;

/**
 * Created by shubhashis on 2/26/2015.
 */
public class Information {
    private String KindName;
    private String InfoName;
    private String InfoDescription;
    private String InformationPic;
    private String DetailInfopic;
    private String latitude;
    private String longitude;
    private String location;

    public String getDetailInfopic() {
        return DetailInfopic;
    }

    public void setDetailInfopic(String detailInfopic) {
        DetailInfopic = detailInfopic;
    }

    public String getKindName() {
        return KindName;
    }

    public void setKindName(String kindName) {
        KindName = kindName;
    }

    public String getInfoName() {
        return InfoName;
    }

    public void setInfoName(String infoName) {
        InfoName = infoName;
    }

    public String getInfoDescription() {
        return InfoDescription;
    }

    public void setInfoDescription(String infoDescription) {
        InfoDescription = infoDescription;
    }

    public String getInformationPic() {
        return InformationPic;
    }

    public void setInformationPic(String informationPic) {
        InformationPic = informationPic;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
