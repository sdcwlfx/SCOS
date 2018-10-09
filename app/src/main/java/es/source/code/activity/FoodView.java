package es.source.code.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import es.source.code.R;
import es.source.code.adapter.MyFragmentPagerAdapter;
import es.source.code.model.User;

//布局参考:https://blog.csdn.net/leaf_130/article/details/54578083
public class FoodView extends AppCompatActivity {

    private User currentUser;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private MyFragmentPagerAdapter myFragmentPagerAdapter;


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

        }
        return true;
    }
}
