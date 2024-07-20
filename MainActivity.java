package com.olegtimermanis.drag_burn;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainActivity extends AppCompatActivity {
      int money = 3;
      int gold = 1;

    int engine = 4;
    int tires = 4;
    int cool = -1;
    int driver = 1;

    int enginestartprice = 13;
    int tiresstartprice = 12;
    int coolstartprice = 11;
    int driverstartprice = 14;
    int buycoeff = 12;
    int wincoeff = 100;
    double afkcoeff = 0.05;
    int goldcoeff=24;
    int maxlvl = 9;
    int afkcounter = 0;
    int afkcountermax = 100;// num*speedupd to gen money

      int minspeed = 100;
      int speed = minspeed;
      int signtime = 300000;
      int cloudtime = 1200000;
      int speedupd = 100;
      int maxspeed = 3000;
      int accelerationbase = engine + tires ;
      int acceleration = accelerationbase;
      int nitro = 0;
      int heat = 0;
      int heatup = 5;
      int tempdelta = 0;
      int distance = 200000;
      int distcount = 0;
      int time = 0;
      int raceon =0;
      int burn = 0;
      int gear = 1;
      int numofgears = 6;
      int gearlow = 0;
      int gearhigh = maxspeed/numofgears;
      int gearnow = 1;
      int gearpressed=0;
      int goodcount=0;


    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_COUNTER0 = "money";
    public static final String APP_PREFERENCES_COUNTER1 = "gold";
    public static final String APP_PREFERENCES_COUNTER2 = "engine";
    public static final String APP_PREFERENCES_COUNTER3 = "tires";
    public static final String APP_PREFERENCES_COUNTER4 = "cool";
    public static final String APP_PREFERENCES_COUNTER5 = "driver";
    public static final String APP_PREFERENCES_COUNTER6 = "money2";
    private SharedPreferences mSettings;

    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 310;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (AUTO_HIDE) {
                        delayedHide(AUTO_HIDE_DELAY_MILLIS);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    view.performClick();
                    break;
                default:
                    break;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
 load();

        setContentView(R.layout.activity_main);
        mVisible = true;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        int widthl = (int)(width*0.3);
        int widthr= (int)(-width*1.3);

        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.sign);

        TextView zz1 = findViewById(R.id.menumoney);
        zz1.invalidate();
        zz1.setText(showmoneyg());

        TextView z1 = findViewById(R.id.menumoney);
        z1.invalidate();
        z1.setText(showmoneyg());

       Button b1 = findViewById(R.id.buttonl);
        b1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //Button Down
                        if (raceon==1) {
                            tempdelta = heatup;
                            nitro = (int) (acceleration / 2);
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        // Released
                        if (raceon==1) {
                            tempdelta = cool;
                            nitro = 0;
                        }
                        return true;
                    case MotionEvent.ACTION_CANCEL:
                        // Released - Dragged finger outside
                        if (raceon==1) {
                            tempdelta = cool;
                            nitro = 0;
                        }
                        return true;
                }
                return false;
            }
        });

        Timer myTimer;
        myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            public void run() {
                timerTick();
            }
        }, 0, speedupd);


        TextView tv1 = (TextView) findViewById(R.id.sign);
        Animation anim = new TranslateAnimation(widthl, widthr, 0, 0);
        anim.setDuration(signtime/speed);

        anim.setAnimationListener(new Animation.AnimationListener() {
@Override
public void onAnimationStart(Animation animation) {}
   @Override
public void onAnimationRepeat(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation arg0) {
                anim.setDuration(signtime/speed); tv1.startAnimation(anim);
            }
    });
                tv1.startAnimation(anim);

        Button tv2 = (Button) findViewById(R.id.cloud);
        Animation anim2 = new TranslateAnimation(widthl, widthr, 0, -50);
        anim2.setDuration(cloudtime/speed);

        anim2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationRepeat(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation arg0) {
                anim2.setDuration(cloudtime/speed); tv2.startAnimation(anim2);
            }
        });
        tv2.startAnimation(anim2);

        ImageView tv3 = (ImageView) findViewById(R.id.car);
        Animation anim3 = new TranslateAnimation(-40, 40, -30, 30);
        anim3.setRepeatCount(Animation.INFINITE);
        anim3.setRepeatMode(Animation.REVERSE);
        anim3.setDuration(1000);

        anim3.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationRepeat(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation arg0) {
                tv3.startAnimation(anim3);
            }
        });
        tv3.startAnimation(anim3);

        ImageView tv4 = (ImageView) findViewById(R.id.car2);
        Animation anim4 = new TranslateAnimation(30, -30, 20, -20);
        anim4.setRepeatCount(Animation.INFINITE);
        anim4.setRepeatMode(Animation.REVERSE);
        anim4.setDuration(1700);

        anim4.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationRepeat(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation arg0) {
                tv4.startAnimation(anim4);
            }
        });
        tv4.startAnimation(anim4);


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    public void onClickr(View view) {
        if (raceon == 1) {
            if (gearpressed == 0) {
                if (speed < maxspeed - (maxspeed / numofgears) && gearhigh - speed < maxspeed / numofgears) {
                    int t = getgear();
                    if (t < 60) {
                        acceleration = accelerationbase * 2 / 3;
                        drawgearbtn();
                    }
                    if (t >= 70 && t <= 90) {
                        goodcount++;
                        acceleration = accelerationbase;
                        drawgearbtn();
                    }
                    if (t > 90) {
                        burn = 2;
                        endrace();
                    }
                } else {
                    if (gearhigh - speed >= maxspeed / numofgears) {
                        acceleration = accelerationbase * 2 / 3;
                        drawgearbtn();
                    }
                }
            }
        }
    }

    private void timerTick() {
        this.runOnUiThread(doTask);
    }

    private Runnable doTask = new Runnable() {
        public void run() {
            afkcounter++; if (afkcounter >=afkcountermax){
                afkcounter=0;
                int z = (int) (countcar()*afkcoeff*driver);
                money+=z;
                save();
            }
            if (raceon==1) {
                int oldspeed = speed;
                speed += acceleration + nitro;
                if (speed > maxspeed) {
                    speed = maxspeed;
                }
                if (speed > gearhigh) {
                    speed = gearhigh;
                }

                if (gearpressed==1){
    if (oldspeed<maxspeed/numofgears && speed >= maxspeed/numofgears ){gearpressed=0;}
    if (oldspeed<maxspeed/numofgears +1*maxspeed/numofgears  && speed >= maxspeed/numofgears +1*maxspeed/numofgears ){gearpressed=0;}
    if (oldspeed<maxspeed/numofgears +2*maxspeed/numofgears  && speed >= maxspeed/numofgears +2*maxspeed/numofgears ){gearpressed=0;}
    if (oldspeed<maxspeed/numofgears +3*maxspeed/numofgears  && speed >= maxspeed/numofgears +3*maxspeed/numofgears ){gearpressed=0;}
    if (oldspeed<maxspeed/numofgears +4*maxspeed/numofgears  && speed >= maxspeed/numofgears +4*maxspeed/numofgears ){gearpressed=0;}
                }

                TextView z1 = findViewById(R.id.speedtext);
                z1.invalidate();
                z1.setText(Integer.toString((int) (speed * 0.1)));

                heat += tempdelta;
                if (heat < 0) {
                    heat = 0;
                }
                if (heat > 100) {
                    burn();
                }

                gear= getgear();
                // if ( gearhigh-speed>=maxspeed/numofgears){gear=10;}

                ProgressBar pg = findViewById(R.id.progressBar);
                pg.invalidate();
                pg.setProgress(heat);
                ProgressBar pg2 = findViewById(R.id.progressBar2);
                pg2.invalidate();
                pg2.setProgress(gear);

                distcount += speed;
                time += speedupd;
                if (distcount >= distance) {
                    endrace();
                }
            }
        }
    };

    private void burn(){
    burn = 1; endrace();
    }

