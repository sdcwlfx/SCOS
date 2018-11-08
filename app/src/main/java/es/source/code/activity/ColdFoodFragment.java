package es.source.code.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import es.source.code.R;
import es.source.code.adapter.FoodAdapter;
import es.source.code.model.CurrentUserFood;
import es.source.code.model.Food;
import es.source.code.model.GlobalData;
import es.source.code.util.Common;

//各个片段：显示不同滑动的页面（加载各种菜），作为FoodView的一部分,嵌入到ViewPager中，ViewPager相当于LinearLayout
//参考：https://www.cnblogs.com/zlj520/p/6961391.html
public class ColdFoodFragment extends Fragment {
    private ArrayList<Food> coldFoodList;//冷菜
    // TODO: 2018-10-09  维护全局对象 ?
    private ArrayList<CurrentUserFood> currentUserFoodList;//所有已点菜品
    private FoodAdapter foodAdapter;
    private RecyclerView coldFoodRecycleView;
    private Context context;//上下文
    private String foodCategory="凉菜";
    private IntentFilter intentFilter;
    private ColdCancelReceiver changeReceiver;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        // TODO: 2018-10-09 加载冷菜，为RecycleView设置适配器

        //coldFoodList=new ArrayList<Food>();
        //测试数据
//        Food food=new Food();
//        food.setFoodImgId(R.mipmap.ic_logo);
//        food.setFoodName("白菜");
//        food.setFoodPrice("10");
//        food.setFoodStackNum(100);
//        coldFoodList.add(food);
//        Food food1=new Food();
//        food1.setFoodImgId(R.mipmap.ic_logo);
//        food1.setFoodName("黄瓜");
//        food1.setFoodPrice("15");
//        food1.setFoodStackNum(101);
//        coldFoodList.add(food1);


        //currentUserFoodList=new ArrayList<CurrentUserFood>();

        View view=inflater.inflate(R.layout.fragment_cold_food,container,false);
        coldFoodRecycleView=(RecyclerView)view.findViewById(R.id.cold_food_recycle_view);
        context=getActivity();//获取依附的上下文（Activity）
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context);
        coldFoodRecycleView.setLayoutManager(linearLayoutManager);
        //foodAdapter=new FoodAdapter(coldFoodList,currentUserFoodList,context);
        foodAdapter=new FoodAdapter(GlobalData.coldFoodList,context,foodCategory);
        coldFoodRecycleView.setAdapter(foodAdapter);

        //注册广播监听凉菜退点动作更新对应凉菜数量和按钮显示
        intentFilter=new IntentFilter();
        intentFilter.addAction("scos.COLD_REFRESH");
        changeReceiver=new ColdCancelReceiver();
        context.registerReceiver(changeReceiver,intentFilter);

        return view;
    }

    //凉菜退点广播接收器
    class ColdCancelReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent){
            foodAdapter.notifyDataSetChanged();//刷新适配器
            abortBroadcast();

        }
    }
}
