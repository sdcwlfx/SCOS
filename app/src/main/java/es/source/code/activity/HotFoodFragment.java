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

public class HotFoodFragment extends Fragment {

    private ArrayList<Food> hotFoodList;//热菜
    // TODO: 2018-10-09  维护全局对象 ?
    private ArrayList<CurrentUserFood> currentUserFoodList;//所有已点菜品
    private FoodAdapter foodAdapter;
    private RecyclerView hotFoodRecycleView;
    private Context context;//上下文
    private String foodCategory="热菜";
    private IntentFilter intentFilter;
    private HotCancelReceiver changeReceiver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        // TODO: 2018-10-09 加载热菜，为RecycleView设置适配器
        //hotFoodList=new ArrayList<Food>();
        //currentUserFoodList=new ArrayList<CurrentUserFood>();

        View view=inflater.inflate(R.layout.fragment_hot_food,container,false);
        hotFoodRecycleView=(RecyclerView)view.findViewById(R.id.hot_food_recycle_view);
        context=getActivity();//获取依附的上下文（Activity）
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context);
        hotFoodRecycleView.setLayoutManager(linearLayoutManager);
        //foodAdapter=new FoodAdapter(GlobalData.hotFoodList,currentUserFoodList,context,foodCategory);
        foodAdapter=new FoodAdapter(GlobalData.hotFoodList,context,foodCategory);
        hotFoodRecycleView.setAdapter(foodAdapter);

        //注册广播监听热菜退点动作更新对应热菜数量和按钮显示
        intentFilter=new IntentFilter();
        intentFilter.addAction("scos.HOT_REFRESH");
        changeReceiver=new HotCancelReceiver();
        context.registerReceiver(changeReceiver,intentFilter);

        return view;

    }

    //热菜退点广播接收器
    class HotCancelReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent){
            foodAdapter.notifyDataSetChanged();//刷新适配器
            abortBroadcast();

        }
    }
}
