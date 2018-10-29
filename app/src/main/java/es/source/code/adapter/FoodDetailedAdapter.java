package es.source.code.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import es.source.code.R;
import es.source.code.model.CurrentUserFood;
import es.source.code.model.Food;

/**
 * FoodDetailed页适配器，横向滑动切换菜品
 * Created by asus on 2018-10-09.
 */

public class FoodDetailedAdapter extends RecyclerView.Adapter<FoodDetailedAdapter.ViewHodler> {

    private ArrayList<Food> foodList;
    private Context context;//上下文
    private ArrayList<CurrentUserFood> currentUserFoodList;//存储用户点过或下单但未支付的菜（维护全局对象？）


    static class ViewHodler extends RecyclerView.ViewHolder{

        View foodView;
        ImageView foodDetailedImageView;
        TextView foodDetailedNameText;
        TextView foodDetailedPriceText;
        TextView remarksEdit;
        Button foodDetailedButton;

        public ViewHodler(View view){
            super(view);
            foodView=view;
            foodDetailedImageView=(ImageView)view.findViewById(R.id.food_detailed_image);
            foodDetailedNameText=(TextView)view.findViewById(R.id.food_detailed_name);
            foodDetailedPriceText=(TextView)view.findViewById(R.id.food_detailed_price);
            remarksEdit=(TextView)view.findViewById(R.id.remarks_edit);
            foodDetailedButton=(Button)view.findViewById(R.id.food_detailed_button);

        }
    }

    // TODO: 2018-10-09  currentUserFoodList 存储所有已点（包括已点未下单，下单未结账）的菜品，不写入库，可以全局
    public FoodDetailedAdapter(ArrayList<Food> foodList, ArrayList<CurrentUserFood> currentUserFoodList, Context context){
        this.foodList=foodList;
        this.currentUserFoodList=currentUserFoodList;
        this.context=context;
    }

    @Override
    public FoodDetailedAdapter.ViewHodler onCreateViewHolder(ViewGroup parent, int viewType){
        //加载子项视图
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.food_detailed_item,parent,false);
        final FoodDetailedAdapter.ViewHodler hodler=new FoodDetailedAdapter.ViewHodler(view);
        //子项最外层点击事件
        hodler.foodView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // TODO: 2018-10-08


            }

        });

        //子项点菜按钮点击事件
        hodler.foodDetailedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2018-10-08 维护一个全局对象 ？currentUserFoodList
                if(hodler.foodDetailedButton.getText().equals(R.string.food_order)){
                    // TODO: 2018-10-08 将菜品加入全局对象 currentUserFoodList
                    int position=hodler.getAdapterPosition();
                    notifyItemChanged(position);//刷新适配器指定行
                    //notifyDataSetChanged();//刷新适配器 目的将按钮改为“退点”
                    Toast.makeText(context,"点菜成功",Toast.LENGTH_SHORT).show();
                }else{
                    // TODO: 2018-10-09 退点，将菜品从全局对象 currentUserFoodList移除，刷新适配器

                    int position=hodler.getAdapterPosition();
                    notifyItemChanged(position);//刷新适配器指定行

                }
            }
        });
        return hodler;
    }

    public void onBindViewHolder(FoodDetailedAdapter.ViewHodler hodler, int position){
        Food food=foodList.get(position);
        hodler.foodDetailedImageView.setImageResource(food.getFoodImgId());
        hodler.foodDetailedNameText.setText(food.getFoodName());
        hodler.foodDetailedPriceText.setText(food.getFoodPrice());
        for(int i=0;i<currentUserFoodList.size();i++){
            if(currentUserFoodList.get(i).getFood().getFoodName().equals(food.getFoodName())){
                hodler.foodDetailedButton.setText(R.string.food_no_order);//按钮显示退订
            }
        }

    }

    @Override
    public int getItemCount(){
        return foodList.size();
    }


}
