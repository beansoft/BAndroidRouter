package com.github.beansoftapp.android.router.interceptor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class JumpInvoker implements Parcelable, Invoker {
    public static final Creator<JumpInvoker> CREATOR = new Creator<JumpInvoker>() {

        public JumpInvoker createFromParcel(Parcel parcel) {
            return new JumpInvoker(parcel);
        }

        public JumpInvoker[] newArray(int i) {
            return new JumpInvoker[i];
        }
    };
    private Bundle bundle;
    private String targetClassName;

    public JumpInvoker(String str, Bundle bundle) {
        this.targetClassName = str;
        if (bundle == null) {
            this.bundle = new Bundle();
        } else {
            this.bundle = bundle;
        }
    }

    protected JumpInvoker(Parcel parcel) {
        this.targetClassName = parcel.readString();
        this.bundle = parcel.readBundle();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.targetClassName);
        parcel.writeBundle(this.bundle);
    }

    public void invoke(Context context) {
        try {
            Intent intent = new Intent(context, Class.forName(this.targetClassName));
            intent.putExtras(this.bundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
