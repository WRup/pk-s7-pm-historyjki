package com.pm.historyjki.api.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.pm.historyjki.R;
import com.pm.historyjki.api.service.StoryDTO;

import java.util.List;


public class StoryAdapter extends ArrayAdapter<StoryDTO> {

    String ZERO_WIDTH_SPACE = "\u200b";
    private final Activity context;
    private final List<StoryDTO> storyDTO;
    private final List<Integer> likes;
    private final List<Integer> dislikes;

    public StoryAdapter(Activity context, List<StoryDTO> storyDTO, List<Integer> likes, List<Integer> dislikes) {
        super(context, R.layout.lv_item, storyDTO);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.storyDTO = storyDTO;
        this.likes = likes;
        this.dislikes = dislikes;
    }

    @NonNull
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.lv_item, null, true);

        TextView storyTitle = rowView.findViewById(R.id.tv_lv_item);
        TextView likeNumber = rowView.findViewById(R.id.like);
        TextView dislikeNumber = rowView.findViewById(R.id.dislike);

        storyTitle.setText(storyDTO.get(position).getTitle());
        likeNumber.setText(String.valueOf(likes.get(position)) + ZERO_WIDTH_SPACE);
        dislikeNumber.setText(String.valueOf(dislikes.get(position)) + ZERO_WIDTH_SPACE);

        return rowView;

    }
}
