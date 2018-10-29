package es.source.code.util;

import android.util.Log;

import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.AuthenticationFailedException;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import static android.content.ContentValues.TAG;

/**
 * https://blog.csdn.net/tjliqy/article/details/52075576?utm_source=blogxgwz1
 * https://blog.csdn.net/thatluck/article/details/51675621
 * 通过代码用smtp的方式登陆发送邮箱send@host.com在通过这个邮箱发送邮件给get@tohost.com
 发送邮件主要包括3个部分：创建连接，创建邮件体，发送邮件。
 * Created by asus on 2018-10-23.
 */

public class MailSenderAuth extends Authenticator {
    private String userName=null;//用户名
    private String password=null;//密码
    private String mailHost="smtp.163.com";//服务器地址
    private Session session;//会话
    private Properties properties;//连接属性

    public MailSenderAuth(){

    }

    public MailSenderAuth(String userName,String password){
        this.userName=userName;
        this.password=password;
        this.properties=new Properties();//初始化连接属性
        properties.setProperty("mail.transport.protocol","SMTP");//设置通信协议
        properties.setProperty("mail.smtp.host",mailHost);//设置发送邮箱服务器地址
        properties.put("mail.smtp.auth","true");//设置是否需要SMTP身份验证
        properties.put("mail.smtp.port","25");
        session=session.getInstance(properties,this);
    }

    protected PasswordAuthentication getPasswordAuthentication(){
        return new PasswordAuthentication(userName,password);
    }

    //邮件标题、内容、发送方邮箱地址、发送方密码
    public synchronized void sendMail(String title,String body,String senderAdd,String recipients){
        try{
            //创建邮件体
            MimeMessage mimeMessage=new MimeMessage(session);
            DataHandler handler=new DataHandler(new ByteArrayDataSource(body.getBytes(),"text/plain"));//内容转成二进制
            mimeMessage.setSender(new InternetAddress(senderAdd));//设置发送方邮件地址
            mimeMessage.setSubject(title);//邮件标题
            mimeMessage.setSentDate(new Date());//发送时间
            mimeMessage.setDataHandler(handler);
            if(password.indexOf(',')>0){//若有多个接受方地址
                mimeMessage.setRecipients(Message.RecipientType.TO,InternetAddress.parse(recipients));

            }else {//有一个接受方地址
                mimeMessage.setRecipient(Message.RecipientType.TO,new InternetAddress(recipients));
            }
            mimeMessage.saveChanges();

            try {
                session.setDebug(true);
                Transport transport=session.getTransport("smtp");//创建连接
                transport.connect(mailHost,userName,password);//连接服务器
                transport.sendMessage(mimeMessage,mimeMessage.getRecipients(Message.RecipientType.TO));//发送邮件
                transport.close();//关闭连接
            }catch (AuthenticationFailedException e){
                e.printStackTrace();
            }catch (MessagingException mex){
                mex.printStackTrace();


            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
