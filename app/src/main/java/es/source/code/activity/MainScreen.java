package es.source.code.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import es.source.code.R;
import es.source.code.model.User;

public class MainScreen extends AppCompatActivity {

    private Bundle bundle;//传值
    private GridView gridView;
    private SimpleAdapter simpleAdapter;
    private ArrayList<Map<String,Object>> dataList;
    private String orderDish="点菜";
    private String orderView="查看订单";
    private String loginOrRegister="登录/注册";
    private String systemHelp="系统帮助";
    private User currentUser;//登录用户
    private Boolean firstLogin=false;//第一次登录

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        //orderDishButton=(Button)findViewById(R.id.main_screen_order_dish);
        //orderViewButton=(Button)findViewById(R.id.main_screen_order_view);
        gridView=(GridView)findViewById(R.id.guide_grid_view);

        new Thread(runable).start();//开启线程
        //initeDataList();//初始化dataList
        /*String[] from={"icon","name"};
        int[] to={R.id.guide_Image_view,R.id.guide_text_view};
        simpleAdapter=new SimpleAdapter(MainScreen.this,dataList,R.layout.main_screen_item,from,to);
        gridView.setAdapter(simpleAdapter);*/

        //导航项点击事件
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String,Object> selectedGuide=dataList.get(position);
                String selectedName=selectedGuide.get("name").toString();//获得选中的功能名
                if(selectedName.equals(orderDish)){//点菜
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("currentUser",currentUser);
                    Intent intent=new Intent(MainScreen.this,FoodView.class);
                    intent.putExtras(bundle);
                    startActivity(intent);

                }else if(selectedName.equals(orderView)){//查看订单
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("currentUser",currentUser);
                    Intent intent=new Intent(MainScreen.this,FoodOrderView.class);
                    intent.putExtras(bundle);
                    startActivity(intent);

                }else if(selectedName.equals(loginOrRegister)){//登录/注册
                    Intent intent=new Intent(MainScreen.this,LoginOrRegister.class);
                    startActivity(intent);
                }else if (selectedName.equals(systemHelp)){//系统帮助

                }else{

                }
            }
        });

       /* try{
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
        }*/
    }


    private void initeDataList(){
        int iconHind[]={R.drawable.ic_logo,R.drawable.ic_logo};
        int icon[]={R.drawable.ic_logo,R.drawable.ic_logo,R.drawable.ic_logo,R.drawable.ic_logo};
        String nameHind[]={"登录/注册","系统帮助"};
        String name[]={"点菜","查看订单","登录/注册","系统帮助"};
        dataList=new ArrayList<Map<String,Object>>();

        //判断是否隐藏点菜、订单项
        if(getIntent()!=null&&getIntent().getExtras()!=null) {
            Intent enrtyIntent = getIntent();
            bundle = enrtyIntent.getExtras();
            //隐藏点菜、订单项
            if(!bundle.getString("String","").equals("FromEntry")){
                for (int i=0;i<iconHind.length;i++){
                    Map<String,Object> map=new HashMap<String, Object>();
                    map.put("icon",iconHind[i]);
                    map.put("name",nameHind[i]);
                    dataList.add(map);
                }
            }else {
                for (int i=0;i<icon.length;i++){
                    Map<String,Object> map=new HashMap<String, Object>();
                    map.put("icon",icon[i]);
                    map.put("name",name[i]);
                    dataList.add(map);
                }
            }

            if(bundle.getString("String","").equals("LoginSuccess")){
                currentUser=(User)bundle.getSerializable("loginUser");
                dataList.clear();//清空
                for (int i=0;i<icon.length;i++){
                    Map<String,Object> map=new HashMap<String, Object>();
                    map.put("icon",icon[i]);
                    map.put("name",name[i]);
                    dataList.add(map);
                }
            }else if(bundle.getString("String","").equals("RegisterSuccess")){
                firstLogin=true;//首次登录
                currentUser=(User)bundle.getSerializable("loginUser");
                dataList.clear();//清空
                for (int i=0;i<icon.length;i++){
                    Map<String,Object> map=new HashMap<String, Object>();
                    map.put("icon",icon[i]);
                    map.put("name",name[i]);
                    dataList.add(map);
                }
            }else{
                currentUser=null;
            }

        }else {
            currentUser=null;
            for (int i=0;i<icon.length;i++){
                Map<String,Object> map=new HashMap<String, Object>();
                map.put("icon",icon[i]);
                map.put("name",name[i]);
                dataList.add(map);
            }
        }
    }

    final Runnable runable=new Runnable() {
        @Override
        public void run() {

            initeDataList();
            myHandler.obtainMessage().sendToTarget();
        }
    };

    private Handler myHandler=new Handler() {
        public void handleMessage(Message msg){
            if(firstLogin){//首次登录
                Toast.makeText(MainScreen.this,"欢迎您成为SCOS新用户",Toast.LENGTH_SHORT).show();
            }
            String[] from={"icon","name"};
            int[] to={R.id.guide_Image_view,R.id.guide_text_view};
            simpleAdapter=new SimpleAdapter(MainScreen.this,dataList,R.layout.main_screen_item,from,to);
            gridView.setAdapter(simpleAdapter);

        }

    };
}
