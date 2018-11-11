package es.source.code.model;

import java.io.Serializable;

/**
 * 实时数据对象
 * Created by asus on 2018-11-08.
 */

public class RealTimeFoodData implements Serializable {

    private String foodName;
    private int foodStackNum;

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getFoodStackNum() {
        return foodStackNum;
    }

    public void setFoodStackNum(int foodStackNum) {
        this.foodStackNum = foodStackNum;
    }
}
