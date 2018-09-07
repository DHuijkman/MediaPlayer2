package com.desktop.duco.mediaplayer2;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BackgroundPlayer {



    private static BackgroundPlayer ourInstance;
    private volatile Boolean stop = false;
    private volatile int currentPosition;
    private int position = 0;
    private int totalDuration;
    private MediaPlayer mp;
    private Context mContext;
    private Uri u;
    private Boolean replay = false;
    private String progressTime,maxTime;
    private String songName;

    private ArrayList<UpdateDelegate> delegates = new ArrayList<>();
    private ArrayList<SetAdapterDelegate> adapterDelegates = new ArrayList<>();
    private List<SongItem> songItems;

    public void registerDelegate(UpdateDelegate delegate) {
        if(!delegates.contains(delegate)){
            delegates.add(delegate);
        }
    }
    public void registerAdapterDelegate(SetAdapterDelegate setAdapterDelegate){
        if(!adapterDelegates.contains(setAdapterDelegate)){
            adapterDelegates.add(setAdapterDelegate);
        }
    }

    public interface UpdateDelegate {
        void onUpdateTimer(int time,String etTime);
        void onNewSong(int startTime, int endTime,String startTimer, String duration , String title);
        void onPlayImgUpdate(int idPlay);
        void onReplayImgUpdate(int idReplay);
    }

    public interface SetAdapterDelegate {
        void onUpdateAdapter(SongItem songItem,int i);
    }
    private BackgroundPlayer() {
        songItems = updateSongList();
    }

    public static BackgroundPlayer getInstance() {
        if(ourInstance == null){
            ourInstance = new BackgroundPlayer();
        }
        return ourInstance;

    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
        startSong(songItems.get(position).getSongFile(),position);
        mediaControl();
        setPlay();
    }

    public List<SongItem> updateSongList() {
        songItems = findSongs(Environment.getExternalStorageDirectory());
        return songItems;
    }

    private List<SongItem> findSongs(File root) {
        ArrayList<SongItem> al = new ArrayList<>();
        File[] files = root.listFiles();
        for (File singleFile : files) {
            if (singleFile.isDirectory() && !singleFile.isHidden()) {
                al.addAll(findSongs(singleFile));
            } else {

                if (singleFile.getName().endsWith(".mp3") && !singleFile.getName().startsWith(".")) {
                    al.add(new SongItem(singleFile));
                }
            }
        }
        return al;
    }

    private void mediaControl() {
        Thread updateSeekBar = new Thread() {
            @Override
            public void run() {
                Activity a = (Activity) mContext;
                while (currentPosition < totalDuration) {
                    try {
                        if (!stop) {
                            currentPosition = mp.getCurrentPosition();
                            if (a != null) {
                                a.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        for (UpdateDelegate delegate : delegates) {
                                            delegate.onUpdateTimer(currentPosition, elapsedTime());
                                        }
                                    }
                                });
                            }
                            if (currentPosition >= totalDuration && !replay) {
                                if (a != null) {
                                    a.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            setNext();
                                        }
                                    });
                                }
                            } else if (currentPosition >= totalDuration && replay) {
                                currentPosition = 0;
                            }
                            sleep(1000);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        updateSeekBar.start();
    }

    public void startSong(File song, int position){
        stop = true;
        if(mp != null){
            mp.stop();
            mp.release();
        }
        this.position = position;
        u = Uri.parse(song.toString());
        mp = MediaPlayer.create(mContext, u);
        mp.start();
        if (replay){
            mp.setLooping(true);
        }
        totalDuration = mp.getDuration();
        currentPosition = 0;
        songName = song.getName().replace(".mp3","");
        for(UpdateDelegate delegate : delegates){
            delegate.onNewSong(currentPosition, totalDuration ,elapsedTime(), displayTime(),songName);
            delegate.onPlayImgUpdate(android.R.drawable.ic_media_pause);
        }
        stop = false;
    }

    private String displayTime(){
        int timeMRT = totalDuration / 60000;
        int timeSRT = (totalDuration / 1000) % 60;
        maxTime = timeMRT + ":" + timeSRT;
        return maxTime;
    }
    private String elapsedTime(){
        int timeMRT = currentPosition / 60000;
        int timeSRT = (currentPosition / 1000) % 60;
        progressTime = timeMRT + ":" + timeSRT;
        return progressTime;
    }

    public void setNext(){
        songItems.get(position).shouldAnimate = false;
        updateAdapter(songItems.get(position), position);
        position = (position+1)%songItems.size();
        songItems.get(position).shouldAnimate = true;
        updateAdapter(songItems.get(position), position);
        startSong(songItems.get(position).getSongFile(),position);
    }

    public void setPrev(){
        position = (position-1<0)? songItems.size() -1: position -1;
        startSong(songItems.get(position).getSongFile(),position);
    }

    public void setPlay(){
        if(mp.isPlaying()){
            for(UpdateDelegate delegate : delegates){
                delegate.onPlayImgUpdate(android.R.drawable.ic_media_play);
            }
            mp.pause();
        }else {
            for(UpdateDelegate delegate : delegates){
                delegate.onPlayImgUpdate(android.R.drawable.ic_media_pause);
            }
            mp.start();
        }
    }

    public void moveFiveSec(int time){
        mp.seekTo(mp.getCurrentPosition() + time);
    }

    public void setReplay(){
        if(!replay){
            replay = true;
            mp.setLooping(true);
            for(UpdateDelegate delegate : delegates){
                delegate.onReplayImgUpdate(android.R.drawable.ic_media_play);
            }
        } else {
            replay = false;
            mp.setLooping(false);
            for(UpdateDelegate delegate : delegates){
                delegate.onReplayImgUpdate(android.R.drawable.ic_menu_rotate);
            }
        }
    }

    public void moveWithSeekBar(int progress) {
        mp.seekTo(progress);
    }

    public String[] getStartSData(){
        String[] strings = {elapsedTime(),displayTime(),songName};
        return strings;
    }
    public int[] getStartIData(){
        int[] ints = {currentPosition,totalDuration};
        return ints;
    }

    public void updateAdapter(SongItem songItem, int pos){
        for(SetAdapterDelegate setAdapterDelegate : adapterDelegates){
            setAdapterDelegate.onUpdateAdapter(songItem, pos);
        }
    }
}
