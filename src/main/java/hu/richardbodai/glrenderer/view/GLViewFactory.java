package hu.richardbodai.glrenderer.view;

import android.content.Context;
import android.opengl.GLSurfaceView;

import hu.richardbodai.glrenderer.config.GLConfig;
import hu.richardbodai.glrenderer.renderer.GLRenderer;
import hu.richardbodai.glrenderer.renderer.GLRendererES2;
import hu.richardbodai.glrenderer.view.GLView;

/**
 * Created by richardbodai on 4/4/16.
 */
public class GLViewFactory {

    public static GLView getGLView(Context context, GLConfig config) {
        GLView glView = new GLView();
        GLRenderer renderer = null;
        GLTouchSurfaceView glSurfaceView = new GLTouchSurfaceView(context);
        switch (config.gl_version) {
            case ES_1:
                glSurfaceView.setEGLContextClientVersion(1);
                break;
            case ES_2:
                glSurfaceView.setEGLContextClientVersion(2);
                renderer = new GLRendererES2(config);
                break;
            default:
                break;
        }
        glSurfaceView.setRenderer(renderer);
        glView.setRenderer(renderer);
        glView.setGLSurfaceView(glSurfaceView);
        return glView;
    }

}
