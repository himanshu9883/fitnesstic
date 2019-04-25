package com.team_hrs.fitnesstic;

import android.content.Intent;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.team_hrs.fitnesstic.RegisterActivity;

import org.junit.rules.Stopwatch;

import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

public class Pedometer extends AppCompatActivity implements SensorEventListener, StepListener {
    private static final String TAG = "a";
    private TextView textView;
    private boolean running=false;
    RegisterActivity reg;
    Timer T=new Timer();
    private StepDetector simpleStepDetector;
    private long currentTime = 0;
    private SensorManager sensorManager;
    TextView time;
    private Sensor accel;
    private static final String TEXT_NUM_STEPS = "";
    private int numSteps;
    private TextView TvSteps; //Number of Steps
    final int MSG_START_TIMER = 0;
    final int MSG_STOP_TIMER = 1;
    final int MSG_UPDATE_TIMER = 2;
    private Button BtnStart;
    private Button BtnStop;
    public float c,factor,calburn;
    private String dist;
    TextView dis; //Distance of Kilometers
    Button viewMyActivtyBtn; //For Viewing Activity
    DatabaseHelper myDataBase;
    long date = System.currentTimeMillis();
    SimpleDateFormat sdf;
    String dateString;
    Button delete_History_btn; //delete button
    private long startTime=0;
    int count=0;
    int sec=0,min=0;
    TextView calburnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedometer);


        // Get an instance of the SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);
        sdf= new SimpleDateFormat("MMM MM dd ,yyyy h:mm a");
        calburnt = (TextView)findViewById(R.id.calories);
        dateString = sdf.format(date);
        time=(TextView)findViewById(R.id.time);
        TvSteps = (TextView) findViewById(R.id.tv_steps);
        BtnStart = (Button) findViewById(R.id.btn_start);
        BtnStop = (Button) findViewById(R.id.btn_stop);
        dis = (TextView) findViewById(R.id.distance); //My distance in Kilometers(TextView)
        viewMyActivtyBtn = (Button) findViewById(R.id.activity_details);
        myDataBase = new DatabaseHelper(this);
        delete_History_btn = (Button) findViewById(R.id.delete_history) ;
        reg=new RegisterActivity();
        Started();
        Stopped();
        ViewAll();
        DeleteAllData();//Function





    } //End of the Onreate of Pedometer





    public void showMessage(String title, String Message){  //Popup Window
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    } //End of showMessage Function


    public void Started(){
        BtnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                numSteps = 0;
                sensorManager.registerListener(Pedometer.this, accel, SensorManager.SENSOR_DELAY_FASTEST);

                T.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sec=count%60;
                                min=count/60;
                                time.setText("0"+min+":"+sec);
                                count++;


                            }
                        });
                    }
                }, 1000, 1000);

            }
        });
    }

    public void Stopped(){
        BtnStop.setOnClickListener(new View.OnClickListener() { //Stops the activity

            @Override
            public void onClick(View arg0) {


                boolean isInserted = myDataBase.insertData((String.valueOf(numSteps)),dist, dateString);
                if (isInserted == true){
                    Toast.makeText(Pedometer.this, "Activity Saved", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(Pedometer.this, "Activity Not Saved", Toast.LENGTH_SHORT).show();
                }


                sensorManager.unregisterListener(Pedometer.this);
                T.cancel();

            }
        });
    }

    public void ViewAll(){ //View Your Data
        viewMyActivtyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor res = myDataBase.getAllData();
                if (res.getCount() == 0){
                    //Message
                    showMessage("Error", "Nothing Found");
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while (res.moveToNext()){
                    buffer.append("Steps: "+res.getString(0)+"\n");
                    buffer.append("Distance(Km): "+res.getString(1)+"\n");
                    buffer.append("Date: "+res.getString(2)+"\n\n");
                }
                showMessage("History",buffer.toString());
            }
        });
    } //End of ViewAll

    public void DeleteAllData(){
        delete_History_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDataBase.deleteAll();
                Toast.makeText(Pedometer.this, "History Deleted!", Toast.LENGTH_SHORT).show();
            }
        });
    } //End of Delete Data Method

  /*  @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

    }*/

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void step(long timeNs) {
        numSteps++;
        TvSteps.setText(TEXT_NUM_STEPS + numSteps);
        float distance = (float) (numSteps * 78) / (float) 100000;
         dist = String.valueOf(distance);
        dis.setText(dist+" KM");
        //c=Float.parseFloat(String.valueOf(reg.textInputEditTextWeight))*0.57;
        c=(float)47.31;
        factor=(float)c/1250;
        calburn=(float)factor*numSteps;
        calburnt.setText(String.valueOf(calburn));


    }

/*    public float getDistanceRun(long numsteps) {

        return distance;

    } */



}