private int getgear(){
int a = 100*(speed-gearlow)/(gearhigh-gearlow);
        return a;
}

private void drawgearbtn(){
    gearpressed=1;
    gearnow++;
    gearlow = gearhigh; gearhigh+=maxspeed/numofgears;
    Button p1_button = (Button)findViewById(R.id.button2);
    p1_button.setText(Integer.toString(gearnow));
}

    private void endrace(){
        raceon=0;
        FrameLayout endscrw = findViewById(R.id.endscr);
        endscrw.setVisibility(View.VISIBLE);

        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String t = formatter.format(time);
        String t2 = "Your time: "+t+" ms";
        String t3 = "FAIL";
        String t4="";

        if (burn==1){t2="BURN"; t3="FAIL";}
        if (burn==2){t2="ENGINE_STOPPED"; t3="FAIL";}

        if (burn==0){
            if (goodcount>2 ){
                t3="WIN";
                int z = countcar()*wincoeff;
                money+=z;
                int z2 = z/goldcoeff;
                if (z2<1){z2=1;}
                gold+=z2;
                t4+= "+ "+z+"$  "+z2+"G";
                t4+="\nTotal: "+showmoneyg();
                save();
            }
        }

        TextView z1 = findViewById(R.id.timestr);
        z1.invalidate();
        z1.setText(t2);
        TextView z2 = findViewById(R.id.endstr);
        z2.invalidate();
        z2.setText(t3);
        TextView z3 = findViewById(R.id.winmoneystr);
        z3.invalidate();
        z3.setText(t4);
    }


