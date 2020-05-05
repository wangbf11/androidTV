package com.example.xueliang.bean;

import java.util.List;

public class TownBean {
    private String id;
    private String name;
    private List<VillageBean> child;

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

    public List<VillageBean> getChild() {
        return child;
    }

    public void setChild(List<VillageBean> child) {
        this.child = child;
    }
}
