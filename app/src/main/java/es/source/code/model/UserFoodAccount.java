package es.source.code.model;

import java.sql.Date;
import java.util.List;

/**
 * 用户就餐生成账单
 * Created by asus on 2018-10-08.
 */

public class UserFoodAccount {
    private String account;//用户账号
    private String foodName;//食物
    private int number;//数量
    private String date;//结账时间
    private String remarks;//备注

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
