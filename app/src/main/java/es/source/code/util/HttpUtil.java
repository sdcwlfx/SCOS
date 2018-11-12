package es.source.code.util;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import es.source.code.model.Food;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 向eclipse中tomcat服务器发送网络请求
 * Created by asus on 2018-11-04.
 */

public class HttpUtil {
    private static String addressLogin="http://192.168.43.155:8080/SCOSServer/LoginValidator";//模拟器测试
    private static String addressRegister="http://192.168.43.155:8080/SCOSServer/Register";//模拟器测试
    private static String addressUpdate="http://192.168.43.155:8080/SCOSServer/FoodUpdateService";//菜品信息更新地址
    private static String addressUpdateFromXML="http://192.168.43.155:8080/SCOSServer/newFood.xml";//菜品信息更新地址

//    private static String addressLogin="http://192.168.0.102:8080/SCOSServer/LoginValidator";//模拟器测试
//    private static String addressRegister="http://192.168.0.102:8080/SCOSServer/Register";//模拟器测试
//    private static String addressUpdate="http://192.168.0.102:8080/SCOSServer/FoodUpdateService";//菜品种类信息更新地址


    //HttpURLConnection方法
    public static String sendLoginHttpRequest(String account,String password,String address){
        HttpURLConnection connection=null;
        BufferedReader reader=null;
        try{
            URL url=new URL(address);
            connection=(HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");//post方法提交数据
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            DataOutputStream out=new DataOutputStream(connection.getOutputStream());
            out.writeBytes("account="+ URLEncoder.encode(account,"UTF-8")+"&password="+URLEncoder.encode(password,"UTF-8"));//提交数据到服务器

            InputStream in=connection.getInputStream();
            reader=new BufferedReader(new InputStreamReader(in));
            StringBuffer response=new StringBuffer();
            String line;
            while ((line=reader.readLine())!=null){
                response.append(line);
            }
            return response.toString();
        }catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }finally {
            if(connection!=null){
                connection.disconnect();//断开连接
            }
        }

    }


    //okhttp3在enqueue中开辟子线程执行Http请求，并将最终结果回调到okhttp3.Callback中，但回调接口还是在子线程中运行的
    public static void sendOkHttpRequestOnThread(String address,String account,String password,okhttp3.Callback callback){
        OkHttpClient client=new OkHttpClient();
        RequestBody requestBody=new FormBody.Builder()
                .add("account",account)
                .add("password",password)
                .build();
        //post方法向服务器传递数据
        Request request=new Request.Builder()
                .url(address)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    //okhttp3执行Http请求，验证登录注册功能
    public static String sendOkHttpRequest(String account,String password,boolean loginButtonSelected){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("RESULTCODE",0);
            String responseData=jsonObject.toString();
            if(loginButtonSelected){//登陆验证
                //OkHttpClient client=new OkHttpClient();
                OkHttpClient client=new OkHttpClient.Builder()
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(20,TimeUnit.SECONDS)
                        .build();
                RequestBody requestBody=new FormBody.Builder()
                        .add("account",account)
                        .add("password",password)
                        .build();
                //post方法向服务器传递数据
                Request request=new Request.Builder()
                        .url(addressLogin)
                        .post(requestBody)
                        .build();
                Response response=client.newCall(request).execute();//执行post请求接受返回的数据
                responseData=response.body().string();//不能使用body().toString()
                return responseData;//以字符串形式返回
            }else {//执行注册
                OkHttpClient client=new OkHttpClient.Builder()
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(20,TimeUnit.SECONDS)
                        .build();
                RequestBody requestBody=new FormBody.Builder()
                        .add("account",account)
                        .add("password",password)
                        .build();
                //post方法向服务器传递数据
                Log.i("注册", String.valueOf(account+"zm"+password));
                Request request=new Request.Builder()
                        .url(addressRegister)
                        .post(requestBody)
                        .build();
                Response response=client.newCall(request).execute();//执行post请求接受返回的数据
                responseData=response.body().string();//不能使用body().toString()
                return responseData;//以字符串形式返回
            }

        }catch (IOException | JSONException e){
            e.printStackTrace();
        }
        return  jsonObject.toString();
    }

