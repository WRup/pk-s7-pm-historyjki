package com.pm.historyjki;

import java.util.Arrays;
import java.util.Iterator;

import com.pm.historyjki.StoryDetailsFragment.OnStoryContinuationOpenListener;
import com.pm.historyjki.api.service.StoryContinuation;

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
import android.widget.ImageButton;
import android.widget.TextView;
import lombok.NoArgsConstructor;


@NoArgsConstructor
public class StoryContinuationFragment extends Fragment {

    private StoryContinuation continuation;

    private OnContinuationSaveListener continuationSaveListener;

    private OnStoryContinuationOpenListener storyContinuationOpenListener;

    private boolean editMode = false;

    public static StoryContinuationFragment newInstance(StoryContinuation continuation) {
        StoryContinuationFragment fragment = new StoryContinuationFragment();
        Bundle args = new Bundle();
        args.putParcelable("continuation", continuation);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnContinuationSaveListener) {
            this.continuationSaveListener = (OnContinuationSaveListener) context;
        }
        if (context instanceof OnStoryContinuationOpenListener) {
            this.storyContinuationOpenListener = (OnStoryContinuationOpenListener) context;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getStarter().setText(continuation.getStarter());

        getContent().setText(continuation.getContent());
        getContent().setEnabled(editMode);

        Iterator<StoryContinuation> it = continuation.getContinuations().iterator();

        if (it.hasNext()) {
            final StoryContinuation c = it.next();
            getFirstContinuation().setText(c.getStarter());
            getFirstContinuation().setEnabled(editMode);

            getOpenFirstContinuationBtn().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    openContinuation(c);
                }
            });
            getOpenFirstContinuationBtn().setEnabled(!editMode);
        }
        if (it.hasNext()) {
            final StoryContinuation c = it.next();
            getSecondContinuation().setText(c.getStarter());
            getSecondContinuation().setEnabled(editMode);

            getOpenSecondContinuationBtn().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    openContinuation(c);
                }
            });
            getOpenSecondContinuationBtn().setEnabled(!editMode);
        }
        if (it.hasNext()) {
            final StoryContinuation c = it.next();
            getThirdContinuation().setText(c.getStarter());
            getThirdContinuation().setEnabled(editMode);

            getOpenThirdContinuationBtn().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    openContinuation(c);
                }
            });
            getOpenThirdContinuationBtn().setEnabled(!editMode);
        }

        initSaveBtn();
    }

    private void openContinuation(StoryContinuation continuation) {
        if (storyContinuationOpenListener != null) {
            storyContinuationOpenListener.onStoryContinuationOpen(continuation);
        }
    }

    private void initSaveBtn() {
        if (!editMode) {
            getSaveBtn().setVisibility(View.INVISIBLE);
        } else {
            getSaveBtn().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    continuation.setContent(getContent().getText().toString());
                    continuation.setContinuations(Arrays.asList(
                            StoryContinuation.builder().starter(getFirstContinuation().getText().toString()).build(),
                            StoryContinuation.builder().starter(getSecondContinuation().getText().toString()).build(),
                            StoryContinuation.builder().starter(getThirdContinuation().getText().toString()).build()
                    ));

                    if (continuationSaveListener != null) {
                        continuationSaveListener.onContinuationSaveLister(continuation);
                    }
                }
            });
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            continuation = getArguments().getParcelable("continuation");
            editMode = (continuation == null || continuation.getContent() == null || continuation.getContent().isEmpty());
        }
    }

    private Button getSaveBtn() {
        return findViewById(R.id.btn_save_continuation);
    }

    private TextView getStarter() {
        return findViewById(R.id.tv_continuation_title);
    }

    private EditText getContent() {
        return findViewById(R.id.et_continuation_content);
    }

    private EditText getFirstContinuation() {
        return findViewById(R.id.et_continuation_first_continuation);
    }

    private EditText getSecondContinuation() {
        return findViewById(R.id.et_continuation_second_continuation);
    }

    private EditText getThirdContinuation() {
        return findViewById(R.id.et_continuation_third_continuation);
    }


    private ImageButton getOpenFirstContinuationBtn() {
        return findViewById(R.id.btn_open_first_continuation);
    }

    private ImageButton getOpenSecondContinuationBtn() {
        return findViewById(R.id.btn_open_second_continuation);
    }

    private ImageButton getOpenThirdContinuationBtn() {
        return findViewById(R.id.btn_open_third_continuation);
    }

    private <T extends View> T findViewById(@IdRes int id) {
        if (getView() == null) {
            throw new NullPointerException();
        } else {
            return getView().findViewById(id);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_story_continuation, container, false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        continuationSaveListener = null;
    }

    public interface OnContinuationSaveListener {
        void onContinuationSaveLister(StoryContinuation continuation);
    }
}
