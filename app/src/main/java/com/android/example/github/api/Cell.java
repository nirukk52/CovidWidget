package com.android.example.github.api;


import android.os.Parcel;
import android.os.Parcelable;

public class Cell implements Parcelable {
    int row;
    int col;
    boolean selected;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.row);
        dest.writeInt(this.col);
        dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
    }

    public Cell() {
    }

    protected Cell(Parcel in) {
        this.row = in.readInt();
        this.col = in.readInt();
        this.selected = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Cell> CREATOR = new Parcelable.Creator<Cell>() {
        @Override
        public Cell createFromParcel(Parcel source) {
            return new Cell(source);
        }

        @Override
        public Cell[] newArray(int size) {
            return new Cell[size];
        }
    };
}
