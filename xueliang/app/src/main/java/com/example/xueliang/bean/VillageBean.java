package com.example.xueliang.bean;

import java.util.List;

public class VillageBean {
    private String id;
    private String name;
    private List<PointBean> child;

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

    public List<PointBean> getChild() {
        return child;
    }

    public void setChild(List<PointBean> child) {
        this.child = child;
    }
}
