package es.source.code.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import es.source.code.R;
import es.source.code.activity.FoodDetailed;
import es.source.code.model.Food;
import es.source.code.model.GlobalData;
import es.source.code.util.HttpUtil;

public class UService extends Service {
    private ArrayList<Food> foodList=null;
    public UService() {
        foodList=new ArrayList<Food>();
    }

    public void onCreate(){
        super.onCreate();
        Log.i("服务：","ServerObserverService");
//        String responseData= HttpUtil.getNewFoodInformation();//获取json数组字符串
//        if(!responseData.equals("")){//有菜品信息更新
//            try {
//                MediaPlayer mediaPlayer=MediaPlayer.create(this,R.raw.noti);//加载音频
//                mediaPlayer.start();//播放菜品更新通知音频
//
//                //JSONObject jsonObject=new JSONObject(repsonseData);//新菜品信息JSON对象
//               /* JSONArray jsonArray=new JSONArray(responseData);
//                for(int i=0;i<jsonArray.length();i++){
//                    JSONObject jsonObject=jsonArray.getJSONObject(i);
//                    String newFoodName=jsonObject.getString("foodName");//新菜品名字
//                    int newFoodPrice=jsonObject.getInt("foodPrice");//价格
//                    String newFoodKind=jsonObject.getString("foodCategory");//类型
//                    int newFoodStackNum=jsonObject.getInt("foodStackNum");//库存量
//                    Food food=new Food();
//                    food.setFoodName(newFoodName);
//                    food.setFoodPrice(newFoodPrice);
//                    food.setFoodCategory(newFoodKind);
//                    food.setFoodImgId(R.drawable.ic_logo);
//                    food.setFoodStackNum(newFoodStackNum);
//                    addTGlobalData(food);
//                    foodList.add(food);
//                }*/
//                foodList=HttpUtil.getNewFoodInfoFromXML();
//                for (int i=0;i<foodList.size();i++){
//                    addTGlobalData(foodList.get(i));
//                }
//
//                Intent intent1=new Intent(this, FoodDetailed.class);
//                Intent intent2=new Intent(this,MyService.class);
//                //通过通知向FoodDetailed传参
//                Bundle bundle=new Bundle();
//                bundle.putSerializable("foodList",foodList);
//                intent1.putExtras(bundle);
//                PendingIntent pi1=PendingIntent.getActivity(this,0,intent1,0);
//                PendingIntent pi2=PendingIntent.getService(this,0,intent2,0);
//
//                //自定义通知布局和布局中取消按钮
//                NotificationManager manager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
//
//
////                Notification notification=new NotificationCompat.Builder(getBaseContext())
////                        .setContentTitle("菜品更新")
////                        .setContentText("新品上架："+foodList.get(0).getFoodName()+" "+foodList.get(0).getFoodPrice()+"元    "+foodList.get(0).getFoodCategory())
////                        .setWhen(System.currentTimeMillis())
////                        .setSmallIcon(R.drawable.ic_logo)
////                        .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_logo))
////                        .setContentIntent(pi1)
////                        .setDefaults(NotificationCompat.DEFAULT_ALL)
////                        .setAutoCancel(true)
////                        .build();
//
//
//                Notification notification=new NotificationCompat.Builder(this)
//                        .setContentTitle("菜品更新")
//                        .setSmallIcon(R.drawable.ic_logo)
//                        .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_logo))
//                        .setWhen(System.currentTimeMillis())
//                        .setContentIntent(pi1)
//                        .setAutoCancel(true)
//                        .build();
//
//
//
//                RemoteViews remoteViews=new RemoteViews(getPackageName(),R.layout.notification_layout);
//                remoteViews.setOnClickPendingIntent(R.id.notification_cancel,pi2);//取消按钮功能
//                remoteViews.setTextViewText(R.id.notification_text_view,foodList.get(0).getFoodName()+"   "+foodList.get(0).getFoodPrice()+"元/份   "+foodList.get(0).getFoodCategory());//设置子控件显示
//                notification.contentView=remoteViews;
//                manager.notify(1,notification);
//            }catch (Exception e){
//                e.printStackTrace();
//            }




//        }
    }
    private void addTGlobalData(Food food){
        String foodCategory=food.getFoodCategory();
        if(foodCategory.equals("凉菜")){
            GlobalData.coldFoodList.add(food);
        }else if(foodCategory.equals("热菜")){
            GlobalData.hotFoodList.add(food);
        }else if(foodCategory.equals("酒水")){
            GlobalData.wineList.add(food);
        }else if(foodCategory.equals("海鲜")){
            GlobalData.seaFoodList.add(food);
        }
    }

