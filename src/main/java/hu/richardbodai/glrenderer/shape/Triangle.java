package hu.richardbodai.glrenderer.shape;

/**
 * Created by richardbodai on 4/4/16.
 */
public class Triangle implements GLShape {

    float mX1, mY1, mX2, mY2, mX3, mY3, mZ1, mZ2, mZ3;
    float[] mColor;

    public static float uvs[];

    public Triangle(float x1, float y1, float x2, float y2, float x3, float y3) {
        mX1 = x1;
        mX2 = x2;
        mX3 = x3;
        mY1 = y1;
        mY2 = y2;
        mY3 = y3;
        mColor = new float[] {
                1.0f, 1.0f, 1.0f, 1.0f
        };
        uvs = new float[] {
                0.0f, 0.0f,
                0.0f, 1.0f,
                0.5f, 0.0f
        };
    }

    public GLShape setColor(float r, float g, float b, float a) {
        mColor[0] = r;
        mColor[1] = g;
        mColor[2] = b;
        mColor[3] = a;
        return this;
    }

    @Override
    public float[] convertToGLFormat() {
        float[] data = {
                mX1, mY1, mZ1,
                /*mColor[0], mColor[1], mColor[2], mColor[3],*/
                mX2, mY2, mZ2,
                /*mColor[0], mColor[1], mColor[2], mColor[3],*/
                mX3, mY3, mZ3
                /*mColor[0], mColor[1], mColor[2], mColor[3]*/
        };
        return data;
    }

    @Override
    public float[] getTexels() {
        return uvs;
    }

    @Override
    public float[] getColor() {
        return new float[0];
    }

    @Override
    public void draw(int p, int t, int s, int c, int m, float[] mh) {

    }

    @Override
    public boolean hasTexture() {
        return false;
    }

    @Override
    public void setTextureHandle(int t) {

    }

    @Override
    public int getImageId() {
        return 0;
    }
}
