package com.desktop.duco.mediaplayer2;

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
    //im not gonna explain these -.-
    static MediaPlayer mp;
    ArrayList<File> mySongs;
    Uri u;
    Thread updateSeekBar;

    SeekBar sb;
    ImageView btPlay, btFF, btFB, btNxt, btPv;

    int position;

    //init everything when the activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        //the buttons connected to their imageviews
        btPlay = (ImageView) findViewById(R.id.btPlay);
        btFF = (ImageView) findViewById(R.id.btFF);
        btFB = (ImageView) findViewById(R.id.btFB);
        btNxt = (ImageView) findViewById(R.id.btNxt);
        btPv = (ImageView) findViewById(R.id.btPv);

        //the seekbar and its sepperate thread to keep an eye on the progress and display it
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
        //the click listeners to control the music
        btPlay.setOnClickListener(this);
        btFF.setOnClickListener(this);
        btFB.setOnClickListener(this);
        btNxt.setOnClickListener(this);
        btPv.setOnClickListener(this);

        //basicaly the array of songs is brought here from the music list to the controller
        //simply speaking
        Intent i = getIntent();
        Bundle b = i.getExtras();
        mySongs = (ArrayList) b.getParcelableArrayList("songlist");
        position = b.getInt("pos",0);

        //if the mediaplayer equals null the stop the music and release its resources this is used
        //to stop a song when you select a new one
        if(mp != null){
            mp.stop();
            mp.release();
        }
        //this part till mp.start basicaly starts the song after selecting it if you dont get it
        // google Uri and MediaPlayer.create
        u = Uri.parse( mySongs.get(position).toString() );
        mp = MediaPlayer.create(getApplicationContext(),u);
        mp.start();
        //start the thread for the seekbar
        updateSeekBar.start();
        //set the max value (how long the song is in milliseconds) for the seekbar
        sb.setMax(mp.getDuration());
        //set the listeners for the seekbar
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

    //onclick method for the control buttons
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
                //stop current song and release resources
                mp.stop();
                mp.release();
                //move up the position by one
                position = (position+1)%mySongs.size();
                //start new song
                u = Uri.parse( mySongs.get(position).toString() );
                mp = MediaPlayer.create(getApplicationContext(),u);
                mp.start();
                //set new max value for the seekbar
                sb.setMax(mp.getDuration());
                //update play button
                btPlay.setImageResource(android.R.drawable.ic_media_pause);
                break;
            case R.id.btPv:
                //stop current song and release resources
                mp.stop();
                mp.release();
                //go down position by one
                position = (position-1<0)? mySongs.size() -1: position -1;
                //start new song
                u = Uri.parse( mySongs.get(position).toString() );
                mp = MediaPlayer.create(getApplicationContext(),u);
                mp.start();
                //set new max value for seekbar
                sb.setMax(mp.getDuration());
                //update play button
                btPlay.setImageResource(android.R.drawable.ic_media_pause);
                break;
        }
    }
}
