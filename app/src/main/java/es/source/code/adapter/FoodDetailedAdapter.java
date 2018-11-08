package es.source.code.adapter;

import android.content.Context;
import android.content.Intent;
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
import es.source.code.model.GlobalData;
import es.source.code.util.Common;

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
        EditText remarksEdit;
        Button foodDetailedButton;

        public ViewHodler(View view){
            super(view);
            foodView=view;
            foodDetailedImageView=(ImageView)view.findViewById(R.id.food_detailed_image);
            foodDetailedNameText=(TextView)view.findViewById(R.id.food_detailed_name);
            foodDetailedPriceText=(TextView)view.findViewById(R.id.food_detailed_price);
            remarksEdit=(EditText)view.findViewById(R.id.remarks_edit);
            foodDetailedButton=(Button)view.findViewById(R.id.food_detailed_button);

        }
    }

    // TODO: 2018-10-09  currentUserFoodList 存储所有已点（包括已点未下单，下单未结账）的菜品，不写入库，可以全局
    public FoodDetailedAdapter(ArrayList<Food> foodList, Context context){
        this.foodList=foodList;
       // this.currentUserFoodList=currentUserFoodList;
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
                String buttonText=hodler.foodDetailedButton.getText().toString();
                int position=hodler.getAdapterPosition();//获取点击的菜品对象
                Food foodSelected=foodList.get(position);//被选中的菜品
                String foodCategory=foodSelected.getFoodCategory();//菜品种类
                String remarks=hodler.remarksEdit.getText().toString();
                if(buttonText.equals("点菜")){
                    // TODO: 2018-10-08 将菜品加入全局对象 currentUserFoodList
//                    int position=hodler.getAdapterPosition();
//                    notifyItemChanged(position);//刷新适配器指定行
//                    //notifyDataSetChanged();//刷新适配器 目的将按钮改为“退点”

                    if(foodCategory.equals("凉菜")){//对应凉菜库存量减一
                        int foodStackNum= GlobalData.coldFoodList.get(position).getFoodStackNum();
                        GlobalData.coldFoodList.get(position).setFoodStackNum(foodStackNum-1);
                        foodList.get(position).setFoodStackNum(foodStackNum-1);
                        Common.addToCurrentUserNotOrderFoodList(foodSelected,remarks);//将菜品写入全局已点菜单变量currentUserFoodList中
                        Intent intent1=new Intent("scos.COLD_REFRESH");
                        context.sendOrderedBroadcast(intent1,null);//通知刷新凉菜碎片
                        notifyItemChanged(position);
                    }else if(foodCategory.equals("热菜")){//对应热菜库存量减一
                        int foodStackNum=GlobalData.hotFoodList.get(position).getFoodStackNum();
                        GlobalData.hotFoodList.get(position).setFoodStackNum(foodStackNum-1);
                        foodList.get(position).setFoodStackNum(foodStackNum-1);
                        Common.addToCurrentUserNotOrderFoodList(foodSelected,remarks);//将菜品写入全局已点菜单变量currentUserFoodList中
                        Intent intent1=new Intent("scos.HOT_REFRESH");
                        context.sendOrderedBroadcast(intent1,null);//通知刷新热菜碎片
                        notifyItemChanged(position);
                    }else if(foodCategory.equals("海鲜")){//对应海鲜库存量减一
                        int foodStackNum=GlobalData.seaFoodList.get(position).getFoodStackNum();
                        GlobalData.seaFoodList.get(position).setFoodStackNum(foodStackNum-1);
                        foodList.get(position).setFoodStackNum(foodStackNum-1);
                        Common.addToCurrentUserNotOrderFoodList(foodSelected,remarks);//将菜品写入全局已点菜单变量currentUserFoodList中
                        Intent intent1=new Intent("scos.SEA_REFRESH");
                        context.sendOrderedBroadcast(intent1,null);//通知刷新海鲜碎片
                        notifyItemChanged(position);

                    }else if(foodCategory.equals("酒水")){//对应酒水库存量减一
                        int foodStackNum=GlobalData.wineList.get(position).getFoodStackNum();
                        GlobalData.wineList.get(position).setFoodStackNum(foodStackNum-1);
                        foodList.get(position).setFoodStackNum(foodStackNum-1);
                        Common.addToCurrentUserNotOrderFoodList(foodSelected,remarks);//将菜品写入全局已点菜单变量currentUserFoodList中
                        Intent intent1=new Intent("scos.WINE_REFRESH");
                        context.sendOrderedBroadcast(intent1,null);//通知刷新酒水碎片
                        notifyItemChanged(position);
                    }
                    Toast.makeText(context,"点菜成功",Toast.LENGTH_SHORT).show();
                }else{
                    // TODO: 2018-10-09 退点，将菜品从全局对象 currentUserFoodList移除，刷新适配器
                    if(foodCategory.equals("凉菜")){//对应凉菜库存量加一
                        int foodStackNum=GlobalData.coldFoodList.get(position).getFoodStackNum();
                        GlobalData.coldFoodList.get(position).setFoodStackNum(foodStackNum+1);
                        foodList.get(position).setFoodStackNum(foodStackNum+1);
                        Common.removeFromCurrentUserNotOrderFoodList(foodSelected);//在已点未下单全局变量中查找并移除菜品
                        Common.removeFromCurrentUserOrderedFoodList(foodSelected);//在已下单未结账全局变量中查找并移除菜品
                        Intent intent1=new Intent("scos.COLD_REFRESH");
                        context.sendOrderedBroadcast(intent1,null);//通知刷新凉菜碎片
                        notifyItemChanged(position);
                    }else if(foodCategory.equals("热菜")){//对应热菜库存量加一
                        int foodStackNum=GlobalData.hotFoodList.get(position).getFoodStackNum();
                        GlobalData.hotFoodList.get(position).setFoodStackNum(foodStackNum+1);
                        foodList.get(position).setFoodStackNum(foodStackNum+1);
                        Common.removeFromCurrentUserNotOrderFoodList(foodSelected);//在已点未下单全局变量中查找并移除菜品
                        Common.removeFromCurrentUserOrderedFoodList(foodSelected);//在已下单未结账全局变量中查找并移除菜品
                        Intent intent1=new Intent("scos.HOT_REFRESH");
                        context.sendOrderedBroadcast(intent1,null);//通知刷新热菜碎片
                        notifyItemChanged(position);
                    }else if(foodCategory.equals("海鲜")){//对应海鲜库存量加一
                        int foodStackNum=GlobalData.seaFoodList.get(position).getFoodStackNum();
                        GlobalData.seaFoodList.get(position).setFoodStackNum(foodStackNum+1);
                        foodList.get(position).setFoodStackNum(foodStackNum+1);
                        Common.removeFromCurrentUserNotOrderFoodList(foodSelected);//在已点未下单全局变量中查找并移除菜品
                        Common.removeFromCurrentUserOrderedFoodList(foodSelected);//在已下单未结账全局变量中查找并移除菜品
                        Intent intent1=new Intent("scos.SEA_REFRESH");
                        context.sendOrderedBroadcast(intent1,null);//通知刷新海鲜碎片
                        notifyItemChanged(position);

                    }else if(foodCategory.equals("酒水")){//对应酒水库存量加一
                        int foodStackNum=GlobalData.wineList.get(position).getFoodStackNum();
                        GlobalData.wineList.get(position).setFoodStackNum(foodStackNum+1);
                        foodList.get(position).setFoodStackNum(foodStackNum+1);
                        Common.removeFromCurrentUserNotOrderFoodList(foodSelected);//在已点未下单全局变量中查找并移除菜品
                        Common.removeFromCurrentUserOrderedFoodList(foodSelected);//在已下单未结账全局变量中查找并移除菜品
                        Intent intent1=new Intent("scos.WINE_REFRESH");
                        context.sendOrderedBroadcast(intent1,null);//通知刷新酒水碎片
                        notifyItemChanged(position);
                    }


                }
            }
        });
        return hodler;
    }

    public void onBindViewHolder(FoodDetailedAdapter.ViewHodler hodler, int position){
        Food food=foodList.get(position);

        //hodler.foodDetailedImageView.setImageResource(food.getFoodImgId());
        hodler.foodDetailedNameText.setText(String.valueOf(food.getFoodName()));
        hodler.foodDetailedPriceText.setText(String.valueOf(food.getFoodPrice())+"元/份");
        hodler.foodDetailedButton.setText("点菜");
        //检查已点未下单数组
        for(int i=0;i<GlobalData.currentUserNotOrderFoodList.size();i++){
            if(GlobalData.currentUserNotOrderFoodList.get(i).getFood().getFoodName().equals(food.getFoodName())){
                hodler.foodDetailedButton.setText("退点");//按钮显示退订
                break;

            }
        }
        //检查已下单未结账数组
        for(int i=0;i<GlobalData.currentUserOrderedFoodList.size();i++){
            if(GlobalData.currentUserOrderedFoodList.get(i).getFood().getFoodName().equals(food.getFoodName())){
                hodler.foodDetailedButton.setText("退订");//按钮显示退订
                break;
            }

        }

    }

    @Override
    public int getItemCount(){
        return foodList.size();
    }


}
