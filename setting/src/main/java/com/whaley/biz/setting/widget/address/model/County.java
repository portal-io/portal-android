package com.whaley.biz.setting.widget.address.model;

public class County {
    public int id;
    public int city_id;
    public String name;

    public County(int id, int city_id, String name) {
        this.id = id;
        this.city_id = city_id;
        this.name = name;
    }
}
