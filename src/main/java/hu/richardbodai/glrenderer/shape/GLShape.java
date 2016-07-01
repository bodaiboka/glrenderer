package hu.richardbodai.glrenderer.shape;

/**
 * Created by richardbodai on 4/4/16.
 */
public interface GLShape {
    float[] convertToGLFormat();

    float[] getTexels();

    float[] getColor();

    void setTexels(float[] data);

    void draw(int p, int t, int s, int c, int m, float[] mh);

    boolean hasTexture();

    void setTextureHandle(int t);

    int getImageId();

    float[] getBackBufferColor();
}
