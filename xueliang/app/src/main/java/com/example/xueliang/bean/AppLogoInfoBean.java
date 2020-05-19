package com.example.xueliang.bean;

import java.io.Serializable;

public class AppLogoInfoBean implements Serializable {

    private String logo;
    private String name;
    private String time;

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
