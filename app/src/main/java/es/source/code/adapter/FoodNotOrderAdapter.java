package es.source.code.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import es.source.code.R;
import es.source.code.model.CurrentUserFood;
import es.source.code.model.Food;

/**
 *e03.7 已点但未下单菜适配器
 * Created by asus on 2018-10-08.
 */

public class FoodNotOrderAdapter extends RecyclerView.Adapter<FoodNotOrderAdapter.ViewHodler> {


    private Context context;//上下文
    private List<CurrentUserFood> currentUserNotOrderFoodList;//存储用户点过但未下单的食物


    static class ViewHodler extends RecyclerView.ViewHolder{

        View foodView;
        ImageView foodNotOrderImageView;//图片
        TextView foodNotOrderNameText;//菜名
        TextView foodNotOrderPriceText;//菜价钱
        TextView foodNotOrderNumberText;//数量
        TextView foodNotOrderRemarksText;//备注
        Button foodNotOrderButton;//退点按钮

        public ViewHodler(View view){
            super(view);
            foodView=view;
            foodNotOrderImageView=(ImageView)view.findViewById(R.id.food_not_order_image);
            foodNotOrderNameText=(TextView)view.findViewById(R.id.food_not_order_name);
            foodNotOrderPriceText=(TextView)view.findViewById(R.id.food_not_order_price);
            foodNotOrderNumberText=(TextView)view.findViewById(R.id.food_not_order_number);
            foodNotOrderRemarksText=(TextView)view.findViewById(R.id.food_not_order_remarks);
            foodNotOrderButton=(Button)view.findViewById(R.id.food_order_button);

        }
    }

    public FoodNotOrderAdapter(List<CurrentUserFood> currentUserNotOrderFoodList, Context context){
        this.currentUserNotOrderFoodList=currentUserNotOrderFoodList;
        this.context=context;
    }

    @Override
    public FoodNotOrderAdapter.ViewHodler onCreateViewHolder(ViewGroup parent, int viewType){
        //加载子项视图
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.food_not_order_recycle_view_item,parent,false);
        final FoodNotOrderAdapter.ViewHodler hodler=new FoodNotOrderAdapter.ViewHodler(view);
        //子项退订按钮点击事件
        hodler.foodNotOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2018-10-08 从currentUserNotOrderFoodList删除选中的，再刷新适配器
                int position=hodler.getAdapterPosition();//获取点击位置
                currentUserNotOrderFoodList.remove(position);//删除
                notifyDataSetChanged();

            }
        });
        return hodler;
    }

    public void onBindViewHolder(FoodNotOrderAdapter.ViewHodler hodler, int position){
        CurrentUserFood currentUserFood=currentUserNotOrderFoodList.get(position);
        hodler.foodNotOrderImageView.setImageResource(currentUserFood.getFood().getFoodImgId());
        hodler.foodNotOrderNameText.setText(currentUserFood.getFood().getFoodName());
        hodler.foodNotOrderPriceText.setText(currentUserFood.getTotalPrice());
        hodler.foodNotOrderNumberText.setText(currentUserFood.getNumber());
        hodler.foodNotOrderRemarksText.setText(currentUserFood.getRemarks());

    }

    @Override
    public int getItemCount(){
        return currentUserNotOrderFoodList.size();
    }
}
