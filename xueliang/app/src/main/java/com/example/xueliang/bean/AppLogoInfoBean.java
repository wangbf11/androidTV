package com.example.xueliang.bean;

import java.io.Serializable;

public class AppLogoInfoBean implements Serializable {

    private String logo;
    private String name;

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
}
