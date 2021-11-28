package com.example.wangduwei.demos.ipc.callback;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @desc:
 * @auther:duwei
 * @date:2018/8/23
 */

public class IPCBean implements Parcelable {

    private int result;

    protected IPCBean(Parcel in) {
        result = in.readInt();
    }

    public static final Creator<IPCBean> CREATOR = new Creator<IPCBean>() {
        @Override
        public IPCBean createFromParcel(Parcel in) {
            return new IPCBean(in);
        }

        @Override
        public IPCBean[] newArray(int size) {
            return new IPCBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(result);
    }
}
