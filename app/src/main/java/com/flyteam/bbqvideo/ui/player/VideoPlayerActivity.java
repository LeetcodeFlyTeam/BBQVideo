package com.flyteam.bbqvideo.ui.player;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.flyteam.bbqvideo.R;

public class VideoPlayerActivity extends AppCompatActivity implements SurfaceHolder.Callback2{

    private static final String SAMPLE = Environment.getExternalStorageDirectory() + "/video.mp4";
    private SurfaceView surfaceView;
    private PlayerThread player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        surfaceView = findViewById(R.id.video_player_surfaceView);
        surfaceView.getHolder().addCallback(this);
    }

    @Override
    public void surfaceRedrawNeeded(SurfaceHolder holder) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (player == null) {
            player = new PlayerThread();
            boolean isInited = player.init(holder.getSurface(), SAMPLE);
            if (isInited) {
                player.start();
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (player != null) {
            player.interrupt();
        }
    }
}