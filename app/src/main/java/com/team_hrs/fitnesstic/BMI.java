package com.team_hrs.fitnesstic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class BMI extends AppCompatActivity {
    TextView BMIdisplay;
    TextView YouAre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);

        BMIdisplay=(TextView)findViewById(R.id.userEmail);
        YouAre=(TextView)findViewById(R.id.userPassword) ;
        display();
    }
    public void display(){
        Bundle extras=getIntent().getExtras();
        /*Intent intent=getIntent();
        String Bmi=intent.getStringExtra("BMI");
        BMIdisplay.setText(Bmi);*/
        if(extras!=null){
            String value=extras.getString("BMI");
            BMIdisplay.setText(value);
            float bmifloatvalue = Float.parseFloat(String.valueOf(value));
            if(bmifloatvalue<18.5){
                YouAre.setText("UnderWeight");
            }
            else if (bmifloatvalue>24.9 && bmifloatvalue<30){
                YouAre.setText("OverWeight");
            }
            else if (bmifloatvalue>=30){
                YouAre.setText("Obese");
            }
            else {
                YouAre.setText("Fit");
            }
        }//End if outer if
    }
    public void Back(View v){
     Intent intent=new Intent(this,RegisterActivity.class);
     startActivity(intent);
    }



}
