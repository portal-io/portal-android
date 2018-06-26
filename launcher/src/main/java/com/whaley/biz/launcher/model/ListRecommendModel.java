package com.whaley.biz.launcher.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2017/8/10.
 */

public class ListRecommendModel<M> implements Parcelable, Serializable {
    private String name;
    private boolean isPage;
    private List<M> recommendAreas;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPage() {
        return isPage;
    }

    public void setPage(boolean page) {
        isPage = page;
    }

    public List<M> getRecommendAreas() {
        return recommendAreas;
    }

    public void setRecommendAreas(List<M> recommendAreas) {
        this.recommendAreas = recommendAreas;
    }

    public static Creator<ListRecommendModel> getCREATOR() {
        return CREATOR;
    }

    public ListRecommendModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeByte(this.isPage ? (byte) 1 : (byte) 0);
        dest.writeList(this.recommendAreas);
    }

    protected ListRecommendModel(Parcel in) {
        this.name = in.readString();
        this.isPage = in.readByte() != 0;
        this.recommendAreas = new ArrayList<M>();
        Type[] types = getRecommendAreas().getClass().getGenericInterfaces();
        if (types[0] instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) types[0];
            Type type = parameterizedType.getActualTypeArguments()[0];
            in.readList(this.recommendAreas, ((Class) type).getClassLoader());
        } else {
            in.readList(this.recommendAreas, getClass().getClassLoader());
        }
    }

    public static final Creator<ListRecommendModel> CREATOR = new Creator<ListRecommendModel>() {
        @Override
        public ListRecommendModel createFromParcel(Parcel source) {
            return new ListRecommendModel(source);
        }

        @Override
        public ListRecommendModel[] newArray(int size) {
            return new ListRecommendModel[size];
        }
    };
}

