package com.example.narein.women_safety_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Add extends AppCompatActivity {

    EditText product,quantity,productid;
    Button add;database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        db=new database(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        product=(EditText)findViewById(R.id.Productentry);
        quantity=(EditText)findViewById(R.id.Quantityentry);
        add=(Button)findViewById(R.id.insert);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isinserted;
                //Log.d("Android:",product.getText().toString()+"  "+quantity.getText().toString()+"   "+productid.getText().toString());
                isinserted=db.insertData(product.getText().toString(),quantity.getText().toString());
                if(isinserted == true)
                    Toast.makeText(Add.this,"Number Inserted",Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(Add.this,"Number not Inserted",Toast.LENGTH_LONG).show();
            }
        });
    }

}
