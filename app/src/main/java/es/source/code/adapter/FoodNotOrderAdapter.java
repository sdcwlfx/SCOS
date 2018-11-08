package es.source.code.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.source.code.R;
import es.source.code.model.CurrentUserFood;
import es.source.code.model.Food;
import es.source.code.model.GlobalData;
import es.source.code.util.Common;

/**
 *e03.7 已点但未下单菜适配器
 * Created by asus on 2018-10-08.
 */

public class FoodNotOrderAdapter extends RecyclerView.Adapter<FoodNotOrderAdapter.ViewHodler> {


    private Context context;//上下文
    private List<CurrentUserFood> currentUserNotOrderFoodList;//存储用户点过但未下单的食物
    private Intent intent;


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
            foodNotOrderButton=(Button)view.findViewById(R.id.food_not_order_button);

        }
    }

    public FoodNotOrderAdapter(ArrayList<CurrentUserFood> currentUserNotOrderFoodList, Context context){
        this.currentUserNotOrderFoodList=currentUserNotOrderFoodList;//对该数组操作即是对全局变量GlobalData.currentUserNotOrderFoodList操作
        this.context=context;
        this.intent=new Intent("scos.NOTORDER");
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
                try{


                int position=hodler.getAdapterPosition();//获取点击位置
                CurrentUserFood currentUserFood=currentUserNotOrderFoodList.get(position);
                Food foodSelected=currentUserFood.getFood();
                String foodCategory=foodSelected.getFoodCategory();
                currentUserNotOrderFoodList.remove(position);//删除，此时也修改了全局变量GlobalData.currentUserNotOrderFoodList的数值
                //GlobalData.currentUserNotOrderFoodList.remove(position);//////////////////////
                context.sendOrderedBroadcast(intent,null);//发送有序广播
                if(foodCategory.equals("凉菜")){//对应凉菜库存量加一
                    Common.addColdFoodStackNum(foodSelected.getFoodName());
                    //GlobalData.coldFoodList.remove(position);
                    //notifyItemChanged(position);//在有多个时，删除第0个会崩溃
                    Intent intent1=new Intent("scos.COLD_REFRESH");
                    context.sendOrderedBroadcast(intent1,null);//通知刷新凉菜碎片
                    notifyDataSetChanged();
                }else if(foodCategory.equals("热菜")){//对应热菜库存量加一
                    Common.addHotFoodStackNum(foodSelected.getFoodName());
                    //GlobalData.hotFoodList.remove(position);
                    context.sendOrderedBroadcast(intent,null);//发送有序广播
                    //notifyItemChanged(position);//在有多个时，删除第0个会崩溃
                    Intent intent1=new Intent("scos.HOT_REFRESH");
                    context.sendOrderedBroadcast(intent1,null);//通知刷新热菜碎片
                    notifyDataSetChanged();
                }else if(foodCategory.equals("海鲜")){//对应海鲜库存量加一
                    Common.addSeaFoodStackNum(foodSelected.getFoodName());
                    //GlobalData.seaFoodList.remove(position);
                    context.sendOrderedBroadcast(intent,null);//发送有序广播
                    //notifyItemChanged(position);//在有多个时，删除第0个会崩溃
                    Intent intent1=new Intent("scos.SEA_REFRESH");
                    context.sendOrderedBroadcast(intent1,null);//通知刷新海鲜碎片
                    notifyDataSetChanged();
                }else if(foodCategory.equals("酒水")){//对应酒水库存量加一
                    Common.addWineStackNum(foodSelected.getFoodName());
                    //GlobalData.wineList.remove(position);
                    context.sendOrderedBroadcast(intent,null);//发送有序广播
                    //notifyItemChanged(position);//在有多个时，删除第0个会崩溃
                    Intent intent1=new Intent("scos.WINE_REFRESH");
                    context.sendOrderedBroadcast(intent1,null);//通知刷新酒水碎片
                    notifyDataSetChanged();
                }
                }catch (Exception e){
                    e.printStackTrace();
                }



            }
        });
        return hodler;
    }

    public void onBindViewHolder(FoodNotOrderAdapter.ViewHodler hodler, int position){
        CurrentUserFood currentUserFood=currentUserNotOrderFoodList.get(position);
        //hodler.foodNotOrderImageView.setImageResource(currentUserFood.getFood().getFoodImgId());
        hodler.foodNotOrderNameText.setText(currentUserFood.getFood().getFoodName());
        hodler.foodNotOrderPriceText.setText(String.valueOf(currentUserFood.getTotalPrice()));
        hodler.foodNotOrderNumberText.setText(String.valueOf(currentUserFood.getNumber()));
        hodler.foodNotOrderRemarksText.setText(currentUserFood.getRemarks());
        hodler.foodNotOrderButton.setText("退点");

    }

    @Override
    public int getItemCount(){
        return currentUserNotOrderFoodList.size();
    }
}
