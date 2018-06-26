package com.whaley.biz.pay.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by mafei on 2017/4/10.
 */

public class OrderListModel implements Parcelable {

    /**
     * content : [{"accountId":"luoliang",
     * "orderId":"normal_sales_0868125d3bd14c908aacf0d25228588f",
     * "merchandiseCode":"ef4f252610df4a3bb458851640ba728e","merchandiseType":"recorded",
     * "amount":"1","currency":"RMB","result":"1","merchandiseName":"aaa","merchandiseImage":""}]
     * total : 1
     * size : 10
     * number : 0
     * first : true
     * totalElements : 1
     * last : true
     * totalPages : 1
     * numberOfElements : 1
     */

    private int total;
    private int size;
    private int number;
    private boolean first;
    private int totalElements;
    private boolean last;
    private int totalPages;
    private int numberOfElements;
    private List<ContentEntity> content;


    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public List<ContentEntity> getContent() {
        return content;
    }

    public void setContent(List<ContentEntity> content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.total);
        dest.writeInt(this.size);
        dest.writeInt(this.number);
        dest.writeByte(this.first ? (byte) 1 : (byte) 0);
        dest.writeInt(this.totalElements);
        dest.writeByte(this.last ? (byte) 1 : (byte) 0);
        dest.writeInt(this.totalPages);
        dest.writeInt(this.numberOfElements);
        dest.writeTypedList(this.content);
    }

    public OrderListModel() {
    }

    protected OrderListModel(Parcel in) {
        this.total = in.readInt();
        this.size = in.readInt();
        this.number = in.readInt();
        this.first = in.readByte() != 0;
        this.totalElements = in.readInt();
        this.last = in.readByte() != 0;
        this.totalPages = in.readInt();
        this.numberOfElements = in.readInt();
        this.content = in.createTypedArrayList(ContentEntity.CREATOR);
    }

    public static final Creator<OrderListModel> CREATOR = new Creator<OrderListModel>() {
        @Override
        public OrderListModel createFromParcel(Parcel source) {
            return new OrderListModel(source);
        }

        @Override
        public OrderListModel[] newArray(int size) {
            return new OrderListModel[size];
        }
    };
}
