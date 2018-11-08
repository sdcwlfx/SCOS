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

//
public class SeaFoodFragment extends Fragment {

   // private ArrayList<Food> seaFoodList;//海鲜
    // TODO: 2018-10-09 维护全局对象 ?
    //private ArrayList<CurrentUserFood> currentUserFoodList;//所有已点菜品
    private FoodAdapter foodAdapter;
    private RecyclerView seaFoodRecycleView;
    private Context context;//上下文
    private String foodCategory="海鲜";
    private IntentFilter intentFilter;
    private SeaCancelReceiver changeReceiver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        // TODO: 2018-10-09 加载海鲜，为RecycleView设置适配器
        //seaFoodList=new ArrayList<Food>();
        //currentUserFoodList=new ArrayList<CurrentUserFood>();

        View view=inflater.inflate(R.layout.fragment_sea_food,container,false);
        seaFoodRecycleView=(RecyclerView)view.findViewById(R.id.sea_food_recycle_view);
        context=getActivity();//获取依附的上下文（Activity）
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context);
        seaFoodRecycleView.setLayoutManager(linearLayoutManager);
        //foodAdapter=new FoodAdapter(seaFoodList,currentUserFoodList,context,foodCategory);
        foodAdapter=new FoodAdapter(GlobalData.seaFoodList,context,foodCategory);
        seaFoodRecycleView.setAdapter(foodAdapter);

        //注册广播监听海鲜退点动作更新对应热菜数量和按钮显示
        intentFilter=new IntentFilter();
        intentFilter.addAction("scos.SEA_REFRESH");
        changeReceiver=new SeaCancelReceiver();
        context.registerReceiver(changeReceiver,intentFilter);
        return view;

    }

    //海鲜退点广播接收器
    class SeaCancelReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent){
            foodAdapter.notifyDataSetChanged();//刷新适配器
            abortBroadcast();

        }
    }
}
