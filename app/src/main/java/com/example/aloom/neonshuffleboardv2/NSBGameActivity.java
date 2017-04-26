package com.example.aloom.neonshuffleboardv2;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;

//Intent is to produce a realistic simulation of a fling on the puck object for the Neon ShuffleBoard game
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
    int maxDuration = 35000;
    ImageView redPuck1;
    ImageView redPuck2;
    ImageView redPuck3;
    ImageView bluePuck1;
    ImageView bluePuck2;
    ImageView bluePuck3;
    int maxScore = 30;
    boolean maxScoreReached = false;
    View.OnTouchListener puckListener;
    GestureDetector detector;
    ImageView[] puckCycleList;
    TextSwitcher blueSwitcher;
    TextSwitcher redSwitcher;
    TextView blueScoreText;
    TextView redScoreText;

    float startPositionTime;
    RectF pointZone;

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
        blueSwitcher = (TextSwitcher)findViewById(R.id.blueScoreSwitcher);
        redSwitcher = (TextSwitcher)findViewById(R.id.redScoreSwitcher);
        blueScoreText = (TextView)findViewById(R.id.blueScoreText);
        redScoreText = (TextView)findViewById(R.id.redScoreText);
        blueScoreText.setText("0");
        redScoreText.setText("0");
        blueScoreText.setTextSize(1,60f);
        redScoreText.setTextSize(1, 60f);
        listenForTouchOnPuck();
        //Round cycle
        redPuck1.setClickable(true);
        playGame();
        //reset
    }

    public void calculateDistance() {
        Log.d(TAG, "initial x pos: " + downXPOS);
        Log.d(TAG, "initial y pos: " + downYPOS);
        flingDistance = (float) Math.sqrt((dx * dx) + (dy * dy));
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

        ObjectAnimator animY = ObjectAnimator.ofFloat(bluePuck1, View.Y,bluePuck1.getY());
    }

    //exclude first puck
    public void setupInitialPuckVisibility() {
        redPuck2.setVisibility(View.GONE);
        redPuck3.setVisibility(View.GONE);
        bluePuck1.setVisibility(View.GONE);
        bluePuck2.setVisibility(View.GONE);
        bluePuck3.setVisibility(View.GONE);
    }

    public void listenForTouchOnPuck() {
        puckListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return detector.onTouchEvent(event);
            }
        };
    }

    public synchronized void playGame() {

        if (puckClock == 0) {
            puckCycleList[puckClock].setOnTouchListener(puckListener);
            puckCycleList[puckClock].setClickable(true);
        }
        if (puckClock == 1) {
            puckCycleList[puckClock].setOnTouchListener(puckListener);
            puckCycleList[puckClock].setClickable(true);
            puckCycleList[puckClock - 1].setClickable(false);
        }
        if (puckClock == 2) {
            puckCycleList[puckClock].setOnTouchListener(puckListener);
            puckCycleList[puckClock].setClickable(true);
            puckCycleList[puckClock - 1].setClickable(false);
        }
        if (puckClock == 3) {
            puckCycleList[puckClock].setOnTouchListener(puckListener);
            puckCycleList[puckClock].setClickable(true);
            puckCycleList[puckClock - 1].setClickable(false);
        }
        if (puckClock == 4) {
            puckCycleList[puckClock].setOnTouchListener(puckListener);
            puckCycleList[puckClock].setClickable(true);
            puckCycleList[puckClock - 1].setClickable(false);
            puckCycleList[puckClock].setVisibility(View.VISIBLE);
            //disablePreviousClickableViews();
        }
        if (puckClock == 5) {
            puckCycleList[puckClock].setOnTouchListener(puckListener);
            puckCycleList[puckClock].setClickable(true);
            puckCycleList[puckClock - 1].setClickable(false);
            //disablePreviousClickableViews();
        }
    }

    public void resetPuckPositions() {
            //All values were calculated manually

    }

    class CustomGestureListener implements GestureDetector.OnGestureListener {

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
            downXPOS = fingerDown.getX();
            Log.d(TAG, "XPOS finger down:  " + downXPOS);
            downYPOS = fingerDown.getY();
            Log.d(TAG, "YPOS finger down:  " + downYPOS);
            Log.d(TAG, "puck left x: " + puckCycleList[puckClock].getLeft());
            Log.d(TAG, "puck right x: " + puckCycleList[puckClock].getRight());
            Log.d(TAG, "puck top y: " + puckCycleList[puckClock].getTop());
            Log.d(TAG, "puck bottom y : " + puckCycleList[puckClock].getBottom());
            Log.i(TAG, "____FLING INITIATED____");
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
            if (puckClock < 6) { //If pucks are still on the table
                puckCycleList[puckClock].setVisibility(View.VISIBLE);
                playGame();
            } else { // Else we just flung the last puck.
                endRound();
            }
            Log.d(TAG, "Puck clock Value " + puckClock);

            return true;
        }

        public void endRound() {
            puckCycleList[puckClock - 1].setClickable(false);
            resetPuckPositions();
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
            final ObjectAnimator animY = ObjectAnimator.ofFloat(puckCycleList[puckClock], View.TRANSLATION_Y, flingDistance * yVelocity);
            final ObjectAnimator animX = ObjectAnimator.ofFloat(puckCycleList[puckClock], View.TRANSLATION_X, flingDistance * xVelocity);
            animY.setDuration(maxDuration);
            animX.setDuration(maxDuration);
            animatePuckProperties = new AnimatorSet();
            animatePuckProperties.setInterpolator(new DecelerateInterpolator(16));
            animatePuckProperties.setStartDelay(0);
            animatePuckProperties.playTogether(animY, animX);
            animatePuckProperties.setupStartValues();
            animatePuckProperties.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationCancel(Animator animation) {
                    super.onAnimationCancel(animation);

                }

                @Override
                public void onAnimationEnd(Animator animation) {

                    super.onAnimationEnd(animation);
                    if(puckClock < 3) {//red pucks
                        if (puckCycleList[puckClock].getY() < 1970 && puckCycleList[puckClock].getY() > 1850) {
                            Log.d(TAG, "Puck Y position testing within boundary: Puck :" +puckClock+" Y Pos+ "+puckCycleList[puckClock].getY() );
                            redSwitcher.setCurrentText("1");
                        }
                        if (puckCycleList[puckClock].getY() < 1970 && puckCycleList[puckClock].getY() > 1850) {
                            Log.d(TAG, "Puck Y position testing within boundary: Puck :" +puckClock+" Y Pos+ "+puckCycleList[puckClock].getY() );
                            redSwitcher.setCurrentText("1");
                        }
                        if (puckCycleList[puckClock].getY() < 1970 && puckCycleList[puckClock].getY() > 1850) {
                            Log.d(TAG, "Puck Y position testing within boundary: Puck :" +puckClock+" Y Pos+ "+puckCycleList[puckClock].getY() );
                            redSwitcher.setCurrentText("1");
                        }
                    }

                    if(puckClock >= 3 && puckClock < 6) {//blue pucks
                        if (puckCycleList[puckClock].getY() < 1970 && puckCycleList[puckClock].getY() > 1850) {
                            Log.d(TAG, "Puck Y position testing within boundary: Puck :" +puckClock+" Y Pos+ "+puckCycleList[puckClock].getY() );
                            redSwitcher.setCurrentText("1");
                        }
                        if (puckCycleList[puckClock].getY() < 1970 && puckCycleList[puckClock].getY() > 1850) {
                            Log.d(TAG, "Puck Y position testing within boundary: Puck :" +puckClock+" Y Pos+ "+puckCycleList[puckClock].getY() );
                            redSwitcher.setCurrentText("1");
                        }
                        if (puckCycleList[puckClock].getY() < 1970 && puckCycleList[puckClock].getY() > 1850) {
                            Log.d(TAG, "Puck Y position testing within boundary: Puck :" +puckClock+" Y Pos+ "+puckCycleList[puckClock].getY() );
                            redSwitcher.setCurrentText("1");
                        }
                    }
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                    super.onAnimationRepeat(animation);
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    playSound(R.raw.realisticslide2);
                }

                @Override
                public void onAnimationPause(Animator animation) {
                    super.onAnimationPause(animation);
                }

                @Override
                public void onAnimationResume(Animator animation) {
                    super.onAnimationResume(animation);
                }
            });
            animatePuckProperties.start();

        }
    }
