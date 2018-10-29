package es.source.code.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import es.source.code.R;
import es.source.code.adapter.MyFragmentPagerAdapter;
import es.source.code.model.User;
import es.source.code.service.ServerObserverService;

/**
 * 布局参考:https://blog.csdn.net/leaf_130/article/details/54578083
 * 服务和活动绑定及通信参考：https://blog.csdn.net/CodeNoodles/article/details/51679532?utm_source=blogxgwz1
 */
public class FoodView extends AppCompatActivity {

    private User currentUser;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private MyFragmentPagerAdapter myFragmentPagerAdapter;
    private Messenger serviceMessenger=null;//向服务发送Message的Messenger对象,获取到的服务中的Messenger
    private Messenger messenger=null;//活动中的Messenger，传入服务中去，服务使用它向本活动传递数据
    private boolean bindState=false;//是否和服务绑定

    private ServiceConnection serviceConnection=new ServiceConnection() {
        /**
         * 执行bindService（）后执行此函数
         * @param name
         * @param service
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //设置已绑定状态
            //通过参数service创建Messenger对象，该对象可想Service发送Message，与Service进行通信
            bindState=true;
            serviceMessenger=new Messenger(service);//获得service中的Messenger
            messenger=new Messenger(sMessageHandler);//创建本活动的Messenger给服务用，服务用它给本活动传数据

        }
        /**
         * 接触绑定后制定
         * @param name
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceMessenger=null;
            bindState=false;
        }
    };

    /**
     * 绑定后，向服务发送what属性为1的Meassage对象，并将本活动的Messenger传给服务，以便服务使用它给本活动传递数据
     */
    public void sendMeassageToService(int arg1){
        if(!bindState){
            return;
        }
        Message message=Message.obtain(null,arg1,0,0);//发送what属性为1
        message.replyTo=messenger;//将本活动的Messenger通过Message对象传给服务使用
        try{
            serviceMessenger.send(message);//通过服务的Messenger对象发送Message对象到服务
        }catch (RemoteException e){
            e.printStackTrace();
        }
    }

    /**
     * 接受服务传来的Message数据，执行对应动作
     */
    Handler sMessageHandler=new Handler(){
        public void handleMessage(Message message){
            switch (message.what){
                case 10:
                    // TODO: 2018-10-29 解析Message携带菜品库存信息（菜品名，库存量），根据该值，更新FoodView菜品信息 
                    Bundle bundle =message.getData();
                    String foodName=bundle.getString("foodName");//菜品名
                    int foodStackNumber=bundle.getInt("foodStackNum");//库存量
                    // TODO: 2018-10-29 更新所有菜品信息对象的全局变量，但怎么更新对应的菜品子项的显示呢？广播？ 
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_view);

        viewPager=(ViewPager)findViewById(R.id.food_view_pager);
        tabLayout=(TabLayout)findViewById(R.id.food_tab_layout);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        currentUser=(User)bundle.getSerializable("currentUser");
        initeViews();//初始化视图

    }


    private void initeViews(){
        //使用适配器将ViewPager和Fragment绑定
        myFragmentPagerAdapter=new MyFragmentPagerAdapter(getSupportFragmentManager(),FoodView.this);
        viewPager.setAdapter(myFragmentPagerAdapter);

        //将TabLayout和ViewPager绑定
        tabLayout.setupWithViewPager(viewPager);

        //设置图片
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_logo);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_logo);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_logo);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_logo);

    }

    /**
     * 加载Menu布局，使右上角出现选项菜单
     * @param menu
     * @return
     */
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.food_view_menu,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        Bundle bundle=new Bundle();
        Intent intent=new Intent(FoodView.this,FoodOrderView.class);
        bundle.putSerializable("currentUser",currentUser);

        switch (item.getItemId()){
            case R.id.food_ordered://已点菜品
                // TODO: 2018-10-08 默认显示“未下单菜”页
                bundle.putString("option","food_ordered");
                intent.putExtras(bundle);
                startActivity(intent);

                break;
            case R.id.order_watch://查看订单
                // TODO: 2018-10-08 默认显示“已下单菜”页
                bundle.putString("option","order_watch");
                intent.putExtras(bundle);
                startActivity(intent);

                break;
            case R.id.call_service://呼叫服务
                // TODO: 2018-10-08


                break;
            case R.id.start_real_time_update://启动实时更新
                String realTimeUpdate=item.getTitle().toString();//获取
                if(realTimeUpdate.equals("启动实时更新")){
                    item.setTitle("停止实时更新");
                    Intent intent1=new Intent(FoodView.this, ServerObserverService.class);
                    bindService(intent1,serviceConnection, Context.BIND_AUTO_CREATE);//绑定服务，直接调用服务的onCreate()
                    sendMeassageToService(1);//发送what属性为1的Message对象给服务
                }
                if(realTimeUpdate.equals("停止实时更新")){
                    sendMeassageToService(0);//发送what属性为0的Message
                }

                break;

        }
        return true;
    }
}
