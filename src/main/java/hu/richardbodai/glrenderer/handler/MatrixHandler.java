package hu.richardbodai.glrenderer.handler;

import android.opengl.Matrix;

/**
 * Created by richardbodai on 4/4/16.
 */
public class MatrixHandler {

    protected float[] mViewMatrix = new float[16];
    protected float[] mProjectionMatrix = new float[16];
    protected float[] mModelMatrix = new float[16];
    protected float[] mTranslateMatrix = new float[16];

    protected float[] mTranslateMatrixOnly_X = new float[16];
    protected float[] mTranslateMatrixOnly_Y = new float[16];

    public MatrixHandler() {
        Matrix.setIdentityM(mTranslateMatrix, 0);
        Matrix.setIdentityM(mTranslateMatrixOnly_X, 0);
        Matrix.setIdentityM(mTranslateMatrixOnly_Y, 0);
    }

    public void setMove(float dx, float dy) {
        Matrix.translateM(mTranslateMatrix, 0, dx, dy, 0.0f);
        Matrix.translateM(mTranslateMatrixOnly_X, 0, dx, 0.0f, 0.0f);
        Matrix.translateM(mTranslateMatrixOnly_Y, 0, 0.0f, dy, 0.0f);
    }

    public float[] getTranslateMatrix() {
        return mTranslateMatrix;
    }

    public float[] getTranslateMatrixOnly_X() {
        return mTranslateMatrixOnly_X;
    }

    public float[] getTranslateMatrixOnly_Y() {
        return mTranslateMatrixOnly_Y;
    }
}
