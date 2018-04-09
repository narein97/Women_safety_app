package com.example.narein.women_safety_app;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import java.util.StringTokenizer;

public class Left_page extends AppCompatActivity implements SimpleGestureFilter.SimpleGestureListener{

    SimpleGestureFilter detector;
    private DisplayMetrics display;
    private int height,width;
    View view;
    ArrayList<String> speech=null;
    TextToSpeech toSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_left_page);
        View view = findViewById(R.id.ai);
        display=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display);
        height=display.heightPixels;
        width=display.widthPixels;
        view.setLayoutParams(new ConstraintLayout.LayoutParams((int)(height*0.57),(int)(width*0.9)));
        view.setX((float)(width*0.02));view.setY((float)(height*0.07));
        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getSpeechInput(v);
                toSpeech=new TextToSpeech(Left_page.this, new TextToSpeech.OnInitListener(){
                    @Override
                    public void onInit(int status){
                        if(status==TextToSpeech.SUCCESS){
                            toSpeech.setLanguage(Locale.getDefault());
                        }
                        else{
                            Toast.makeText(Left_page.this,"There's something wrong with my voice.",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        detector = new SimpleGestureFilter(this,this);
    }

    public void TTS(View v){
        toSpeech.speak(speech.get(0),TextToSpeech.QUEUE_FLUSH,null);
        if(speech.get(0).equalsIgnoreCase("Glad I could help.")){
            onBackPressed();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent me){
        // Call onTouchEvent of SimpleGestureFilter class
        this.detector.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }


    @Override
    public void onSwipe(int direction) {
        String str = "";
        switch (direction) {
            case SimpleGestureFilter.SWIPE_LEFT:
                str = "Swipe Left";
                if(toSpeech!=null){toSpeech.stop();toSpeech.shutdown();}
                this.finish();
                startActivity(new Intent(Left_page.this,MainActivity.class));
                Left_page.this.overridePendingTransition(R.anim.left_page_right,R.anim.left_page_left);
                break;
        }
    }

    public void getSpeechInput(View v){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        if(intent.resolveActivity(getPackageManager())!=null){
            view=v;
            startActivityForResult(intent,10);
        }
        else{
            Toast.makeText(Left_page.this,"I'm sorry, I cannot hear you through this device",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestcode, int resultcode, Intent data){
        super.onActivityResult(requestcode,resultcode,data);
        if(requestcode==10){
            if(resultcode==RESULT_OK && data != null){
                speech=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                TextView text = (TextView)findViewById(R.id.ai_speech);
                text.setText(speech.get(0));
                nlp();
            }
        }
    }

    public void nlp(){
        StringTokenizer st = new StringTokenizer(speech.get(0)," ");
        ArrayList<String> token = new ArrayList<String>();
        while(st.hasMoreElements()){token.add(st.nextToken());}
        if(token.contains("am") && token.contains("danger")){
            speech.set(0,"Hey, don't worry. I am already on the lookout for help. Of course, I will not be able to help you as of now, since I am still being tested at MVJ College of Engineering.");
        }
        else if(token.contains("stupid") || token.contains("idiot")){
            speech.set(0,"Well, aren't you smart ?");
        }
        else if(token.contains("Thank you") || token.contains("bye") || token.contains("goodbye")){
            speech.set(0,"Glad I could help.");
        }
        else{
            speech.set(0,"I am sorry, I did not understand. I guess I still have a lot to learn.");
        }
        TTS(view);
    }

    @Override
    public void onBackPressed(){
        if(toSpeech!=null){toSpeech.stop();toSpeech.shutdown();}
        this.finish();
        startActivity(new Intent(Left_page.this,MainActivity.class));
        Left_page.this.overridePendingTransition(R.anim.left_page_right,R.anim.left_page_left);
    }
}

