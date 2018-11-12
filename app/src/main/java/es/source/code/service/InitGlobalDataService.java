package es.source.code.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import es.source.code.model.Food;
import es.source.code.model.GlobalData;
import es.source.code.util.HttpUtil;

/**
 * 在进入欢迎页面后台运行服务初始化
 * helper methods.
 */
public class InitGlobalDataService extends IntentService {
    private String coldFoodAddress="http://192.168.43.155:8080/SCOSServer/ColdFood";
    private String hotFoodAddress="http://192.168.43.155:8080/SCOSServer/HotFood";
    private String seaFoodAddress="http://192.168.43.155:8080/SCOSServer/SeaFood";
    private String wineAddress="http://192.168.43.155:8080/SCOSServer/Wine";
//    private String coldFoodAddress="http://192.168.0.102:8080/SCOSServer/ColdFood";
//    private String hotFoodAddress="http://192.168.0.102:8080/SCOSServer/HotFood";
//    private String seaFoodAddress="http://192.168.0.102:8080/SCOSServer/SeaFood";
//    private String wineAddress="http://192.168.0.102:8080/SCOSServer/Wine";




    public InitGlobalDataService() {
        super("InitGlobalDataService");
    }





    @Override
    protected void onHandleIntent(Intent intent) {

        Gson gson=new Gson();
        //初始化凉菜静态变量
        String coldJsonData= HttpUtil.getFoodInformation(coldFoodAddress);
        GlobalData.coldFoodList=gson.fromJson(coldJsonData,new TypeToken<ArrayList<Food>>(){}.getType());

        //初始化热菜静态变量
        String hotJsonData= HttpUtil.getFoodInformation(hotFoodAddress);
        GlobalData.hotFoodList=gson.fromJson(hotJsonData,new TypeToken<ArrayList<Food>>(){}.getType());

        //初始化海鲜静态变量
        String seaJsonData= HttpUtil.getFoodInformation(seaFoodAddress);
        GlobalData.seaFoodList=gson.fromJson(seaJsonData,new TypeToken<ArrayList<Food>>(){}.getType());

        //初始化酒水静态变量
        String wineJsonData= HttpUtil.getFoodInformation(wineAddress);
        GlobalData.wineList=gson.fromJson(wineJsonData,new TypeToken<ArrayList<Food>>(){}.getType());





    }
}