    //一分钟向服务器请求一次
    public int onStartCommand(Intent intent,int flags,int startId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                foodList = HttpUtil.getNewFoodInfoFromXML();
                //String responseData = HttpUtil.getNewFoodInformation();//获取json数组字符串
                //if (!responseData.equals("")) {//有菜品信息更新
                if(foodList.size()>0){//有菜品更新就显示通知，没有就不通知
                    try {
                        MediaPlayer mediaPlayer = MediaPlayer.create(UService.this, R.raw.noti);//加载音频
                        mediaPlayer.start();//播放菜品更新通知音频

                        //JSONObject jsonObject=new JSONObject(repsonseData);//新菜品信息JSON对象
               /* JSONArray jsonArray=new JSONArray(responseData);
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    String newFoodName=jsonObject.getString("foodName");//新菜品名字
                    int newFoodPrice=jsonObject.getInt("foodPrice");//价格
                    String newFoodKind=jsonObject.getString("foodCategory");//类型
                    int newFoodStackNum=jsonObject.getInt("foodStackNum");//库存量
                    Food food=new Food();
                    food.setFoodName(newFoodName);
                    food.setFoodPrice(newFoodPrice);
                    food.setFoodCategory(newFoodKind);
                    food.setFoodImgId(R.drawable.ic_logo);
                    food.setFoodStackNum(newFoodStackNum);
                    addTGlobalData(food);
                    foodList.add(food);
                }*/


                        for (int i = 0; i < foodList.size(); i++) {
                            addTGlobalData(foodList.get(i));
                        }

                        Intent intent1 = new Intent(UService.this, FoodDetailed.class);
                        Intent intent2 = new Intent(UService.this, MyService.class);
                        //通过通知向FoodDetailed传参
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("foodList", foodList);
                        intent1.putExtras(bundle);
                        PendingIntent pi1 = PendingIntent.getActivity(UService.this, 0, intent1, 0);
                        PendingIntent pi2 = PendingIntent.getService(UService.this, 0, intent2, 0);

                        //自定义通知布局和布局中取消按钮
                        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


//                Notification notification=new NotificationCompat.Builder(getBaseContext())
//                        .setContentTitle("菜品更新")
//                        .setContentText("新品上架："+foodList.get(0).getFoodName()+" "+foodList.get(0).getFoodPrice()+"元    "+foodList.get(0).getFoodCategory())
//                        .setWhen(System.currentTimeMillis())
//                        .setSmallIcon(R.drawable.ic_logo)
//                        .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_logo))
//                        .setContentIntent(pi1)
//                        .setDefaults(NotificationCompat.DEFAULT_ALL)
//                        .setAutoCancel(true)
//                        .build();


                        Notification notification = new NotificationCompat.Builder(UService.this)
                                .setContentTitle("菜品更新")
                                .setSmallIcon(R.drawable.ic_logo)
                                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_logo))
                                .setWhen(System.currentTimeMillis())
                                .setContentIntent(pi1)
                                .setAutoCancel(true)
                                .build();


                        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_layout);
                        remoteViews.setOnClickPendingIntent(R.id.notification_cancel, pi2);//取消按钮功能
                        remoteViews.setTextViewText(R.id.notification_text_view, foodList.get(0).getFoodName() + "   " + foodList.get(0).getFoodPrice() + "元/份   " + foodList.get(0).getFoodCategory());//设置子控件显示
                        notification.contentView = remoteViews;
                        manager.notify(1, notification);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }).start();
        AlarmManager manager=(AlarmManager)getSystemService(ALARM_SERVICE);
        int one=1000*60;//一分钟执行一次
        long triggerAtTime= SystemClock.elapsedRealtime()+one;
        Intent i=new Intent(this,UService.class);//唤醒自己
        PendingIntent pi=PendingIntent.getService(this,0,i,0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        return super.onStartCommand(intent,flags,startId);

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
