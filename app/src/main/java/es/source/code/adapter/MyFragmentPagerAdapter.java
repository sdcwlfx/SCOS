package es.source.code.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import es.source.code.activity.ColdFoodFragment;
import es.source.code.activity.HotFoodFragment;
import es.source.code.activity.SeaFoodFragment;
import es.source.code.activity.WineFragment;

/**
 * 将Fragment与ViewPager进行适配
 * Created by asus on 2018-10-08.
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<String> titleList=new ArrayList<String>();
    private Context context;

    public MyFragmentPagerAdapter(FragmentManager fragmentManager, Context context){
        super(fragmentManager);
        this.context=context;
        titleList.add("冷菜");
        titleList.add("热菜");
        titleList.add("海鲜");
        titleList.add("酒水");
    }
    @Override
    public Fragment getItem(int position){
        if (position==1){
            return new ColdFoodFragment();
        }else if(position==2){
            return new HotFoodFragment();
        }else if(position==3){
            return new SeaFoodFragment();
        }else{
            return new WineFragment();
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
