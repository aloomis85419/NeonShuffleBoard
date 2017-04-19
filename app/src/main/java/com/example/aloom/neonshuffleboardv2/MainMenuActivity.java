package com.example.aloom.neonshuffleboardv2;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.VideoView;

public class MainMenuActivity extends FragmentActivity {
    static int settingsRequestCode = 1;
    static int settingsResultCode;
    static int playGameRequestCode = 2;
    static int playGameResultCode;
    static private MediaPlayer soundPlayer;
    static private MediaPlayer musicLoop;
    Intent initiateActivity;
    VideoView gifVideo;
    private int videoPosition = 0;
    private int musicPostion = 0;
    private Button playGame;
    private Button gameSettings;
    private boolean isPlaying = true;
    private String videoUri =  "android.resource://" + "com.example.aloom.neonshuffleboardv2" + "/" + R.raw.nsbmainmenu;

    //REMINDER: Need to add onPause and onOrientationChange so that music will be paused for main menu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        gifVideo = (VideoView)findViewById(R.id.gifVideo);
        gifVideo.setVideoURI(Uri.parse(videoUri));
        gifVideo.start();
        gifVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
        playGame = (Button)findViewById(R.id.playGame);
        gameSettings = (Button)findViewById(R.id.gameSettings);

        playGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound(R.raw.playgamesound);
                initiateNSBGameActivity();
            }
        });
        gameSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound(R.raw.settingsselectsound);
                initiateSettingsActivity();
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putInt("Position",gifVideo.getCurrentPosition());
        gifVideo.pause();
    }

    @Override
    public  void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);

        videoPosition = savedInstanceState.getInt("Position");
        gifVideo.seekTo(videoPosition);

    }
    private void playSound(int soundID){
        soundPlayer = new MediaPlayer();
        soundPlayer = MediaPlayer.create(this,soundID);
        soundPlayer.setOnCompletionListener( new MediaPlayer.OnCompletionListener(){
            @Override
            public void onCompletion( MediaPlayer mp){
                mp.release();
            }
        });
        soundPlayer.start();
    }



    @Override
    public void onPause(){
        super.onPause();
        if (isPlaying)
        {
            if(musicLoop!=null)
            {
                gifVideo.pause();
                musicLoop.pause();
                isPlaying = false;
            }
            musicPostion = musicLoop.getCurrentPosition();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (musicLoop == null) {
            musicLoop = MediaPlayer.create(getApplicationContext(), R.raw.comtruiseglawio);
        }
        musicLoop.start();
        boolean isPlaying = true;

        if (isPlaying) {
            musicPostion = musicLoop.getCurrentPosition();
            musicLoop.seekTo(musicPostion);
            gifVideo.setVideoURI(Uri.parse(videoUri));
            gifVideo.start();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        Configuration config=getResources().getConfiguration();
        if(config.orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            setContentView(R.layout.activity_main_menu);
        }
        else if(config.orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            setContentView(R.layout.activity_main_menu);
        }
    }
    //causes app crash make sure on pause and on resume are called when this initiates
    public void initiateSettingsActivity(){
        initiateActivity = new Intent(MainMenuActivity.this,GameSettingsActivity.class);
        musicLoop.pause();
        startActivity(initiateActivity);
    }
    //needs to use startActivityForResults eventually
    public void initiateNSBGameActivity(){
        initiateActivity = new Intent(MainMenuActivity.this,NSBGameActivity.class);
        musicLoop.pause();
        startActivity(initiateActivity);
    }

}
