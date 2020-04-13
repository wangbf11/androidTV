package com.example.xueliang.bean;

import java.util.List;

public class VillageBean {
    private String vid;
    private String vName;
    private List<PointBean> points;

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getvName() {
        return vName;
    }

    public void setvName(String vName) {
        this.vName = vName;
    }

    public List<PointBean> getPoints() {
        return points;
    }

    public void setPoints(List<PointBean> points) {
        this.points = points;
    }
}
