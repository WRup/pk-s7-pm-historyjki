package com.pm.historyjki.api.service;

import java.util.List;

import lombok.Data;

@Data
public class StoryDTO {
    private String id;
    private String authorName;
    private String title;
    private List<String> tags;
    private Integer likeNumber;
    private Integer dislikeNumber;
    private StoryContent content;
}
