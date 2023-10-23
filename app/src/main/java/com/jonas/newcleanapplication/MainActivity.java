package com.jonas.newcleanapplication;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

import java.net.InetAddress;
import java.net.SocketException;
import java.util.Date;


public class MainActivity extends AppCompatActivity {





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        systemTime();
        /*
        try {
            setNTPTime();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }*/

    }

    public void systemTime(){
        // Hämta fältet som vi vill skriva till
        //final TextView text1 = (TextView) findViewById(R.id.textview_first);
        final TextView systemTime =  findViewById(R.id.textView);
        //text1.setTextSize(30);
        final Date datum = new Date();

        systemTime.setText((datum.getHours() < 10 ? "0" : "") + datum.getHours()
                + ":" + (datum.getMinutes() < 10 ? "0" : "") + datum.getMinutes() + ":"
                + (datum.getSeconds() < 10 ? "0" : "") + datum.getSeconds());

    }

    private  Date setNTPTime() throws SocketException {
        final TextView serverTime = findViewById(R.id.textView);
        NTPUDPClient timeClient = new NTPUDPClient();
        timeClient.setDefaultTimeout(2000);
        TimeInfo timeInfo;
        try {
            InetAddress inetAddress = InetAddress.getByName("time.nist.gov");
            timeInfo = timeClient.getTime(inetAddress);
            long NTPTime = timeInfo.getMessage().getTransmitTimeStamp().getTime();
            Date date = new Date(NTPTime);
            System.out.println("getTime() returning NTPServer time: " + date);

            //return date;                // Return ntptime
            return date;
        } catch (Exception e) {
            System.out.println("getTime() returning System time: " + new Date());
            return new Date();            // If exception is thrown return systemtime
        }
    }





}