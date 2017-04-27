package com.tsi.android.mycoursework;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class TodoItem implements Parcelable {

    private int id;
    private String title;
    private Date date;
    private boolean checkbox;
    private String description;

    public TodoItem(String title, Date date, boolean checkbox) {
        this.title = title;
        this.date = date;
        this.checkbox = checkbox;
    }

    public TodoItem(int id, String title, Date date, Boolean checkbox, String description) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.checkbox = checkbox;
        this.description = description;
    }

    public TodoItem() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public Date getDate() { return date; }

    public void setDate(Date date) { this.date = date; }

    public boolean isCheckbox() {
        return checkbox;
    }

    public void setCheckbox(boolean checkbox) {
        this.checkbox = checkbox;
    }

    public String getDescription() { return description; }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeSerializable(date);
        dest.writeByte((byte) (checkbox ? 1 : 0));
    }

    protected TodoItem(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        date = (Date) in.readSerializable();
        checkbox = in.readByte() != 0;
    }


    public static final Creator<TodoItem> CREATOR = new Creator<TodoItem>() {
        @Override
        public TodoItem createFromParcel(Parcel in) {
            return new TodoItem(in);
        }

        @Override
        public TodoItem[] newArray(int size) {
            return new TodoItem[size];
        }
    };

}
