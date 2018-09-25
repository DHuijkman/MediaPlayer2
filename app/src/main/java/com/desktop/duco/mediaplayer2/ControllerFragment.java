package com.desktop.duco.mediaplayer2;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class ControllerFragment extends Fragment implements BackgroundPlayer.UpdateDelegate, View.OnClickListener {

    private Context mContext;


    SeekBar sb;
    ImageView btPlay, btFF, btFB, btNxt, btPv, btReplay;
    int iCTime, iMTime;
    String sCTime, sMTime,sTitle;

    public TextView tvET, tvRT, tvTitle;

    @Override
    public void onAttach(Context context) {
        if(isAdded())
        {
            return;
        }
        super.onAttach(context);
        this.mContext = context;
        BackgroundPlayer.getInstance().registerDelegate(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         View v = inflater.inflate(R.layout.activity_player,null,false);



        btPlay = (ImageView) v.findViewById(R.id.btPlay);
        btFF = (ImageView) v.findViewById(R.id.btFF);
        btFB = (ImageView) v.findViewById(R.id.btFB);
        btNxt = (ImageView) v.findViewById(R.id.btNxt);
        btPv = (ImageView) v.findViewById(R.id.btPv);
        btReplay = (ImageView) v.findViewById(R.id.btReplay);
        tvET = (TextView) v.findViewById(R.id.tvET);
        tvRT = (TextView) v.findViewById(R.id.tvRT);
        tvTitle = (TextView) v.findViewById(R.id.tvTitle);

        sb = (SeekBar) v.findViewById(R.id.seekBar);

        btPlay.setOnClickListener(this);
        btFF.setOnClickListener(this);
        btFB.setOnClickListener(this);
        btNxt.setOnClickListener(this);
        btPv.setOnClickListener(this);
        btReplay.setOnClickListener(this);

        iCTime = BackgroundPlayer.getInstance().getStartIData()[0];
        iMTime = BackgroundPlayer.getInstance().getStartIData()[1];

        sCTime = BackgroundPlayer.getInstance().getStartSData()[0];
        sMTime = BackgroundPlayer.getInstance().getStartSData()[1];
        sTitle = BackgroundPlayer.getInstance().getStartSData()[2];

        sb.setMax(iMTime);
        sb.setProgress(iCTime);

        tvET.setText(sCTime);
        tvRT.setText(sMTime);
        tvTitle.setText(sTitle);

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                BackgroundPlayer.getInstance().moveWithSeekBar(seekBar.getProgress());
            }
        });



        return v;
    }
    @Override
    public void onStart() {
        super.onStart();

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btPlay:
                BackgroundPlayer.getInstance().setPlay();
                break;
            case R.id.btFF:
                //skip 5 seconds ahead in the song
                BackgroundPlayer.getInstance().moveFiveSec(5000);
                break;
            case R.id.btFB:
                //go back 5 seconds
                BackgroundPlayer.getInstance().moveFiveSec(-5000);
                break;
            case R.id.btNxt:
                nextSong();
                break;
            case R.id.btPv:
                prevSong();
                break;
            case R.id.btReplay:
                BackgroundPlayer.getInstance().setReplay();
                break;
        }
    }

    public void nextSong(){
        //stop current song and release resources
        BackgroundPlayer.getInstance().setNext();
    }

    public void prevSong(){
        BackgroundPlayer.getInstance().setPrev();

    }

    @Override
    public void onUpdateTimer(int time,String etTime) {
        sb.setProgress(time);
        tvET.setText(etTime);
    }

    @Override
    public void onNewSong(int startTime, int endTime,String startTimer, String duration , String title) {

        sb.setMax(endTime);
        sb.setProgress(startTime);
        tvET.setText(startTimer);
        tvRT.setText(duration);
        tvTitle.setText(title);
    }


    @Override
    public void onPlayImgUpdate(int idPlay) {
        btPlay.setImageResource(idPlay);
    }

    @Override
    public void onReplayImgUpdate(int idReplay) {
        btReplay.setImageResource(idReplay);
    }
}
