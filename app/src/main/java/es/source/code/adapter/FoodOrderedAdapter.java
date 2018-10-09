package es.source.code.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import es.source.code.R;
import es.source.code.model.CurrentUserFood;

/**
 * 已下单但未结账适配器
 * Created by asus on 2018-10-09.
 */

public class FoodOrderedAdapter extends RecyclerView.Adapter<FoodOrderedAdapter.ViewHodler> {

    private Context context;//上下文
    // TODO: 2018-10-09 最好是：维护两个全局List,一个下单未结账list，一个已点未下单list,退出应用时结束无需存储
    private List<CurrentUserFood> currentUserOrderedFoodList;//存储用户下过单为结账的食物


    static class ViewHodler extends RecyclerView.ViewHolder{

        View foodView;
        ImageView foodOrderedImageView;//图片
        TextView foodOrderedNameText;//菜名
        TextView foodOrderedPriceText;//菜价钱
        TextView foodOrderedNumberText;//数量
        TextView foodOrderedRemarksText;//备注

        public ViewHodler(View view){
            super(view);
            foodView=view;
            foodOrderedImageView=(ImageView)view.findViewById(R.id.food_not_order_image);
            foodOrderedNameText=(TextView)view.findViewById(R.id.food_not_order_name);
            foodOrderedPriceText=(TextView)view.findViewById(R.id.food_not_order_price);
            foodOrderedNumberText=(TextView)view.findViewById(R.id.food_not_order_number);
            foodOrderedRemarksText=(TextView)view.findViewById(R.id.food_not_order_remarks);

        }
    }

    public FoodOrderedAdapter(List<CurrentUserFood> currentUserOrderedFoodList, Context context){
        this.currentUserOrderedFoodList=currentUserOrderedFoodList;
        this.context=context;
    }

    @Override
    public FoodOrderedAdapter.ViewHodler onCreateViewHolder(ViewGroup parent, int viewType){
        //加载子项视图
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.food_ordered_recycle_view_item,parent,false);
        final FoodOrderedAdapter.ViewHodler hodler=new FoodOrderedAdapter.ViewHodler(view);
        return hodler;
    }

    public void onBindViewHolder(FoodOrderedAdapter.ViewHodler hodler, int position){
        CurrentUserFood currentUserFood=currentUserOrderedFoodList.get(position);
        hodler.foodOrderedImageView.setImageResource(currentUserFood.getFood().getFoodImgId());
        hodler.foodOrderedNameText.setText(currentUserFood.getFood().getFoodName());
        hodler.foodOrderedPriceText.setText(currentUserFood.getTotalPrice());
        hodler.foodOrderedNumberText.setText(currentUserFood.getNumber());
        hodler.foodOrderedRemarksText.setText(currentUserFood.getRemarks());

    }

    @Override
    public int getItemCount(){
        return currentUserOrderedFoodList.size();
    }
}
