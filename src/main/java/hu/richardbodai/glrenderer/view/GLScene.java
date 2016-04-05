package hu.richardbodai.glrenderer.view;

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
    private FloatBuffer mVertexBufferOnly_X;
    private FloatBuffer mVertexBufferOnly_Y;
    private float[] mData;
    private float[] mDataOnly_X;
    private float[] mDataOnly_Y;

    public FloatBuffer getVertexBufferOnly_Y() {
        return mVertexBufferOnly_Y;
    }

    public enum BindDimension {
        X, Y, Z
    }

    public GLScene() {
        mVertexBuffer = null;
    }

    public void setVertexBuffer(float[] data) {
        mData = data;
        mVertexBuffer = ByteBuffer.allocateDirect(data.length * BYTES_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mVertexBuffer.put(data).position(0);
    }

    public void setVertexBuffer(float[] data, BindDimension bindDimension) {
        switch (bindDimension) {
            case X:
                mDataOnly_Y = data;
                mVertexBufferOnly_Y = ByteBuffer.allocateDirect(data.length * BYTES_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer();
                mVertexBufferOnly_Y.put(data).position(0);
                break;
            case Y:
                mDataOnly_X = data;
                mVertexBufferOnly_X = ByteBuffer.allocateDirect(data.length * BYTES_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer();
                mVertexBufferOnly_X.put(data).position(0);
                break;
            case Z:
                break;
        }

    }

    public FloatBuffer getVertexBuffer() {
        return mVertexBuffer;
    }

    public FloatBuffer getVertexBufferOnly_X() {
        return mVertexBufferOnly_X;
    }

    public int getVertexCount() {
        return mData.length / 7;
    }

    public int getVertexCountBindY() {
        return mDataOnly_X.length / 7;
    }
    public int getVertexCountBindX() {
        return mDataOnly_Y.length / 7;
    }

    public void addShapes(ArrayList<GLShape> shapes) {
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

    public void addShapes(ArrayList<GLShape> shapes, BindDimension bindDimension) {
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
        setVertexBuffer(data, bindDimension);
    }
}
