package com.pm.historyjki.api.service;

import lombok.Data;

import java.util.List;

@Data
public class StoryDTO {
    private String id;
    private String authorName;
    private String title;
    private List<String> tags;
    private Integer likeNumber;
    private Integer dislikeNumber;
    private StoryContent content;

    @Override
    public String toString() {
        return title;
    }
}


