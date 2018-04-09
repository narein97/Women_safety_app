package com.example.narein.women_safety_app;

import android.app.Activity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by narein on 31/3/18.
 */

public class SimpleGestureFilter extends GestureDetector.SimpleOnGestureListener{
    public static final int SWIPE_UP=1,SWIPE_DOWN=2,SWIPE_LEFT=3,SWIPE_RIGHT=4;
    public static final int MODE_TRANSPARENT=0,MODE_SOLID=1,MODE_DYNAMIC=2,ACTION_FAKE=-14;
    private int swipe_Min_Distance=100,swipe_Max_Distance=600,swipe_Min_Velocity=100;
    private int mode= MODE_DYNAMIC;
    private boolean running= true;
    private boolean tapIndicator= false;
    private Activity context;
    private GestureDetector detector;
    private SimpleGestureListener listener;

    public SimpleGestureFilter(Activity context, SimpleGestureListener sgl){
        this.context=context;
        this.detector= new GestureDetector(context,this);
        this.listener=sgl;
    }

    public void onTouchEvent(MotionEvent event){
        if(!this.running){return;}
        boolean result = this.detector.onTouchEvent(event);
        if(this.mode == MODE_SOLID)
            event.setAction(MotionEvent.ACTION_CANCEL);
        else if (this.mode == MODE_DYNAMIC) {

            if (event.getAction() == ACTION_FAKE)
                event.setAction(MotionEvent.ACTION_UP);
            else if (result)
                event.setAction(MotionEvent.ACTION_CANCEL);
            else if (this.tapIndicator) {
                event.setAction(MotionEvent.ACTION_DOWN);
                this.tapIndicator = false;
            }
        }
    }

    public void setMode(int m){
        this.mode = m;
    }

    public int getMode(){
        return this.mode;
    }

    public void setEnabled(boolean status){
        this.running = status;
    }

    public void setSwipeMaxDistance(int distance){
        this.swipe_Max_Distance = distance;
    }

    public void setSwipeMinDistance(int distance){
        this.swipe_Min_Distance = distance;
    }

    public void setSwipeMinVelocity(int distance){
        this.swipe_Min_Velocity = distance;
    }

    public int getSwipeMaxDistance(){
        return this.swipe_Max_Distance;
    }

    public int getSwipeMinDistance(){
        return this.swipe_Min_Distance;
    }

    public int getSwipeMinVelocity(){
        return this.swipe_Min_Velocity;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {

        final float xDistance = Math.abs(e1.getX() - e2.getX());
        final float yDistance = Math.abs(e1.getY() - e2.getY());

        if(xDistance > this.swipe_Max_Distance || yDistance > this.swipe_Max_Distance)
            return false;

        velocityX = Math.abs(velocityX);
        velocityY = Math.abs(velocityY);
        boolean result = false;

        if(velocityX > this.swipe_Min_Velocity && xDistance > this.swipe_Min_Distance){
            if(e1.getX() > e2.getX()) // right to left
                this.listener.onSwipe(SWIPE_LEFT);
            else
                this.listener.onSwipe(SWIPE_RIGHT);

            result = true;
        }
        else if(velocityY > this.swipe_Min_Velocity && yDistance > this.swipe_Min_Distance){
            if(e1.getY() > e2.getY()) // bottom to up
                this.listener.onSwipe(SWIPE_UP);
            else
                this.listener.onSwipe(SWIPE_DOWN);

            result = true;
        }
        Log.d("swipe","Values: xDistance = "+xDistance+" yDistance = "+yDistance+" velocityX = "+velocityX+" velocityY = "+velocityY);
        return result;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        this.tapIndicator = true;
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent arg) {
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent arg) {
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent arg) {

        if(this.mode == MODE_DYNAMIC){        // we owe an ACTION_UP, so we fake an
            arg.setAction(ACTION_FAKE);      //action which will be converted to an ACTION_UP later.
            this.context.dispatchTouchEvent(arg);
        }

        return false;
    }

    static interface SimpleGestureListener{
        void onSwipe(int direction);
    }


}
