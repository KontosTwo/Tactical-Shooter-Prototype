package com.mygdx.camera;

import com.mygdx.physics.Locatable;
import com.mygdx.physics.MyMath;
import com.mygdx.physics.MyVector2;
import com.mygdx.physics.PrecisePoint;

/**
 * Created by user on 1/3/17.
 */
 class CameraActionCreator {
    private final Camera camera;

    CameraActionCreator(Camera camera){
        this.camera = camera;
    }


    CameraStrategy createIdle(){
        return new CameraStrategy() {
            @Override
            public void begin() {

            }

            @Override
            public void update() {

            }

            @Override
            public boolean finished() {
                return false;
            }

            @Override
            public void end() {

            }
        };
    }

    CameraStrategy createFocus(Locatable foci){
        return new CameraStrategy(){
            @Override
            public void begin() {
                camera.snapTo(foci.getX(),foci.getY());
            }
            @Override
            public void update(){
                camera.snapTo(foci.getX(),foci.getY());
            }

            @Override
            public boolean finished() {
                return false;
            }

            @Override
            public void end() {

            }
        };
    }

    CameraStrategy createPanTo(Locatable target,double speed){
        return new CameraStrategy(){
            private MyVector2 velocity;


            @Override
            public void begin() {
                PrecisePoint camInitialLocation = new PrecisePoint(camera.position.x,camera.position.y);
                velocity = MyMath.findDx(new PrecisePoint(camera.position.x,camera.position.y),target);
                velocity.scale(speed);
            }
            @Override
            public void update(){
                camera.translate(velocity);
            }

            @Override
            public boolean finished() {
                return stillPan();
            }

            @Override
            public void end() {
                camera.snapTo(target.getX(),target.getY());
            }

            private boolean stillPan() {
                double futurex = camera.position.x + velocity.getX();
                double futurey = camera.position.y + velocity.getY();

                return (MyMath.sameSign(target.getX() - futurex,target.getX() - camera.position.x)
                        && MyMath.sameSign(target.getY() - futurey,target.getY() - camera.position.y));
            }
        };
    }

    interface CameraStrategy{
        public void begin();
        public void update();
        public boolean finished();
        public void end();
    }
}
