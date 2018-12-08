package com.pm.historyjki.api.service;

import java.util.List;

import lombok.Data;

@Data
public class StoryContent {
    private String id;
    private String content;
    private List<String> continuations;
    private String parent;
}
