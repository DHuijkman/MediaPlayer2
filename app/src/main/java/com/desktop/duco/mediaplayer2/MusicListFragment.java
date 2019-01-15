package com.desktop.duco.mediaplayer2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;


import com.desktop.duco.mediaplayer2.objects.SongItem;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.listeners.EventHook;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

public class MusicListFragment extends Fragment implements SongItem.SimpleItemClickDelegate,BackgroundPlayer.SetAdapterDelegate , BackgroundPlayer.MissingDataDelegate{

    RecyclerView recyclerView;
    ImageView optionDrop;
    FastItemAdapter adapter;

    SongItem lastClicked;
    Context mContext;
    Intent volumeSettings;

    @Override
    public void onAttach(Context context) {
        if(isAdded())
        {
            return;
        }
        super.onAttach(context);
        this.mContext = context;
        BackgroundPlayer.getInstance().registerMissingDataDelegate(this);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_layout,null,false);

        optionDrop = v.findViewById(R.id.iv_options);

        recyclerView = v.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new FastItemAdapter();
        recyclerView.setAdapter(adapter);

        adapter.withEventHook((EventHook) new SongItem.ClickDelegate<>(new WeakReference<MusicListFragment>(this)));
        adapter.withEventHook((EventHook) new SongItem.OptionClickDelegate<>(new WeakReference<MusicListFragment>(this)));
        runApp();
        BackgroundPlayer.getInstance().registerAdapterDelegate(this);

        return v;
    }



    public void runApp(){
        BackgroundPlayer.getInstance().updateSongList();
    }



    @Override
    public void onSimpleItemClick(SongItem songItem, View v) {
        int position = 0;
        if(lastClicked != null) {
            position = adapter.getPosition(lastClicked);
            lastClicked.shouldAnimate = false;
            adapter.set(position,lastClicked);
        }
        lastClicked = songItem;
        position = adapter.getPosition(songItem);
        songItem.shouldAnimate = true;
        adapter.set(position, songItem);


        BackgroundPlayer.getInstance().startSong(songItem.getSongFile(),position);
    }

    @Override
    public void onOptionClick(final SongItem songItem, View v) {
        final int position = adapter.getPosition(songItem);
        PopupMenu menu = new PopupMenu(mContext, v);
        menu.getMenuInflater().inflate(R.menu.popupmenu,menu.getMenu());
        menu.show();

        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.settings:
                        volumeSettings = new Intent(mContext,Settings.class);
                        startActivity(volumeSettings);
                        break;
                    case R.id.delete:
                        onAlert(songItem.getSongFile(),songItem.getSongTitle(), position);
                        //songFiles.get(position).delete();
                        break;
                    case R.id.share:
                        String sharePath = songItem.getSongFile().getPath();
                        Uri uri = Uri.parse(sharePath);
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("audio/*");
                        share.putExtra(Intent.EXTRA_STREAM, uri);
                        startActivity(Intent.createChooser(share, "Share Sound File"));
                        break;
                }
                return false;
            }
        });


    }

    public void onAlert(final File song, final String name, final int i){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        boolean b = song.delete();
                        if(b){
                            adapter.remove(i);
                            BackgroundPlayer.getInstance().updateSongList();

                            Toast.makeText(mContext, "deleted: " + name ,
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(mContext," failed",Toast.LENGTH_LONG).show();
                        }

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    @Override
    public void onUpdateAdapter(SongItem songItem, int i) {
        adapter.set(i,songItem);
    }

    @Override
    public void onFindSongRequest(List<SongItem> item) {
        adapter.setNewList(item);
    }
}
