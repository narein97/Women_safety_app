package com.example.narein.women_safety_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class Bottom_page extends AppCompatActivity implements SimpleGestureFilter.SimpleGestureListener{

    database db;TableLayout table;Button add;
    TextView product,quantity;

    SimpleGestureFilter detector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_page);
        db=new database(this);
        detector = new SimpleGestureFilter(this,this);
    }

    @Override
    protected void onStart(){
        super.onStart();
        if(!isEmpty()){
            TableRow row;
            table=(TableLayout)findViewById(R.id.table);
            table.removeAllViews();
            table.setColumnStretchable(0,true);
            table.setColumnStretchable(1,true);
            Cursor point = db.getAllData();
            point.moveToFirst();
            while(!point.isAfterLast()){
                row=new TableRow(this);
                product=new TextView(this);quantity=new TextView(this);
                final TextView temp=product;
                product.setText(point.getString(0));quantity.setText(point.getString(1));
                product.setTextSize(18);quantity.setTextSize(18);
                product.setGravity(Gravity.CENTER);quantity.setGravity(Gravity.CENTER);
                product.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder
                                .setMessage("Message "+product.getText()+" ?")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setNegativeButton("No", null)						//Do nothing on no
                                .show();
                    }
                });
                row.addView(product);row.addView(quantity);
                table.addView(row);
                point.moveToNext();
            }point.close();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        //Log.d(msg,"onResume::inventory--needed if any transition required");
        add=(Button)findViewById(R.id.addproduct);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Bottom_page.this,Add.class);
                startActivity(i);
            }
        });
    }

    protected boolean isEmpty(){
        if(db.isempty()){
            TextView text = (TextView) findViewById(R.id.product);text.setText("");
            text = (TextView) findViewById(R.id.quantity);text.setText("");
            text = (TextView) findViewById(R.id.no_items);text.setText("No Contacts");
            return true;
        }
        else{
            TextView text = (TextView) findViewById(R.id.product);text.setText("Name");
            text = (TextView) findViewById(R.id.quantity);text.setText("Phone");
            text = (TextView) findViewById(R.id.no_items);text.setText("");
            return false;
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
            case SimpleGestureFilter.SWIPE_DOWN:
                str = "Swipe Up";
                this.finish();
                startActivity(new Intent(Bottom_page.this,MainActivity.class));
                Bottom_page.this.overridePendingTransition(R.anim.main_up,R.anim.bottom_up);
                break;
        }
    }

    @Override
    public void onBackPressed(){
        this.finish();
        startActivity(new Intent(Bottom_page.this,MainActivity.class));
        Bottom_page.this.overridePendingTransition(R.anim.main_up,R.anim.bottom_up);
    }
}
