package com.example.pictureinpicture;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    //video urls links//
    private static final String videoUrlOne = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";
    private static final String videoUrlTwo = "https://www.youtube.com/watch?v=AnNZhNXYuoY";
    private static final String videoUrlThree = "https://www.youtube.com/watch?v=GvSEczMmx2k";


    private Button videoOneBtn, videoTwoBtn, videoThreeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //UI views
        videoOneBtn = findViewById(R.id.videoOneBtn);
        videoTwoBtn = findViewById(R.id.videoTwoBtn);
        videoThreeBtn = findViewById(R.id.videoThreeBtn);

        //click for videos
        videoOneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playVideo(videoUrlOne);


             }
        });
        videoTwoBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                playVideo(videoUrlTwo);

            }
        });
        videoThreeBtn.setOnClickListener (new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                playVideo(videoUrlThree);

            }
        });
   }
   private void playVideo(String videoUrl){
        //Intent to start video url
       Intent intent = new Intent(MainActivity.this, PIPActivity.class);
       intent.putExtra("videoURL", videoUrl);
       startActivity(intent);
   }
}