package com.example.narein.women_safety_app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.test.mock.MockPackageManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import static android.R.id.message;

public class MainActivity extends AppCompatActivity implements SimpleGestureFilter.SimpleGestureListener{

    private SimpleGestureFilter detector;
    private ImageView sos;
    GPSTracker gps;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int REQUEST_CODE_PERMISSION = 2;
    String phoneNo,message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sos = (ImageView)findViewById(R.id.sos);
        sos.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                sendSMSMessage();
                Toast.makeText(MainActivity.this,"Dont worry, we have called for help :)",Toast.LENGTH_LONG).show();
                return false;
            }
        });
        detector = new SimpleGestureFilter(this,this);
        ImageView siren = (ImageView)findViewById(R.id.siren);
        siren.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                try{
                    MediaPlayer mPlayer = MediaPlayer.create(MainActivity.this, R.raw.siren);
                    mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mPlayer.start();

                }catch(Exception e){e.printStackTrace();}
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent me){
        // Call onTouchEvent of SimpleGestureFilter class
        this.detector.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }

    protected void sendSMSMessage() {
        phoneNo = "+917259379340";
        message += "\nQUICK ! IS IN GRAVE DANGER !\nHERE ARE HER COORDINATES :\n LATITUDE: 12.9991875\n LONGITUDE: 77.7575462\n";
        Log.d("STEP:","sendSMSMessage");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {Log.d("STEP:","sendSMSif");
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
                Log.d("STEP:","sendSMSelse");
            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        Log.d("STEP:","onRequestPermissionResult");
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {Log.d("STEP:","onreq-MY_PER");
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("STEP:","onreq-MY_PER-if");
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, message, null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Log.d("STEP:","onreq-MY_PER-else");
                    Toast.makeText(getApplicationContext(),
                            "SMS failed, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            case 1: {Log.d("STEP:","onreq-case1");
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("STEP:","onreq-case1-if");
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.d("STEP:","onreq-case1-else");
                }
                return;
            }
        }
    }


    @Override
    public void onSwipe(int direction) {
        String str = "";
        switch (direction) {
            case SimpleGestureFilter.SWIPE_RIGHT : str = "Swipe Right";
                this.finish();
                startActivity(new Intent(MainActivity.this,Left_page.class));
                MainActivity.this.overridePendingTransition(R.anim.slide_right,R.anim.slide_left);
                break;
            case SimpleGestureFilter.SWIPE_LEFT :  str = "Swipe Left";
                this.finish();
                startActivity(new Intent(MainActivity.this,Right_page.class));
                MainActivity.this.overridePendingTransition(R.anim.main_right_left,R.anim.main_right_right);
                break;
            case SimpleGestureFilter.SWIPE_DOWN :  str = "Swipe Down";
                this.finish();
                startActivity(new Intent(MainActivity.this,Top_page.class));
                MainActivity.this.overridePendingTransition(R.anim.bottom_up,R.anim.main_up);
                break;
            case SimpleGestureFilter.SWIPE_UP :    str = "Swipe Up";
                this.finish();
                startActivity(new Intent(MainActivity.this,Bottom_page.class));
                MainActivity.this.overridePendingTransition(R.anim.slide_top,R.anim.slide_down);
                break;
        }
    }
}
