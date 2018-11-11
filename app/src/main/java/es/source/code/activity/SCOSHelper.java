package es.source.code.activity;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.source.code.R;
import es.source.code.util.MailSenderAuth;


public class SCOSHelper extends AppCompatActivity {
    private GridView helpGridView;
    private ArrayList<Map<String,Object>> dataList;
    private SimpleAdapter simpleAdapter;
    private String SMS_SEND_ACTION="SMS_SEND";
    private String SMS_DELIVERED_ACTION="SMS_DELIVERED";
    private SmsStatusReceiver smsStatusReceiver;//监听短信发送状态广播
    private SmsDeliveryStatusReceiver smsDeliveryStatusReceiver;//接受消息被传送到接收方时的广播
    private MailSenderAuth mailSenderAuth;
    private static final int SIGN=1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoshelper);

        helpGridView=(GridView)findViewById(R.id.help_grid_view);
        String name[]={"用户使用协议","关于系统","电话人工帮助","短信帮助","邮件帮助"};
        int icon[]={R.drawable.user_agreement,R.drawable.about_system,R.drawable.phone_helper,R.drawable.message_helper,R.drawable.email_helper};
        dataList=new ArrayList<Map<String,Object>>();
        for(int i=0;i<name.length;i++){
            Map<String,Object> map=new HashMap<String,Object>();
            map.put("icon",icon[i]);
            map.put("name",name[i]);
            dataList.add(map);
        }

        String[] from={"icon","name"};
        int[] to={R.id.help_Image_view,R.id.help_text_view};
        simpleAdapter=new SimpleAdapter(SCOSHelper.this,dataList,R.layout.help_item,from,to);
        helpGridView.setAdapter(simpleAdapter);

        helpGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String,Object> selectedGuide=dataList.get(position);
                String selectedName=selectedGuide.get("name").toString();//获得选中的功能名
                if(selectedName.equals("用户使用协议")){//用户使用协议

                }else if(selectedName.equals("关于系统")){//关于系统

                }else if(selectedName.equals("电话人工帮助")){//隐式意图实现电话人工帮助
                    //判断用户是否已经授权，未授权申请
                    if(ContextCompat.checkSelfPermission(SCOSHelper.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(SCOSHelper.this,new String[]{Manifest.permission.CALL_PHONE},1);
                    }else{//已经授权，执行打电话操作
                        autoCall();
                    }
                }else if(selectedName.equals("短信帮助")){//短信帮助
                    //判断用户是否已经授权，未授权申请
                    if(ContextCompat.checkSelfPermission(SCOSHelper.this, Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(SCOSHelper.this,new String[]{Manifest.permission.SEND_SMS},2);
                    }else{//已经授权，执行自动发短信
                        sendMessage();
                    }

                }else {//邮件帮助
                    MailSender mailSender=new MailSender();
                    new Thread(mailSender).start();//邮件帮助线程

                }
            }
        });

    }

    protected void onResume(){
        super.onResume();
        //动态注册两个有关短信的广播接收器，指定监听的广播信息号为SMS_SEND_ACTION和SMS_DELIVERED_ACTION
        smsStatusReceiver=new SmsStatusReceiver();
        registerReceiver(smsStatusReceiver,new IntentFilter(SMS_SEND_ACTION));//注册监听SMS_SEND_ACTION广播接收器

        smsDeliveryStatusReceiver=new SmsDeliveryStatusReceiver();
        registerReceiver(smsDeliveryStatusReceiver,new IntentFilter(SMS_DELIVERED_ACTION));//注册监听SMS_DELIVERED_ACTION广播接收器
    }

    protected void onPause(){
        super.onPause();
        //取消动态注册的广播接收器
        unregisterReceiver(smsStatusReceiver);
        unregisterReceiver(smsDeliveryStatusReceiver);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults){
        switch (requestCode){
            case 1://运行时电话权限
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    autoCall();

                }else {
                    Toast.makeText(this,"请求被拒绝",Toast.LENGTH_SHORT).show();
                }
                break;
            case 2://运行时发送短信权限
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    sendMessage();
                }else{
                    Toast.makeText(this,"请求被拒绝",Toast.LENGTH_SHORT).show();
                }
            default:
        }
    }

    //自动打电话
    private void autoCall(){
        try {
            Intent intent=new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:5554"));
            startActivity(intent);

        }catch (SecurityException e){
            e.printStackTrace();
        }

    }

    //自动发送短信给“5554”
    private void sendMessage(){
        SmsManager smsManager=SmsManager.getDefault();
        PendingIntent sendIntent=PendingIntent.getBroadcast(SCOSHelper.this,0,new Intent(SMS_SEND_ACTION),0);
        PendingIntent deliveryIntent=PendingIntent.getBroadcast(SCOSHelper.this,0,new Intent(SMS_DELIVERED_ACTION),0);
        smsManager.sendTextMessage("5554",null,"test scos helper",sendIntent,deliveryIntent);//消息成功发送或失败就广播sendIntent中的SMS_SEND_ACTION,当消息成功传送到接收者就广播deliveryIntent中SMS_DELIVERED_ACTION   详见:https://blog.csdn.net/fengyuzhengfan/article/details/38167305
    }

    //监听SMS_SEND_ACTION广播
    public class SmsStatusReceiver extends BroadcastReceiver{
        public void onReceive(Context context,Intent intent){
            switch (getResultCode()){
                case AppCompatActivity.RESULT_OK:
                    Toast.makeText(SCOSHelper.this,"求助短信发送成功",Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                case SmsManager.RESULT_ERROR_NULL_PDU:
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    Toast.makeText(SCOSHelper.this,"求助短信发送失败",Toast.LENGTH_SHORT).show();
                    break;

            }

        }
    }

    //监听SMS_DELIVERED_ACTION广播
    public class SmsDeliveryStatusReceiver extends BroadcastReceiver{
        public void onReceive(Context context,Intent intent){
            switch (getResultCode()){
                case Activity.RESULT_OK:
                   // Toast.makeText(SCOSHelper.this,"短信已被接受",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }



    //邮件帮助线程
    private class MailSender implements Runnable{
        public void run(){
            mailSenderAuth=new MailSenderAuth("18463102717@163.com","lfx152453");
            mailSenderAuth.sendMail("SCOS求助","test scos helper","18463102717@163.com","810254021@qq.com");
            Message message=new Message();
            message.what=SIGN;
            myHandler.sendMessage(message);//发给主线程
            //myHandler.obtainMessage().sendToTarget();
        }
    }

    private Handler myHandler=new Handler() {
        public void handleMessage(Message msg){
            switch (msg.what){
                case SIGN:
                    Toast.makeText(SCOSHelper.this,"求助邮件发送成功",Toast.LENGTH_SHORT).show();
                    break;
            }


        }

    };






}
