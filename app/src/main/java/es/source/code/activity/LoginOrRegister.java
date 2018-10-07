package es.source.code.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.source.code.R;

public class LoginOrRegister extends AppCompatActivity {
    private ProgressBar progressBar;//登录进度
    private Button loginButton;//登录按钮
    private Button returnButton;//返回按钮
    private EditText passwordEdit;//密码
    private EditText userNameEdit;//登录名
    private Pattern pattern;//正则对象
    private Matcher matcher;//正则匹配
    private String errorWarning="输入内容不符合规则";
    private Bundle loginBundle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_register);
        loginButton=(Button)findViewById(R.id.login_button);
        returnButton=(Button)findViewById(R.id.return_button);
        progressBar=(ProgressBar)findViewById(R.id.login_progress_bar);
        userNameEdit=(EditText)findViewById(R.id.user_name_edit);
        passwordEdit=(EditText)findViewById(R.id.user_password_edit);


        pattern=Pattern.compile("[A-Za-z0-9]+");//不为空，数字和英文字符
        //登录按钮点击事件
        loginButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                boolean userNameMatch=pattern.matcher(userNameEdit.getText()).matches();
                boolean passwordMatch=pattern.matcher(passwordEdit.getText()).matches();
                //显示进度条
                if(progressBar.getVisibility()==View.GONE) {
                    progressBar.setVisibility(View.VISIBLE);
                }
                if(!userNameMatch){
                    userNameEdit.setText(errorWarning);
                }
                if (!passwordMatch){
                    passwordEdit.setText(errorWarning);
                }
                //关闭进度条
                if(progressBar.getVisibility()==View.VISIBLE){
                    progressBar.setVisibility(View.GONE);
                }
                //符合规则，跳转MainScreen
                if(userNameMatch&&passwordMatch){
                    loginBundle=new Bundle();
                    loginBundle.putString("String","LoginSuccess");
                    Intent mainScreenIntent=new Intent(LoginOrRegister.this,MainScreen.class);
                    mainScreenIntent.putExtras(loginBundle);
                    startActivity(mainScreenIntent);

                }

            }
        });

        //返回按钮
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginBundle=new Bundle();
                loginBundle.putString("String","Return");
                Intent mainScreenIntent=new Intent(LoginOrRegister.this,MainScreen.class);
                mainScreenIntent.putExtras(loginBundle);
                startActivity(mainScreenIntent);
            }
        });
    }

}
