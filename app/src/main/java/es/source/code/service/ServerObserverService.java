package es.source.code.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import es.source.code.model.RealTimeFoodData;
import es.source.code.util.Common;

public class ServerObserverService extends Service{
    private Messenger mActivityMessenger=null;//活动的Messenger，使用它向SCOS app的进程的活动发送数据
   // private Messenger msger=null;//返给活动，Messenger可以关联到本Service里的Handler
    private final Messenger msger=new Messenger(new MessageHandler());
    private Thread mThread=null;
    private boolean mWorking=true;//控制线程任务是否继续进行，控制线程的关闭
//    private String realTimeColdFoodAddress="http://192.168.0.102:8080/SCOSServer/RealTimeColdFood";//凉菜实时同步地址
//    private String realTimeHotFoodAddress="http://192.168.0.102:8080/SCOSServer/RealTimeHotFood";//热菜实时同步地址
//    private String realTimeSeaFoodAddress="http://192.168.0.102:8080/SCOSServer/RealTimeSeaFood";//海鲜实时同步地址
//    private String realTimeWineAddress="http://192.168.0.102:8080/SCOSServer/RealTimeWine";//酒水实时同步地址
    private String realTimeColdFoodAddress="http://192.168.43.155:8080/SCOSServer/RealTimeColdFood";//凉菜实时同步地址
    private String realTimeHotFoodAddress="http://192.168.43.155:8080/SCOSServer/RealTimeHotFood";//热菜实时同步地址
    private String realTimeSeaFoodAddress="http://192.168.43.155:8080/SCOSServer/RealTimeSeaFood";//海鲜实时同步地址
    private String realTimeWineAddress="http://192.168.43.155:8080/SCOSServer/RealTimeWine";//酒水实时同步地址
    private ArrayList<RealTimeFoodData> realTimeColdFoods=null;
    private ArrayList<RealTimeFoodData> realTimeHotFoods=null;
    private ArrayList<RealTimeFoodData> realTimeSeaFoods=null;
    private ArrayList<RealTimeFoodData> realTimeWines=null;

    public ServerObserverService() {
    }


    //处理来自FoodView 活动的Message
   // private Handler cMessageHandler=new Handler(){
    class MessageHandler extends Handler{
        public void handleMessage(final Message msg){
            Bundle bundle=msg.getData();
            Boolean state=bundle.getBoolean("state");
            switch (msg.what){
                case 0://关闭模拟接收菜品库存信息的多线程
                    if(mWorking){
                        mWorking=false;
                       // stopSelf();
                    }
                    break;
                case 1://启动多线程模拟接收服务器传回菜品库存信息(菜名、库存量)
                    Log.i("线程为1啦啦啦啦啦啦啦啦","服务");
                    if(state){//按了实时同步,修改在FoodView页面内第二次点击实时同步时无法开启同步，因为此时mWorking=false
                        mWorking=true;
                    }
                    mActivityMessenger=msg.replyTo;//获取到活动的Messenger对象
//                    Message message=Message.obtain();
//                    message.what=10;
//                    try{
//                        mActivityMessenger.send(message);//使用活动的Messenger向活动发送数据
//                    }catch (RemoteException e){
//                        e.printStackTrace();
//
//                    }
                    mThread=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // TODO: 2018-10-28 接收到服务器新数据，判断SCOS app进程是否运行状态
                            Log.i("服务：","调用啦");
                            while (mWorking){
                                //String foodName="";
                                //int foodNum=0;
                                realTimeColdFoods=Common.getRealTimeFoodData(realTimeColdFoodAddress);//实时凉菜数据
                                realTimeHotFoods=Common.getRealTimeFoodData(realTimeHotFoodAddress);//实时热菜数据
                                realTimeSeaFoods=Common.getRealTimeFoodData(realTimeSeaFoodAddress);//实时海鲜数据
                                realTimeWines=Common.getRealTimeFoodData(realTimeWineAddress);//实时酒水数据

                                boolean isRunning= Common.isProcessRunning(ServerObserverService.this,"es.source.code");
                                if(isRunning){
                                    Message message=Message.obtain();
                                    message.what=10;
                                    Bundle bundle=new Bundle();
                                    bundle.putSerializable("realTimeColdFoods",realTimeColdFoods);
                                    bundle.putSerializable("realTimeHotFoods",realTimeHotFoods);
                                    bundle.putSerializable("realTimeSeaFoods",realTimeSeaFoods);
                                    bundle.putSerializable("realTimeWines",realTimeWines);
                                    //bundle.putString("foodName",foodName);//传菜品名
                                    //bundle.putInt("foodStackNum",foodNum);//传库存量
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
        Log.i("服务：","ServerObserverService");
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
