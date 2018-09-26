package com.desktop.duco.mediaplayer2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.style.BackgroundColorSpan;
import android.widget.Button;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;


public class MainActivity extends AppCompatActivity {
    private static final int MY_READ_REQUEST = 1;
    private ViewPager pager;
    private MainPagerAdapter adapter;
    Button triggerList,triggerController;
    //sdsdgoknsdbgnbd

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        pager = findViewById(R.id.pager);

        //pop up a permission request if needed if not continue running the the rest of the app
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_READ_REQUEST);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_READ_REQUEST);
            }
        } else {
            loadFragment();

        }
    }

    public void loadFragment( ) {


        adapter = new MainPagerAdapter(this);
        pager.setAdapter(adapter);


        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabStrip.setShouldExpand(true);
        tabStrip.setViewPager(pager);

        BackgroundPlayer.getInstance().setmContext(this, this, android.os.Process.myPid());

    }

    public void toast(String text){
        //some test shit i cba to remove
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case MY_READ_REQUEST: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                   if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                           ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                       toast("Permissions granted!");
                       loadFragment();
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
