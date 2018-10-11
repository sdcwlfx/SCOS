package es.source.code.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import es.source.code.R;
import es.source.code.adapter.FoodNotOrderAdapter;
import es.source.code.adapter.FoodOrderedAdapter;
import es.source.code.model.CurrentUserFood;
import es.source.code.model.User;


public class NotOrderFoodFragment extends Fragment {

    private FoodNotOrderAdapter foodNotOrdeAdapter;
    private TextView foodNumber;//菜品总数
    private TextView accountTotalPrice;//订单总价
    private Button commitAccountButton;//提交订单
    private RecyclerView notOrderFoodRecycleView;
    private ArrayList<CurrentUserFood> currentUserFoodArrayList;//已点但未下单
    private Context context;

    public NotOrderFoodFragment(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO: 2018-10-09已点未下单菜全局变量 （另有两个全局变量 ：已下单未结账list,所有已点菜list）
        currentUserFoodArrayList=new ArrayList<CurrentUserFood>();

        View view=inflater.inflate(R.layout.fragment_not_order_food, container, false);
        notOrderFoodRecycleView=(RecyclerView)view.findViewById(R.id.not_order_food_recycle_view);
        foodNumber=(TextView)view.findViewById(R.id.food_number_not_text_view);
        accountTotalPrice=(TextView)view.findViewById(R.id.account_total_price_not_text_view);
        commitAccountButton=(Button)view.findViewById(R.id.commit_acount_button);
        context=getActivity();

        // TODO: 2018-10-09  开辟子线程加载信息,添加ProgressBar显示加载中
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context);
        notOrderFoodRecycleView.setLayoutManager(linearLayoutManager);
        foodNotOrdeAdapter=new FoodNotOrderAdapter(currentUserFoodArrayList,context);
        notOrderFoodRecycleView.setAdapter(foodNotOrdeAdapter);
        foodNumber.setText("菜品总数");
        accountTotalPrice.setText("订单总价");


        commitAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2018-10-09 提交订单
            }
        });
        return view;
    }


}
