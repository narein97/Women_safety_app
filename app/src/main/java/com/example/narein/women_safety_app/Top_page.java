package com.example.narein.women_safety_app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Top_page extends AppCompatActivity implements SimpleGestureFilter.SimpleGestureListener{

    LinearLayout ll;
    DisplayMetrics display;
    TextView text;
    static int i=1,j,ID=-1;
    static String name="";


    SimpleGestureFilter detector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_page);
        detector = new SimpleGestureFilter(this,this);
        display=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display);
        final int height=display.heightPixels;
        final int width=display.widthPixels;
        final int height_ad,width_ad;
        if(this.getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT){
            height_ad=(int)(height/4);
            width_ad= width;
        }
        else{
            height_ad= (int) (height/1.8);
            width_ad=width*3/4;
        }
        ll=(LinearLayout)findViewById(R.id.top_ll);
        for(j=0;j<ll.getChildCount();j++){
            text = (TextView)ll.getChildAt(j);
            text.setWidth(width_ad);
            text.setHeight(height_ad);
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(ID==-1){
                        text=(TextView)v;
                        text.setHeight(height);text.setWidth(width);
                        int resID=getResources().getIdentifier("pic"+i,"drawable",getPackageName());
                        Log.d("ID: "," "+resID);
                        if(i==10){i=1;}else{i++;}
                        text.setBackground(getResources().getDrawable(resID));
                        name=(String)text.getText();
                        text.setText("");
                        ID=text.getId();
                    }
                    else{
                        text=(TextView)v;
                        if(ID==text.getId()){
                            text=(TextView)v;
                            text.setText(name);
                            name="";
                            text.setHeight(height_ad);
                            text.setBackgroundColor(getResources().getColor(R.color.tip_background));
                            ID=-1;
                        }
                        else{
                            TextView t = (TextView)findViewById(ID);
                            t.setHeight(height_ad);
                            t.setBackgroundColor(getResources().getColor(R.color.tip_background));
                            t.setText(name);
                            text.setHeight(height);text.setWidth(width);
                            int resID=getResources().getIdentifier("pic"+i,"drawable",getPackageName());
                            if(i==10){i=1;}else{i++;}
                            Log.d("ID: "," "+resID);
                            text.setBackground(getResources().getDrawable(resID));
                            name=(String)text.getText();
                            text.setText("");
                            ID=text.getId();
                        }
                    }
                }
            });
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent me){
        // Call onTouchEvent of SimpleGestureFilter class
        //this.detector.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }

    @Override
    public void onSwipe(int direction) {
        //String str = "";
        //switch (direction) {
        //    case SimpleGestureFilter.SWIPE_UP:
        //        str = "Swipe Up";
        //        this.finish();
        //        startActivity(new Intent(Top_page.this,MainActivity.class));
        //        Top_page.this.overridePendingTransition(R.anim.slide_top,R.anim.slide_down);
        //        break;
        //}
    }

    @Override
    public void onBackPressed(){
        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            this.finish();
            startActivity(new Intent(Top_page.this,MainActivity.class));
            Top_page.this.overridePendingTransition(R.anim.slide_top,R.anim.slide_down);
        } else {
            getFragmentManager().popBackStack();
        }
    }
}
