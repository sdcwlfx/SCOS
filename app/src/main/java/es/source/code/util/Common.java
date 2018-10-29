package es.source.code.util;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * 判断SCOS app进程是否正在运行
 * 原文：https://blog.csdn.net/zhonglinliu/article/details/56679100
 * Created by asus on 2018-10-28.
 */

public class Common {
    public static boolean isProcessRunning(Context context, String processName) {
        boolean isRunning=false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> lists = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : lists) {
            //判断后天是否有该应用进程运行或者当前运行的活动进程为该应用
            if (info.processName.equals(processName)) {
                isRunning=true;
            }
        }
        return isRunning;
    }



}
