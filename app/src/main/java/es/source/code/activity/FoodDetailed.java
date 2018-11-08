package es.source.code.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import es.source.code.R;
import es.source.code.adapter.FoodDetailedAdapter;
import es.source.code.model.CurrentUserFood;
import es.source.code.model.Food;
import es.source.code.model.GlobalData;

//显示单个菜品详情
public class FoodDetailed extends AppCompatActivity {
    private FoodDetailedAdapter foodDetailedAdapter;
    private ArrayList<Food> foodArrayList;//所有菜
    //private ArrayList<CurrentUserFood> currentUserFoodArrayList;//所有已点（包括下单未结账和已点未下单）菜品
    private RecyclerView foodDetailedRecyleView;
    private LinearSnapHelper linearSnapHelper;//RecyclerView 帮助类，配合使item居中显示
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detailed);

        foodDetailedRecyleView=(RecyclerView)findViewById(R.id.food_detailed_recycle_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);//水平方向
        foodDetailedRecyleView.setLayoutManager(layoutManager);

        linearSnapHelper=new LinearSnapHelper();
        linearSnapHelper.attachToRecyclerView(foodDetailedRecyleView);
        // TODO: 2018-10-09 实际用全局变量 currentUserFoodArrayList 接受传来的ArrayList<Food>对象及位置position,先适配再转到该位置菜品页即可查看指定菜品详情

        // TODO: 2018-10-09 子线程中加载
        Bundle bundle=getIntent().getExtras();
        int position=bundle.getInt("position",0);
        foodArrayList=(ArrayList<Food>)bundle.getSerializable("foodList");
        //currentUserFoodArrayList=new ArrayList<CurrentUserFood>();
        //foodArrayList=new ArrayList<Food>();

        //foodDetailedAdapter=new FoodDetailedAdapter(foodArrayList,currentUserFoodArrayList,FoodDetailed.this);
        foodDetailedAdapter=new FoodDetailedAdapter(foodArrayList,FoodDetailed.this);
        foodDetailedRecyleView.setAdapter(foodDetailedAdapter);
        foodDetailedRecyleView.smoothScrollToPosition(position);//跳转到指定位置



    }
}
