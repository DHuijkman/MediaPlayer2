package com.desktop.duco.mediaplayer2;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.listeners.EventHook;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SimpleItem.SimpleItemClickDelegate {
    private static final int MY_READ_REQUEST = 1;
    private static final int MY_WRITE_REQUEST = 2;
    ListView lv;
    RecyclerView recyclerView;
    ImageView optionDrop;
    String[] items;
    Intent playerActivity, volumeSettings;
    FastItemAdapter adapter;
    ArrayList<File> songFiles;
    SimpleItem lastClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        optionDrop = findViewById(R.id.iv_options);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new FastItemAdapter();
        recyclerView.setAdapter(adapter);

        adapter.withEventHook((EventHook) new SimpleItem.ClickDelegate<>(new WeakReference<MainActivity>(this)));
        adapter.withEventHook((EventHook) new SimpleItem.OptionClickDelegate<>(new WeakReference<MainActivity>(this)));


        //pop up a permission request if needed if not continue running the the rest of the app
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)){
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_READ_REQUEST);
            }else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_READ_REQUEST);
            }
            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_WRITE_REQUEST);
            }else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_WRITE_REQUEST);
            }
        }else {
            runApp();
        }
    }

    public void runApp(){

        songFiles = findSongs(Environment.getExternalStorageDirectory());
        List<SimpleItem> simpleItems = new ArrayList<>();


        for(int i = 0; i <songFiles.size(); i++){

            simpleItems.add(new SimpleItem(songFiles.get(i).getName().replace(".mp3","")));
        }


        adapter.setNewList(simpleItems);
    }

    public ArrayList<File> findSongs(File root){
        //make new array which will hold return value(the songs that have been found)
        ArrayList<File> al = new ArrayList<File>();
        //make array with type File and fill it with the files from the directory that has been giving
        File[] files = root.listFiles();
        //iterate through files and recursively look through all(including subdirectories) files
        for(File singleFile : files){
            //check subdirectory as long as its not a hidden file
            if(singleFile.isDirectory() && !singleFile.isHidden()){
                al.addAll(findSongs(singleFile));
            }else{
                //if the name of current file ends with .mp3 it gets added note that i also made it
                //so that files starting with a '.' got rejected because of certain files on my phone
                //its comletely optional you for example add another requirement saying
                // 'singleFile.endsWith(".wav")' to let diffrent kind of extensions also get added
                if(singleFile.getName().endsWith(".mp3") && !singleFile.getName().startsWith(".")){
                    al.add(singleFile);
                }
            }
        }
        //return the list of music files
        return al;
    }

    public void toast(String text){
        //some test shit i cba to remove
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case MY_READ_REQUEST: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                   if(ContextCompat.checkSelfPermission(MainActivity.this,
                           Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                       toast("Permission granted!");
                       runApp();
                   }
                }else {
                    toast("No permission granted!");
                    finish();
                }
                return;
            }
        }
    }

    @Override
    public void onSimpleItemClick(SimpleItem simpleItem,View v) {
        int position = 0;
        if(lastClicked != null) {
            position = adapter.getPosition(lastClicked);
            lastClicked.shouldAnimate = false;
            adapter.set(position,lastClicked);
        }
        lastClicked = simpleItem;
        position = adapter.getPosition(simpleItem);
        simpleItem.shouldAnimate = true;
        adapter.set(position,simpleItem);


        playerActivity = new Intent(getApplicationContext(),
                Player.class).putExtra("pos",position).putExtra("songlist",songFiles);

        startActivity(playerActivity);
    }

    @Override
    public void onOptionClick(SimpleItem simpleItem,View v) {
        final int position = adapter.getPosition(simpleItem);
        PopupMenu menu = new PopupMenu(this, v);
        menu.getMenuInflater().inflate(R.menu.popupmenu,menu.getMenu());
        menu.show();

        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.settings:
                        volumeSettings = new Intent(getApplicationContext(),Settings.class);
                        startActivity(volumeSettings);
                        break;
                    case R.id.delete:
                        onAlert(songFiles.get(position).getName(), position);
                        //songFiles.get(position).delete();
                        break;
                    case R.id.share:
                        break;
                }

                return false;
            }
        });


    }

    public void onAlert(final String name, final int i){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        String path = songFiles.get(i).getPath();
                        adapter.remove(i);
                        Toast.makeText(getBaseContext(), "deleted: " + name ,
                                Toast.LENGTH_LONG).show();
                        //runApp();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

}
