package com.example.martinosama.bakingapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StepDetailFragment extends Fragment {
    @BindView(R.id.videoPlayer) SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer player = null;
    @BindView(R.id.recipeInstruction) TextView textView;
    @BindView(R.id.thumbnail)
    ImageView imageView;
    private String instruction ="",url= "",imageUrl ="";
    private int position= 0,maxSize ;
     @BindView(R.id.nextBtn) Button nextBtn;
     @BindView(R.id.backBtn) Button backBtn;
    private Bundle extras;
    public StepDetailFragment(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
     View rootView = inflater.inflate(R.layout.step_detail_fragmentl,container,false);
     ButterKnife.bind(this,rootView);
        extras = getArguments();
     if(savedInstanceState != null){
         position = savedInstanceState.getInt("POSITION");
         maxSize = savedInstanceState.getInt("MAX_SIZE");
         instruction = savedInstanceState.getString("DESCRIPTION");
         url = savedInstanceState.getString("VIDEO_URL");
         imageUrl = savedInstanceState.getString("THUMBNAIL_URL");
     }else {
         position = extras.getInt("POSITION");
         maxSize = extras.getStringArrayList("DESCRIPTION").size();
         instruction = extras.getStringArrayList("DESCRIPTION").get(position);
         url = extras.getStringArrayList("VIDEO_URL").get(position);
         imageUrl = extras.getStringArrayList("THUMBNAIL_URL").get(position);
     }
         if(!imageUrl.equals("")){
             Picasso.get().load(imageUrl).into(imageView);
         }else   imageView.setVisibility(View.GONE);

        try {
                 preparingVideo();
             if (savedInstanceState != null) {
                 player.seekTo(savedInstanceState.getLong("PLAYER_POSITION"));
                 player.setPlayWhenReady(savedInstanceState.getBoolean("CURRENT_STATE"));
             } else
                 player.setPlayWhenReady(true);

         }catch (Exception e){}

        textView.setText(instruction);
     nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position < maxSize-1) {
                    position++;
                    StepDetailFragment stepDetailFragment= new StepDetailFragment();
                    extras.putInt("POSITION",position);
                    stepDetailFragment.setArguments(extras);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.detailed_step, stepDetailFragment).commit();
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position > 0) {
                    position--;
                    StepDetailFragment stepDetailFragment= new StepDetailFragment();

                    extras.putInt("POSITION",position);
                    stepDetailFragment.setArguments(extras);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.detailed_step, stepDetailFragment).commit();
                }

            }
        });
        return rootView;
    }

    @Override
    public void onPause() {
        if(player != null) {
            player.release();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("DESCRIPTION",instruction);
        outState.putString("VIDEO_URL",url);
        outState.putInt("POSITION",position);
        outState.putInt("MAX_SIZE",maxSize);
        outState.putString("THUMBNAIL_URL",imageUrl);
        outState.putLong("PLAYER_POSITION",player.getCurrentPosition());
        outState.putBoolean("CURRENT_STATE",player.getPlayWhenReady());
        super.onSaveInstanceState(outState);
    }
    private void preparingVideo(){
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
        player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
        Uri videoURI = Uri.parse(url);
        DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        MediaSource mediaSource = new ExtractorMediaSource(videoURI, dataSourceFactory, extractorsFactory, null, null);
        simpleExoPlayerView.setPlayer(player);
        player.prepare(mediaSource);
    }
}
