package com.pm.historyjki.api.boundService;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.util.Consumer;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.pm.historyjki.R;
import com.pm.historyjki.api.service.StoryApiCallService;
import com.pm.historyjki.api.service.StoryDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MyBoundService extends Service {
    private StoryApiCallService storyApiCallService;
    private List<StoryDTO> stories;
    private final IBinder binder = new MyBinder();
    private Toast toast;
    private Timer timer;
    private TimerTask timerTask;

    @Override
    public void onCreate() {
        super.onCreate();
        timer = new Timer();
        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        storyApiCallService = new StoryApiCallService(this);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        clearTimerSchedule();
        initTask();
        timer.scheduleAtFixedRate(timerTask, 6 * 1000, 6 * 1000);
        return super.onStartCommand(intent, flags, startId);
    }

    private class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    storyApiCallService.getAllStories(new Consumer<List<StoryDTO>>() {
                                                          @Override
                                                          public void accept(List<StoryDTO> storyDTOS) {
                                                              stories = new ArrayList<>(storyDTOS);
                                                              showToast("Pobralem dane z bazy.");
                                                          }
                                                      }, new Response.ErrorListener() {
                                                          @Override
                                                          public void onErrorResponse(VolleyError error) {
                                                              showToast((String) getText(R.string.somethingGoesWrongErrorMsg));
                                                          }
                                                      }
                    );
                }
            });

        }
    }

    public List<StoryDTO> getAllStories() {
        return stories;
    }

    private void showToast(String text) {
        toast.setText(text);
        toast.show();
    }

    private void clearTimerSchedule() {
        if (timerTask != null) {
            timerTask.cancel();
            timer.purge();
        }
    }

    private void initTask() {
        timerTask = new MyTimerTask();
    }

    @Override
    public void onDestroy() {
        clearTimerSchedule();
        super.onDestroy();
    }

    public class MyBinder extends Binder {
        public MyBoundService getMyService() {
            return MyBoundService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
