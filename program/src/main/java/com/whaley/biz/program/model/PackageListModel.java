package com.whaley.biz.program.model;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author: qxw
 * Date: 2017/4/13
 */

public class PackageListModel implements Parcelable {
    private PackageItemTotalModel items;

    private PackageModel pack;

    public PackageItemTotalModel getItems() {
        return items;
    }

    public void setItems(PackageItemTotalModel items) {
        this.items = items;
    }

    public PackageModel getPack() {
        return pack;
    }

    public void setPack(PackageModel pack) {
        this.pack = pack;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.items, flags);
        dest.writeParcelable(this.pack, flags);
    }

    public PackageListModel() {
    }

    protected PackageListModel(Parcel in) {
        this.items = in.readParcelable(PackageItemTotalModel.class.getClassLoader());
        this.pack = in.readParcelable(PackageModel.class.getClassLoader());
    }

    public static final Creator<PackageListModel> CREATOR = new Creator<PackageListModel>() {
        @Override
        public PackageListModel createFromParcel(Parcel source) {
            return new PackageListModel(source);
        }

        @Override
        public PackageListModel[] newArray(int size) {
            return new PackageListModel[size];
        }
    };
}
