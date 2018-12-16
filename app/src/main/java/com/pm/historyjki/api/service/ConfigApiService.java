package com.pm.historyjki.api.service;

import java.io.IOException;
import java.util.logging.Logger;

import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pm.historyjki.BuildConfig;

import android.content.Context;
import android.support.v4.util.Consumer;

public class ConfigApiService {

    private final RequestQueue requestQueue;

    private static final String CONFIG_URL = BuildConfig.BASE_API_URL + "config/";

    public ConfigApiService(Context context) {
        this.requestQueue = Volley.newRequestQueue(context);
    }

    public void getConfiguration(final Consumer<Configuration> configConsumer, ErrorListener errorListener) {
        JsonObjectRequest request = new JsonObjectRequest(Method.GET, CONFIG_URL, null, new Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ConfigApiService.this.acceptResponse(response, configConsumer);
            }
        }, errorListener);
        requestQueue.add(request);
    }

    private void acceptResponse(JSONObject response, Consumer<Configuration> consumer) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Configuration configuration = mapper.readValue(response.toString(), Configuration.class);
            consumer.accept(configuration);
        } catch (IOException e) {
            consumer.accept(null);
        }
    }
}
