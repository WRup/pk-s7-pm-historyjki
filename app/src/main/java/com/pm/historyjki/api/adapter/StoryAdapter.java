package com.pm.historyjki.api.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pm.historyjki.R;
import com.pm.historyjki.StoryActivity;
import com.pm.historyjki.api.service.StoryDTO;

import java.util.List;


public class StoryAdapter extends ArrayAdapter<StoryDTO> {
    private final Activity context;
    private final List<StoryDTO> stories;

    public StoryAdapter(Activity context, List<StoryDTO> stories) {
        super(context, R.layout.lv_item, stories);
        this.context = context;
        this.stories = stories;
    }

    @NonNull
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.lv_item, null, true);
        final StoryDTO story = stories.get(position);


        TextView storyTitle = rowView.findViewById(R.id.tv_lv_item);
        TextView likeNumber = rowView.findViewById(R.id.like);
        TextView dislikeNumber = rowView.findViewById(R.id.dislike);

        storyTitle.setText(story.getTitle());
        likeNumber.setText(String.valueOf(story.getLikeNumber()));
        dislikeNumber.setText(String.valueOf(story.getDislikeNumber()));

        rowView.findViewById(R.id.btn_open_story).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openStoryDetails(story.getId());
            }
        });

        return rowView;
    }

    private void openStoryDetails(String storyId) {
        Intent intent = new Intent(getContext(), StoryActivity.class);
        intent.putExtra("storyId", storyId);
        getContext().startActivity(intent);
    }
}
