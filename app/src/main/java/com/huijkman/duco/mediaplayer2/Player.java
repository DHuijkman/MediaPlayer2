package com.huijkman.duco.mediaplayer2;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import java.io.File;
import java.util.ArrayList;

public class Player extends AppCompatActivity implements View.OnClickListener{

    static MediaPlayer mp;
    ArrayList<File> mySongs;
    Uri u;
    Thread updateSeekBar;

    SeekBar sb;
    ImageView btPlay, btFF, btFB, btNxt, btPv;

    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        btPlay = (ImageView) findViewById(R.id.btPlay);
        btFF = (ImageView) findViewById(R.id.btFF);
        btFB = (ImageView) findViewById(R.id.btFB);
        btNxt = (ImageView) findViewById(R.id.btNxt);
        btPv = (ImageView) findViewById(R.id.btPv);

        sb = (SeekBar) findViewById(R.id.seekBar);
        updateSeekBar = new Thread(){
            @Override
            public void run(){
                int totalDuration = mp.getDuration();
                int currentPosition = 0;
                while(currentPosition < totalDuration){
                    try {
                        sleep(500);
                        currentPosition = mp.getCurrentPosition();
                        sb.setProgress(currentPosition);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };

        btPlay.setOnClickListener(this);
        btFF.setOnClickListener(this);
        btFB.setOnClickListener(this);
        btNxt.setOnClickListener(this);
        btPv.setOnClickListener(this);

        Intent i = getIntent();
        Bundle b = i.getExtras();
        mySongs = (ArrayList) b.getParcelableArrayList("songlist");
        position = b.getInt("pos",0);

        if(mp != null){
            mp.stop();
            mp.release();
        }

        u = Uri.parse( mySongs.get(position).toString() );
        mp = MediaPlayer.create(getApplicationContext(),u);
        mp.start();
        updateSeekBar.start();

        sb.setMax(mp.getDuration());

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mp.seekTo(seekBar.getProgress());
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btPlay:
                if(mp.isPlaying()){
                    btPlay.setImageResource(android.R.drawable.ic_media_play);
                    mp.pause();
                }else {
                    btPlay.setImageResource(android.R.drawable.ic_media_pause);
                    mp.start();
                }
                break;
            case R.id.btFF:
                mp.seekTo(mp.getCurrentPosition()+5000);
                break;
            case R.id.btFB:
                mp.seekTo(mp.getCurrentPosition()-5000);
                break;
            case R.id.btNxt:
                mp.stop();
                mp.release();
                position = (position+1)%mySongs.size();
                u = Uri.parse( mySongs.get(position).toString() );
                mp = MediaPlayer.create(getApplicationContext(),u);
                mp.start();
                sb.setMax(mp.getDuration());
                btPlay.setImageResource(android.R.drawable.ic_media_pause);
                break;
            case R.id.btPv:
                mp.stop();
                mp.release();
                position = (position-1<0)? mySongs.size() -1: position -1;
                u = Uri.parse( mySongs.get(position).toString() );
                mp = MediaPlayer.create(getApplicationContext(),u);
                mp.start();
                sb.setMax(mp.getDuration());
                btPlay.setImageResource(android.R.drawable.ic_media_pause);
                break;
        }
    }
}
