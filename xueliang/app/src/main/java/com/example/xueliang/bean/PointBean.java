package com.example.xueliang.bean;

import java.io.Serializable;

public class PointBean implements Serializable {
    private String equipment_num;
    private String town;
    private String village;
    private String location;
    private String id;
    private String name;
    private String rtmpSrc;
    private String rtspSrc;
    private String playrealUrl;

    public String getPlayrealUrl() {
        return playrealUrl;
    }

    public void setPlayrealUrl(String playrealUrl) {
        this.playrealUrl = playrealUrl;
    }

    public String getRtspSrc() {
        return rtspSrc;
    }

    public void setRtspSrc(String rtspSrc) {
        this.rtspSrc = rtspSrc;
    }

    public String getEquipment_num() {
        return equipment_num;
    }

    public void setEquipment_num(String equipment_num) {
        this.equipment_num = equipment_num;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRtmpSrc() {
        return rtmpSrc;
    }

    public void setRtmpSrc(String rtmpSrc) {
        this.rtmpSrc = rtmpSrc;
    }


}
