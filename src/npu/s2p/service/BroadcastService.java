package npu.s2p.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;


import npu.s2p.activity.PeerActivity;
import npu.s2p.activity.Send;

/**
 * Created by vietuan on 1/25/14.
 */
public class BroadcastService extends Service {


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        // TODO start

        Toast.makeText(getApplicationContext(), "Broadcast Service Created", 1).show();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        Toast.makeText(getApplicationContext(), "Service Destroy", 1).show();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO sent broadcast here

        if(PeerActivity.peer != null){
//            Toast.makeText(getApplicationContext(), "Service Running, get address ", 1).show();
            PeerActivity.peer.pingToPeer(Send.broadcastaddress);
        }
        return super.onStartCommand(intent, flags, startId);
    }


}
