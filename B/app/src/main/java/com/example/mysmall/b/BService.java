package com.example.mysmall.b;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by 97952 on 2017/12/12.
 */

public class BService extends Service {
    Messenger mMessenger=new Messenger(new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Log.e("k",msg.getData().getString("data"));
            Message message=Message.obtain();
            Bundle bundle=new Bundle();
            bundle.putString("data","A你要说什么，我没听清");
            message.setData(bundle);
            try {
                msg.replyTo.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            super.handleMessage(msg);
        }
    });
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //绑定成功
        return mMessenger.getBinder();
    }

    public void sendMessage(){
        Message message=Message.obtain(null,1);
        message.replyTo=mMessenger;
        Bundle bundle=new Bundle();
        bundle.putString("data","我是B主动发送过来的");
        message.setData(bundle);
        try {
            mMessenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
