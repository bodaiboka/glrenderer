package hu.richardbodai.glrenderer.renderer;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.ByteBuffer;
import java.util.HashMap;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import hu.richardbodai.glrenderer.config.GLConfig;
import hu.richardbodai.glrenderer.handler.MatrixHandler;
import hu.richardbodai.glrenderer.handler.ShaderHandler;
import hu.richardbodai.glrenderer.handler.ShaderManager;
import hu.richardbodai.glrenderer.handler.ShaderProgram;
import hu.richardbodai.glrenderer.shape.GLShape;
import hu.richardbodai.glrenderer.util.TextureHelper;
import hu.richardbodai.glrenderer.util.text.TextManager;
import hu.richardbodai.glrenderer.util.text.TextObject;

/**
 * Created by richardbodai on 4/3/16.
 */
public class GLRendererES2 extends GLRenderer {

    /** How many bytes per float. */
    private final int mBytesPerFloat = 4;

    private float[] mMVPMatrix = new float[16];

    private int[] mViewPortArray;

    /** This will be used to pass in the transformation matrix. */
    private int mMVPMatrixHandle;

    /** This will be used to pass in model position information. */
    private int mPositionHandle;

    /** This will be used to pass in model color information. */
    private int mColorHandle;

    /** How many elements per vertex. */
    private final int mStrideBytes = 7 * mBytesPerFloat;

    /** Offset of the position data. */
    private final int mPositionOffset = 0;

    /** Size of the position data in elements. */
    private final int mPositionDataSize = 3;

    /** Offset of the color data. */
    private final int mColorOffset = 3;

    /** Size of the color data in elements. */
    private final int mColorDataSize = 4;

    float	mScreenWidth = 1280;
    float	mScreenHeight = 768;
    float 	ssu = 1.0f;
    float 	ssx = 1.0f;
    float 	ssy = 1.0f;
    float 	swp = 320.0f;
    float 	shp = 480.0f;



    private GLConfig glConfig;


    TextManager textManager = new TextManager();

    public GLRendererES2(GLConfig glConfig) {
        this.glConfig = glConfig;
    }

    private OnClicklistener onClicklistener;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // Set the background clear color to gray.
        GLES20.glClearColor(0.8f, 0.8f, 0.8f, 0.0f);
        //TextureHelper.loadTexture(glConfig.context, glConfig.getTextureImageId);

        GLES20.glDisable(GLES20.GL_DITHER);

        GLES20.glEnable( GLES20.GL_DEPTH_TEST );
        GLES20.glDepthFunc( GLES20.GL_LEQUAL );
        GLES20.glDepthMask( true );

        HashMap<String, Integer> textureResources = new HashMap<>();
        textureResources.put("icons", glConfig.getTextureImageId);
        textureResources.put("text", glConfig.getTextureTextId);
        TextureHelper.loadTextures(glConfig.context, textureResources);
        onClicklistener = glConfig.getOnClicklistener();

        SetupScaling();
        SetupText();

        for (int i = 0; i < mGLScenes.size(); i++) {
            if (mGLScenes.get(i) instanceof TextManager) {
                ((TextManager)(mGLScenes).get(i)).creatShaderProgram();
                ((TextManager)(mGLScenes).get(i)).setTextureID(TextureHelper.getTextureId("text"));
                ((TextManager)(mGLScenes).get(i)).setUniformscale(1.0f);
                ((TextManager)(mGLScenes).get(i)).PrepareDraw();
            }
        }

        // Position the eye behind the origin.
        final float eyeX = 0.0f;
        final float eyeY = 0.0f;
        final float eyeZ = 1.5f;

        // We are looking toward the distance
        final float lookX = 0.0f;
        final float lookY = 0.0f;
        final float lookZ = -5.0f;

        // Set our up vector. This is where our head would be pointing were we holding the camera.
        final float upX = 0.0f;
        final float upY = 1.0f;
        final float upZ = 0.0f;

        // Set the view matrix. This matrix can be said to represent the camera position.
        // NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
        // view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.
        //Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);
        for (int i = 0; i < mGLScenes.size(); i++) {
            mGLScenes.get(i).createShader();
        }
        ShaderManager.linkAllShader();



    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // Set the OpenGL viewport to the same size as the surface.
        GLES20.glViewport(0, 0, width, height);
        // Create a new perspective projection matrix. The height will stay the same
        // while the width will vary as per aspect ratio.
        final float ratio = (float) width / height;
        final float left = 0;
        final float right = width;
        final float bottom = height;
        final float top = 0;
        final float near = 0.1f;
        final float far = 10.0f;

        mViewPortArray = new int[4];
        mViewPortArray[0] = 0;
        mViewPortArray[1] = 0;
        mViewPortArray[2] = width;
        mViewPortArray[3] = height;

        //Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
        Matrix.orthoM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }


    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClearDepthf(1.0f);

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        if (onClick) {
            Log.i("renderer", "onclick");
            Matrix.setIdentityM(mViewMatrix, 0);
            Matrix.setIdentityM(mModelMatrix, 0);
            Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
            for (int i = 0; i < mGLScenes.size(); i++) {
                mGLScenes.get(i).drawOnBackbuffer(mMVPMatrix, mViewMatrix, mProjectionMatrix);
            }
            doSelect(actX, actY);
        }


        onClick = false;
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        Matrix.setIdentityM(mViewMatrix, 0);
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
        textManager.draw(mMVPMatrix, mViewMatrix, mProjectionMatrix);

        for (int i = 0; i < mGLScenes.size(); i++) {
            mGLScenes.get(i).draw(mMVPMatrix, mViewMatrix, mProjectionMatrix);
        }



        GLES20.glFlush();
    }

    @Override
    public void doTranslate(float dx, float dy) {
        for (int i = 0; i < mGLScenes.size(); i++) {
            mGLScenes.get(i).doTranslate(dx, dy);
        }
        textManager.doTranslate(dx, dy);
    }

    public void SetupText()
    {
        // Create our text manager
        textManager = glConfig.textManager;

        textManager.creatShaderProgram();
        textManager.setTextureID(TextureHelper.getTextureId("text"));
        textManager.setUniformscale(1.0f);
        textManager.PrepareDraw();

    }

    public void SetupScaling()
    {
        // The screen resolutions
        swp = (int) (glConfig.context.getResources().getDisplayMetrics().widthPixels);
        shp = (int) (glConfig.context.getResources().getDisplayMetrics().heightPixels);

        // Orientation is assumed portrait
        ssx = swp / 320.0f;
        ssy = shp / 480.0f;

        // Get our uniform scaler
        if(ssx > ssy)
            ssu = ssy;
        else
            ssu = ssx;
    }



    public void doSelect(float actX, float actY) {
        int x = Math.round(actX);
        int y = Math.round(actY);
        ByteBuffer pixel;
        pixel = ByteBuffer.allocate(4);
        GLES20.glReadPixels(x, mViewPortArray[3]-y,1,1, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, pixel);
        Log.i("DO_SELECT", "r: " + (pixel.get(0) & 0xFF) + " g: " + (pixel.get(1) & 0xFF)+ " b: " + (pixel.get(2) & 0xFF));
        if (onClicklistener != null) {
            onClicklistener.onClick(pixel.array());
        }
    }


}
