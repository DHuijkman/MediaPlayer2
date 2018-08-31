package com.desktop.duco.mediaplayer2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView lv;
    String[] items;

    private static final int MY_PERMISSION_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //pop up a permission request if needed if not continue running the the rest of the app
        if(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)){
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            }else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            }
        }else {
            runApp();
        }
    }
    //after permission to read storage has been granted start setting up everything
    public void runApp(){
        lv = (ListView) findViewById(R.id.lvPlayList);

        //call the findsongs method to get a list of all mp3 files on your phone
        final ArrayList<File> mySongs = findSongs(Environment.getExternalStorageDirectory());
        //create a string array (to keep the names of the files in) with the same size as mySongs
        items = new String[mySongs.size()];
        //fill items and at the same time remove the '.mp3' part of the string to tidy it up
        for(int i = 0; i <mySongs.size(); i++){
            //toast(mySongs.get(i).getName());
            items[i] = mySongs.get(i).getName().replace(".mp3","");
        }

        //set new adapter for the listview
        ArrayAdapter<String> adp = new ArrayAdapter<>(getApplicationContext(),R.layout.song_layout,R.id.textView,items);
        lv.setAdapter(adp);
        //set listener for the listview so that when you click on a song something (new activitiy) actualy happens
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //start new activity (the song control screen where you can:
                // fastforward/backward,play/pause,control seekbar,skip to previous or next song
                startActivity(new Intent(getApplicationContext(),
                        Player.class).putExtra("pos",position).putExtra("songlist",mySongs));
            }
        });
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

    //this so the method runApp actually gets called after giving permissions to read storage
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSION_REQUEST: {
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
}
