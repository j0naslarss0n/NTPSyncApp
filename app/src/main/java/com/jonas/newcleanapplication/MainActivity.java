package com.jonas.newcleanapplication;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity implements SNTPClient.Listener {

    private TextView timeTextView;

    private Button button1;
    private Button button2;
    //private Object listener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timeTextView = findViewById(R.id.textView);

        // Time text

        final Handler handler = new Handler(Objects.requireNonNull(Looper.myLooper()));
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                NTPtime();
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(runnable);
        // TODO: Button
        button1 = findViewById(R.id.button);
        button1.setOnClickListener( v -> NTPtime());

        button2 = findViewById(R.id.button2);
        button2.setOnClickListener( v -> setTime());
        //systemTime();



    }

    private void setTime() {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        final Date systemDate = new Date();
        SimpleDateFormat sysSdf = new SimpleDateFormat("yyyy-MM-dd \n HH:mm:ss");
        String sysTime = "System time\n " + sysSdf.format(systemDate);
        // Handle errors, update the UI on the main thread
        mainHandler.post(() -> timeTextView.setText( sysTime));


    }

    @Override
    protected void onResume() {
        super.onResume();
        //NTPtime();
    }

    private void NTPtime(){

        // Create a Handler for the main UI thread
        Handler mainHandler = new Handler(Looper.getMainLooper());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd \n HH:mm:ss");

        // Create and start a new thread to fetch NTP time
        Thread ntpThread = new Thread(() -> {

            try {
                SNTPClient.getDate(TimeZone.getTimeZone("Asia/Tokyo"), (rawDate, date, ex) -> {

                    String time;
                    if (date != null ) {
                        time = " NTP time \n" + sdf.format(date);
                    } else {
                        final Date systemTime = new Date();
                        time = "System time\n " + sdf.format(systemTime);
                    }
                    mainHandler.post(() -> timeTextView.setText(time));
                });

            } catch (Exception e) {
                e.printStackTrace();

            }
        });
        ntpThread.start();
    }


    @Override
    public void onTimeResponse(String rawDate, Date date, Exception ex) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}


