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

import hu.richardbodai.glrenderer.config.GLConfig;
import hu.richardbodai.glrenderer.handler.ShaderManager;
import hu.richardbodai.glrenderer.handler.ShaderProgram;
import hu.richardbodai.glrenderer.shape.GLShape;
import hu.richardbodai.glrenderer.util.TextureHelper;
import hu.richardbodai.glrenderer.util.text.TextObject;

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
    boolean hasTexture = false;
    ArrayList<GLShape> shapes;
    FloatBuffer colorBuffer;
    ShaderProgram shaderProgram;
    private int shaderProgramId;

    ArrayList<String> attribs = new ArrayList<>();
    ArrayList<String> uniforms = new ArrayList<>();
    private FloatBuffer backBufferColorBuffer;

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

        attribs.add("a_Position");
        attribs.add("a_TexCoord");
        attribs.add("a_Color");
        uniforms.add("u_MVPMatrix");
        uniforms.add("s_Texture");

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

        attribs.add("a_Position");
        attribs.add("a_TexCoord");
        attribs.add("a_Color");
        uniforms.add("u_MVPMatrix");
        uniforms.add("s_Texture");
    }

    public void createShader() {
        shaderProgram = ShaderManager.createShaderProgram(GLConfig.vs_Image, GLConfig.fs_Image, attribs, uniforms);
        shaderProgramId = shaderProgram.getmProgramId();
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

    public void setColorBuffer(float[] data) {
        colorBuffer = ByteBuffer.allocateDirect(data.length * BYTES_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer();
        colorBuffer.put(data).position(0);
    }

    public void setBackBufferColorBuffer(float[] data) {
        backBufferColorBuffer = ByteBuffer.allocateDirect(data.length * BYTES_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer();
        backBufferColorBuffer.put(data).position(0);
    }

    public int getVertexCount() {
        return mData.length / 3;
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

    public boolean isHasTexture() {
        return hasTexture;
    }

    public void setHasTexture(boolean hasTexture) {
        this.hasTexture = hasTexture;
    }

    public int getShaderProgram() {
        return shaderProgramId;
    }

    public void draw(float[] pMVPMatrix, float[] pViewMatrix, float[] pProjectionMatrix) {

        GLES20.glUseProgram(shaderProgram.getmProgramId());

        Matrix.multiplyMM(pMVPMatrix, 0, pViewMatrix, 0, mModelMatrix, 0);
        Matrix.multiplyMM(pMVPMatrix, 0, pMVPMatrix, 0, mTranslateMatrix, 0);
        Matrix.multiplyMM(pMVPMatrix, 0, pProjectionMatrix, 0, pMVPMatrix, 0);

        int pMVPMatrixHandle = GLES20.glGetUniformLocation(shaderProgram.getmProgramId(), "u_MVPMatrix");
        int pPositionHandle = GLES20.glGetAttribLocation(shaderProgram.getmProgramId(), "a_Position");
        int colorHandle = GLES20.glGetAttribLocation(shaderProgram.getmProgramId(), "a_Color");
        int pTexCoordLoc = GLES20.glGetAttribLocation(shaderProgram.getmProgramId(), "a_TexCoord");
        int pSamplerLoc = GLES20.glGetUniformLocation(shaderProgram.getmProgramId(), "s_Texture");

        GLES20.glUniformMatrix4fv(pMVPMatrixHandle, 1, false, pMVPMatrix, 0);
        mVertexBuffer.position(0);
        GLES20.glVertexAttribPointer(pPositionHandle, 3, GLES20.GL_FLOAT, false,
                0, mVertexBuffer);
        GLES20.glEnableVertexAttribArray(pPositionHandle);
        colorBuffer.position(0);
        GLES20.glVertexAttribPointer(colorHandle, 4, GLES20.GL_FLOAT, false,
                0, colorBuffer);
        GLES20.glEnableVertexAttribArray(colorHandle);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, TextureHelper.getTextureId("icons"));
        GLES20.glUniform1i(pSamplerLoc, 0);
        // Prepare the texturecoordinates
        GLES20.glVertexAttribPointer(pTexCoordLoc, 2, GLES20.GL_FLOAT,
                false,
                0, mTextureBuffer);
        if (hasTexture) {

            GLES20.glEnableVertexAttribArray(pTexCoordLoc);
        }

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, getVertexCount());
        if (hasTexture) {
            GLES20.glDisableVertexAttribArray(pTexCoordLoc);
        }

        GLES20.glDisableVertexAttribArray(pPositionHandle);
        GLES20.glDisableVertexAttribArray(colorHandle);

    }

    private void addShapesTexture(ArrayList<GLShape> shapes) {
        if (shapes != null) {
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
    }

    private void addShapesColor(ArrayList<GLShape> shapes) {
        if(shapes != null) {
            ArrayList<Float> stockData = new ArrayList<>();
            for (int i = 0; i < shapes.size(); i++) {
                float[] texels = shapes.get(i).getColor();
                for (int j = 0; j < texels.length; j++) {
                    stockData.add(texels[j]);
                }
            }
            float[] data = new float[stockData.size()];
            int i = 0;
            for (Float f : stockData) {
                data[i++] = (f != null ? f : Float.NaN);
            }
            setColorBuffer(data);
        }
    }

    private void addShapesBackBufferColor(ArrayList<GLShape> shapes) {
        if (shapes != null) {
            ArrayList<Float> stockData = new ArrayList<>();
            for (int i = 0; i < shapes.size(); i++) {
                if (shapes.get(i).getBackBufferColor() != null) {
                    float[] texels = shapes.get(i).getBackBufferColor();
                    for (int j = 0; j < texels.length; j++) {
                        stockData.add(texels[j]);
                    }
                }
            }
            float[] data = new float[stockData.size()];
            int i = 0;
            for (Float f : stockData) {
                data[i++] = (f != null ? f : Float.NaN);
            }
            setBackBufferColorBuffer(data);
        }
    }

    /*private void addShapesBackBufferColor(ArrayList<GLShape> shapes) {
        float colors[] = {
                1,0,0,1,
                1,0,0,1,
                1,0,0,1,
                1,0,0,1,
                1,0,0,1,
                1,0,0,1
        };
        ArrayList<Float> stockData = new ArrayList<>();
        for (int i = 0; i < shapes.size(); i++) {
            float[] texels = colors;
            for (int j = 0; j < texels.length; j++) {
                stockData.add(texels[j]);
            }
        }
        float[] data = new float[stockData.size()];
        int i = 0;
        for (Float f : stockData) {
            data[i++] = (f != null ? f : Float.NaN);
        }
        setBackBufferColorBuffer(data);
    }*/

    public void doTranslate(float dx, float dy) {
        mTranformationInterface.onTranslate(dx, dy, mTranslateMatrix);
    }

    public void setBuffers() {
        addShapesColor(shapes);
        addShapesTexture(shapes);
        addShapesBackBufferColor(shapes);
    }

    public void drawOnBackbuffer(float[] pMVPMatrix, float[] pViewMatrix, float[] pProjectionMatrix) {
        if (mVertexBuffer != null) {
            GLES20.glUseProgram(shaderProgram.getmProgramId());

            Matrix.multiplyMM(pMVPMatrix, 0, pViewMatrix, 0, mModelMatrix, 0);
            Matrix.multiplyMM(pMVPMatrix, 0, pMVPMatrix, 0, mTranslateMatrix, 0);
            Matrix.multiplyMM(pMVPMatrix, 0, pProjectionMatrix, 0, pMVPMatrix, 0);

            int pMVPMatrixHandle = GLES20.glGetUniformLocation(shaderProgram.getmProgramId(), "u_MVPMatrix");
            int pPositionHandle = GLES20.glGetAttribLocation(shaderProgram.getmProgramId(), "a_Position");
            int colorHandle = GLES20.glGetAttribLocation(shaderProgram.getmProgramId(), "a_Color");


            GLES20.glUniformMatrix4fv(pMVPMatrixHandle, 1, false, pMVPMatrix, 0);
            mVertexBuffer.position(0);
            GLES20.glVertexAttribPointer(pPositionHandle, 3, GLES20.GL_FLOAT, false,
                    0, mVertexBuffer);
            GLES20.glEnableVertexAttribArray(pPositionHandle);
            backBufferColorBuffer.position(0);
            GLES20.glVertexAttribPointer(colorHandle, 4, GLES20.GL_FLOAT, false,
                    0, backBufferColorBuffer);
            GLES20.glEnableVertexAttribArray(colorHandle);


            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, getVertexCount());
        }

    }
}
