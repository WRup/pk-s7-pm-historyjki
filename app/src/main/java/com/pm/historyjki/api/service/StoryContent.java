package com.pm.historyjki.api.service;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StoryContent implements Parcelable {

    private String content;

    private List<StoryContinuation> continuations = new ArrayList<>();

    private StoryContent(Parcel in) {
        content = in.readString();
        continuations = in.createTypedArrayList(StoryContinuation.CREATOR);
    }

    public static final Creator<StoryContent> CREATOR = new Creator<StoryContent>() {
        @Override
        public StoryContent createFromParcel(Parcel in) {
            return new StoryContent(in);
        }

        @Override
        public StoryContent[] newArray(int size) {
            return new StoryContent[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(content);
        dest.writeTypedList(continuations);
    }
}
