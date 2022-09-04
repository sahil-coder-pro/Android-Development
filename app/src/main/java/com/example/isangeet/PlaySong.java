package com.example.isangeet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PlaySong extends AppCompatActivity {

    TextView textView ;
    ImageView pause, previous, next ;
    ArrayList<File> songs ;
    MediaPlayer mediaPlayer ;
    String textContent ;
    int position ;
    SeekBar seekBar ;
    Thread updateSeekBar ;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop() ;
        mediaPlayer.release();

        updateSeekBar.interrupt();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);

        // setting the views
        textView = findViewById(R.id.textView) ;
        pause = findViewById(R.id.pause) ;
        previous = findViewById(R.id.previous) ;
        next = findViewById(R.id.next) ;
        seekBar = findViewById(R.id.seekBar) ;



        // getting our intents
        Intent intent = getIntent() ;
        Bundle bundle = intent.getExtras() ;

        songs = (ArrayList)bundle.getParcelableArrayList("songList") ;
        textContent = intent.getStringExtra("currentSong") ;
        position = intent.getIntExtra("position", 0);

        Log.d("mytag", position + "received as intent") ;
        Log.d("mytag", String.valueOf(songs)) ;


        // setting the song name
        textView.setText(textContent);

        textView.setSelected(true);


        // preparing the media player to play the song
        Log.d("mytag", "just before playing " + position) ;
        Log.d("mytag", songs.get(position).toString()) ;

        Uri uri = Uri.parse(songs.get(position).toString()) ;
        mediaPlayer = MediaPlayer.create(this, uri) ;



        mediaPlayer.start() ;

//         on completion call the next button
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                next.performClick() ;
            }
        });

        // working on seekbar
        seekBar.setMax(mediaPlayer.getDuration()) ;

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    mediaPlayer.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        // thread to update the seekbar
        updateSeekBar = new Thread(){
            @Override
            public void run() {
                int currentPosition = 0 ;

                try {
                    while (currentPosition < mediaPlayer.getDuration()) {
                        currentPosition = mediaPlayer.getCurrentPosition() ;
                        seekBar.setProgress(currentPosition);
                        sleep(1000) ;
                    }
                }

                catch (Exception e){
                    e.printStackTrace();
                }
            }
        };

        updateSeekBar.start() ;


        // now we are attaching event listener to our image views
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    pause.setImageResource(R.drawable.play);
                    mediaPlayer.pause() ;
                }

                else {
                    mediaPlayer.start();
                    pause.setImageResource(R.drawable.pause);
                }
            }
        });



        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mediaPlayer.stop() ;
                mediaPlayer.release();

                if (position != 0) {
                    position-- ;
                }
                else {
                    position = songs.size()-1 ;
                }

                Uri uri = Uri.parse(songs.get(position).toString()) ;
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri) ;
                mediaPlayer.start() ;

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        next.performClick() ;
                    }
                });

                seekBar.setMax(mediaPlayer.getDuration()) ;
//                seekBar.setProgress(0);
//                updateSeekBar.notify() ;


                textContent = songs.get(position).getName().replace(".mp3", "") ;
                textView.setText(textContent);

                pause.setImageResource(R.drawable.pause);

                updateSeekBar = new Thread(){
                    @Override
                    public void run() {
                        int currentPosition = 0 ;

                        try {
                            while (currentPosition < mediaPlayer.getDuration()) {
                                currentPosition = mediaPlayer.getCurrentPosition() ;
                                seekBar.setProgress(currentPosition);
                                sleep(1000) ;
                            }
                        }

                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };

                updateSeekBar.start() ;




            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mediaPlayer.stop() ;
                mediaPlayer.release();


                position =   ++position % songs.size();

                Uri uri = Uri.parse(songs.get(position).toString()) ;
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri) ;
                mediaPlayer.start() ;

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        next.performClick() ;
                    }
                });

                seekBar.setMax(mediaPlayer.getDuration()) ;
//                seekBar.setProgress(0);
//                updateSeekBar.notify() ;


                textContent = songs.get(position).getName().replace(".mp3", "") ;
                textView.setText(textContent);

                pause.setImageResource(R.drawable.pause);

                updateSeekBar = new Thread(){
                    @Override
                    public void run() {
                        int currentPosition = 0 ;

                        try {
                            while (currentPosition < mediaPlayer.getDuration()) {
                                currentPosition = mediaPlayer.getCurrentPosition() ;
                                seekBar.setProgress(currentPosition);
                                sleep(1000) ;
                            }
                        }

                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };

                updateSeekBar.start() ;
            }
        });

    }
}