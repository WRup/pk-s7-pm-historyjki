package com.pm.historyjki.api.service;

import android.os.Parcel;
import android.os.Parcelable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class StoryDTO implements Parcelable {
    private String id;
    private String authorName;
    private String title;
    private List<String> tags = new ArrayList<>();
    private Integer likeNumber = 0;
    private Integer dislikeNumber = 0;
    private StoryContent content;

    private StoryDTO(Parcel in) {
        id = in.readString();
        authorName = in.readString();
        title = in.readString();
        tags = in.createStringArrayList();
        if (in.readByte() == 0) {
            likeNumber = null;
        } else {
            likeNumber = in.readInt();
        }
        if (in.readByte() == 0) {
            dislikeNumber = null;
        } else {
            dislikeNumber = in.readInt();
        }
        content = in.readParcelable(StoryContent.class.getClassLoader());
    }

    public static final Creator<StoryDTO> CREATOR = new Creator<StoryDTO>() {
        @Override
        public StoryDTO createFromParcel(Parcel in) {
            return new StoryDTO(in);
        }

        @Override
        public StoryDTO[] newArray(int size) {
            return new StoryDTO[size];
        }
    };

    @Override
    public String toString() {
        return title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(authorName);
        dest.writeString(title);
        dest.writeStringList(tags);
        if (likeNumber == null) {
            dest.writeString(null);
        } else {
            dest.writeInt(likeNumber);
        }
        if (dislikeNumber == null) {
            dest.writeString(null);
        } else {
            dest.writeInt(dislikeNumber);
        }
        dest.writeParcelable(content, flags);
    }
}


