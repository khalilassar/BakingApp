package com.example.bakingapp.Fragments;


import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.bakingapp.BakingViewModel;
import com.example.bakingapp.MainActivity;
import com.example.bakingapp.R;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;


public class RecipeVideoFragment extends Fragment {
    BakingViewModel mBakingViewModel;
    PlayerView playerView;
    SimpleExoPlayer player;
    private boolean playWhenReady = true;
    private long playbackPosition = 0;
    private int windowIndex = 0;
    private static final String POSITION_KEY = "POS";
    private static final String PLAY_STATE = "STATE";
    private static final String PLAY_WINDOW = "WINDOWS";


    public RecipeVideoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(POSITION_KEY, playbackPosition);
        outState.putBoolean(PLAY_STATE, playWhenReady);
        outState.putInt(PLAY_WINDOW, windowIndex);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            playbackPosition = savedInstanceState.getLong(POSITION_KEY);
            playWhenReady = savedInstanceState.getBoolean(PLAY_STATE);
            windowIndex = savedInstanceState.getInt(PLAY_WINDOW);
            Log.d("ddddd", "not null pos is" + playbackPosition);
        }

        // Inflate the layout for this fragment
        mBakingViewModel = ViewModelProviders.of(getActivity()).get(BakingViewModel.class);
        View v = inflater.inflate(R.layout.fragment_recipe_video, container, false);
        playerView = v.findViewById(R.id.player_view);
        initializePlayer();
        mBakingViewModel.getmSelectedRecipeDetail().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer == 0) {
                    playerView.setVisibility(View.GONE);

                } else if (!mBakingViewModel.getmRecipes().getValue().get(mBakingViewModel.getmSelectedRecipe().getValue()).getSteps().get(integer - 1).getVideoURL().isEmpty()) {
                    buildMediaSource(Uri.parse(mBakingViewModel.getmRecipes().getValue().get(mBakingViewModel.getmSelectedRecipe().getValue()).getSteps().get(integer - 1).getVideoURL()));
                    playerView.setVisibility(View.VISIBLE);

                } else if (!mBakingViewModel.getmRecipes().getValue().get(mBakingViewModel.getmSelectedRecipe().getValue()).getSteps().get(integer - 1).getThumbnailURL().isEmpty()) {
                    buildMediaSource(Uri.parse(mBakingViewModel.getmRecipes().getValue().get(mBakingViewModel.getmSelectedRecipe().getValue()).getSteps().get(integer - 1).getThumbnailURL()));
                    playerView.setVisibility(View.VISIBLE);
                } else {
                    playerView.setVisibility(View.GONE);
                }
            }
        });
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //to make sure HeightToUseIfLandScape value is set "set in activity on create"
        if (((MainActivity) getActivity()).isLandscapeOrientation() && !((MainActivity) getActivity()).isTablentView() && ((MainActivity) getActivity()).getHeightToUseIfLandScape() != 0) {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) playerView.getLayoutParams();
            params.width = params.MATCH_PARENT;
            params.height = ((MainActivity) getActivity()).getHeightToUseIfLandScape();
            playerView.setLayoutParams(params);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //    if (Util.SDK_INT <= 23) {
        releasePlayer();
        //   }
    }

    @Override
    public void onStop() {
        super.onStop();
        // if (Util.SDK_INT > 23) {
        releasePlayer();
        // }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    private void initializePlayer() {
        if (player == null) {
            player = ExoPlayerFactory.newSimpleInstance(
                    getActivity(),
                    new DefaultRenderersFactory(getActivity().getApplicationContext()),
                    new DefaultTrackSelector(),
                    new DefaultLoadControl());
            playerView.setPlayer(player);
            player.seekTo(windowIndex, playbackPosition);
            player.setPlayWhenReady(playWhenReady);
            Log.d("ddddd", "initilize pos is" + playbackPosition);

        }
    }

    private void buildMediaSource(Uri mUri) {

        MediaSource videoSource = new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory(getString(R.string.app_name))).
                createMediaSource(mUri);
        initializePlayer();
        player.seekTo(windowIndex, playbackPosition);
        player.setPlayWhenReady(playWhenReady);
        player.prepare(videoSource);
        Log.d("ddddd", "buildMediaSource pos is" + playbackPosition);


    }

    private void releasePlayer() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            playbackPosition = player.getCurrentPosition();
            windowIndex = player.getCurrentWindowIndex();

            player.release();
            player = null;
        }
    }

}
