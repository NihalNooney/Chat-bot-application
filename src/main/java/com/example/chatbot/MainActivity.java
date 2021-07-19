package com.example.chatbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //Variables
    String messageR = "";
    String numberR = "";
    int stateNumber = 0;
    BroadcastReceiver br;
    boolean c1 = false;
    boolean c2 = true;
    boolean c3 = false;
    boolean c4 = true;
    double tc= 0.0;
    //List of states
    String[][] statesList = new String[][]{
            {"Hey! let's go out!", "Hi We should go out yo", "Bonjour! let's go out to eat", "We should go out!"},
            {"I'm tryna hang bro", "For fun", "Cuz I'm hungry", "For Godly Food"},
            {"How bout 8", "I booked us a table at 8:00", "I scheduled a picnic at 8:00pm", "We got a place at 8pm"},
            {"Marquand Park", "Tacoria", "Papa John's", "Olive Garden", "McDonald's"},
            {"Yay, be safe on the way", "Bye, I'll see you in a few", "Bet that's a plan", "Meet you there!"}
    };

    String currentArea="";
    String restaurantArea="";

    //textview info about states
    String[] stateText = new String[]{"1'st greeting state", "2'nd asking why state", "3'rd asking time state",
            "4th asking place state", "5th goodbye state"};

    TextView stateDisplay;
    TextView numb;

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(br, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(br);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS,Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE }, 1);

            /*

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WHATEVER IT IS CALLED) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WHATEVER IT IS CALLED}, 0);
        }

        At the end, the 0 also needs to be a 1 -> try putting in one line of code
             */


        }

        numb=findViewById(R.id.textView);
        stateDisplay = findViewById(R.id.yo);
        stateDisplay.setText(stateText[stateNumber]);

        //broadcast
        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                //extras are being obtained in the bundle
                Bundle bundle = intent.getExtras();

                if(bundle != null){
                    Object[] pdus = (Object[])bundle.get("pdus");
                    SmsMessage[] smsMessages = new SmsMessage[pdus.length];
                    for(int i=0;i<pdus.length;i++) {
                        byte[] b = (byte[]) pdus[i];
                        smsMessages[i] = SmsMessage.createFromPdu(b, bundle.getString("format"));
                        messageR = smsMessages[i].getMessageBody();
                        numberR = smsMessages[i].getOriginatingAddress();
                        numb.setText("The number you are texting is "+ numberR);


                        msgCheck(messageR);
                    }
                }

            }
        };

        registerReceiver(br, (new IntentFilter("android.provider.Telephony.SMS_RECEIVED")));



    }
//sending and handler
    public void send(final String message){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SmsManager m = SmsManager.getDefault();
                m.sendTextMessage(numberR, null, message, null, null);
            }
        }, 2000);
    }
//checking to see which msg should be sent
    public void msgCheck(final String messageReceived){
        //state1
        if(stateNumber == 0 && (messageReceived.toLowerCase().contains("hi") || messageReceived.toLowerCase().contains("hello") || messageReceived.toLowerCase().contains("hey"))){
            int rand = (int)(Math.random()*5);
            String messageSend = statesList[stateNumber][rand];
            send(messageSend);
            stateNumber++;
            stateDisplay.setText(stateText[stateNumber]);
        }
        //state 2
        else if(stateNumber == 1 && (messageReceived.toLowerCase().contains("why") || messageReceived.toLowerCase().contains("reason"))){
            int rand = (int)(Math.random()*5);
            String messageSend = statesList[stateNumber][rand];
            send(messageSend);
            stateNumber++;
            stateDisplay.setText(stateText[stateNumber]);
        }
        //state3
        else if(stateNumber == 2 && (messageReceived.toLowerCase().contains("time") || messageReceived.toLowerCase().contains("when"))){
            int rand = (int)(Math.random()*5);
            String messageSend = statesList[stateNumber][rand];
            send(messageSend);
            stateNumber++;
            stateDisplay.setText(stateText[stateNumber]);
        }
        //state4
        else if(stateNumber == 3 && (messageReceived.toLowerCase().contains("where") || messageReceived.toLowerCase().contains("place"))){
            int rand = (int)(Math.random()*5);
            String messageSend = statesList[stateNumber][rand];
            send(messageSend);
            stateNumber++;
            stateDisplay.setText(stateText[stateNumber]);
        }

//state 5
        else if(stateNumber == 4 && (messageReceived.toLowerCase().contains("sure") || messageReceived.toLowerCase().contains("bet") || messageReceived.toLowerCase().contains("ok") || messageReceived.toLowerCase().contains("yes"))){
            int rand = (int)(Math.random()*5);
            String messageSend = statesList[stateNumber][rand];
            send(messageSend);
            stateNumber++;
            stateDisplay.setText("5th state, States have ellapsed");
        }


        else{
            //misc. response
            if(stateNumber != 5) {
                send("Sorry. I dont understand what you are trying to say. Please make your message more clear");
                stateDisplay.setText("Unknown response");
            }
        }
    }

}