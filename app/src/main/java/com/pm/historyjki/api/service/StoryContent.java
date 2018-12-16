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
    private String id;
    private String content;
    private List<String> continuations = new ArrayList<>();
    private String parent;

    private StoryContent(Parcel in) {
        id = in.readString();
        content = in.readString();
        continuations = in.createStringArrayList();
        parent = in.readString();
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
        dest.writeString(id);
        dest.writeString(content);
        dest.writeStringList(continuations);
        dest.writeString(parent);
    }
}
