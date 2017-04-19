package com.example.aloom.neonshuffleboardv2;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import java.util.concurrent.TimeUnit;

//Intent is to produce a realistic simulation of a fling on the puck object for the Neon ShuffleBoard game
public class NSBGameActivity extends AppCompatActivity implements Cloneable{
    private static final int ANIMATION_TIME = 240;
    private final String TAG = "PuckAnimatorActivity";
    //All physical state variables of the puck that track finger touch movement properties
    AnimatorSet animatePuckProperties;
    Animation.AnimationListener animState;
    ConstraintLayout gameView;

    float flingDistance;
    float downXPOS;
    float downYPOS;
    float upXPOS;
    float upYPOS;
    float xVelocity;
    float yVelocity;
    float dx;
    float dy;
    //start to think about how the views will get cycled through
    //might be able to remove red and blue puck imageviews
    int puckClock;
    ImageView redPuck1;
    ImageView redPuck2;
    ImageView redPuck3;
    ImageView bluePuck1;
    ImageView bluePuck2;
    ImageView bluePuck3;
    GestureDetector detector;
    ImageView[]puckCycleList = {redPuck1,redPuck2,redPuck3,bluePuck1,bluePuck2,bluePuck3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nsbgame);
        createAllPucks();
        puckCycleList[0].setVisibility(View.VISIBLE);
        gameView = (ConstraintLayout)findViewById(R.id.nsbGame);
        detector = new GestureDetector(this, new CustomGestureListener());
        animatePuckPropertiesOnTouch();
    }

    public void calculateDistance(MotionEvent event){
        Log.d(TAG, "initial x pos: "+ downXPOS);
        Log.d(TAG, "initial y pos: "+ downYPOS);
        flingDistance = (float) Math.sqrt((dx*dx)+(dy*dy));
    }

    public void animatePuckPropertiesOnTouch(){
        //can change this line to allow player to select their puck color in future update
        //only initial values
        setupInitialPuckVisibility();
        puckCycleList[0].setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent ev) {
                for (int puckID = 0; puckID < puckCycleList.length; puckID++) {
                    setupInitialPuckVisibility();
                    switch(puckID){

                        case 0:
                            puckClock = puckID;
                            puckCycleList[puckID].setVisibility(View.VISIBLE);
                            break;
                        case 1:
                            puckClock = puckID;
                            puckCycleList[puckID].setVisibility(View.VISIBLE);
                            break;
                        case 2:
                            puckClock = puckID;
                            puckCycleList[puckID].setVisibility(View.VISIBLE);
                            break;
                        case 3:
                            puckClock = puckID;
                            puckCycleList[puckID].setVisibility(View.VISIBLE);
                            break;
                        case 4:
                            puckClock = puckID;
                            puckCycleList[puckID].setVisibility(View.VISIBLE);
                            break;
                        case 5:
                            puckClock = puckID;
                            puckCycleList[puckID].setVisibility(View.VISIBLE);
                            break;
                        case 6:
                            puckClock = puckID;
                            //reset
                            puckID=0;
                            animatePuckPropertiesOnTouch();
                    }

                    /*else if (i == 6){
                        i=0; //reset until score is reached
                    }*/
                }
            //}
                return true;
            }
        });
        //cant be selected until redPlayers turn is finished. setClickable(true) once finished
    }

    class CustomGestureListener implements GestureDetector.OnGestureListener{

        private static final int SWIPE_MIN_DISTANCE = 120;
        private static final int SWIPE_MAX_OFF_PATH = 250;
        private static final float SWIPE_THRESHOLD_VELOCITY = 200;

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent fingerDown, MotionEvent move, float velocityX, float velocityY) {
            Log.i(TAG, "____FLING INITIATED____");
            //animatePuck();
            downXPOS = fingerDown.getX();
            Log.d(TAG, "XPOS finger down:  "+downXPOS);
            downYPOS = fingerDown.getY();
            Log.d(TAG, "YPOS finger down:  "+downYPOS);
            upXPOS = move.getX();
            Log.d(TAG, "End X POS:  "+upXPOS);
            upYPOS = move.getY();
            Log.d(TAG, "End Y POS:  "+upYPOS);
            dx = (upXPOS-downXPOS);
            dy = (upYPOS-downYPOS);
            Log.d(TAG, "End Y POS:  "+upYPOS);
            //multiplies distance the puck will travel
            xVelocity = (float) (velocityX*.00025);
            Log.d(TAG, "Velocity X:  "+velocityX);
            yVelocity = (float) (velocityY*.00025);
            Log.d(TAG, "Velocity Y:  "+velocityY);
            calculateDistance(move);
            animatePuck();
            return true;
        }

        public void animatePuck(){
            int SCREEN_BOTTOM_BOUNDS = gameView.getBottom();
            Log.d(TAG, "SCREEN_BOTTOM_BOUNDS:  "+SCREEN_BOTTOM_BOUNDS);
            int SCREEN_TOP_BOUNDS = gameView.getTop();
            Log.d(TAG, "SCREEN_TOP_BOUNDS:  "+SCREEN_TOP_BOUNDS);
            int puckSpeed = 30000;
            puckCycleList[puckClock].setAdjustViewBounds(true);
            int PUCK_TOP =  puckCycleList[puckClock].getTop();
            Log.d(TAG, "PUCK_TOP:  "+PUCK_TOP);
            int PUCK_BOTTOM  =  puckCycleList[puckClock].getBottom();
            Log.d(TAG, "PUCK_BOTTOM:  "+PUCK_BOTTOM);
            int[]puckXYLocation = new int[2];
            puckCycleList[puckClock].getLocationOnScreen(puckXYLocation);
            int puckXPOS = puckXYLocation[0];
            Log.d(TAG, "puck x pos:  "+puckXPOS);
            int puckYPOS = puckXYLocation[1];
            Log.d(TAG, "puck y pos:  "+puckYPOS);
            int bottomBoundary  = SCREEN_BOTTOM_BOUNDS-PUCK_BOTTOM;
            Log.d(TAG, "bottomBoundary:  "+bottomBoundary);
            int topBoundary =  SCREEN_TOP_BOUNDS + PUCK_TOP;
            Log.d(TAG, "topBoundary:  "+topBoundary);
            Log.d(TAG, "initial bottom boundary:  "+bottomBoundary);

            if( bottomBoundary < puckYPOS){//double check logic
                ObjectAnimator animY = ObjectAnimator.ofFloat(puckCycleList[puckClock], View.TRANSLATION_Y, flingDistance*yVelocity);
                ObjectAnimator animX = ObjectAnimator.ofFloat( puckCycleList[puckClock],View.TRANSLATION_X, flingDistance*xVelocity);
                //ObjectAnimator animZ = ObjectAnimator.ofFloat(puck,"z",flingDistance);
                animatePuckProperties = new AnimatorSet();
                animatePuckProperties.setInterpolator(new DecelerateInterpolator(10));
                animatePuckProperties.setStartDelay(0);
                animatePuckProperties.playTogether(animY,animX);
                animatePuckProperties.setDuration(puckSpeed);
                animatePuckProperties.start();
                playSound(R.raw.realisticslide2);
            }
        }
    }

    public void waitTime(int seconds){
        try{
            TimeUnit.SECONDS.sleep(seconds);
        }catch (Exception e){
            Log.d("Wait Method: ","Cause: "+e.getCause());
        }
    }

    private void playSound(int soundID){
        MediaPlayer soundPlayer = MediaPlayer.create(this,soundID);
        soundPlayer.setOnCompletionListener( new MediaPlayer.OnCompletionListener(){
            @Override
            public void onCompletion( MediaPlayer mp){
                mp.release();
            }
        });
        soundPlayer.start();
    }

    public void createAllPucks(){
        redPuck1 = new ImageView(NSBGameActivity.this);
        redPuck1 = (ImageView)findViewById(R.id.redPuck1);
        redPuck2 = new ImageView(NSBGameActivity.this);
        redPuck2 = (ImageView)findViewById(R.id.redPuck2);
        redPuck3 = new ImageView(NSBGameActivity.this);
        redPuck3 = (ImageView)findViewById(R.id.redPuck3);
        bluePuck1 = new ImageView(NSBGameActivity.this);
        bluePuck1 = (ImageView)findViewById(R.id.bluePuck1);
        bluePuck2 = new ImageView(NSBGameActivity.this);
        bluePuck2 = (ImageView)findViewById(R.id.bluePuck2);
        bluePuck3 = new ImageView(NSBGameActivity.this);
        bluePuck3 = (ImageView)findViewById(R.id.bluePuck3);
    }
    //excludes the first puck in the sequence
    public void setupInitialPuckVisibility(){
        redPuck2.setVisibility(View.GONE);
        redPuck3.setVisibility(View.GONE);
        bluePuck1.setVisibility(View.GONE);
        bluePuck2.setVisibility(View.GONE);
        bluePuck3.setVisibility(View.GONE);
    }
}
