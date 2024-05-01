package com.example.smsreader;

import android.Manifest;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import java.io.*;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
TextView textView,textView1;
EditText editText;
public  String input;
//String ss="+8801874469267";
Intent mserviceIntent;

ArrayList<String> storage=new ArrayList<String>();
    public static final String SMS_RECEIVED="android.provider.Telephony.SMS_RECEIVED";



    ReceiveSms receiveSms=new ReceiveSms()
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            super.onReceive(context, intent);
            if(msg_from.equals(readFile2())) {


                writefile(msg_body);
                readFile();
                Intent sms = new Intent(context, ExampleService.class);
                sms.putExtra("EXTRA", msg_body);
                context.startService(sms);

                // nuber=input;
                //Toast.makeText(context,"Number added"+nuber,Toast.LENGTH_LONG).show();
            }
        }
    };

    private void writefile(String lol) {

        try {
            FileOutputStream fileOutputStream=openFileOutput("Dairy.txt",Context.MODE_PRIVATE);
            fileOutputStream.write(lol.getBytes());
            fileOutputStream.close();
            Toast.makeText(getApplicationContext(),"Data stored",Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }


    private void notification() {

        Intent bb=new Intent();
        bb.setAction("service");
        bb.setClass(this,ReceiveSms.class);
        this.sendBroadcast(bb);

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiveSms,new IntentFilter(SMS_RECEIVED));
    }

    @Override
   protected void onDestroy() {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, ReceiveSms.class);
        this.sendBroadcast(broadcastIntent);
        super.onDestroy();
        unregisterReceiver(receiveSms);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS}, 100);
        }
        textView = findViewById(R.id.textview);
        // textView1=findViewById(R.id.textview1);
        editText = findViewById(R.id.edittextid);
        readFile();

        ExampleService exampleService = new ExampleService();
        mserviceIntent = new Intent(this, exampleService.getClass());
        if (!isMyServiceRunning(exampleService.getClass())) {
            startService(mserviceIntent);
        }


    }







    private boolean isMyServiceRunning(Class<? extends ExampleService> aClass) {
        ActivityManager manager= (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
        {
            if(aClass.getName().equals(service.service.getClassName()))
            {
                Log.i ("Service status", "Running");
                return true;
            }

        }
        Log.i ("Service status", "Not running");
        return false;

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==100)
        {
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(getApplicationContext(),"Permission Granted",Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Permission denied",Toast.LENGTH_LONG).show();
                finish();

            }

        }
    }


     public void storePhoneNum(View view) {

            String shor = editText.getText().toString();
            //storage.add(shor);

            try {
                FileOutputStream fileOutputStream = openFileOutput("Short.txt", Context.MODE_PRIVATE);
                fileOutputStream.write(shor.getBytes());
                fileOutputStream.close();
                Toast.makeText(getApplicationContext(), "Number stored", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }





      /*storage.add(0, editText.getText().toString());
      Toast.makeText(getApplicationContext(),"NUmber adeed",Toast.LENGTH_LONG).show();
      textView.setText(storage.get(0));

      Intent xx= new Intent(this,GetNumber.class);
      startActivity(xx);*/
      /*  Intent save_number = new Intent(getApplicationContext(),GetNumber.class);
        save_number.putExtra("CLOUD",storage.get(0));
        startActivity(save_number);
       // save_number.setData(Uri.parse())
     // input=storage.get(0);*/
     /*   String num = editText.getText().toString();


        if (!num.isEmpty()) {
            if (num.length() == 14) {
                String got = editText.getText().toString();

                Intent intent = new Intent();
                intent.setAction("NEW_NUMBER");
                intent.putExtra("CLOUD", got);
                intent.setClass(this, ReceiveSms.class);
                this.sendBroadcast(intent);


            }*/



    }


    /*public void startService(View view) {
        Intent serviceintent=new Intent(this,ExampleService.class);
        ContextCompat.startForegroundService(this,serviceintent);

    }*/
   /* public ArrayList<String> getList()
    {

        return storage;
    }*/

   public void readFile()
   {
       try {
           FileInputStream fileInputStream=openFileInput("Dairy.txt");
           InputStreamReader inputStreamReader=new InputStreamReader(fileInputStream);
           BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
           String line;
           StringBuffer stringBuffer= new StringBuffer();
           while((line=bufferedReader.readLine())!=null)

           {
               stringBuffer.append(line);

           }

           textView.setText(stringBuffer.toString());

       } catch (FileNotFoundException e) {
           e.printStackTrace();
       } catch (IOException e) {
           e.printStackTrace();
       }


   }
   public String readFile2()
   {
       String ui=null;
       try {
           FileInputStream fileInputStream=openFileInput("Short.txt");
           InputStreamReader inputStreamReader=new InputStreamReader(fileInputStream);
           BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
           String line;
           StringBuffer stringBuffer= new StringBuffer();
           while((line=bufferedReader.readLine())!=null)

           {
               stringBuffer.append(line);

           }

           ui= stringBuffer.toString();





       } catch (FileNotFoundException e) {
           e.printStackTrace();
       } catch (IOException e) {
           e.printStackTrace();
       }

       return ui;
   }


}
