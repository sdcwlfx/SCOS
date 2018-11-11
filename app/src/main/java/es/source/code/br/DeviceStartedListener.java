package es.source.code.br;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import es.source.code.service.UService;
import es.source.code.service.UpdateService;

/**
 * 监听开机广播，开启UpdateService服务
 */
public class DeviceStartedListener extends BroadcastReceiver {
    public DeviceStartedListener() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //throw new UnsupportedOperationException("Not yet implemented");
        Toast.makeText(context,"开机广播",Toast.LENGTH_SHORT).show();
        System.out.print("开机广播");
        Intent intent1=new Intent(context,UpdateService.class);
        //Intent intent1=new Intent(context,UService.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(intent1);//启动服务


    }
}
