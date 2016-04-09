package hu.richardbodai.glrenderer.view;

import android.opengl.GLSurfaceView;

import java.util.ArrayList;

import hu.richardbodai.glrenderer.renderer.GLRenderer;

/**
 * Created by richardbodai on 4/4/16.
 */
public class GLView {
    private GLSurfaceView mGLSurfaceView;
    private GLRenderer mRenderer;
    private GLScene mGLScene;

    public GLView() {
        mGLScene = new GLScene();
    }

    public GLSurfaceView getGLSurfaceView() {
        return mGLSurfaceView;
    }

    public void setGLSurfaceView(GLSurfaceView mGLSurfaceView) {
        this.mGLSurfaceView = mGLSurfaceView;
    }

    public GLSurfaceView.Renderer getRenderer() {
        return mRenderer;
    }

    public void setRenderer(GLRenderer mRenderer) {
        this.mRenderer = mRenderer;
        mRenderer.setGLScene(mGLScene);
    }

    public GLScene getGLScene() {
        return mGLScene;
    }

    public void setGLScenes(ArrayList<GLScene> scenes) {
        for (int i = 0; i < scenes.size(); i++) {
            scenes.get(i).setBuffers();
        }
        mRenderer.setGLScenes(scenes);
    }
}
