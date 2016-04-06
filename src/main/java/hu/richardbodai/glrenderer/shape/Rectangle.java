package hu.richardbodai.glrenderer.shape;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by richardbodai on 4/4/16.
 */
public class Rectangle implements GLShape {

    Triangle triangle1;
    Triangle triangle2;
    public static float uvs[];
    int mTextureHandle;
    float[] mData;
    FloatBuffer mVertexBuffer;
    int mPositionHandle;
    int mPositionDataSize;
    private FloatBuffer mTextureBuffer;
    private int mImageId;
    public boolean hasTexture = false;
    private FloatBuffer colorBuffer;


    public Rectangle(float upperLeftX, float upperLeftY, float downerRightX, float downerRightY) {
        triangle1 = new Triangle(upperLeftX, upperLeftY, upperLeftX, downerRightY, downerRightX, downerRightY);
        triangle2 = new Triangle(upperLeftX, upperLeftY, downerRightX, downerRightY, downerRightX, upperLeftY);
        uvs = new float[] {
                0.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 1.0f,
                0.0f, 0.0f,
                1.0f, 1.0f,
                1.0f, 0.0f
        };
        setVertexBuffer(convertToGLFormat());
        setTextureBuffer(uvs);
        setColor(1,1,1,1);
    }

    public int getVertexCount() {
        return mData.length / 7;
    }

    public Rectangle setColor(float r, float g, float b, float a) {
        triangle1.setColor(r, g, b, a);
        triangle2.setColor(r, g, b, a);
        float colors[] = {
                r,g,b,a,
                r,g,b,a,
                r,g,b,a,
                r,g,b,a,
                r,g,b,a,
                r,g,b,a
        };
        setColorBuffer(colors);
        return this;
    }

    @Override
    public float[] convertToGLFormat() {
        float[] data = new float[42];
        float[] data1 = triangle1.convertToGLFormat();
        float[] data2 = triangle2.convertToGLFormat();
        for (int i = 0; i < data1.length; i++) {
            data[i] = data1[i];
        }
        for (int i = 0; i < data2.length; i++) {
            data[data1.length+i] = data2[i];
        }
        return data;
    }

    public void setVertexBuffer(float[] data) {
        mData = data;
        mVertexBuffer = ByteBuffer.allocateDirect(data.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mVertexBuffer.put(data).position(0);
    }

    public void setTextureBuffer(float[] data) {
        mTextureBuffer = ByteBuffer.allocateDirect(data.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mTextureBuffer.put(data).position(0);
    }

    public void setColorBuffer(float[] data) {
        colorBuffer = ByteBuffer.allocateDirect(data.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        colorBuffer.put(data).position(0);
    }
    
    public void setTextureImage(int id) {
        mImageId = id;
        hasTexture = true;
    }

    public int getImageId() {
        return mImageId;
    }

    @Override
    public float[] getTexels() {
        return uvs;
    }

    @Override
    public void draw(int mPositionHandle, int mTexCoordLoc, int mSamplerLoc, int colorHandel) {
        mVertexBuffer.position(0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false,
                0, mVertexBuffer);

        GLES20.glEnableVertexAttribArray(mPositionHandle);

        colorBuffer.position(0);
        GLES20.glVertexAttribPointer(colorHandel, 4, GLES20.GL_FLOAT, false,
                0, colorBuffer);
        GLES20.glEnableVertexAttribArray(colorHandel);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureHandle);
        GLES20.glUniform1i(mSamplerLoc, 0);

        // Prepare the texturecoordinates
        GLES20.glVertexAttribPointer ( mTexCoordLoc, 2, GLES20.GL_FLOAT,
                false,
                0, mTextureBuffer);

        GLES20.glEnableVertexAttribArray(mTexCoordLoc);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
    }

    @Override
    public boolean hasTexture() {
        return hasTexture;
    }

    @Override
    public void setTextureHandle(int t) {
        mTextureHandle = t;
    }


}
