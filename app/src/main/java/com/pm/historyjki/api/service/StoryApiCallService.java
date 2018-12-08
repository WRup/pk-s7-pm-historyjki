package com.pm.historyjki.api.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pm.historyjki.BuildConfig;

import android.content.Context;
import android.support.v4.util.Consumer;


public class StoryApiCallService {

    private RequestQueue requestQueue;

    private static final String STORY_URL = BuildConfig.BASE_API_URL + "story/";

    public StoryApiCallService(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    public void getAllStories(final Consumer<List<StoryDTO>> dtoConsumer, ErrorListener errorListener) {
        JsonArrayRequest request = new JsonArrayRequest(Method.GET, STORY_URL, null,
                new Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        StoryApiCallService.this.acceptArrayResponse(response, dtoConsumer);
                    }
                }, errorListener);

        requestQueue.add(request);
    }

    public void getStory(String storyId, final Consumer<StoryDTO> dtoConsumer, ErrorListener errorListener) {
        JsonObjectRequest request = new JsonObjectRequest(Method.GET, STORY_URL + storyId, null,
                new Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        StoryApiCallService.this.acceptResponse(response, dtoConsumer);
                    }
                }, errorListener);

        requestQueue.add(request);
    }

    public void addStory(StoryDTO dto, final Consumer<StoryDTO> dtoConsumer, ErrorListener errorListener) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(dto);
            JSONObject jsonObject = new JSONObject(json);

            JsonObjectRequest request = new JsonObjectRequest(Method.POST, STORY_URL,jsonObject ,
                    new Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            StoryApiCallService.this.acceptResponse(response, dtoConsumer);
                        }
                    }, errorListener);

            requestQueue.add(request);
        } catch (Exception ignored) {
            dtoConsumer.accept(null);
        }
    }

    public void updateStory(StoryDTO dto, final Consumer<StoryDTO> dtoConsumer, ErrorListener errorListener) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(dto);
            JSONObject jsonObject = new JSONObject(json);

            JsonObjectRequest request = new JsonObjectRequest(Method.PUT, STORY_URL,jsonObject ,
                    new Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            StoryApiCallService.this.acceptResponse(response, dtoConsumer);
                        }
                    }, errorListener);

            requestQueue.add(request);
        } catch (Exception ignored) {
            dtoConsumer.accept(null);
        }
    }

    private void acceptResponse(JSONObject response, Consumer<StoryDTO> dtoConsumer) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            StoryDTO responseDTO = objectMapper.readValue(response.toString(), StoryDTO.class);
            dtoConsumer.accept(responseDTO);
        } catch (IOException e) {
            dtoConsumer.accept(null);
        }
    }

    private void acceptArrayResponse(JSONArray response, Consumer<List<StoryDTO>> dtoConsumer) {
        ObjectMapper mapper = new ObjectMapper();
        List<StoryDTO> dtos = new ArrayList<>();
        for (int i = 0; i < response.length(); i++) {
            try {
                JSONObject object = response.getJSONObject(i);
                StoryDTO dto = mapper.readValue(object.toString(), StoryDTO.class);
                dtos.add(dto);
            } catch (Exception ignored) {
                dtoConsumer.accept(null);
            }
        }
        dtoConsumer.accept(dtos);
    }
}