public void preparetorace(View view){
     speed = minspeed;
    accelerationbase = engine + tires;
    acceleration = accelerationbase;
     nitro = 0;
     distcount = 0;
     time = 0;
     raceon =1;
     burn = 0;
     heat=0;
     tempdelta=0;
     gear = 1;
     gearlow = 0;
     gearhigh = maxspeed/numofgears;
     gearnow = 1;
     gearpressed=0;
     goodcount=0;
    closescr();
    Button p1_button = (Button)findViewById(R.id.button2);
    p1_button.invalidate();
    p1_button.setText(Integer.toString(gearnow));
}


private int countcar(){
int a=engine* enginestartprice + tires*tiresstartprice - cool*coolstartprice + driver* driverstartprice;
return a;
}


private String showmoneyg(){
String s=money+"$  "+gold+"G";
return s;
}


    public void menuopen(View view) {
        closescr();
        FrameLayout men = findViewById(R.id.menu);
        men.setVisibility(View.VISIBLE);
        TextView z1 = findViewById(R.id.menumoney);
        z1.invalidate();
        z1.setText(showmoneyg());
    }


    @Override
    protected void onResume() {
        super.onResume();
        load();
    }

    @Override
    protected void onPause() {
        super.onPause();
 save();
    }


    public void tour(View view) {
Toast toast = Toast.makeText(getApplicationContext(),"In the future",Toast.LENGTH_SHORT); toast.show();
// Toast toast2 = Toast.makeText(getApplicationContext(),money + " "+gold+" "+engine+" "+tires+" "+cool+" "+driver,Toast.LENGTH_LONG); toast2.show();
    }

public void save(){
    SharedPreferences.Editor editor = mSettings.edit();
    editor.putInt(APP_PREFERENCES_COUNTER0, money);
    editor.putInt(APP_PREFERENCES_COUNTER1, gold);
    editor.putInt(APP_PREFERENCES_COUNTER2, engine);
    editor.putInt(APP_PREFERENCES_COUNTER3, tires);
    editor.putInt(APP_PREFERENCES_COUNTER4, cool);
    editor.putInt(APP_PREFERENCES_COUNTER5, driver);
    editor.putInt(APP_PREFERENCES_COUNTER6, money);
    editor.apply();
}

