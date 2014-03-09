package com.studioY.mms2email;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class MMSMonitorService extends Service {
    private MMSMonitor mmsMonitor;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Intent okIntent = new Intent();
            okIntent.setAction(Commons.COM_STUDIO_Y_MMS2EMAIL_SERVICE_RUNS_OK);
            sendBroadcast(okIntent);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("ysakng", "service started");

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Commons.COM_STUDIO_Y_MMS2EMAIL_CHECK_MMSMONITOR);
        registerReceiver(broadcastReceiver, intentFilter);

        Notification notification = new Notification(R.drawable.ic_launcher, "MMS 전달 서비스", System.currentTimeMillis());
        notification.setLatestEventInfo(getApplicationContext(), "MMS 전달 서비스", "MMS가 수신되면 지정된 e-mail로 전송됩니다.", null);
        startForeground(1, notification);
        startForeground(startId, notification);

        mmsMonitor = new MMSMonitor(this);
        mmsMonitor.startMMSMonitoring();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d("ysakng", "service stopped");

        mmsMonitor.stopMMSMonitoring();
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }
}