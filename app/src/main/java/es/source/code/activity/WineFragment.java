package es.source.code.activity;

import android.content.Context;
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

//酒水片段,作为FoodView中ViewPager加载的部分
public class WineFragment extends Fragment {

    private ArrayList<Food> wineList;//酒水
    // TODO: 2018-10-09 维护全局对象 ?
    private ArrayList<CurrentUserFood> currentUserFoodList;//所有已点菜品
    private FoodAdapter foodAdapter;
    private RecyclerView wineRecycleView;
    private Context context;//上下文

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        // TODO: 2018-10-09 加载酒水，为RecycleView设置适配器
        wineList=new ArrayList<Food>();
        currentUserFoodList=new ArrayList<CurrentUserFood>();

        View view=inflater.inflate(R.layout.fragment_wine,container,false);
        wineRecycleView=(RecyclerView)view.findViewById(R.id.wine_recycle_view);
        context=getActivity();//获取依附的上下文（Activity）
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context);
        wineRecycleView.setLayoutManager(linearLayoutManager);
        foodAdapter=new FoodAdapter(wineList,currentUserFoodList,context);
        wineRecycleView.setAdapter(foodAdapter);
        return view;
    }
}