public void load(){
    if (mSettings.contains(APP_PREFERENCES_COUNTER0)) {
        // read data
        money = mSettings.getInt(APP_PREFERENCES_COUNTER0, 0);
        gold = mSettings.getInt(APP_PREFERENCES_COUNTER1, 0);
        engine =  money = mSettings.getInt(APP_PREFERENCES_COUNTER2, 0);
        tires = mSettings.getInt(APP_PREFERENCES_COUNTER3, 0);
        cool =  money = mSettings.getInt(APP_PREFERENCES_COUNTER4, 0);
        driver = mSettings.getInt(APP_PREFERENCES_COUNTER5, 0);
        money = mSettings.getInt(APP_PREFERENCES_COUNTER6, 0);
    }
}


    public void infoop(View view) {
        closescr();
        FrameLayout men = findViewById(R.id.infoscr);
        men.setVisibility(View.VISIBLE);
    }


    public void closescr(){
        FrameLayout endscrw0 = findViewById(R.id.menu);
        endscrw0.setVisibility(View.GONE);
        FrameLayout endscrw = findViewById(R.id.endscr);
        endscrw.setVisibility(View.GONE);
        FrameLayout endscrw2 = findViewById(R.id.infoscr);
        endscrw2.setVisibility(View.GONE);
        FrameLayout endscrw3 = findViewById(R.id.garagescr);
        endscrw3.setVisibility(View.GONE);
    }


    public void garageop(View view) {
        closescr();
        TextView z1 = findViewById(R.id.garagemoney);
        z1.invalidate();
        z1.setText(showmoneyg());
        FrameLayout men = findViewById(R.id.garagescr);
        men.setVisibility(View.VISIBLE);

        Button p1_button = (Button)findViewById(R.id.enginebtn);
        p1_button.invalidate();
        int e =engine*enginestartprice*buycoeff;
        int e2 = e/goldcoeff;
        String w="engine: "+(engine+1)+"  for: " +e + "$  "+e2+"G";
        p1_button.setText(w);

        Button p1_button2 = (Button)findViewById(R.id.tiresbtn);
        p1_button2.invalidate();
        int ee =tires*tiresstartprice*buycoeff;
        int ee2 = ee/goldcoeff;
        String w2="tires: "+(tires+1)+"  for: " +ee + "$  "+ee2+"G";
        p1_button2.setText(w2);

        Button p1_button3 = (Button)findViewById(R.id.coolantbtn);
        p1_button3.invalidate();
        int eee =-cool*coolstartprice*buycoeff;
        int eee2 = eee/goldcoeff;
        String w3="coolant: "+(-(cool-1))+"  for: " +eee + "$  "+eee2+"G";
        p1_button3.setText(w3);

        Button p1_button4 = (Button)findViewById(R.id.driverbtn);
        p1_button4.invalidate();
        int e1ee =driver*driverstartprice*buycoeff;
        int e1ee2 = e1ee/goldcoeff;
        String w4="driver: "+(driver+1)+"  for: " +e1ee + "$  "+e1ee2+"G";
        p1_button4.setText(w4);
    }

    public void engineupgrade(View view) {
        if (engine<maxlvl){
if (money>= engine*enginestartprice*buycoeff && gold >= engine*enginestartprice*buycoeff/goldcoeff){
engine++;
    money-=engine*enginestartprice*buycoeff;
    gold-=engine*enginestartprice*buycoeff/goldcoeff;
save();
closescr();
    FrameLayout men = findViewById(R.id.menu);
    men.setVisibility(View.VISIBLE);
    TextView z1 = findViewById(R.id.menumoney);
    z1.invalidate();
    z1.setText(showmoneyg());
}
else{
    nocash();
}
    }
    }

    public void tiresupgrade(View view) {
        if (tires<maxlvl){
            if (money>= tires*tiresstartprice*buycoeff && gold >= tires*tiresstartprice*buycoeff/goldcoeff){
                tires++;
                money-=tires*tiresstartprice*buycoeff;
                gold-=tires*tiresstartprice*buycoeff/goldcoeff;
                save();
                closescr();
                FrameLayout men = findViewById(R.id.menu);
                men.setVisibility(View.VISIBLE);
                TextView z1 = findViewById(R.id.menumoney);
                z1.invalidate();
                z1.setText(showmoneyg());
            }
            else{
                nocash();
            }
        }
    }

    public void coolupgrade(View view) {
        if (-cool<maxlvl){
            if (money>= -cool*coolstartprice*buycoeff && gold >= -cool*coolstartprice*buycoeff/goldcoeff){
                cool--;
                money-=-cool*coolstartprice*buycoeff;
                gold-=-cool*coolstartprice*buycoeff/goldcoeff;
                save();
                closescr();
                FrameLayout men = findViewById(R.id.menu);
                men.setVisibility(View.VISIBLE);
                TextView z1 = findViewById(R.id.menumoney);
                z1.invalidate();
                z1.setText(showmoneyg());
            }
            else{
                nocash();
            }
        }
    }

    public void driverupgrade(View view) {
        if (driver<maxlvl){
            if (money>= driver*driverstartprice*buycoeff && gold >= driver*driverstartprice*buycoeff/goldcoeff){
                driver++;
                money-=driver*driverstartprice*buycoeff;
                gold-=driver*driverstartprice*buycoeff/goldcoeff;
                save();
                closescr();
                FrameLayout men = findViewById(R.id.menu);
                men.setVisibility(View.VISIBLE);
                TextView z1 = findViewById(R.id.menumoney);
                z1.invalidate();
                z1.setText(showmoneyg());
            }
            else{
                nocash();
            }
        }
    }


  public void nocash(){
        Toast toast = Toast.makeText(getApplicationContext(),"NO $ or G",Toast.LENGTH_SHORT); toast.show();
  }
}