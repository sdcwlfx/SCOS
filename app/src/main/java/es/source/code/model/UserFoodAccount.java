package es.source.code.model;

import java.sql.Date;
import java.util.List;

/**
 * 用户就餐账单
 * Created by asus on 2018-10-08.
 */

public class UserFoodAccount {
    private User user;
    private Food food;//食物
    private int number;//数量
    private String date;//结账时间
    private String remarks;//备注

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
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
