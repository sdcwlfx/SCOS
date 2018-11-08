package es.source.code.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import es.source.code.R;
import es.source.code.adapter.FoodNotOrderAdapter;
import es.source.code.adapter.FoodOrderedAdapter;
import es.source.code.model.CurrentUserFood;
import es.source.code.model.GlobalData;
import es.source.code.model.User;
import es.source.code.util.Common;


public class NotOrderFoodFragment extends Fragment {

    private FoodNotOrderAdapter foodNotOrdeAdapter;
    private TextView foodNumber;//菜品总数
    private TextView accountTotalPrice;//订单总价
    private Button commitAccountButton;//提交订单
    private RecyclerView notOrderFoodRecycleView;
    private ArrayList<CurrentUserFood> currentUserFoodArrayList;//已点但未下单
    private Context context;
    private IntentFilter intentFilter;
    private NotOrderChangeReceiver changeReceiver;

    public NotOrderFoodFragment(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO: 2018-10-09已点未下单菜全局变量 （另有两个全局变量 ：已下单未结账list,所有已点菜list）
        //currentUserFoodArrayList=new ArrayList<CurrentUserFood>();

        View view=inflater.inflate(R.layout.fragment_not_order_food, container, false);
        notOrderFoodRecycleView=(RecyclerView)view.findViewById(R.id.not_order_food_recycle_view);
        foodNumber=(TextView)view.findViewById(R.id.food_number_not_text_view);
        accountTotalPrice=(TextView)view.findViewById(R.id.account_total_price_not_text_view);
        commitAccountButton=(Button)view.findViewById(R.id.commit_acount_button);
        context=getActivity();
        Log.i("已点未下单","。。。");

        // TODO: 2018-10-09  开辟子线程加载信息,添加ProgressBar显示加载中
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context);
        notOrderFoodRecycleView.setLayoutManager(linearLayoutManager);
        foodNotOrdeAdapter=new FoodNotOrderAdapter(GlobalData.currentUserNotOrderFoodList,context);
        notOrderFoodRecycleView.setAdapter(foodNotOrdeAdapter);
        foodNumber.setText("菜品总数："+String.valueOf(GlobalData.currentUserNotOrderFoodList.size()));//必须先转成String不然应用会崩溃
        accountTotalPrice.setText("订单总价："+ String.valueOf(Common.getTotalPrice(GlobalData.currentUserNotOrderFoodList)));

        Log.i("已点未下单","进行了");
        //注册广播监听已点未下单菜品变化以便实时改变菜品总数和订单总价
        intentFilter=new IntentFilter();
        intentFilter.addAction("scos.NOTORDER");
        changeReceiver=new NotOrderChangeReceiver();
        context.registerReceiver(changeReceiver,intentFilter);



        //提交订单
        commitAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2018-10-09 提交订单
                for(int i=0;i<GlobalData.currentUserNotOrderFoodList.size();i++){
                    CurrentUserFood currentUserFood=GlobalData.currentUserNotOrderFoodList.get(i);
                    GlobalData.currentUserOrderedFoodList.add(currentUserFood);
                    GlobalData.currentUserNotOrderFoodList.remove(i);
                    i--;
                }
                foodNotOrdeAdapter.notifyDataSetChanged();//刷新适配器
                foodNumber.setText("菜品总数："+String.valueOf(GlobalData.currentUserNotOrderFoodList.size()));//必须先转成String不然应用会崩溃
                accountTotalPrice.setText("订单总价："+ String.valueOf(Common.getTotalPrice(GlobalData.currentUserNotOrderFoodList)));
                //发送有序广播，更新已下单菜页面的菜品数和菜单总价
                Intent intent=new Intent("scos.COMMIT");
                context.sendOrderedBroadcast(intent,null);
                Toast.makeText(context,"订单提交成功",Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    class NotOrderChangeReceiver extends BroadcastReceiver{

        public void onReceive(Context context, Intent intent){
            foodNumber.setText("菜品总数："+String.valueOf(GlobalData.currentUserNotOrderFoodList.size()));
            accountTotalPrice.setText("订单总价："+ String.valueOf(Common.getTotalPrice(GlobalData.currentUserNotOrderFoodList)));
            abortBroadcast();

        }
    }



}
