package es.source.code.model;

import java.lang.ref.PhantomReference;

/**
 * 用户对应食物
 * Created by asus on 2018-10-08.
 */
//此无需存储到数据库，和所有加载菜品比较显示“点菜”还是“退点”，可维护一个List暂时存储，用户退出应用时数据消失，结过账的存储到数据库
public class CurrentUserFood {
    private Food food;//食物
    private int number;//数量
    private int totalPrice;//该食物价钱
    private int state;//状态 1：已点（未下单可选择退点）  0：未点或退点  2：已下单（未结账）
    private String remarks;//备注


    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
