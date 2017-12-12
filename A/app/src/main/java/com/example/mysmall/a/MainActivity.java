package com.example.mysmall.a;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Messenger mMessenger;
    ServiceConnection serviceConnection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serviceCreate();
        setBindler();
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送消息
                sendMessageToB("发送消息给B");
            }
        });
    }


    private void serviceCreate(){
         serviceConnection=new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mMessenger=new Messenger(service);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.e("k","链接断开");
            }
        };
    }

    private void setBindler(){
        Intent intent=new Intent();
        intent.setComponent(new ComponentName("com.example.mysmall.b","com.example.mysmall.b.BService"));
        bindService(intent,serviceConnection,BIND_AUTO_CREATE);
    }
    //发送消息
    public void sendMessageToB(String str){
        Message message=Message.obtain(null,1);
        message.replyTo=replyMessenger;
        Bundle bundle=new Bundle();
        bundle.putString("data",str);
        message.setData(bundle);
        try {
            mMessenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    Messenger replyMessenger=new Messenger(new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(MainActivity.this,"B:"+msg.getData().getString("data"),Toast.LENGTH_SHORT).show();
            super.handleMessage(msg);
        }
    });
}
