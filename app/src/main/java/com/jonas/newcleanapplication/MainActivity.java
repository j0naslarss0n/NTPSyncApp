package com.jonas.newcleanapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
        //systemTime();

        ntpTimeTextView = findViewById(R.id.textView);

        // Create a Handler for the main UI thread
        Handler mainHandler = new Handler(Looper.getMainLooper());

        // Create and start a new thread to fetch NTP time
        Thread ntpThread = new Thread(() -> {
            try {
                SNTPClient.getDate(TimeZone.getTimeZone("Europe/Stockholm"), new SNTPClient.Listener() {
                    @Override
                    public void onTimeResponse(String rawDate, Date date, Exception ex) {


                        //Date ntpDate = new Date(SNTPClient.getDate();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        //sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                        //String ntpTime = "NTP: " + sdf.format(ntpDate);
                        System.out.println("NTPtime: " + date);

                        // Update the UI on the main thread
                        mainHandler.post(() -> ntpTimeTextView.setText(sdf.format(date)));

                    }
                });
                //SNTPClient sntpClient = new SNTPClient();

            } catch (Exception e) {
                System.out.println("Systemtime " +" 00:00" );
                e.printStackTrace();

                // Handle errors, update the UI on the main thread
                mainHandler.post(() -> ntpTimeTextView.setText("Failed to fetch NTP time"));
            }
        });
        ntpThread.start();
    }
        /*
        try {
            setNTPTime();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }*/


    }


