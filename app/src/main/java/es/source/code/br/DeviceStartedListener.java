package es.source.code.br;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

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
        Intent intent1=new Intent(context,UpdateService.class);
        context.startService(intent1);//启动服务
    }
}
