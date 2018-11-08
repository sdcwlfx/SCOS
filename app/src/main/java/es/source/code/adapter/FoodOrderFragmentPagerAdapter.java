package es.source.code.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;

import es.source.code.activity.NotOrderFoodFragment;
import es.source.code.activity.OrderedFoodFragment;
import es.source.code.model.User;

/**
 * Created by asus on 2018-10-08.
 */

public class FoodOrderFragmentPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<String> titleList=new ArrayList<String>();
    private User currentUser;
    private Context context;
    private FragmentManager fragmentManager;

    public FoodOrderFragmentPagerAdapter(FragmentManager fragmentManager, User currentUser){
        super(fragmentManager);
        titleList.add("未下单菜");
        titleList.add("已下单菜");
        this.currentUser=currentUser;/////////////////////////////////
    }
    @Override
    public Fragment getItem(int position){
        if(position==0) {//未下单
            return new NotOrderFoodFragment();////////////////////////
        }else{//下单未结账
            // TODO: 2018-10-09 未清楚是否传参成功currentUser
            OrderedFoodFragment orderedFoodFragment=new OrderedFoodFragment();//传递数据到Fragment中
            Bundle bundle=new Bundle();
            bundle.putSerializable("currentUser",currentUser);
            orderedFoodFragment.setArguments(bundle);
            return orderedFoodFragment;/////////////////////////
        }

    }

    @Override
    public int getCount(){
        return titleList.size();
    }

    //ViewPageer和TabLayout绑定后,获取到的是tablayout的text(文字)
    @Override
    public CharSequence getPageTitle(int position){
        return titleList.get(position);
    }
}
