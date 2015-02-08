package com.amine.torf;


import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amine.torf.helpers.DataBaseHelper;
import com.amine.torf.helpers.DataManager;
import com.amine.torf.pojo.QuizPojo;
import com.andtinder.model.CardModel;
import com.andtinder.view.CardContainer;
import com.andtinder.view.SimpleCardStackAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class Timer_questions extends Activity {
    String que, category, comment = "";
    int isTrue, difficulty;
    int rightans = 0;
    int wrongans = 0;
    int i = 0;
    String right, wrong, next;
    DataBaseHelper db;
    TextView txtcategoryname, tv, tanoofque, lifeline;
    static int currentQuestion = 0;
    MediaPlayer mp;
    List<QuizPojo> getquestions = null;
    QuizPojo cn = null;
    MyCounter timer = null;
    Setting_preference pref;
    String no_of_questions, strtimer;
    long savedtimer;
    int totalQueLen;
    boolean cbvibrate, cbtimer;
    Vibrator vibe;
    String categoryname;
    int mistake = DataManager.mistake;
    int numofquestions = DataManager.noofquestions;
    private AdView adView;
    Button btnpass, btntimer;
    private static final String AD_UNIT_ID = DataManager.admobid;
    TextView[] btn1, btn2;
    // Random rand = new Random();
    boolean fifty = false, pass = false, time = false;
    Typeface normal, bold;
    LinearLayout lltimer;
    // //swipe card
    private CardContainer mCardContainerQ, mCardContainerC,
            mCardContainerLIntro, mCardContainerRIntro;
    final static int INTERVAL = 800; // 1 second
    boolean whichColor = true;
    CardModel cardModelQuestion, cardModelComment;
    SimpleCardStackAdapter adapterQuestion, adapterComment;
    LinearLayout linearBoundQ, linearBoundC, linearButtons;

    public long timerCount = 15000;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quizscreen);

        adapterQuestion = new SimpleCardStackAdapter(this);
        adapterComment = new SimpleCardStackAdapter(this);

        linearBoundQ = (LinearLayout) findViewById(R.id.layoutBoundQ);
        linearBoundC = (LinearLayout) findViewById(R.id.layoutBoundC);
        linearButtons = (LinearLayout) findViewById(R.id.buttons);
        linearBoundC.setVisibility(View.GONE);

        cardModelQuestion = new CardModel("");
        cardModelComment = new CardModel("");

        mCardContainerQ = (CardContainer) findViewById(R.id.layoutCardQuestion);
        mCardContainerC = (CardContainer) findViewById(R.id.layoutCardComment);

        normal = Typeface.createFromAsset(getAssets(), "normal.ttf");
        bold = Typeface.createFromAsset(getAssets(), "bold.ttf");

        tv = (TextView) findViewById(R.id.tv);
        tv.setText(" ");
        db = new DataBaseHelper(getApplicationContext());

        pref = new Setting_preference(getApplicationContext());

        strtimer = DataManager.timer;
        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        savedtimer = Long.parseLong(strtimer);
        categoryname = getIntent().getStringExtra("categoryname");

        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(Timer_questions.this);

        cbvibrate = prefs.getBoolean("cbvibrate", true);
        cbtimer = prefs.getBoolean("cbtimer", true);
        btntimer = (Button) this.findViewById(R.id.btntimer);
        lltimer = (LinearLayout) this.findViewById(R.id.lltimer);

        if (cbtimer) {
            timer = new MyCounter(savedtimer * 1000, 1000);
            timer.start();
        } else {
            tv.setVisibility(View.INVISIBLE);
            btntimer.setVisibility(View.GONE);
            lltimer.setVisibility(View.GONE);
        }

        currentQuestion = 0;
        rightans = 0;
        wrongans = 0;

        lifeline = (TextView) this.findViewById(R.id.txtlife);
        btnpass = (Button) this.findViewById(R.id.btnskip);
        txtcategoryname = (TextView) this.findViewById(R.id.txtcategoryname);
        tanoofque = (TextView) this.findViewById(R.id.tanoofque1);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        right = "Right answer";
        wrong = "Wrong answer";
        next = "Next Question";

        tanoofque.setTypeface(normal);
        lifeline.setTypeface(bold);
        btntimer.setTypeface(bold);
        btnpass.setTypeface(bold);
        txtcategoryname.setTypeface(normal);
        tv.setTypeface(bold);
        totalQueLen = 20;

        getquestions = db.getquestion(categoryname);

        Log.w("questions ", "" + getquestions.get(0).get_isTrue());
        getquestionsanswers(currentQuestion);

        txtcategoryname.setText(categoryname.toUpperCase());
        btntimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cbtimer) {
                    time = true;
                    long newtime = 15000;
                    timer.cancel();
                    timer = new MyCounter(newtime, 1000);
                    timer.start();
                    btntimer.setVisibility(View.INVISIBLE);
                }
            }
        });

        btnpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pass = true;
                nextquestion(0);
                btnpass.setVisibility(View.INVISIBLE);
            }
        });

        cardModelComment.setTitle("Swipe right if the answer is wright");

        cardModelComment
                .setOnCardDimissedListener(new CardModel.OnCardDimissedListener() {
                    public void onRight() {
                        Log.i("Swipeable Cards ", "right");

                    }

                    public void onLeft() {
                        Log.i("Swipeable Cards ", "Left");

                    }
                });
        adapterComment.add(cardModelComment);
        mCardContainerC.setAdapter(adapterComment);

        lifeline.setText("Life : " + mistake);

        adView = new AdView(this);

        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(AD_UNIT_ID);
        AdRequest adRequest = new AdRequest.Builder().build();

        adView.loadAd(adRequest);
        LinearLayout ll = (LinearLayout) findViewById(R.id.ad);
        ll.addView(adView);

    }

    public void userSwipe(int isTrueAnswer, boolean swipedRight, String comment) {

        if ((isTrueAnswer == 1 && swipedRight)
                || (isTrueAnswer == 0 && !swipedRight)) {
            rightans++;
            linearBoundQ.setBackgroundColor(Color.GREEN);
            nextquestion(500);
        }

        else {
            if (isTrueAnswer == 0 && swipedRight) {

                showComment();
            }

            if (isTrueAnswer == 1 && !swipedRight) {
                nextquestion(500);
                linearBoundQ.setBackgroundColor(Color.RED);
            }

            wrongans++;
            vibrate();
            mistake--;
            lifeline.setText("Life : " + mistake);

        }
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(INTERVAL);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    updateColor();

                }
            }
        }).start();
    }

    public void nextquestion(int SPLASHTIME) {

        if (cbtimer) {
            timer.cancel();
        }
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                db.close();
                currentQuestion++;

                if (mistake > 0) {

                    if (currentQuestion < totalQueLen) {
                        if (cbtimer) {
                            timer = new MyCounter(savedtimer * 1000, 1000);
                            timer.start();
                        }
                        getquestionsanswers(currentQuestion);

                    } else {

                        if (cbtimer) {
                            timer.cancel();
                        }
                        Intent iScore = new Intent(Timer_questions.this,
                                Score.class);
                        iScore.putExtra("rightans", rightans);
                        iScore.putExtra("totalques", totalQueLen);
                        iScore.putExtra("category", category);
                        finish();
                        startActivity(iScore);

                    }
                } else {
                    if (cbtimer) {
                        timer.cancel();
                    }
                    Intent iScore = new Intent(Timer_questions.this,
                            Score.class);
                    iScore.putExtra("rightans", rightans);
                    iScore.putExtra("totalques", totalQueLen);
                    iScore.putExtra("category", category);
                    finish();
                    startActivity(iScore);
                }

            }

        }, SPLASHTIME);

    }

    public void vibrate() {
        if (cbvibrate) {
            vibe.vibrate(700);
        }
    }

    public void getquestionsanswers(int index) {
        cn = getquestions.get(currentQuestion);
        que = cn.get_question();
        isTrue = cn.get_isTrue();
        comment = cn.get_comment();
        setCurrentQuestion(que, isTrue, comment);
    }

    public void showComment() {

        if (cbtimer)
            timer.cancel();

        linearBoundQ.setVisibility(View.GONE);
        linearButtons.setVisibility(View.INVISIBLE);
        linearBoundC.setVisibility(View.VISIBLE);
        linearBoundC.setBackgroundColor(Color.RED);

        tanoofque.setTextColor(Color.RED);
        tanoofque.setText("Swipe to pass!!");

        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(100); // You can manage the time of the blink with this
        // parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        tanoofque.startAnimation(anim);

        cardModelComment.setTitle("'" + comment + "'");

        cardModelComment
                .setOnCardDimissedListener(new CardModel.OnCardDimissedListener() {
                    public void onRight() {
                        Log.i("Swipeable Cards ", "right");
                        passComment(0);

                    }

                    public void onLeft() {
                        Log.i("Swipeable Cards ", "Left");
                        passComment(0);

                    }
                });
        adapterComment.add(cardModelComment);
        mCardContainerC.setAdapter(adapterComment);

        // passComment(2200);

    }

    public void passComment(int timer) {
        linearBoundQ.setVisibility(View.VISIBLE);
        linearBoundC.setVisibility(View.GONE);
        linearButtons.setVisibility(View.VISIBLE);

        nextquestion(timer);

    }

    public void setCurrentQuestion(String question, final int isTrueQuestion,
                                   final String comment) {

        i++;
        String noofque = "Question No. " + i + " out of " + totalQueLen + " "
                + isTrueQuestion;
        tanoofque.setTextColor(Color.BLACK);
        tanoofque.setText(noofque);
        tanoofque.clearAnimation();
        cardModelQuestion.setTitle(question);

        // Resources r = getResources();
        Log.w(question, " " + isTrueQuestion);

        cardModelQuestion
                .setOnCardDimissedListener(new CardModel.OnCardDimissedListener() {
                    public void onRight() {
                        Log.i("Swipeable Cards ", "right");

                        if (cbtimer) {
                            timer.cancel();
                        }

                        userSwipe(isTrueQuestion, true, comment);
                        db.insertIsanswered(cn.get_id());
                    }

                    public void onLeft() {
                        Log.i("Swipeable Cards ", "Left");

                        if (cbtimer) {
                            timer.cancel();
                        }

                        userSwipe(isTrueQuestion, false, comment);
                        db.insertIsanswered(cn.get_id());

                    }
                });

        adapterQuestion.add(cardModelQuestion);
        mCardContainerQ.setAdapter(adapterQuestion);

        // taQue.setText(que);

    }

    private void updateColor() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                linearBoundQ.setBackgroundResource(R.drawable.background);

            }
        });
    }

    public class MyCounter extends CountDownTimer {

        public MyCounter(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            Log.w("Counter", " " + timerCount);
            if (timerCount <= 2000) {
                mistake--;
                lifeline.setText("Life : " + mistake);
                nextquestion(400);
            }

        }

        @SuppressLint("UseValueOf")
        @Override
        public void onTick(long millisUntilFinished) {

            timerCount = millisUntilFinished;
            Integer milisec = new Integer(
                    new Double(millisUntilFinished).intValue());

            Integer cd_secs = milisec / 1000;

            Integer seconds = (cd_secs % 3600) % 60;

            tv.setTextColor(Color.BLACK);
            tv.setText("Timer  : " + String.format("%02d", seconds));

            if (seconds < 6) {
                tv.setTextColor(Color.RED);
            }
        }

    }

    @Override
    public void onBackPressed() {
        if (cbtimer) {
            timer.cancel();
        }
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Do you want to leave this Test?")
                .setNegativeButton(android.R.string.no,
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {

                                if (cbtimer) {
                                    timer.cancel();
                                }

                                timer = new MyCounter(timerCount, 1000);
                                timer.start();

                            }
                        })
                .setPositiveButton(android.R.string.yes,
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {

                                if (cbtimer) {
                                    timer.cancel();
                                }
                                int totalQueLen = db.getContactsCount();
                                Intent i = new Intent(Timer_questions.this,
                                        Score.class);
                                i.putExtra("rightans", rightans);
                                i.putExtra("totalques", totalQueLen);
                                i.putExtra("category", category);
                                finish();
                                startActivity(i);
                            }
                        }).create().show();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (cbtimer) {
            timer.cancel();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // timer = new MyCounter(timerCount, 1000);
        // timer.start();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (cbtimer) {
            timer.cancel();
        }
    }

}
