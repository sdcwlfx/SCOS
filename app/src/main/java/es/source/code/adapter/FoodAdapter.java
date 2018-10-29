package es.source.code.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.source.code.activity.FoodDetailed;
import es.source.code.model.CurrentUserFood;
import es.source.code.model.Food;
import es.source.code.R;

/**
 * 食物适配器：图片、菜名、价格、点菜按钮
 * Created by asus on 2018-10-08.
 */

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHodler> {


    private ArrayList<Food> foodList;
    private Context context;//上下文
    private ArrayList<CurrentUserFood> currentUserFoodList;//存储用户点过或下单但未支付的菜（维护全局对象？）


    static class ViewHodler extends RecyclerView.ViewHolder{

        View foodView;
        ImageView foodImageView;
        TextView foodNameText;
        TextView foodPriceText;
        TextView foodStackNumText;
        Button foodOrderButton;

        public ViewHodler(View view){
            super(view);
            foodView=view;
            foodImageView=(ImageView)view.findViewById(R.id.food_image);
            foodNameText=(TextView)view.findViewById(R.id.food_name);
            foodPriceText=(TextView)view.findViewById(R.id.food_price);
            foodStackNumText=(TextView)view.findViewById(R.id.food_stock_number);
            foodOrderButton=(Button)view.findViewById(R.id.food_order_button);

        }
    }

    // TODO: 2018-10-09  currentUserFoodList 存储所有已点（包括已点未下单，下单未结账）的菜品，不写入库，可以全局
    public FoodAdapter(ArrayList<Food> foodList, ArrayList<CurrentUserFood> currentUserFoodList, Context context){
        this.foodList=foodList;
        this.currentUserFoodList=currentUserFoodList;
        this.context=context;
    }

    @Override
    public ViewHodler onCreateViewHolder(ViewGroup parent,int viewType){
        //加载子项视图
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.food_recycle_view_item,parent,false);
        final ViewHodler hodler=new ViewHodler(view);
        //子项最外层点击事件
        hodler.foodView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // TODO: 2018-10-08  进入菜单详细页面
                Intent intent=new Intent(context, FoodDetailed.class);
                Bundle bundle=new Bundle();
                int position=hodler.getAdapterPosition();//记住点击位置
                bundle.putSerializable("foodList",foodList);
                bundle.putInt("position",position);
                intent.putExtras(bundle);
                Log.i("进入详情页面","子项最外层点击事件");
                context.startActivity(intent);

            }

        });

        //子项点菜按钮点击事件
        hodler.foodOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2018-10-08 维护一个全局对象 ？currentUserFoodList
                Log.i("点菜",hodler.foodOrderButton.getText().toString());
                String buttonText=hodler.foodOrderButton.getText().toString();
                if(buttonText.equals("点菜")){
                    // TODO: 2018-10-08 将菜品加入全局对象 currentUserFoodList
                    Log.i("点菜成功","点菜按钮");
                    Toast.makeText(context,"点菜成功",Toast.LENGTH_SHORT).show();
                   // notifyDataSetChanged();//刷新适配器 目的将按钮改为“退点”
                    notifyItemChanged(hodler.getAdapterPosition());

                }
            }
        });
        return hodler;
    }

    public void onBindViewHolder(ViewHodler hodler,int position){
        Food food=foodList.get(position);
        hodler.foodImageView.setImageResource(food.getFoodImgId());
        hodler.foodNameText.setText(food.getFoodName());
        hodler.foodPriceText.setText(food.getFoodPrice());
        hodler.foodStackNumText.setText(food.getFoodStackNum());
        hodler.foodOrderButton.setText("点菜");
        for(int i=0;i<currentUserFoodList.size();i++){
            if(currentUserFoodList.get(i).getFood().getFoodName().equals(food.getFoodName())){
                hodler.foodOrderButton.setText("退订");//按钮显示退订
            }
        }

    }

    @Override
    public int getItemCount(){
        return foodList.size();
    }



}
