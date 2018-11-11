package es.source.code.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import es.source.code.R;
import es.source.code.adapter.FoodOrderFragmentPagerAdapter;
import es.source.code.model.User;

/**
 * 显示两个选项卡：未下单菜、已下单菜
 */
public class FoodOrderView extends AppCompatActivity {
    private User currentUser;
    private FoodOrderFragmentPagerAdapter foodOrderFragmentPagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_order_view);
        viewPager=(ViewPager)findViewById(R.id.food_order_view_pager);
        tabLayout=(TabLayout)findViewById(R.id.food_order_tab_layout);
        initeViews();
    }

    private void initeViews(){
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        currentUser=null;
        if(bundle.getSerializable("currentUser")!=null){
            currentUser=(User)bundle.getSerializable("currentUser");
        }
        String option=bundle.getString("option","");//获得选项名
        //使用适配器将ViewPager和Fragment绑定
        foodOrderFragmentPagerAdapter=new FoodOrderFragmentPagerAdapter(getSupportFragmentManager(),currentUser);
        viewPager.setAdapter(foodOrderFragmentPagerAdapter);

        //将TabLayout和ViewPager绑定
        tabLayout.setupWithViewPager(viewPager);

        //设置图片
        tabLayout.getTabAt(0).setIcon(R.mipmap.not_order_food);
        tabLayout.getTabAt(1).setIcon(R.mipmap.ordered_food);
        if(option.equals("food_ordered")){ //默认显示“未下单菜”页
            tabLayout.getTabAt(0).select();
        }else if(option.equals("order_watch")){ //默认显示“已下单菜”页
            tabLayout.getTabAt(1).select();
        }

    }
}
