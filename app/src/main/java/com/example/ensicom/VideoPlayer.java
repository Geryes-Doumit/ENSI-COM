package com.example.ensicom;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.VideoView;

import java.util.Locale;

public class VideoPlayer extends AppCompatActivity {
    private VideoView videoView;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        videoView = findViewById(R.id.videoPlayer);
        ImageButton playButton = findViewById(R.id.playVideoButton);
        String videoUrl = getIntent().getStringExtra("videoUrl");
        videoView.setVideoURI(Uri.parse(videoUrl));
        videoView.setOnPreparedListener(mp -> mp.setLooping(true));
        videoView.start();
        videoView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (videoView.isPlaying()) {
                    videoView.pause();
                    playButton.setVisibility(View.VISIBLE);
                }
            }
            return false;
        });
        playButton.setOnClickListener(v -> {
            videoView.start();
            playButton.setVisibility(View.INVISIBLE);
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Release the resources used by the VideoView
        videoView.stopPlayback();
    }
}