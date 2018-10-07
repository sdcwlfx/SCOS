package es.source.code.util;

import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * 触摸屏处理事件
 * https://blog.csdn.net/u014646004/article/details/50816275
 * Created by asus on 2018-10-05.
 */

public class MyGestureListener implements GestureDetector.OnGestureListener {
    private MyRightLeftListener listener;
    private float distanceX;//水平移动的绝对值距离
    private float distance=100;//规定移动距离大于该值时才触发滑动事件


    public MyGestureListener(MyRightLeftListener listener){
        this.listener=listener;
    }

    /**
     *手在屏幕上滑动过程中触发
     */
    @Override
    public boolean onScroll(MotionEvent e1,MotionEvent e2,float distanceX,float distanceY){
        return false;
    }

    /**
     * 触摸屏按下，马上触发事件
     * @param e
     * @return
     */
    @Override
    public boolean onDown(MotionEvent e){
        return false;
    }

    /**
     * 点击触摸屏，但没有移动和弹起动作
     * @param e
     * onShowPress和onDown的区别在于：onDown在触摸屏按下就会触发，而onShowPres在触摸屏按下一段时间内，若没有移动鼠标和弹起事件才会触发
     */
    @Override
    public void onShowPress(MotionEvent e){
    }

    /**
     * 轻击触摸屏后弹起触发事件，但若触发了onLongPress、onScroll或onFling事件，
     * 就不会触发该事件
     * @param e
     * @return
     */
    @Override
    public boolean onSingleTapUp(MotionEvent e){
        return false;
    }

    /**
     * 长按屏幕触发事件
     * @param e
     */
    @Override
    public void onLongPress(MotionEvent e){

    }

    /**
     * 在屏幕滑动后手离开屏幕时触发
     * @param e1
     * @param e2
     * @param velocityX
     * @param velocityY
     * @return
     */
    @Override
    public boolean onFling(MotionEvent e1,MotionEvent e2,float velocityX,float velocityY){
        //按下时的（x,y）坐标
        float startX=e1.getX();
        float startY=e1.getY();
        //抬起时的(x,y)坐标
        float endX=e2.getX();
        float endY=e2.getY();

        //水平方向移动的绝对值距离
        distanceX=Math.abs(startX-endX);
        //水平方向移动距离大于规定值
        if(distanceX>distance){
            //从右向左滑动
            if(startX>endX){
                listener.onLeft();
            }
        }
        return false;
    }
}
