package hu.richardbodai.glrenderer.renderer;

import android.opengl.GLSurfaceView;

import java.util.ArrayList;

import hu.richardbodai.glrenderer.handler.MatrixHandler;
import hu.richardbodai.glrenderer.view.GLScene;

/**
 * Created by richardbodai on 4/3/16.
 */
public abstract class GLRenderer implements GLSurfaceView.Renderer {

    protected float[] mViewMatrix = new float[16];
    protected float[] mProjectionMatrix = new float[16];
    protected float[] mModelMatrix = new float[16];

    protected GLScene mGLScene;

    protected ArrayList<GLScene> mGLScenes;

    protected MatrixHandler mMatrixHandler = new MatrixHandler();

    public MatrixHandler getMatrixHandler() {
        return mMatrixHandler;
    }

    public void setGLScenes(ArrayList<GLScene> scenes) {
        mGLScenes = scenes;
    }

    public void setGLScene(GLScene scene) {
        mGLScene = scene;
    }

    public abstract void doTranslate(float dx, float dy);

}
