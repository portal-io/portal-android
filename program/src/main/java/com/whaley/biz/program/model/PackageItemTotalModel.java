package com.whaley.biz.program.model;

import android.os.Parcel;

import java.util.List;

/**
 * Author: qxw
 * Date: 2017/4/13
 */

public class PackageItemTotalModel extends BaseModel {


    private List<PackageItemModel> content;
    private List<PackageSortModel> sort;

    public List<PackageItemModel> getContent() {
        return content;
    }

    public void setContent(List<PackageItemModel> content) {
        this.content = content;
    }

    public List<PackageSortModel> getSort() {
        return sort;
    }

    public void setSort(List<PackageSortModel> sort) {
        this.sort = sort;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeTypedList(this.content);
        dest.writeTypedList(this.sort);
    }

    public PackageItemTotalModel() {
    }

    protected PackageItemTotalModel(Parcel in) {
        super(in);
        this.content = in.createTypedArrayList(PackageItemModel.CREATOR);
        this.sort = in.createTypedArrayList(PackageSortModel.CREATOR);
    }

    public static final Creator<PackageItemTotalModel> CREATOR = new Creator<PackageItemTotalModel>() {
        @Override
        public PackageItemTotalModel createFromParcel(Parcel source) {
            return new PackageItemTotalModel(source);
        }

        @Override
        public PackageItemTotalModel[] newArray(int size) {
            return new PackageItemTotalModel[size];
        }
    };
}
