package com.whaley.biz.program.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2017/8/10.
 */

public class RecommendAreasBean extends BaseModel implements Parcelable,Serializable {
    private String name;
    private String code;
    private String type;
    private int position;
    private String style;
    private String recommendPageCode;
    private int status;
    private List<RecommendModel> recommendElements;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getRecommendPageCode() {
        return recommendPageCode;
    }

    public void setRecommendPageCode(String recommendPageCode) {
        this.recommendPageCode = recommendPageCode;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<RecommendModel> getRecommendElements() {
        return recommendElements;
    }

    public void setRecommendElements(List<RecommendModel> recommendElements) {
        this.recommendElements = recommendElements;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.name);
        dest.writeString(this.code);
        dest.writeString(this.type);
        dest.writeInt(this.position);
        dest.writeString(this.style);
        dest.writeString(this.recommendPageCode);
        dest.writeInt(this.status);
        dest.writeList(this.recommendElements);
    }

    public RecommendAreasBean() {
    }

    protected RecommendAreasBean(Parcel in) {
        super(in);
        this.name = in.readString();
        this.code = in.readString();
        this.type = in.readString();
        this.position = in.readInt();
        this.style = in.readString();
        this.recommendPageCode = in.readString();
        this.status = in.readInt();
        this.recommendElements = new ArrayList<RecommendModel>();
        in.readList(this.recommendElements, List.class.getClassLoader());
    }

    public static final Creator<RecommendAreasBean> CREATOR = new Creator<RecommendAreasBean>() {
        public RecommendAreasBean createFromParcel(Parcel source) {
            return new RecommendAreasBean(source);
        }

        public RecommendAreasBean[] newArray(int size) {
            return new RecommendAreasBean[size];
        }
    };
}

