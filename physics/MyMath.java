package com.mygdx.physics;

/**
 * Created by user on 1/8/17.
 */
public class MyMath {
    public static MyVector2 findDx(Locatable start,Locatable end){
        double dx = end.getX() - start.getX();
        double dy = end.getY() - start.getY();
        double hyp = Math.sqrt(dx*dx + dy*dy);
        return new MyVector2(dx/hyp,dy/hyp);
    }
    public static boolean sameSign(double x1,double x2){
        if(x1 == 0 && x2 == 0) {
            return true;
        }
        else {
            return x1*x2 > 0;
        }
    }
}
