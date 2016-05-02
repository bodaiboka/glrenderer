package hu.richardbodai.glrenderer.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import hu.richardbodai.glrenderer.renderer.GLRenderer;

/**
 * Created by richardbodai on 4/4/16.
 */
public class GLTouchSurfaceView extends GLSurfaceView {

    private final float TOUCH_SCALE_FACTOR = 1;
    private GLRenderer mRenderer;
    private float mPreviousX;
    private float mPreviousY;


    Timer timerForTracking;
    Timer timerForAcceleration;
    float prevY;
    float actualY;
    float prevX;
    float actualX;

    int n = 0;

    float time_scale = 0.01f;
    float accel = 1000.0f;

    float speedY;
    float speedX;
    enum DirectionY {
        UP, DOWN
    }

    enum DirectionX {
        LEFT, RIGHT
    }

    DirectionY directionY;
    DirectionX directionX;

    public GLTouchSurfaceView(Context context) {
        super(context);
    }

    @Override
    public void setRenderer(Renderer renderer) {
        super.setRenderer(renderer);
        mRenderer = (GLRenderer)renderer;
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        actualY = y;
        actualX = x;
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                Log.i("nn", "down");
                timerForTracking = new Timer();
                timerForTracking.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        track();
                    }
                }, new Date(), 10);
                break;

            case MotionEvent.ACTION_UP:
                Log.i("nn", "up");
                timerForTracking.cancel();
                track();
                timerForAcceleration = new Timer();
                n = 0;
                timerForAcceleration.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (directionY == DirectionY.DOWN) {
                            accelerateUp();
                        }
                        else {
                            accelerateDown();
                        }

                        if (directionX == DirectionX.RIGHT) {
                            accelerateLeft();
                        }
                        else {
                            accelerateRight();
                        }
                        requestRender();
                        checkSpeed();
                    }
                }, new Date(), 10);
                break;

            case MotionEvent.ACTION_MOVE:
                float dx = (x - mPreviousX) * TOUCH_SCALE_FACTOR;
                float dy = (y - mPreviousY) * TOUCH_SCALE_FACTOR;

                mRenderer.doTranslate(dx, dy);
                requestRender();
                break;
        }

        mPreviousX = x;
        mPreviousY = y;
        return true;
    }


    private void track() {
        if (n == 0) {
            prevY = actualY;
            prevX = actualX;
        }
        float s = actualY - prevY;
        float v = s / 0.01f;
        Log.i("Task", "track prevY = " + mPreviousY + " actY = " + actualY + " v = " + v);
        prevY = actualY;
        n++;
        speedY = v;
        if (speedY < 0) {
            directionY = DirectionY.UP;
        }
        else {
            directionY = DirectionY.DOWN;
        }

        s = actualX - prevX;
        v = s / 0.01f;
        Log.i("Task", "track prevX = " + mPreviousY + " actX = " + actualY + " v = " + v);
        prevX = actualX;
        n++;
        speedX = v;
        if (speedX < 0) {
            directionX = DirectionX.LEFT;
        }
        else {
            directionX = DirectionX.RIGHT;
        }
    }

    private void accelerateDown() {

        speedY += accel * time_scale;
        float dy = speedY * time_scale;
        if (speedY < 0) {
            mRenderer.doTranslate(0, dy);
        }
        else {
            speedY = 0;
        }
        Log.i("Acceleration", "accdown");
    }

    private void accelerateUp() {
        float acc = -1.0f * accel;


        speedY += acc * time_scale;
        float dy = speedY * time_scale;
        if (speedY > 0) {
            mRenderer.doTranslate(0, dy);
        }
        else {
            speedY = 0;
        }
        Log.i("Acceleration", "accup");
    }

    private void accelerateRight() {

        speedX += accel * time_scale;
        float dx = speedX * time_scale;
        if (speedX < 0) {
            mRenderer.doTranslate(dx, 0);
        }
        else {
            speedX = 0;
        }
        Log.i("Acceleration", "accdown");
    }

    private void accelerateLeft() {
        float acc = -1.0f * accel;

        speedX += acc * time_scale;
        float dx = speedX * time_scale;
        if (speedX > 0) {
            mRenderer.doTranslate(dx, 0);
        }
        else {
            speedX = 0;
        }
        Log.i("Acceleration", "accup");
    }

    private void checkSpeed() {
        if (speedX == 0 && speedY == 0) {
            timerForAcceleration.cancel();
        }
    }
}
