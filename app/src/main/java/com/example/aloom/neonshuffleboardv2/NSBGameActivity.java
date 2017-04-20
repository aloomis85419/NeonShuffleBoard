package com.example.aloom.neonshuffleboardv2;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
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

//Intent is to produce a realistic simulation of a fling on the puck object for the Neon ShuffleBoard game
public class NSBGameActivity extends AppCompatActivity {
    private final String TAG = "PuckAnimatorActivity";
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
    int puckClock = 0;
    ImageView redPuck1;
    ImageView redPuck2;
    ImageView redPuck3;
    ImageView bluePuck1;
    ImageView bluePuck2;
    ImageView bluePuck3;
    View.OnTouchListener redPuck1Listener;
    View.OnTouchListener redPuck2Listener;
    View.OnTouchListener redPuck3Listener;
    View.OnTouchListener bluePuck1Listener;
    View.OnTouchListener bluePuck2Listener;
    View.OnTouchListener bluePuck3Listener;
    GestureDetector detector;
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
        setupInitialPuckVisibility();
        gameView = (ConstraintLayout) findViewById(R.id.nsbGame);
        detector = new GestureDetector(this, new CustomGestureListener());
        listenForTouchOnRedPuck1();
        listenForTouchOnRedPuck2();
        listenForTouchOnRedPuck3();
        listenForTouchOnBluePuck1();
        listenForTouchOnBluePuck2();
        listenForTouchOnBluePuck3();
        redPuck1.setOnTouchListener(redPuck1Listener);
        redPuck2.setOnTouchListener(redPuck2Listener);
        redPuck3.setOnTouchListener(redPuck3Listener);
        bluePuck1.setOnTouchListener(bluePuck1Listener);
        bluePuck2.setOnTouchListener(bluePuck2Listener);
        bluePuck3.setOnTouchListener(bluePuck3Listener);
    }

    public void calculateDistance() {
        Log.d(TAG, "initial x pos: "+ downXPOS);
        Log.d(TAG, "initial y pos: "+ downYPOS);
        flingDistance = (float) Math.sqrt((dx*dx)+(dy*dy));
    }

    public void waitTime(int milliseconds) {
        try {
            wait(milliseconds);
        } catch (Exception e) {
            Log.d("Wait Method: ", "Cause: " + e.getCause());
        }
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

    //exclude first puck
    public void setupInitialPuckVisibility() {
        redPuck2.setVisibility(View.GONE);
        redPuck3.setVisibility(View.GONE);
        bluePuck1.setVisibility(View.GONE);
        bluePuck2.setVisibility(View.GONE);
        bluePuck3.setVisibility(View.GONE);
    }

    public void listenForTouchOnRedPuck1() {
        redPuck1Listener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (puckClock == 0) {
                    redPuck1.setClickable(true);
                    return detector.onTouchEvent(event);
                }
                return false;
            }
        };
    }

    public void listenForTouchOnRedPuck2() {
        redPuck2Listener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (puckClock == 1) {
                    redPuck2.setClickable(true);
                    return detector.onTouchEvent(event);
                }
                return false;

            }
        };
    }

    public void listenForTouchOnRedPuck3() {
        redPuck3Listener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (puckClock == 2) {
                    redPuck3.setClickable(true);
                    return detector.onTouchEvent(event);
                }
                return false;
            }
        };
    }

    public void listenForTouchOnBluePuck1() {
        bluePuck1Listener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (puckClock == 3) {
                    bluePuck1.setClickable(true);
                    return detector.onTouchEvent(event);
                }
                return false;
            }
        };
    }

    public void listenForTouchOnBluePuck2() {
        bluePuck2Listener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (puckClock == 3) {
                    bluePuck2.setClickable(true);
                    return detector.onTouchEvent(event);
                }
                return false;
            }
        };
    }

    public void listenForTouchOnBluePuck3() {
        bluePuck3Listener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (puckClock == 5) {
                    bluePuck3.setClickable(true);
                    return detector.onTouchEvent(event);
                }
                return false;
            }
        };
    }

    class CustomGestureListener implements GestureDetector.OnGestureListener{

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
            calculateDistance();
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
                ObjectAnimator animZ = ObjectAnimator.ofFloat(puckClock, "z", flingDistance);
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
}
