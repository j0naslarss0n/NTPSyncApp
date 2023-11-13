package com.jonas.newcleanapplication;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity implements SNTPClient.Listener {

    // Deklarerar texten
    private TextView timeTextView;

    private CardView cardView;

    private Button button1;
    private Button button2;




    // När appen startas
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timeTextView = findViewById(R.id.textView); // sätter textrutan
        cardView = findViewById(R.id.card_view);    // Sätter färgindikation

        // Skemalägger appen  ...
        final Handler handler = new Handler(Objects.requireNonNull(Looper.myLooper()));

        // .. och kör appen i en egen tråd
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                getTime();
                handler.postDelayed(this, 1000);
            }
        };
        // Skriver ut NTPtime (om det går)
        handler.post(runnable);

    }




    private void getTime(){

        // En handler för main UI tråden
        Handler mainHandler = new Handler(Looper.getMainLooper());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd \n HH:mm:ss");

        // Ny tråd för att hämta NTP-tiden
        Thread ntpThread = new Thread(() -> {

            try {
                // Testa att hämta datum/tid
                SNTPClient.getDate(TimeZone.getTimeZone("Europe/Stockholm"), (rawDate, date, ex) -> {

                    String time;
                    // Om det går att få NTP-tiden, att date inte tom
                    if (date != null ) {
                        // Lägg date i time
                        time = " NTP time \n" + sdf.format(date);
                        // ändra färg på indikatorn till grön
                        cardView.setBackgroundColor(Color.GREEN);


                    } else {
                        //... annars ta system-Date och lägga i time-variabeln
                        final Date systemTime = new Date();
                        time = "System time\n " + sdf.format(systemTime);
                        // Ändra färg på indikatorn till röd
                        cardView.setBackgroundColor(Color.RED);
                    }
                    // Lägg ut tiden som finns i time
                    mainHandler.post(() -> timeTextView.setText(time));
                });

            } catch (Exception e) {
                e.printStackTrace();

            }
        });
        // Kör tråden ovan
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


