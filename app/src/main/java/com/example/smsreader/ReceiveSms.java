package com.example.smsreader;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

import static android.widget.Toast.*;

public class ReceiveSms extends BroadcastReceiver {
    String msg_from;
    String number12;
    Intent mserviceIntent;
    public static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    public static final String RESTART = "restartservice";
    public static final String NUMBER = "NEW_NUMBER";
    ArrayList<String> arrayList = new ArrayList<String>();


    String msg_body="";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(NUMBER)) {
            number12 = intent.getStringExtra("CLOUD");
            Toast.makeText(context, "Passing to method" + number12, LENGTH_LONG).show();
            // return_num(number12)


        }


        if (intent.getAction().equals(RESTART)) {

            context.startService(new Intent(context, ExampleService.class));

        }

        if (intent.getAction().equals(SMS_RECEIVED)) {
            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs;

            String format = bundle.getString("format");
            if (bundle != null) {
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < msgs.length; i++) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                        }
                        msg_from = msgs[i].getOriginatingAddress();
                        msg_body = msgs[i].getDisplayMessageBody();
                        Toast.makeText(context, "From:" + msg_from + "body:" + msg_body, LENGTH_SHORT).show();
                        Toast.makeText(context, "From:" +new MainActivity().readFile2(), LENGTH_SHORT).show();

                        // MainActivity activity=new MainActivity() ;
                        // number12=activity.readFile();


                        //  makeText(context,"number"+number12, LENGTH_LONG).show();
                        //arrayList.add(0, "+8801874469267");
                        //number12= intent.getStringExtra("CLOUD");


                    }

                    /* if (msg_from.equals(arrayList.get(0)))
                  {
                      Intent sms = new Intent(context, ExampleService.class);
                      sms.putExtra("EXTRA", msg_body);
                      context.startService(sms);




                          Toast.makeText(context,"Sending to Main Actiivity",Toast.LENGTH_LONG).show();
                          Intent number= new Intent(context, MainActivity.class);
                          number.putExtra("NUMBER",msg_body);
                          context.startActivity(number);

                  } */


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }


        }


    }
}


