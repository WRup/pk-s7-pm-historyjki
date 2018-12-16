package com.pm.historyjki.api.service;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoryContinuation implements Parcelable {

    private String starter;

    private String content;

    @Builder.Default
    private List<StoryContinuation> continuations = new ArrayList<>();

    private StoryContinuation(Parcel in) {
        starter = in.readString();
        content = in.readString();
        continuations = in.createTypedArrayList(StoryContinuation.CREATOR);
    }

    public static final Creator<StoryContinuation> CREATOR = new Creator<StoryContinuation>() {
        @Override
        public StoryContinuation createFromParcel(Parcel in) {
            return new StoryContinuation(in);
        }

        @Override
        public StoryContinuation[] newArray(int size) {
            return new StoryContinuation[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(starter);
        dest.writeString(content);
        dest.writeTypedList(continuations);
    }
}
