package com.whaley.biz.program.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author: qxw
 * Date: 2017/4/13
 */

public class PackageSortModel implements Parcelable {

//         {
//             "direction": "DESC",
//              "property": "sort",
//                      "ignoreCase": false,
//                      "nullHandling": "NATIVE",
//                      "ascending": false
//}

    private String direction;
    private String property;
    private String nullHandling;
    private boolean ignoreCase;
    private boolean ascending;

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getNullHandling() {
        return nullHandling;
    }

    public void setNullHandling(String nullHandling) {
        this.nullHandling = nullHandling;
    }

    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    public void setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

    public boolean isAscending() {
        return ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.direction);
        dest.writeString(this.property);
        dest.writeString(this.nullHandling);
        dest.writeByte(this.ignoreCase ? (byte) 1 : (byte) 0);
        dest.writeByte(this.ascending ? (byte) 1 : (byte) 0);
    }

    public PackageSortModel() {
    }

    protected PackageSortModel(Parcel in) {
        this.direction = in.readString();
        this.property = in.readString();
        this.nullHandling = in.readString();
        this.ignoreCase = in.readByte() != 0;
        this.ascending = in.readByte() != 0;
    }

    public static final Creator<PackageSortModel> CREATOR = new Creator<PackageSortModel>() {
        @Override
        public PackageSortModel createFromParcel(Parcel source) {
            return new PackageSortModel(source);
        }

        @Override
        public PackageSortModel[] newArray(int size) {
            return new PackageSortModel[size];
        }
    };
}
