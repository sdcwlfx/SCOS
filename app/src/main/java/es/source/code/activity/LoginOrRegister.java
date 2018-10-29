package es.source.code.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.source.code.R;
import es.source.code.model.User;

//创建子线程实现ProgressBar在主线程显示，子线程沉睡2s后返回到主线程操作UI
public class LoginOrRegister extends AppCompatActivity {
    private ProgressBar progressBar;//登录进度
    private Button loginButton;//登录按钮
    private Button returnButton;//返回按钮
    private Button registerButton;//注册按钮
    private EditText passwordEdit;//密码
    private EditText userNameEdit;//登录名
    private Pattern pattern;//正则对象
    private Matcher matcher;//正则匹配
    private String errorWarning="输入内容不符合规则";
    private Bundle loginBundle;
    private boolean userNameMatch;
    private boolean passwordMatch;
    private User loginUser;//登录用户
    private Boolean registerButtonSelected;
    private Boolean loginButtonSelected;
    private SharedPreferences sharedPreferences;//键值对存储




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_register);
        loginButton=(Button)findViewById(R.id.login_button);
        returnButton=(Button)findViewById(R.id.return_button);
        registerButton=(Button)findViewById(R.id.register_button);
        progressBar=(ProgressBar)findViewById(R.id.login_progress_bar);
        userNameEdit=(EditText)findViewById(R.id.user_name_edit);
        passwordEdit=(EditText)findViewById(R.id.user_password_edit);

        initView();//初始化登陆按钮和注册按钮的显示与否

        pattern=Pattern.compile("[A-Za-z0-9]+");//不为空，数字和英文字符
        //登录按钮点击事件
        loginButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                userNameMatch=pattern.matcher(userNameEdit.getText()).matches();
                passwordMatch=pattern.matcher(passwordEdit.getText()).matches();
                if(!userNameMatch) {
                    //userNameEdit.setText(errorWarning);
                    userNameEdit.setError(errorWarning);
                    loginButtonSelected=false;//

                }else if(!passwordMatch){
                    passwordEdit.setError(errorWarning);
                }else{
                    loginButtonSelected=true;//点了登录按钮
                    //显示进度条
                    if(progressBar.getVisibility()==View.GONE) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    //在子线程等待2s
                    new Thread(runnable).start();
                }
            }
        });

        //注册按钮
        registerButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                userNameMatch=pattern.matcher(userNameEdit.getText()).matches();
                passwordMatch=pattern.matcher(passwordEdit.getText()).matches();
                if(!userNameMatch||!passwordMatch) {
                    userNameEdit.setText(errorWarning);
                    registerButtonSelected=false;

                }else{
                    registerButtonSelected=true;//点击了注册按钮
                    //显示进度条
                    if(progressBar.getVisibility()==View.GONE) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    //在子线程等待2s
                    new Thread(runnable).start();
                }
            }
        });



        //返回按钮
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //曾经登陆注册过的，设置登录状态loginState为0
                if(sharedPreferences.getString("userName",null)!=null){
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("loginState","0");
                    editor.apply();
                }


                loginBundle=new Bundle();
                loginBundle.putString("String","Return");
                Intent mainScreenIntent=new Intent(LoginOrRegister.this,MainScreen.class);
                mainScreenIntent.putExtras(loginBundle);
                startActivity(mainScreenIntent);
            }
        });
    }

    final Runnable runnable=new Runnable() {
        @Override
        public void run() {
            try{
                Thread.sleep(2000);//子线程休息2s
                handler.obtainMessage().sendToTarget();
            }catch (Exception e){
                e.printStackTrace();

            }

        }
    };

    private Handler handler=new Handler(){
        public void handleMessage(Message msg){
            //关闭进度条
            if(progressBar.getVisibility()==View.VISIBLE){
                progressBar.setVisibility(View.GONE);
            }

            //符合规则，创建用户跳转MainScreen
            if(userNameMatch&&passwordMatch){
                //保存登录名userName及登录状态loginState
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("userName",userNameEdit.getText().toString());
                editor.putString("loginState","1");
                editor.apply();

                loginUser=new User();
                loginBundle=new Bundle();
                loginUser.setUserName(userNameEdit.getText().toString());
                loginUser.setPassword(passwordEdit.getText().toString());
                if(loginButtonSelected!=null&&loginButtonSelected==true){//登录操作
                    loginUser.setOldUser(true);
                    loginBundle.putString("String","LoginSuccess");
                }else{//注册操作
                    loginUser.setOldUser(false);
                    loginBundle.putString("String","RegisterSuccess");
                }
                loginBundle.putSerializable("loginUser",loginUser);//传递用户对象（序列化）
                Intent mainScreenIntent=new Intent(LoginOrRegister.this,MainScreen.class);
                mainScreenIntent.putExtras(loginBundle);
                startActivity(mainScreenIntent);

            }
        }

    };


    //判断是否注册或登陆过SCOS，若没有，则隐藏登录按钮，若有，隐藏注册按钮，设置默认登陆名
    private void initView(){
        sharedPreferences=getSharedPreferences("SCOS",MODE_PRIVATE);//获取SCOS，没有就建立SCOS数据库
        if(sharedPreferences.getString("userName",null)==null){//找到userName,没有返回null
            loginButton.setVisibility(View.GONE);//登录按钮隐藏
        }else{//隐藏注册按钮，设置默认登录名
            registerButton.setVisibility(View.GONE);
            String userName=sharedPreferences.getString("userName",null);
            loginButton.setText(userName);
        }
    }

}
