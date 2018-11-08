package es.source.code.util;

import android.app.ActivityManager;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import es.source.code.model.CurrentUserFood;
import es.source.code.model.Food;
import es.source.code.model.GlobalData;

/**
 * 判断SCOS app进程是否正在运行
 * 原文：https://blog.csdn.net/zhonglinliu/article/details/56679100
 * Created by asus on 2018-10-28.
 */

public class Common {
    public static boolean isProcessRunning(Context context, String processName) {
        boolean isRunning=false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> lists = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : lists) {
            //判断后天是否有该应用进程运行或者当前运行的活动进程为该应用
            if (info.processName.equals(processName)) {
                isRunning=true;
            }
        }
        return isRunning;
    }
    //将点的菜品加入全局变量currentUserNotOrderFoodList中
    public static void addToCurrentUserNotOrderFoodList(Food food,String remarks){
        CurrentUserFood currentUserFood=new CurrentUserFood();
        currentUserFood.setFood(food);
        currentUserFood.setNumber(1);
        currentUserFood.setRemarks(remarks);
        currentUserFood.setTotalPrice(food.getFoodPrice()*currentUserFood.getNumber());
        //currentUserFood.setState(1);//设置为已点但未下单状态
        GlobalData.currentUserNotOrderFoodList.add(currentUserFood);
    }

    //将退点的菜品从全局变量currentUserNotOrderFoodList中移除
    public static void removeFromCurrentUserNotOrderFoodList(Food food){
        for(int i=0;i<GlobalData.currentUserNotOrderFoodList.size();i++){
            if(GlobalData.currentUserNotOrderFoodList.get(i).getFood().getFoodName().equals(food.getFoodName())){
                GlobalData.currentUserNotOrderFoodList.remove(i);
                break;
            }
        }
    }

    //将退点的菜品从全局变量currentUserOrderedFoodList中移除
    public static void removeFromCurrentUserOrderedFoodList(Food food){
        for(int i=0;i<GlobalData.currentUserOrderedFoodList.size();i++){
            if(GlobalData.currentUserOrderedFoodList.get(i).getFood().getFoodName().equals(food.getFoodName())){
                GlobalData.currentUserOrderedFoodList.remove(i);
                break;
            }
        }
    }

    //返回订单总价
    public static int getTotalPrice(ArrayList<CurrentUserFood> foods){
        int totalPrice=0;
        for (int i=0;i<foods.size();i++){
            totalPrice+=foods.get(i).getTotalPrice();
        }
        return totalPrice;
    }

    //从凉菜全局变量添加对应菜库存量
    public static void addColdFoodStackNum(String foodName){
        for(int i=0;i<GlobalData.coldFoodList.size();i++){
            if(GlobalData.coldFoodList.get(i).getFoodName().equals(foodName)){
                int stackNum=GlobalData.coldFoodList.get(i).getFoodStackNum();
                GlobalData.coldFoodList.get(i).setFoodStackNum(stackNum+1);
                break;
            }
        }

    }

    //从热菜全局变量添加对应菜库存量
    public static void addHotFoodStackNum(String foodName){
        for(int i=0;i<GlobalData.hotFoodList.size();i++){
            if(GlobalData.hotFoodList.get(i).getFoodName().equals(foodName)){
                int stackNum=GlobalData.hotFoodList.get(i).getFoodStackNum();
                GlobalData.hotFoodList.get(i).setFoodStackNum(stackNum+1);
                break;
            }
        }

    }

    //从海鲜全局变量添加对应菜库存量
    public static void addSeaFoodStackNum(String foodName){
        for(int i=0;i<GlobalData.seaFoodList.size();i++){
            if(GlobalData.seaFoodList.get(i).getFoodName().equals(foodName)){
                int stackNum=GlobalData.seaFoodList.get(i).getFoodStackNum();
                GlobalData.seaFoodList.get(i).setFoodStackNum(stackNum+1);
                break;
            }
        }
    }

    //从酒水全局变量添加对应菜库存量
    public static void addWineStackNum(String foodName){
        for(int i=0;i<GlobalData.wineList.size();i++){
            if(GlobalData.wineList.get(i).getFoodName().equals(foodName)){
                int stackNum=GlobalData.wineList.get(i).getFoodStackNum();
                GlobalData.wineList.get(i).setFoodStackNum(stackNum+1);
                break;
            }
        }

    }








}
