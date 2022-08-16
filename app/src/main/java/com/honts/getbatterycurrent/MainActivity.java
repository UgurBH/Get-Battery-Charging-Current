package com.honts.getbatterycurrent;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;



import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static String TAG = "getBattery-Current-healthd";
    BatteryManager batteryManager;
    TextView batteryTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        batteryManager = (BatteryManager) getSystemService(Context.BATTERY_SERVICE);
        batteryTextView = (TextView) findViewById(R.id.textView);



        MySyncTask mySyncTask = new MySyncTask();
        mySyncTask.execute();

    }

    //below Async task shows battery info and logs battery info only for 5 minutes (approximately), after then, app has to be killed and relaunched.
    private class MySyncTask extends AsyncTask {


        @Override
        protected Object doInBackground(Object[] objects) {
            for(int i=0; i<200; i++){
                Long chargingCurrent = batteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW) / 1000;
                Long averageCurrent = batteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_AVERAGE);


                IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                Intent intent = MainActivity.this.registerReceiver(null, intentFilter);

                float batteryTemperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0)/10;
                Log.i(TAG, "onCreate: start capturing battery state :>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                Log.i(TAG, "onCreate: charging current is " + chargingCurrent + " mAh");
                Log.i(TAG, "onCreate: battery temperature is " + batteryTemperature + " °C");
                Log.i(TAG, "onCreate: plugged " + intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0));
                Log.i(TAG, "onCreate: charging status is " + batteryManager.isCharging());
                Log.i(TAG, "onCreate: battery level is " + intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0) + " %");
                Log.i(TAG, "onCreate: end capturing battery state :>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");


                batteryTextView.setText("current is " + chargingCurrent + " mAh" + "\n" + "battery temperature is " + batteryTemperature +  " °C" + "\n" + "battery level is " + intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0) +" %" + "\n" + "charging status is " + batteryManager.isCharging()  );
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }


            return null;
        }
    }

}