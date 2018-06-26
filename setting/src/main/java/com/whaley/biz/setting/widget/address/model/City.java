package com.whaley.biz.setting.widget.address.model;

public class City {

    public int id;
    public int province_id;
    public String name;

    public City(int id, int province_id, String name) {
        this.id = id;
        this.province_id = province_id;
        this.name = name;
    }
}
