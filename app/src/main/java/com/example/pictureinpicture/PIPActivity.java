package com.example.pictureinpicture;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PictureInPictureParams;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Rational;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.VideoView;

public class PIPActivity extends AppCompatActivity {

    private Uri videoUri;
    private static final String TAG = "PIP_TAG";

    private VideoView videoView;
    private ImageButton pipBtn;

    private ActionBar actionBar;

    private PictureInPictureParams.Builder pictureInPictureParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pipactivity);

        //actionbar
        actionBar = getSupportActionBar();

        //init UI views
        videoView = findViewById(R.id.videoView);
        pipBtn = findViewById(R.id.pipBtn);
        setVideoView(getIntent());

        //init PictureInPictureParams
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            pictureInPictureParams = new PictureInPictureParams.Builder();
        }

        //handle click, enter pip mode
        pipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pictureInPictureMode();
            }
        });

    }

    private void setVideoView(Intent intent) {
        String videoURL = intent.getStringExtra("videoURL");
        Log.d(TAG, "setVideoView: URL:" + videoURL);

        //Media controller for play, pause, seek the bar, time etc
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);

        videoUri = Uri.parse(videoURL);

        //set media controller
        videoView.setMediaController(mediaController);

        //set video uri
        videoView.setVideoURI(videoUri);

        //add video prepare listener
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                //when video is ready play it
                Log.d(TAG, "onPrepared: Video Prepared, Playing...");
                mediaPlayer.start();
            }
        });
    }

    private void pictureInPictureMode() {
        //Require Android 0 and higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d(TAG, "pictureInPictureMode: Support PIP");

            //setup height and width of PIP window
            Rational aspectRation = new Rational(videoView.getWidth(), videoView.getHeight());
            pictureInPictureParams.setAspectRatio(aspectRation).build();
            enterPictureInPictureMode(pictureInPictureParams.build());
        }
        else {
            Log.d(TAG, "pictureInPictureMode: Doesn't support PIP");
        }
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        //call when user press Home button, enter PIP mode, requires Android
        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.N){
            if (!isInPictureInPictureMode()){
                Log.d(TAG, "onUserLeaveHint: was not in PIP");
                pictureInPictureMode();
            }
            else{
                Log.d(TAG, "onUserLeaveHint: Already in PIP");
            }
        }
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);
        if (isInPictureInPictureMode){
            Log.d(TAG, "onPictureInPictureModeChanged: Entered PIP");
            //hide PIP button and actionbar
            pipBtn.setVisibility(View.GONE);
            actionBar.hide();
        }
        else{
            Log.d(TAG, "onPictureInPictureModeChanged: Exited PIP");
            //show PIP button
            pipBtn.setVisibility(View.VISIBLE);
            actionBar.show();
        }
    }
    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent: Play new video");
        setVideoView(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (videoView.isPlaying()){
            videoView.stopPlayback();
        }
    }
}