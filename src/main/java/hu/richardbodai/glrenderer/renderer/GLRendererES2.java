package hu.richardbodai.glrenderer.renderer;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import hu.richardbodai.glrenderer.R;
import hu.richardbodai.glrenderer.config.GLConfig;
import hu.richardbodai.glrenderer.handler.MatrixHandler;
import hu.richardbodai.glrenderer.handler.ShaderHandler;
import hu.richardbodai.glrenderer.shape.GLShape;
import hu.richardbodai.glrenderer.util.TextureHelper;

/**
 * Created by richardbodai on 4/3/16.
 */
public class GLRendererES2 extends GLRenderer {

    /** How many bytes per float. */
    private final int mBytesPerFloat = 4;

    private float[] mMVPMatrix = new float[16];

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

    private ShaderHandler mShaderHandler;
    private int mTexCoordLoc;
    private int mSamplerLoc;

    private GLConfig glConfig;
    private int mTex1;
    private int mTex2;
    private int mDefaultProgram;

    public GLRendererES2(GLConfig glConfig) {
        this.glConfig = glConfig;
        mShaderHandler = new ShaderHandler(glConfig.vertex_shader, glConfig.fragment_shader);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // Set the background clear color to gray.
        GLES20.glClearColor(0.8f, 0.8f, 0.8f, 1.0f);

        mDefaultProgram = mShaderHandler.getDefaultProgram();
        GLConfig.defaultProgramHandle = mDefaultProgram;

        for (int i = 0; i < mGLScenes.size(); i++) {
            for (int j = 0; j < mGLScenes.get(i).getShapes().size(); j++) {
                GLShape shape = mGLScenes.get(i).getShapes().get(j);
                if (shape.hasTexture()) {
                    shape.setTextureHandle(TextureHelper.loadTexture(glConfig.context, shape.getImageId()));
                }
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
        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);

        mShaderHandler.loadShaders();
        mShaderHandler.linkShadersToProgram();
        GLConfig.textureProgramHandle = mShaderHandler.getProgramGLId();
        // Set program handles. These will later be used to pass in values to the program.
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mShaderHandler.getProgramGLId(), "u_MVPMatrix");
        mPositionHandle = GLES20.glGetAttribLocation(mShaderHandler.getProgramGLId(), "a_Position");
        mColorHandle = GLES20.glGetAttribLocation(mShaderHandler.getProgramGLId(), "a_Color");
/*        mTexCoordLoc = GLES20.glGetAttribLocation(mShaderHandler.getProgramGLId(), "a_TexCoord");
        mSamplerLoc = GLES20.glGetUniformLocation(mShaderHandler.getProgramGLId(), "s_Texture");*/
        // Tell OpenGL to use this program when rendering.
        GLES20.glUseProgram(mShaderHandler.getProgramGLId());


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
        final float near = 1.0f;
        final float far = 10.0f;

        //Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
        Matrix.orthoM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);


        for (int i = 0; i < mGLScenes.size(); i++) {
            mGLScenes.get(i).draw(mMVPMatrix, mViewMatrix, mProjectionMatrix, mMVPMatrixHandle, mTexCoordLoc, mSamplerLoc, mPositionHandle, mColorHandle);
        }
    }

    @Override
    public void doTranslate(float dx, float dy) {
        for (int i = 0; i < mGLScenes.size(); i++) {
            mGLScenes.get(i).doTranslate(dx, dy);
        }
    }
}
