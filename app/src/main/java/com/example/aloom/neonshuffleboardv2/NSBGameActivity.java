package com.example.aloom.neonshuffleboardv2;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.RectF;
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
    float upXPOS;
    float upYPOS;
    float xVelocity;
    float yVelocity;
    float dx;
    float dy;
    int puckClock = 0;
    int maxDuration = 30000;
    RectF touchZone;
    ImageView redPuck1;
    ImageView redPuck2;
    ImageView redPuck3;
    ImageView bluePuck1;
    ImageView bluePuck2;
    ImageView bluePuck3;
    View.OnTouchListener puckListener;
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
        initialVisibility();
        puckCycleList = new ImageView[]{redPuck1, redPuck2, redPuck3
                , bluePuck1, bluePuck2, bluePuck3};
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

    @Override
    public void onResume() {
        super.onResume();
        final Thread gameThread = new Thread(new Runnable() {

            @Override
            public void run() {

            }
        });
        gameThread.start();
    }

    public void initialVisibility() {

        redPuck2.setVisibility(View.GONE);
        redPuck3.setVisibility(View.GONE);
        bluePuck1.setVisibility(View.GONE);
        bluePuck2.setVisibility(View.GONE);
        bluePuck3.setVisibility(View.GONE);
    }

    public void calculateDistance() {
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

    //to handle flings (swipes)
    class PuckGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String DEBUG_TAG = "Gestures"; //for Log.d
        private final int VELOCITY_TRIGGER = 0;
        private final int DIST_TRIGGER = 190;

        @Override
        public boolean onDown(MotionEvent event) {
            Log.d(DEBUG_TAG, "onDown: " + event.toString());
            return true;    //needed so all gestures are checked
        }

        @Override
        public boolean onFling(MotionEvent fingerDown, MotionEvent move,
                               float velocityX, float velocityY) {
            Log.d(DEBUG_TAG, "onFling: " + velocityX + " --- " + fingerDown.toString() + move.toString());
            Log.d(DEBUG_TAG, "         x0=" + fingerDown.getX() + "   x1=" + move.getX());

            Log.i(TAG, "____FLING INITIATED____");
            downXPOS = fingerDown.getX();
            Log.d(TAG, "XPOS finger down:  " + downXPOS);
            downYPOS = fingerDown.getY();
            Log.d(TAG, "YPOS finger down:  " + downYPOS);
            Log.d(TAG, "puck left x: " + puckCycleList[puckClock].getLeft());
            Log.d(TAG, "puck right x: " + puckCycleList[puckClock].getRight());
            Log.d(TAG, "puck top y: " + puckCycleList[puckClock].getTop());
            Log.d(TAG, "puck bottom y : " + puckCycleList[puckClock].getBottom());
            upXPOS = move.getX();
            Log.d(TAG, "End X POS:  " + upXPOS);
            upYPOS = move.getY();
            Log.d(TAG, "End Y POS:  " + upYPOS);
            dx = (upXPOS - downXPOS);
            dy = (upYPOS - downYPOS);
            Log.d(TAG, "End Y POS:  " + upYPOS);
            xVelocity = (float) (velocityX * .00025);
            Log.d(TAG, "Velocity X:  " + velocityX);
            yVelocity = (float) (velocityY * .00025);
            Log.d(TAG, "Velocity Y:  " + velocityY);
            calculateDistance();
            animatePuck();
            puckClock++;
            puckCycleList[puckClock].setVisibility(View.VISIBLE);
            Log.d(TAG, "Puck clock Value " + puckClock);
            return true;
        }

        public void animatePuck() {
            int SCREEN_BOTTOM_BOUNDS = gameView.getBottom();
            Log.d(TAG, "SCREEN_BOTTOM_BOUNDS:  " + SCREEN_BOTTOM_BOUNDS);
            int SCREEN_TOP_BOUNDS = gameView.getTop();
            Log.d(TAG, "SCREEN_TOP_BOUNDS:  " + SCREEN_TOP_BOUNDS);
            puckCycleList[puckClock].setAdjustViewBounds(true);
            int PUCK_TOP = puckCycleList[puckClock].getTop();
            Log.d(TAG, "PUCK_TOP:  " + PUCK_TOP);
            int PUCK_BOTTOM = puckCycleList[puckClock].getBottom();
            Log.d(TAG, "PUCK_BOTTOM:  " + PUCK_BOTTOM);
            int[] puckXYLocation = new int[2];
            puckCycleList[puckClock].getLocationOnScreen(puckXYLocation);
            int puckXPOS = puckXYLocation[0];
            Log.d(TAG, "puck x pos:  " + puckXPOS);
            int puckYPOS = puckXYLocation[1];
            Log.d(TAG, "puck y pos:  " + puckYPOS);
            int bottomBoundary = SCREEN_BOTTOM_BOUNDS - PUCK_BOTTOM;
            Log.d(TAG, "bottomBoundary:  " + bottomBoundary);
            int topBoundary = SCREEN_TOP_BOUNDS + PUCK_TOP;
            Log.d(TAG, "topBoundary:  " + topBoundary);
            Log.d(TAG, "initial bottom boundary:  " + bottomBoundary);
            ObjectAnimator animY = ObjectAnimator.ofFloat(puckCycleList[puckClock], View.TRANSLATION_Y, flingDistance * yVelocity);
            Log.d(TAG, "Puck: " + puckCycleList[puckClock]);
            ObjectAnimator animX = ObjectAnimator.ofFloat(puckCycleList[puckClock], View.TRANSLATION_X, flingDistance * xVelocity);
            ObjectAnimator animZ = ObjectAnimator.ofFloat(puckCycleList[puckClock], View.TRANSLATION_Z, flingDistance * 2000);
            animatePuckProperties = new AnimatorSet();
            animatePuckProperties.setInterpolator(new DecelerateInterpolator(10));
            animatePuckProperties.setStartDelay(0);
            animatePuckProperties.playTogether(animY, animX);
            animatePuckProperties.setDuration(maxDuration);
            animatePuckProperties.start();
            playSound(R.raw.realisticslide2);
        }
    }
}