package com.desktop.duco.mediaplayer2;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class Player extends AppCompatActivity implements View.OnClickListener{

    static MediaPlayer mp;
    ArrayList<File> mySongs;
    Uri u;
    Thread updateSeekBar;
    Boolean replay = false;
    volatile Boolean stop = false;

    SeekBar sb;
    ImageView btPlay, btFF, btFB, btNxt, btPv, btReplay;
    TextView tvET, tvRT, tvTitle;

    int position,timeMin,timeSec,totalDuration,max;
    volatile int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);


        btPlay = (ImageView) findViewById(R.id.btPlay);
        btFF = (ImageView) findViewById(R.id.btFF);
        btFB = (ImageView) findViewById(R.id.btFB);
        btNxt = (ImageView) findViewById(R.id.btNxt);
        btPv = (ImageView) findViewById(R.id.btPv);
        btReplay = (ImageView) findViewById(R.id.btReplay);
        tvET = (TextView) findViewById(R.id.tvET);
        tvRT = (TextView) findViewById(R.id.tvRT);
        tvTitle = (TextView) findViewById(R.id.tvTitle);


        //the seekbar and its sepperate thread to keep an eye on the progress and display it
        sb = (SeekBar) findViewById(R.id.seekBar);
        updateSeekBar = new Thread(){
            @Override
            public void run(){
                totalDuration = mp.getDuration();
                currentPosition = 0;

                while(currentPosition < totalDuration && !stop){
                    try {

                        currentPosition = mp.getCurrentPosition();
                        timeMin = currentPosition / 60000;
                        timeSec = (currentPosition / 1000) % 60;
                        sb.setProgress(currentPosition);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvET.setText(timeMin + ":" + timeSec);
                                if(currentPosition >= totalDuration && !replay){
                                    onNextSong();

                                }else if (currentPosition >= totalDuration && replay){

                                    currentPosition = 0;
                                }

                            }
                        });
                        sleep(1000);
//                        if(currentPosition >= totalDuration){
//                            currentPosition = 0;
//
//                        }


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
        btReplay.setOnClickListener(this);


        Intent i = getIntent();
        Bundle b = i.getExtras();
        mySongs = (ArrayList) b.getParcelableArrayList("songlist");
        position = b.getInt("pos",0);
        tvTitle.setText(mySongs.get(position).getName());

        if(mp != null){
            mp.stop();
            mp.release();
        }

        u = Uri.parse( mySongs.get(position).toString() );
        mp = MediaPlayer.create(getApplicationContext(),u);
        mp.start();

        updateSeekBar.start();
        max = mp.getDuration();
        sb.setMax(max);
        displayTime(max);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //this allows you to skip to a certain point in the song(again in milliseconds)
                mp.seekTo(seekBar.getProgress());
            }
        });
    }

    @Override
    public void onBackPressed(){
        stop = true;
        finish();


    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btPlay:
                //if music is playing pause the music and change the image vise versa for the else part
                if(mp.isPlaying()){
                    btPlay.setImageResource(android.R.drawable.ic_media_play);
                    mp.pause();
                }else {
                    btPlay.setImageResource(android.R.drawable.ic_media_pause);
                    mp.start();
                }
                break;
            case R.id.btFF:
                //skip 5 seconds ahead in the song
                mp.seekTo(mp.getCurrentPosition()+5000);
                break;
            case R.id.btFB:
                //go back 5 seconds
                mp.seekTo(mp.getCurrentPosition()-5000);
                break;
            case R.id.btNxt:
                onNextSong();
                break;
            case R.id.btPv:
                //stop current song and release resources
                mp.stop();
                mp.release();
                //go down position by one
                position = (position-1<0)? mySongs.size() -1: position -1;
                tvTitle.setText(mySongs.get(position).getName());
                //start new song
                u = Uri.parse( mySongs.get(position).toString() );
                mp = MediaPlayer.create(getApplicationContext(),u);
                mp.start();
                //set new max value for seekbar
                max = mp.getDuration();
                sb.setMax(max);
                displayTime(max);
                currentPosition = 0;
                totalDuration = max;
                //update play button
                btPlay.setImageResource(android.R.drawable.ic_media_pause);
                break;
            case R.id.btReplay:
                if(!replay){
                    replay = true;
                    mp.setLooping(true);
                    btReplay.setImageResource(android.R.drawable.ic_media_play);
                } else {
                    replay = false;
                    mp.setLooping(false);
                    btReplay.setImageResource(android.R.drawable.ic_menu_rotate);
                }
                break;
        }
    }

    public void onNextSong(){
        //stop current song and release resources

        mp.stop();
        mp.release();
        //move up the position by one
        position = (position+1)%mySongs.size();
        tvTitle.setText(mySongs.get(position).getName());
        //start new song
        u = Uri.parse( mySongs.get(position).toString() );
        mp = MediaPlayer.create(getApplicationContext(),u);
        mp.start();
        if (replay){
            mp.setLooping(true);
        }

        max = mp.getDuration();
        sb.setMax(max);
        displayTime(max);
        currentPosition = 0;
        totalDuration = max;
        //update play button
        btPlay.setImageResource(android.R.drawable.ic_media_pause);
    }
    public void displayTime(int maxTime){
        int timeMRT = maxTime / 60000;
        int timeSRT = (maxTime / 1000) % 60;
        tvRT.setText(timeMRT + ":" + timeSRT);
    }
}
