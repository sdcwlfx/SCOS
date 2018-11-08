package es.source.code.model;

import java.io.Serializable;

/**
 * 菜类：图片、菜名、价格、库存量
 * Created by asus on 2018-10-08.
 */

// TODO: 2018-10-09 菜品按分类分四种存储到数据库 
public class Food implements Serializable{
    private int foodImgId;//菜图片
    private String foodName;//菜名
    private int foodPrice;//菜价格
    private int foodStackNum;//库存量
    private String foodCategory;//类别


    public int getFoodImgId() {
        return foodImgId;
    }

    public void setFoodImgId(int foodImgId) {
        this.foodImgId = foodImgId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(int foodPrice) {
        this.foodPrice = foodPrice;
    }

    public int getFoodStackNum() {
        return foodStackNum;
    }

    public void setFoodStackNum(int foodStackNum) {
        this.foodStackNum = foodStackNum;
    }

    public String getFoodCategory() {
        return foodCategory;
    }

    public void setFoodCategory(String foodCategory) {
        this.foodCategory = foodCategory;
    }
}
