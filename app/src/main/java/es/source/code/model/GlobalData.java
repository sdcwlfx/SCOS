package es.source.code.model;

import android.app.Application;

import java.util.ArrayList;

/**
 * 全局数据，存储从服务器中获取到的四种菜，及已经点过未下单的菜、点过下单未结账的菜
 * 其中全局变量当实参传入函数时，在函数内的操作即为对该全局变量操作
 * Created by asus on 2018-11-06.
 */

public class GlobalData extends Application {
    public static User currentUser=new User();//当前登录用户
    public static ArrayList<Food> coldFoodList=new ArrayList<Food>();//凉菜全局变量
    public static ArrayList<Food> hotFoodList=new ArrayList<Food>();//热菜全局变量
    public static ArrayList<Food> seaFoodList=new ArrayList<Food>();//海鲜全局变量
    public static ArrayList<Food> wineList=new ArrayList<Food>();//酒水全局变量
    public static ArrayList<CurrentUserFood> currentUserNotOrderFoodList=new ArrayList<CurrentUserFood>();//存储用户点过但未下单的菜品
    public static ArrayList<CurrentUserFood> currentUserOrderedFoodList=new ArrayList<CurrentUserFood>();//存储用户下过单但未结账的菜品
    public static ArrayList<CurrentUserFood> orderedFoodList=new ArrayList<CurrentUserFood>();//下过单未结账的菜品
    public static ArrayList<CurrentUserFood> notOrderFoodList=new ArrayList<CurrentUserFood>();//已点但未下单的菜品


}
