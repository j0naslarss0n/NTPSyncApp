package com.jonas.newcleanapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity {

    private TextView ntpTimeTextView;
    //private Object listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Time text
        ntpTimeTextView = findViewById(R.id.textView);
        // TODO: Button

        //systemTime();

        NTPtime();


    }

    private void NTPtime(){
        ntpTimeTextView = findViewById(R.id.textView);

        // Create a Handler for the main UI thread
        Handler mainHandler = new Handler(Looper.getMainLooper());

        // Create and start a new thread to fetch NTP time
        Thread ntpThread = new Thread(() -> {

            try {
                SNTPClient.getDate(TimeZone.getTimeZone("Europe/Stockholm"), (rawDate, date, ex) -> {
                    if (ex == null && date != null) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String ntpTime = "NTP Time: " + sdf.format(date);

                        // Update the UI on the main thread
                        mainHandler.post(() -> ntpTimeTextView.setText(ntpTime));
                    } else {
                        final Date systemDate = new Date();
                        SimpleDateFormat sysSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String sysTime = "Sys time: " + sysSdf.format(systemDate);
                        Log.e("NTPTime", "Error fetching NTP time", ex);

                        // Handle errors, update the UI on the main thread
                        mainHandler.post(() -> ntpTimeTextView.setText( sysTime));
                    }
                });

            } catch (Exception e) {
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                String systemTime = "System Time " + sdf.format(date);
                //System.out.println("Systemtime " + date );
                e.printStackTrace();

                // Handle errors, update the UI on the main thread
                mainHandler.post(() -> ntpTimeTextView.setText("SystemTime: " +systemTime));
            }
        });
        ntpThread.start();
    }



    }


