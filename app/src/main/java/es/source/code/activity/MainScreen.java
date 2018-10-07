package es.source.code.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import es.source.code.R;

public class MainScreen extends AppCompatActivity {

    private Bundle bundle;//传值
    private Button orderDishButton;//点菜
    private Button orderViewButton;//订单
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        orderDishButton=(Button)findViewById(R.id.main_screen_order_dish);
        orderViewButton=(Button)findViewById(R.id.main_screen_order_view);
        try{
            //获取SCOSEntry传来的变量String，决定隐藏导航项
            if(getIntent()!=null&&getIntent().getExtras()!=null){
                Intent enrtyIntent=getIntent();
                bundle=enrtyIntent.getExtras();
                //找不到默认返回“”
                if(!bundle.getString("String","").equals("FromEntry")){
                    orderDishButton.setVisibility(View.GONE);
                    orderViewButton.setVisibility(View.GONE);
                }
                //获取LoginOrRegister传来的String,若为LoginSuccess，则代表登录成功，显示点菜和查看订单选项
                if(bundle.getString("String","").equals("LoginSuccess")){
                    if(orderDishButton.getVisibility()==View.GONE){
                        orderDishButton.setVisibility(View.VISIBLE);
                    }
                    if(orderViewButton.getVisibility()==View.GONE){
                        orderViewButton.setVisibility(View.VISIBLE);
                    }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }



    }
}
