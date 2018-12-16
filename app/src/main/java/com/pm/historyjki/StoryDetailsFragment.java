package com.pm.historyjki;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

import com.pm.historyjki.api.service.StoryContent;
import com.pm.historyjki.api.service.StoryDTO;
import com.pm.historyjki.db.model.FavouriteStory;
import com.pm.historyjki.service.FavouriteStoryService;

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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import lombok.NoArgsConstructor;


@NoArgsConstructor
public class StoryDetailsFragment extends Fragment {

    public static final String TAG = StoryDetailsFragment.class.getSimpleName();

    private OnStoryDetailsSaveListener saveListener;

    private OnStoryChangeListener changeListener;

    private StoryDTO storyDTO;

    boolean checked = false;

    private boolean saveBtnVisible = true;

    private boolean likeBtnVisible = true;

    private boolean readOnlyView = false;

    private LikeDislikeButtonState likeDislikeButtonState;

    private FavouriteStoryService favouriteStoryService;

    public static StoryDetailsFragment newInstance(StoryDTO storyDTO, boolean readOnlyView) {
        StoryDetailsFragment fragment = new StoryDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(StoryDTO.class.getSimpleName(), storyDTO);
        args.putBoolean("readOnlyView", readOnlyView);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStoryDetailsSaveListener) {
            saveListener = (OnStoryDetailsSaveListener) context;
        } else {
            saveBtnVisible = false;
        }

        if (context instanceof OnStoryChangeListener) {
            changeListener = (OnStoryChangeListener) context;
        } else {
            likeBtnVisible = false;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            storyDTO = getArguments().getParcelable(StoryDTO.class.getSimpleName());
            readOnlyView = getArguments().getBoolean("readOnlyView");
        }
        favouriteStoryService = new FavouriteStoryService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_story_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initSaveBtn(view);
        initLikeBtn(view);
        initView();
    }

    private boolean isFavouriteBtnEnable() {
        return !(favouriteStoryService == null || storyDTO == null || !readOnlyView);
    }

    private boolean isFavourite() {
        if (storyDTO == null || favouriteStoryService == null) {
            return false;
        }
        return favouriteStoryService.isFavourite(storyDTO.getId());
    }

    private void initView() {
        if (storyDTO != null) {
            getStoryTitle().setText(storyDTO.getTitle());
            if (storyDTO.getContent() != null) {
                getStoryContent().setText(storyDTO.getContent().getContent());

                Iterator<String> it = storyDTO.getContent().getContinuations().iterator();
                if(it.hasNext()) {
                    getFirstContinuation().setText(it.next());
                }
                if(it.hasNext()) {
                    getSecondContinuation().setText(it.next());
                }
                if (it.hasNext()) {
                    getThirdContinuation().setText(it.next());
                }
            }
        }
        getStoryContent().setEnabled(!readOnlyView);
        getFirstContinuation().setEnabled(!readOnlyView);
        getSecondContinuation().setEnabled(!readOnlyView);
        getThirdContinuation().setEnabled(!readOnlyView);

        getFavBtn().setEnabled(isFavouriteBtnEnable());
        getFavBtn().setChecked(isFavourite());
        getFavBtn().setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                favouriteChange(isChecked);
            }
        });
    }

    private TextView getStoryTitle() {
        return findViewById(R.id.tv_story_details_title);
    }

    private void favouriteChange(boolean favourite) {
        if (storyDTO != null && favouriteStoryService != null) {
            if (favourite) {
                favouriteStoryService.addStoryToFavourite(storyDTO);
            } else {
                favouriteStoryService.removeStoryFromFavourite(storyDTO);
            }
        }
    }

    private CheckBox getFavBtn() {
        return findViewById(R.id.btn_story_details_fav);
    }

    private void initSaveBtn(@NonNull View view) {
        Button btn = view.findViewById(R.id.btn_save_new_story);
        if (!saveBtnVisible) {
            btn.setVisibility(View.INVISIBLE);
        } else {
            btn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (saveListener != null) {
                        storyDTO.setLikeNumber(0);
                        storyDTO.setDislikeNumber(0);

                        StoryContent content = new StoryContent();
                        content.setContent(getStoryContent().getText().toString());
                        content.setContinuations(Arrays.asList(
                                getFirstContinuation().getText().toString(),
                                getSecondContinuation().getText().toString(),
                                getThirdContinuation().getText().toString()
                        ));
                        storyDTO.setContent(content);
                        saveListener.onSave(storyDTO);
                    }
                }
            });
        }
    }

    private EditText getStoryContent() {
        return findViewById(R.id.et_story);
    }

    private EditText getFirstContinuation() {
        return findViewById(R.id.et_continuation_1);
    }

    private EditText getSecondContinuation() {
        return findViewById(R.id.et_continuation_2);
    }

    private EditText getThirdContinuation() {
        return findViewById(R.id.et_continuation_3);
    }

    private void initLikeBtn(@NonNull View view) {
        final Button like = view.findViewById(R.id.btn_like);
        final Button dislike = view.findViewById(R.id.btn_dislike);

        if (!likeBtnVisible) {
            like.setVisibility(View.INVISIBLE);
            dislike.setVisibility(View.INVISIBLE);
        } else {
            like.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    setLikeDislikeState(LikeDislikeButtonState.LIKE);
                    like.setEnabled(false);
                    dislike.setEnabled(true);
                }
            });

            dislike.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    setLikeDislikeState(LikeDislikeButtonState.DISLIKE);
                    dislike.setEnabled(false);
                    like.setEnabled(true);
                }
            });
        }
    }

    private void setLikeDislikeState(LikeDislikeButtonState state) {
        if (Objects.equals(state, likeDislikeButtonState)) {
            return;
        }

        int like = 0;
        int dislike = 0;

        if (likeDislikeButtonState == LikeDislikeButtonState.LIKE) {
            like = -1;
            if (state == LikeDislikeButtonState.DISLIKE) {
                dislike = 1;
            }
        } else if (likeDislikeButtonState == LikeDislikeButtonState.DISLIKE) {
            dislike = -1;
            if (state == LikeDislikeButtonState.LIKE) {
                like = 1;
            }
        } else {
            if (state == LikeDislikeButtonState.LIKE) {
                like = 1;
            } else if (state == LikeDislikeButtonState.DISLIKE) {
                dislike = 1;
            }
        }
        likeDislikeButtonState = state;
        storyDTO.setLikeNumber(storyDTO.getLikeNumber() + like);
        storyDTO.setDislikeNumber(storyDTO.getDislikeNumber() + dislike);
        changeListener.onChange(storyDTO);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        saveListener = null;
    }

    public interface OnStoryDetailsSaveListener {
        void onSave(StoryDTO storyDTO);
    }

    public interface OnStoryChangeListener {
        void onChange(StoryDTO storyDTO);
    }

    private <T extends View> T findViewById(@IdRes int id) {
        if (getView() == null) {
            throw new NullPointerException();
        } else {
            return getView().findViewById(id);
        }
    }

    private enum LikeDislikeButtonState {
        LIKE,
        DISLIKE
    }
}
