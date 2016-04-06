package hu.richardbodai.glrenderer.shape;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import hu.richardbodai.glrenderer.config.GLConfig;

/**
 * Created by richardbodai on 4/4/16.
 */
public class Rectangle implements GLShape {

    Triangle triangle1;
    Triangle triangle2;
    public float uvs[];
    int mTextureHandle;
    float[] mData;
    FloatBuffer mVertexBuffer;
    int mPositionHandle;
    int mPositionDataSize;
    private FloatBuffer mTextureBuffer;
    private int mImageId;
    public boolean hasTexture = false;
    private FloatBuffer colorBuffer;
    float[] mColors;


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
        return mData.length / 3;
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
        mColors = colors;
        setColorBuffer(colors);
        return this;
    }

    @Override
    public float[] convertToGLFormat() {
        float[] data = new float[18];
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
    public float[] getColor() {
        return mColors;
    }

    @Override
    public void setTexels(float[] data) {
        uvs = data;
    }

    @Override
    public void draw(int mPositionHandle, int mTexCoordLoc, int mSamplerLoc, int colorHandel, int pMVPMatrixHandle, float[] pMVPMatrix) {
        /*int defmMVPMatrixHandle = GLES20.glGetUniformLocation(GLConfig.defaultProgramHandle, "u_MVPMatrix");
        int defmPositionHandle = GLES20.glGetAttribLocation(GLConfig.defaultProgramHandle, "a_Position");
        int defmColorHandle = GLES20.glGetAttribLocation(GLConfig.defaultProgramHandle, "a_Color");
        // Tell OpenGL to use this program when rendering.
        if (hasTexture) {
            int mMVPMatrixHandle = GLES20.glGetUniformLocation(GLConfig.textureProgramHandle, "u_MVPMatrix");
            int vmPositionHandle = GLES20.glGetAttribLocation(GLConfig.textureProgramHandle, "a_Position");
            int vmColorHandle = GLES20.glGetAttribLocation(GLConfig.textureProgramHandle, "a_Color");
            int vmTexCoordLoc = GLES20.glGetAttribLocation(GLConfig.textureProgramHandle, "a_TexCoord");
            int vmSamplerLoc = GLES20.glGetUniformLocation(GLConfig.textureProgramHandle, "s_Texture");

            GLES20.glUseProgram(GLConfig.textureProgramHandle);
            GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, pMVPMatrix, 0);
            mVertexBuffer.position(0);
            GLES20.glVertexAttribPointer(vmPositionHandle, 3, GLES20.GL_FLOAT, false,
                    0, mVertexBuffer);
            GLES20.glEnableVertexAttribArray(vmPositionHandle);
            colorBuffer.position(0);
            GLES20.glVertexAttribPointer(vmColorHandle, 4, GLES20.GL_FLOAT, false,
                    0, colorBuffer);
            GLES20.glEnableVertexAttribArray(vmColorHandle);

            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureHandle);
            GLES20.glUniform1i(vmSamplerLoc, 0);
            // Prepare the texturecoordinates
            GLES20.glVertexAttribPointer(mTexCoordLoc, 2, GLES20.GL_FLOAT,
                    false,
                    0, mTextureBuffer);
            GLES20.glEnableVertexAttribArray(mTexCoordLoc);
        }
        else {

            GLES20.glUseProgram(GLConfig.defaultProgramHandle);
            GLES20.glUniformMatrix4fv(defmMVPMatrixHandle, 1, false, pMVPMatrix, 0);
            mVertexBuffer.position(0);
            GLES20.glVertexAttribPointer(defmPositionHandle, 3, GLES20.GL_FLOAT, false,
                    0, mVertexBuffer);
            GLES20.glEnableVertexAttribArray(defmPositionHandle);
            colorBuffer.position(0);
            GLES20.glVertexAttribPointer(defmColorHandle, 4, GLES20.GL_FLOAT, false,
                    0, colorBuffer);
            GLES20.glEnableVertexAttribArray(defmColorHandle);
        }*/

        GLES20.glUniformMatrix4fv(pMVPMatrixHandle, 1, false, pMVPMatrix, 0);
        mVertexBuffer.position(0);
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false,
                0, mVertexBuffer);
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        colorBuffer.position(0);
        GLES20.glVertexAttribPointer(colorHandel, 4, GLES20.GL_FLOAT, false,
                0, colorBuffer);
        GLES20.glEnableVertexAttribArray(colorHandel);



        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, getVertexCount());
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
