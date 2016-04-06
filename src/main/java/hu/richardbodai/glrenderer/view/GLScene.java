package hu.richardbodai.glrenderer.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.shapes.Shape;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import hu.richardbodai.glrenderer.shape.GLShape;

/**
 * Created by richardbodai on 4/4/16.
 */
public class GLScene {

    private final int BYTES_PER_FLOAT = 4;
    private FloatBuffer mVertexBuffer;
    private FloatBuffer mTextureBuffer;
    private float[] mData;
    private float[] mUVsData;
    private float[] mModelMatrix = new float[16];
    private float[] mTranslateMatrix = new float[16];
    private ITransformation mTranformationInterface;
    private Context mContext;
    private int mId;
    ArrayList<GLShape> shapes;

    public GLScene() {
        mVertexBuffer = null;
        mTextureBuffer = null;
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.setIdentityM(mTranslateMatrix, 0);
        mTranformationInterface = new ITransformation() {
            @Override
            public void onTranslate(float dx, float dy, float[] translationMatrix) {
                Matrix.translateM(translationMatrix, 0, dx, dy, 0.0f);
            }
        };
    }

    public ArrayList<GLShape> getShapes() {
        return shapes;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public void setImage(int id) {
        mId = id;
    }

    public GLScene(ITransformation customTransfomration) {
        mVertexBuffer = null;
        mTextureBuffer = null;
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.setIdentityM(mTranslateMatrix, 0);
        mTranformationInterface = customTransfomration;
    }

    public void setCustomTransformationInterface(ITransformation customTransformationInterface) {
        mTranformationInterface = customTransformationInterface;
    }

    public void setVertexBuffer(float[] data) {
        mData = data;
        mVertexBuffer = ByteBuffer.allocateDirect(data.length * BYTES_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mVertexBuffer.put(data).position(0);
    }

    public void setTextureBuffer(float[] data) {
        mUVsData = data;
        mTextureBuffer = ByteBuffer.allocateDirect(data.length * BYTES_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mTextureBuffer.put(data).position(0);
    }

    public FloatBuffer getVertexBuffer() {
        return mVertexBuffer;
    }

    public int getVertexCount() {
        return mData.length / 7;
    }

    public void addShapes(ArrayList<GLShape> shapes) {
        this.shapes = shapes;
        ArrayList<Float> stockData = new ArrayList<>();
        for (int i = 0; i < shapes.size(); i++) {
            float[] vertices = shapes.get(i).convertToGLFormat();
            for (int j = 0; j < vertices.length; j++) {
                stockData.add(vertices[j]);
            }
        }
        float[] data = new float[stockData.size()];
        int i = 0;
        for (Float f : stockData) {
            data[i++] = (f != null ? f : Float.NaN);
        }
        setVertexBuffer(data);
    }

    public void draw(float[] pMVPMatrix, float[] pViewMatrix, float[] pProjectionMatrix, int pMVPMatrixHandle, int pTexCoordLoc, int pSamplerLoc, int pPositionHandle, int colorHandel) {
        // Pass in the position information

        // This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(pMVPMatrix, 0, pViewMatrix, 0, mModelMatrix, 0);

        Matrix.multiplyMM(pMVPMatrix, 0, pMVPMatrix, 0, mTranslateMatrix, 0);

        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).
        Matrix.multiplyMM(pMVPMatrix, 0, pProjectionMatrix, 0, pMVPMatrix, 0);
        GLES20.glUniformMatrix4fv(pMVPMatrixHandle, 1, false, pMVPMatrix, 0);

        for (int i = 0; i < shapes.size(); i++) {
            shapes.get(i).draw(pPositionHandle, pTexCoordLoc, pSamplerLoc, colorHandel);
        }


        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
    }

    public void addShapesTexture(ArrayList<GLShape> shapes) {
        ArrayList<Float> stockData = new ArrayList<>();
        for (int i = 0; i < shapes.size(); i++) {
            float[] texels = shapes.get(i).getTexels();
            for (int j = 0; j < texels.length; j++) {
                stockData.add(texels[j]);
            }
        }
        float[] data = new float[stockData.size()];
        int i = 0;
        for (Float f : stockData) {
            data[i++] = (f != null ? f : Float.NaN);
        }
        setTextureBuffer(data);
    }

    void initTexture() {

    }

    public void doTranslate(float dx, float dy) {
        mTranformationInterface.onTranslate(dx, dy, mTranslateMatrix);
    }

    public FloatBuffer getTextureBuffer() {
        return mTextureBuffer;
    }
}