    //okhttp3请求Http请求，获取菜品更新信息
    public static String getNewFoodInformation(){
        String responseData="";
        try{
            //OkHttpClient client=new OkHttpClient();
            OkHttpClient client=new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(20,TimeUnit.SECONDS)
                    .build();
            Request request=new Request.Builder()
                    .url(addressUpdate)
                    .build();
            Response response=client.newCall(request).execute();//执行GET请求接受返回的新的菜品信息
            responseData=response.body().string();//不能使用body().toString()
            return responseData;
        }catch (IOException e){
            e.printStackTrace();
        }

        return responseData;
    }


    //从服务器中获取存储形式为XML文件的新菜品类
    public static ArrayList<Food> getNewFoodInfoFromXML(){
        ArrayList<Food> newFoodArrayList=new ArrayList<Food>();
        try{
            OkHttpClient client=new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(20,TimeUnit.SECONDS)
                    .build();
            Request request=new Request.Builder()
                    .url(addressUpdateFromXML)
                    .build();
            Response response=client.newCall(request).execute();
            String responseData=response.body().string();
            //解析XML文件内容
            XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser=factory.newPullParser();
            xmlPullParser.setInput(new StringReader(responseData));
            int eventType=xmlPullParser.getEventType();
            String foodName="";
            int foodPrice=0;
            int foodStackNum=0;
            String foodCategory="";
            while (eventType!=XmlPullParser.END_DOCUMENT){
                String nodeName=xmlPullParser.getName();
                switch (eventType){
                    case XmlPullParser.START_TAG:
                        if("foodName".equals(nodeName)){
                            foodName=xmlPullParser.nextText();
                        }else if("foodPrice".equals(nodeName)){
                            foodPrice=Integer.parseInt(xmlPullParser.nextText());
                        }else if("foodStackNum".equals(nodeName)){
                            foodStackNum=Integer.parseInt(xmlPullParser.nextText());
                        }else if("foodCategory".equals(nodeName)){
                            foodCategory=xmlPullParser.nextText();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if("food".equals(nodeName)){
                            Food food=new Food();
                            food.setFoodName(foodName);
                            food.setFoodCategory(foodCategory);
                            food.setFoodStackNum(foodStackNum);
                            food.setFoodPrice(foodPrice);
                            newFoodArrayList.add(food);
                        }
                        break;
                    default:
                        break;
                }
                eventType=xmlPullParser.next();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return newFoodArrayList;
    }





    //okhttp3请求Http请求，获取菜品信息，初始化全局变量
    public static String getFoodInformation(String addressFood){
        String responseData="";
        try{
            //OkHttpClient client=new OkHttpClient();
            OkHttpClient client=new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(20,TimeUnit.SECONDS)
                    .build();
            Request request=new Request.Builder()
                    .url(addressFood)
                    .build();
            Response response=client.newCall(request).execute();//执行GET请求接受返回的菜品信息
            responseData=response.body().string();//不能使用body().toString()
            return responseData;
        }catch (IOException e){
            e.printStackTrace();
        }
        return responseData;
    }

    //从服务器获取到实时菜品数据已达到实时更新效果
    public static String getRealTimeUpdateFoodInfomation(String addressRealTimeUpdate){
        String responseData="";
        try{
            //OkHttpClient client=new OkHttpClient();
            OkHttpClient client=new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(20,TimeUnit.SECONDS)
                    .build();
            Request request=new Request.Builder()
                    .url(addressRealTimeUpdate)
                    .build();
            Response response=client.newCall(request).execute();//执行GET请求接受返回的菜品信息
            responseData=response.body().string();//不能使用body().toString()
            return responseData;
        }catch (IOException e){
            e.printStackTrace();
        }
        return responseData;


    }









}
