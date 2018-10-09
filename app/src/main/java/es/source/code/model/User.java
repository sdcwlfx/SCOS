package es.source.code.model;

import java.io.Serializable;

/**
 * 用户类实现Serializable接口，可序列化传递对象
 * Created by asus on 2018-10-08.
 */

public class User implements Serializable{
    private String userName;//用户名
    private String password;//密码
    private Boolean oldUser;

    //快捷方式：空白处右键->Geneerate
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getOldUser() {
        return oldUser;
    }

    public void setOldUser(Boolean oldUser) {
        this.oldUser = oldUser;
    }
}
