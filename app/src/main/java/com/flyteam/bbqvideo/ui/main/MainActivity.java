package com.flyteam.bbqvideo.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.flyteam.bbqvideo.R;
import com.flyteam.bbqvideo.ui.player.VideoPlayerActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_video_player).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_video_player:
                Intent intent = new Intent(MainActivity.this, VideoPlayerActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}