package es.source.code.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import es.source.code.util.Common;

public class ServerObserverService extends Service{
    private Messenger mActivityMessenger=null;//活动的Messenger，使用它向SCOS app的进程的活动发送数据
    private Messenger msger=null;//Messenger可以关联到Service里的Handler
    private Thread mThread=null;
    private boolean mWorking=true;//控制线程任务是否继续进行，控制线程的关闭

    public ServerObserverService() {
    }


    //处理来自FoodView 活动的Message
    private Handler cMessageHandler=new Handler(){
        public void handleMessage(final Message msg){
            switch (msg.what){
                case 0://关闭模拟接收菜品库存信息的多线程
                    if(mWorking){
                        mWorking=false;
                    }
                    break;
                case 1://启动多线程模拟接收服务器传回菜品库存信息(菜名、库存量)
                    mThread=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // TODO: 2018-10-28 接收到服务器新数据，判断SCOS app进程是否运行状态
                            while (mWorking){
                                String foodName="";
                                int foodNum=0;
                                mActivityMessenger=msg.replyTo;//获取到活动的Messenger对象
                                boolean isRunning= Common.isProcessRunning(ServerObserverService.this,"es.source.code");
                                if(isRunning){
                                    Message message=new Message();
                                    message.what=10;
                                    Bundle bundle=new Bundle();
                                    bundle.putString("foodName",foodName);//传菜品名
                                    bundle.putInt("foodStackNum",foodNum);//传库存量
                                    message.setData(bundle);
                                    //sendMessage(message);//发送数据到activity
                                    try{
                                        mActivityMessenger.send(message);//使用活动的Messenger向活动发送数据
                                    }catch (RemoteException e){
                                        e.printStackTrace();

                                    }

                                }
                                try{
                                    Thread.sleep(300);//休眠300ms
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                    mThread.start();//开启线程
                    break;
                default:
                    super.handleMessage(msg);

            }

        }
    };

    public void onCreate(){
        super.onCreate();
        msger=new Messenger(cMessageHandler);
    }

    public int onStartCommand(Intent intent,int flags,int started){

        return super.onStartCommand(intent,flags,started);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return msger.getBinder();

    }




}
