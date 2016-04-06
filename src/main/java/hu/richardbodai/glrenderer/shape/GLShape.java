package hu.richardbodai.glrenderer.shape;

/**
 * Created by richardbodai on 4/4/16.
 */
public interface GLShape {
    float[] convertToGLFormat();

    float[] getTexels();

    void draw(int p, int t, int s);

    boolean hasTexture();

    void setTextureHandle(int t);

    int getImageId();
}
