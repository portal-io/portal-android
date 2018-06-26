package com.whaley.biz.common.model.base;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: qxw
 * Date: 2016/10/13
 */

public class ListModel<M> implements Parcelable, Serializable {
    private boolean last;
    private boolean first;
    private int numberOfElements;
    private int number;
    private int size;
    private int totalPages;
    private int totalElements;
    private List<M> content;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public List<M> getContent() {
        return content;
    }

    public void setContent(List<M> content) {
        this.content = content;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.last ? (byte) 1 : (byte) 0);
        dest.writeByte(this.first ? (byte) 1 : (byte) 0);
        dest.writeInt(this.numberOfElements);
        dest.writeInt(this.number);
        dest.writeInt(this.size);
        dest.writeInt(this.totalPages);
        dest.writeInt(this.totalElements);
        dest.writeList(this.content);
    }

    public ListModel() {
    }

    protected ListModel(Parcel in) {
        this.last = in.readByte() != 0;
        this.first = in.readByte() != 0;
        this.numberOfElements = in.readInt();
        this.number = in.readInt();
        this.size = in.readInt();
        this.totalPages = in.readInt();
        this.totalElements = in.readInt();
        this.content = new ArrayList<M>();
        Type[] types = getContent().getClass().getGenericInterfaces();
        if (types[0] instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) types[0];
            Type type = parameterizedType.getActualTypeArguments()[0];
            in.readList(this.content, ((Class) type).getClassLoader());
        } else {
            in.readList(this.content, getClass().getClassLoader());
        }

    }

    public static final Parcelable.Creator<ListModel> CREATOR = new Parcelable.Creator<ListModel>() {
        @Override
        public ListModel createFromParcel(Parcel source) {
            return new ListModel(source);
        }

        @Override
        public ListModel[] newArray(int size) {
            return new ListModel[size];
        }
    };
}
