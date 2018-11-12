package es.source.code.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import es.source.code.R;
import es.source.code.adapter.FoodOrderedAdapter;
import es.source.code.model.CurrentUserFood;
import es.source.code.model.GlobalData;
import es.source.code.model.User;
import es.source.code.util.Common;

/**
 * 已下单未结账Fragment(相当于ViewPager中的子视图)
 * A simple {@link Fragment} subclass.
 */
public class OrderedFoodFragment extends Fragment {
    private FoodOrderedAdapter foodOrderedAdapter;
    private TextView foodNumber;//菜品总数
    private TextView accountTotalPrice;//订单总价
    private Button checkOutAccountButton;//结账
    private RecyclerView orderedFoodRecycleView;
    private ArrayList<CurrentUserFood> currentUserFoodArrayList;//已下单但未结账菜
    private Context context;
    private User currentUser;
    private Bundle bundle;
    private ProgressBar progressBar;
    private IntentFilter intentFilter;
    private OrderedChangeReceiver changeReceiver;//接受提交订单的广播，修改菜品数和菜单总价的值




    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO: 2018-10-09已下单未结账菜全局变量 （另有两个全局变量：已点未下单菜list,所有已点菜list）
       // currentUserFoodArrayList=new ArrayList<CurrentUserFood>();

        currentUser=null;
        View view=inflater.inflate(R.layout.fragment_odered_food, container, false);
        orderedFoodRecycleView=(RecyclerView)view.findViewById(R.id.ordered_food_recycle_view);
        foodNumber=(TextView)view.findViewById(R.id.food_number_ordered_text_view);
        accountTotalPrice=(TextView)view.findViewById(R.id.account_total_price_ordered_text_view);
        checkOutAccountButton=(Button)view.findViewById(R.id.check_out_acount_button);
        progressBar=(ProgressBar)view.findViewById(R.id.check_out_progress_bar);
        context=getActivity();
        Log.i("下单未付账","。。。");

        // TODO: 2018-10-09  开辟子线程加载信息，添加ProgressBar显示加载中
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context);
        orderedFoodRecycleView.setLayoutManager(linearLayoutManager);
        foodOrderedAdapter=new FoodOrderedAdapter(GlobalData.currentUserOrderedFoodList,context);
        orderedFoodRecycleView.setAdapter(foodOrderedAdapter);
        foodNumber.setText("菜品总数："+ String.valueOf(GlobalData.currentUserOrderedFoodList.size()));
        accountTotalPrice.setText("订单总价："+ String.valueOf(Common.getTotalPrice(GlobalData.currentUserOrderedFoodList)));
        Log.i("下单未付账","进行了");
        bundle=getArguments();
        if(bundle.getSerializable("currentUser")!=null){
            currentUser=(User)bundle.getSerializable("currentUser");//获取用户
        }
        Log.i("下单未付账","进行了");

        //注册广播监听提交订单按钮以便改变菜品总数和订单总价
        intentFilter=new IntentFilter();
        intentFilter.addAction("scos.COMMIT");
        changeReceiver=new OrderedChangeReceiver();
        context.registerReceiver(changeReceiver,intentFilter);

        checkOutAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2018-10-09 结账
                if(currentUser!=null){
                    new CheckOutTask().execute();//AsynTask子线程模拟结账
//                    GlobalData.currentUserOrderedFoodList.clear();//清空已点未下单全局变量
//                    foodOrderedAdapter.notifyDataSetChanged();//刷新适配器
                    if(currentUser.getOldUser()){
                        Toast.makeText(context,"您好，老顾客，本次你可享受7折优惠",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return view;
    }

    //广播接收器
    class OrderedChangeReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent){
            foodNumber.setText("菜品总数："+String.valueOf(GlobalData.currentUserOrderedFoodList.size()));
            accountTotalPrice.setText("订单总价："+ String.valueOf(Common.getTotalPrice(GlobalData.currentUserOrderedFoodList)));
            abortBroadcast();//截断有序广播

        }
    }

    //AsyncTask多线程模拟后台结账
     class CheckOutTask extends AsyncTask<String,Integer,String>{
        @Override
        protected void onPreExecute(){
            try{
                if(progressBar.getVisibility()==View.GONE){
                    progressBar.setVisibility(View.VISIBLE);
                }
            }catch (Exception e){
                e.printStackTrace();
            }


        }

        @Override
        protected String doInBackground(String... params){
            try {
                int count=0;
                int lenght=1;
                while (count<99){
                    count+=lenght;
                    publishProgress(count);//显示进度，马上调用onProgressUpdate()
                    Thread.sleep(60);//总共延时6秒(100*60)
                }

            }catch (InterruptedException e){
                e.printStackTrace();
            }
            return null;
        }

        //执行UI更新操作
        @Override
        protected void onProgressUpdate(Integer... progresses){
            progressBar.setProgress(progresses[0]);//设置进度条值

        }

        //执行任务收尾操作
        @Override
        protected void onPostExecute(String result){
            if(progressBar.getVisibility()==View.VISIBLE){
                progressBar.setVisibility(View.GONE);
            }
            if(progressBar.getVisibility()==View.VISIBLE){
                progressBar.setVisibility(View.GONE);
            }
            String totalMoney=String.valueOf(Common.getTotalPrice(GlobalData.currentUserOrderedFoodList));
            GlobalData.currentUserOrderedFoodList.clear();//清空已点未下单全局变量
            foodOrderedAdapter.notifyDataSetChanged();//刷新适配器
            foodNumber.setText("菜品总数："+ String.valueOf(GlobalData.currentUserOrderedFoodList.size()));
            accountTotalPrice.setText("订单总价："+ "0");
            checkOutAccountButton.setClickable(false);//结账按钮不可点
            Toast.makeText(context,"本次共消费"+totalMoney+"元，增加xx积分",Toast.LENGTH_SHORT).show();

        }




    }

}
