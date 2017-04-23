package com.example.aloom.neonshuffleboardv2;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

public class NSBGameActivity extends AppCompatActivity {
    private final String TAG = "PuckAnimatorActivity";
    AnimatorSet animatePuckProperties;
    ConstraintLayout gameView;
    float flingDistance;
    float downXPOS;
    float downYPOS;
    Bundle firstRoundData;
    float upXPOS;
    float upYPOS;
    float resetPuckXPOS;
    float resetPuckYPOS;
    float xVelocity;
    float yVelocity;
    float dx;
    float dy;
    int puckClock = 0;
    int animClock = 0;
    int maxDuration = 20000;
    ImageView redPuck1;
    ImageView redPuck2;
    ImageView redPuck3;
    ImageView bluePuck1;
    ImageView bluePuck2;
    ImageView bluePuck3;
    GestureDetectorCompat mDetector;
    ImageView[] puckCycleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nsbgame);
        redPuck1 = (ImageView) findViewById(R.id.redPuck1);
        redPuck2 = (ImageView) findViewById(R.id.redPuck2);
        redPuck3 = (ImageView) findViewById(R.id.redPuck3);
        bluePuck1 = (ImageView) findViewById(R.id.bluePuck1);
        bluePuck2 = (ImageView) findViewById(R.id.bluePuck2);
        bluePuck3 = (ImageView) findViewById(R.id.bluePuck3);
        puckCycleList = new ImageView[]{redPuck1, redPuck2, redPuck3
                , bluePuck1, bluePuck2, bluePuck3};
        clearTheBoard();
        initialVisibility();
        calculateResetPosition();
        if (savedInstanceState != null) {
            resetPuckXPOS = savedInstanceState.getFloat("resetXposition");
            resetPuckYPOS = savedInstanceState.getFloat("resetYposition");
        }
        puckCycleList[puckClock].setClickable(true);
        gameView = (ConstraintLayout) findViewById(R.id.nsbGame);
        mDetector = new GestureDetectorCompat(this, new PuckGestureListener());
        puckCycleList[puckClock].setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mDetector.onTouchEvent(event);
            }
        });
    }

    //Chris this is where we can save the state of the initial position
    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putFloat("resetXposition ", resetPuckXPOS);
        outState.putFloat("resetYposition ", resetPuckYPOS);

    }

    private void resetRound() {
        puckClock = 0;
        for (int i = 0; i < puckCycleList.length; i++) {
            ObjectAnimator resetX = ObjectAnimator.ofFloat(puckCycleList[i], View.TRANSLATION_X, resetPuckXPOS);
            ObjectAnimator resetY = ObjectAnimator.ofFloat(puckCycleList[i], View.TRANSLATION_Y, resetPuckYPOS);
            animatePuckProperties.playTogether(resetX, resetY);
            animatePuckProperties.setStartDelay(0);
            animatePuckProperties.setDuration(0);
            animatePuckProperties.start();
        }
        clearTheBoard();

    }

    private void initialVisibility() {
        redPuck1.setVisibility(View.VISIBLE);
    }

    private void calculateDistance() {
        Log.d(TAG, "initial x pos: " + downXPOS);
        Log.d(TAG, "initial y pos: " + downYPOS);
        flingDistance = (float) Math.sqrt((dx * dx) + (dy * dy));
    }

    private void playSound(int soundID) {
        MediaPlayer soundPlayer = MediaPlayer.create(this, soundID);
        soundPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        soundPlayer.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private void calculateResetPosition() {
        resetPuckXPOS = redPuck1.getX();
        resetPuckYPOS = redPuck1.getY();
    }

    private void clearTheBoard() {
        //has a round already occurred? is this the first round? doesnt matter need to clear the board for bot situations.

        for (int i = 0; i < puckCycleList.length; i++) {
            puckCycleList[i].setVisibility(View.GONE);
        }
    }

    //chris modify this
    private void resetPosition(int i) {
        ObjectAnimator resetX = ObjectAnimator.ofFloat(puckCycleList[puckClock], View.TRANSLATION_X, resetPuckXPOS);
        ObjectAnimator resetY = ObjectAnimator.ofFloat(puckCycleList[puckClock], View.TRANSLATION_Y, resetPuckYPOS);
        animatePuckProperties.playTogether(resetX, resetY);
        animatePuckProperties.setStartDelay(0);
        animatePuckProperties.setDuration(0);
        animatePuckProperties.start();
    }

    private void waitTime(int milliseconds) {
        try {
            Thread.sleep(milliseconds);                 //1000 milliseconds is one second.
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    public boolean isNotEndOfRound() {
        return puckClock < 5;
    }

    //to handle flings (swipes)
    class PuckGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent event) {
            return true;    //needed so all gestures are checked
        }

        @Override
        public boolean onFling(MotionEvent fingerDown, MotionEvent move,
                               float velocityX, float velocityY) {

            if (isNotEndOfRound() == true) {
                downXPOS = fingerDown.getX();
                downYPOS = fingerDown.getY();
                upXPOS = move.getX();
                upYPOS = move.getY();
                dx = (upXPOS - downXPOS);
                dy = (upYPOS - downYPOS);
                xVelocity = (float) (velocityX * .00025);
                yVelocity = (float) (velocityY * .00025);
                calculateDistance();
                animatePuck();
                //animation occurred
            } else if (isNotEndOfRound() == false) {
                waitTime(5000);
                resetRound();
                initialVisibility();
                return false;
            }
            puckClock++;

            puckCycleList[puckClock - 1].setClickable(false);
            puckCycleList[puckClock].setVisibility(View.VISIBLE);
            return true;
        }

        private void animatePuck() {
            ObjectAnimator animY = ObjectAnimator.ofFloat(puckCycleList[puckClock], View.TRANSLATION_Y, flingDistance * yVelocity);
            ObjectAnimator animX = ObjectAnimator.ofFloat(puckCycleList[puckClock], View.TRANSLATION_X, flingDistance * xVelocity);
            animatePuckProperties = new AnimatorSet();
            animatePuckProperties.setInterpolator(new DecelerateInterpolator(15));
            animatePuckProperties.setStartDelay(0);
            animatePuckProperties.playTogether(animY, animX);
            animatePuckProperties.setDuration(maxDuration);
            animatePuckProperties.start();
            playSound(R.raw.realisticslide2);
        }
    }
