package hu.richardbodai.glrenderer.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.view.View;

import hu.richardbodai.glrenderer.renderer.GLRenderer;

/**
 * Created by richardbodai on 4/4/16.
 */
public class GLTouchSurfaceView extends GLSurfaceView {

    private final float TOUCH_SCALE_FACTOR = 1;
    private GLRenderer mRenderer;
    private float mPreviousX;
    private float mPreviousY;

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

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dx = (x - mPreviousX) * TOUCH_SCALE_FACTOR;
                float dy = (y - mPreviousY) * TOUCH_SCALE_FACTOR;

                mRenderer.getMatrixHandler().setMove(dx, dy);
                requestRender();
                break;
        }

        mPreviousX = x;
        mPreviousY = y;
        return true;
    }
}
