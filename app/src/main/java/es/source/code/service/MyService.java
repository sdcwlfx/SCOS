package es.source.code.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * 停掉指定id的通知
 */
public class MyService extends Service {
    public MyService() {
    }

    public void onCreate(){
        super.onCreate();
        NotificationManager manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(1);//停掉id为1的通知
        stopSelf();//停止服务
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
