package com.example.isangeet;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    // quick instructions
//    How do I transfer files from laptop to emulator?
//    Go to "Device File Explorer" which is on the bottom right of android studio. If you have more than one device connected, select the device you want from the drop-down list on top. mnt>sdcard is the location for SD card on the emulator. Right click on the folder and click Upload.
    // one more way is to directly download on phone, but that's bit difficult as I have not signed in anywhere, better is to download on pc and upload on emulator



    private ListView listView ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView) ;


        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
//                        Toast.makeText(MainActivity.this, "Runtime Permission Granted", Toast.LENGTH_SHORT).show();

                        // start progress dialog box
//                        ProgressDialog dialog=new ProgressDialog(MainActivity.this);
//                        dialog.setMessage("Checking for songs");
//                        dialog.setCancelable(false);
//                        dialog.setInverseBackgroundForced(false);
//                        dialog.show();

                        Log.d("time", "thread end") ;


                        // get all the mp3 songs in the external storage
                        ArrayList<File> mySongs = fetchSongs(Environment.getExternalStorageDirectory()) ;

//                        dialog.dismiss();
                        // getting the names of the songs so as put on the listview and show on the screen

                        String[] items = new String[mySongs.size()] ;

                        for (int i=0; i<items.length; i++) {
                            items[i] = mySongs.get(i).getName().replace(".mp3", "" ); // removing the mp3 at the end of the song to display it in listview
                        }
//                        Arrays.sort(items) ;
//                        Collections.sort(mySongs) ;


                        // creating adapter for the listview
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_expandable_list_item_1, items) ;
                        listView.setAdapter(adapter);


                        // making new activity for the song to play
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                Intent intent = new Intent(MainActivity.this, PlaySong.class) ;
                                String currentSong = listView.getItemAtPosition(position).toString() ;

                                intent.putExtra("songList", mySongs) ;
                                intent.putExtra("currentSong", currentSong) ;
                                Log.d("mytag", position + "sending as intent") ;
                                intent.putExtra("position", position) ;

                                startActivity(intent) ;

                            }
                        });

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(MainActivity.this, "Permission is required to access the songs", Toast.LENGTH_SHORT).show();

                        MainActivity.this.finishAffinity(); // closes the app
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {                     permissionToken.continuePermissionRequest();
                        permissionToken.continuePermissionRequest();
                    }
                })
                .check();
    }


    public ArrayList<File> fetchSongs(File file) {

        ArrayList<File> arrayList = new ArrayList<>() ;

        File[] allFiles = file.listFiles() ;

        if (allFiles != null) {

            for (File myFile : allFiles) {

                if (!myFile.isHidden() && myFile.isDirectory()) {
                    arrayList.addAll(fetchSongs(myFile)) ;

                }

                else {
                    if (myFile.getName().endsWith(".mp3") && !myFile.getName().startsWith(".")) {
                        arrayList.add(myFile) ;

                    }
                }
            }
        }

        return arrayList ;

    }
}