package es.source.code.activity;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import es.source.code.R;
import es.source.code.adapter.FoodOrderedAdapter;
import es.source.code.model.CurrentUserFood;
import es.source.code.model.User;

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




    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO: 2018-10-09已下单未结账菜全局变量 （另有两个全局变量：已点未下单菜list,所有已点菜list）
        currentUserFoodArrayList=new ArrayList<CurrentUserFood>();

        currentUser=null;
        View view=inflater.inflate(R.layout.fragment_odered_food, container, false);
        orderedFoodRecycleView=(RecyclerView)view.findViewById(R.id.ordered_food_recycle_view);
        foodNumber=(TextView)view.findViewById(R.id.food_number_ordered_text_view);
        accountTotalPrice=(TextView)view.findViewById(R.id.account_total_price_ordered_text_view);
        checkOutAccountButton=(Button)view.findViewById(R.id.check_out_acount_button);
        context=getActivity();

        // TODO: 2018-10-09  开辟子线程加载信息，添加ProgressBar显示加载中
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context);
        orderedFoodRecycleView.setLayoutManager(linearLayoutManager);
        foodOrderedAdapter=new FoodOrderedAdapter(currentUserFoodArrayList,context);
        orderedFoodRecycleView.setAdapter(foodOrderedAdapter);
        foodNumber.setText("菜品总数");
        accountTotalPrice.setText("订单总价");

        bundle=getArguments();
        if(bundle.getSerializable("currentUser")!=null){
            currentUser=(User)bundle.getSerializable("currentUser");//获取用户
        }

        checkOutAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2018-10-09 结账
                if(currentUser!=null){
                    if(currentUser.getOldUser()){
                        Toast.makeText(context,"您好，老顾客，本次你可享受7折优惠",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return view;
    }

}
