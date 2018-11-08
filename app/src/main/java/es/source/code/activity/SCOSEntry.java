package es.source.code.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

import es.source.code.R;
import es.source.code.service.InitGlobalDataService;
import es.source.code.util.MyGestureListener;
import es.source.code.util.MyRightLeftListener;

public class SCOSEntry extends AppCompatActivity {

    //手势识别对象
    private GestureDetector detector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry);
        Intent intentInitGlobalData=new Intent(this, InitGlobalDataService.class);
        startService(intentInitGlobalData);//开启服务从服务器加载所有菜品数据


        //左滑进入主页面MainScreen
        detector=new GestureDetector(this,new MyGestureListener(new MyRightLeftListener() {
            @Override
            public void onLeft() {
                // TODO: 2018-10-09 改了跳转
                   callMainScreen();
//                Intent intent=new Intent(SCOSEntry.this,LoginOrRegister.class);
//                startActivity(intent);
            }
        }));
    }

    private void callMainScreen(){
        try{
            Intent mainScreenIntent=new Intent(SCOSEntry.this,MainScreen.class);
            //存储传递数据
            Bundle bundle=new Bundle();
            bundle.putString("String","FromEntry");
            mainScreenIntent.putExtras(bundle);
            startActivity(mainScreenIntent);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 将本Activity的onTouch事件交给GestureDetector处理
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event){
        return detector.onTouchEvent(event);
    }

   /* @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults){
        switch (requestCode){
            case 1:
                if(grantResults.length>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    callMainScreen();
                }else {
                    Toast.makeText(this,"请求被拒绝",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }*/

}
