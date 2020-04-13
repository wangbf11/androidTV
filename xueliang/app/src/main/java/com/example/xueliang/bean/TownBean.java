package com.example.xueliang.bean;

import java.util.List;

public class TownBean {
    private String tid;
    private String tName;
    private List<VillageBean> villages;

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String gettName() {
        return tName;
    }

    public void settName(String tName) {
        this.tName = tName;
    }

    public List<VillageBean> getVillages() {
        return villages;
    }

    public void setVillages(List<VillageBean> villages) {
        this.villages = villages;
    }
}
