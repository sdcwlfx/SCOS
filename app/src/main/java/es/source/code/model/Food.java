package es.source.code.model;

import java.io.Serializable;

/**
 * 菜类：图片、菜名、价格
 * Created by asus on 2018-10-08.
 */

// TODO: 2018-10-09 菜品按分类分四种存储到数据库 
public class Food implements Serializable{
    private int foodImgId;//菜图片
    private String foodName;//菜名
    private String foodPrice;//菜价格


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

    public String getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(String foodPrice) {
        this.foodPrice = foodPrice;
    }
}
