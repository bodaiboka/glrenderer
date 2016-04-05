package hu.richardbodai.glrenderer.shape;

/**
 * Created by richardbodai on 4/4/16.
 */
public class Rectangle implements GLShape {

    Triangle triangle1;
    Triangle triangle2;

    public Rectangle(float upperLeftX, float upperLeftY, float downerRightX, float downerRightY) {
        triangle1 = new Triangle(upperLeftX, upperLeftY, upperLeftX, downerRightY, downerRightX, downerRightY);
        triangle2 = new Triangle(upperLeftX, upperLeftY, downerRightX, downerRightY, downerRightX, upperLeftY);
    }

    public GLShape setColor(float r, float g, float b, float a) {
        triangle1.setColor(r, g, b, a);
        triangle2.setColor(r, g, b, a);
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
}
