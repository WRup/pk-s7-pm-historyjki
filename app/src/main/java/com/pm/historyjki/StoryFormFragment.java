package com.pm.historyjki;

import com.pm.historyjki.api.service.StoryDTO;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class StoryFormFragment extends Fragment {

    public static final String TAG = StoryFormFragment.class.getSimpleName();

    private StoryDTO storyDTO;

    private OnNewStorySubmitListener submitListener;

    public static StoryFormFragment newInstance(StoryDTO storyDTO) {
        StoryFormFragment fragment = new StoryFormFragment();
        Bundle args = new Bundle();
        args.putParcelable(StoryDTO.class.getSimpleName(), storyDTO);
        fragment.setArguments(args);


        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnNewStorySubmitListener) {
            submitListener = (OnNewStorySubmitListener) context;
        } else {
            throw new IllegalArgumentException(context.toString() + " must implement" + OnNewStorySubmitListener.class.getSimpleName());
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            storyDTO = getArguments().getParcelable(StoryDTO.class.getSimpleName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_story_form, container, false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        submitListener = null;
    }


    private void init() {
        initSubmitButton();
    }

    private void initSubmitButton() {
        Button btn = findViewById(R.id.btn_create_story);
        final EditText title = findViewById(R.id.et_story_creator_title);
        final EditText author = findViewById(R.id.et_story_creator_author);

        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (storyDTO == null) {
                    storyDTO = new StoryDTO();
                }
                storyDTO.setTitle(title.getText().toString());
                storyDTO.setAuthorName(author.getText().toString());
                if (submitListener != null) {
                    submitListener.onSubmit(storyDTO);
                }
            }
        });
    }

    private <T extends View> T findViewById(@IdRes int id) {
        if (getView() == null) {
            throw new NullPointerException();
        } else {
            return getView().findViewById(id);
        }
    }

    public interface OnNewStorySubmitListener {
        void onSubmit(StoryDTO storyDTO);
    }
}
