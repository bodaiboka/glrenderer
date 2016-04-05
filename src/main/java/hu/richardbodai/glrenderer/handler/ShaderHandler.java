package hu.richardbodai.glrenderer.handler;

import android.opengl.GLES20;

/**
 * Created by richardbodai on 4/4/16.
 */
public class ShaderHandler {
    private String mVertexShader;
    private String mFragmentShader;

    private int mVertexShaderGLId;
    private int mFragmentShaderGLId;
    private int mProgramGLId;

    public ShaderHandler(String vertexShader, String fragmentShader) {
        mVertexShader = vertexShader;
        mFragmentShader = fragmentShader;
    }

    /**
     * Need to call in Renderer's onSurfaceCreated method
     */
    public void loadShaders() {
        // Load in the vertex shader.
        int vertexShaderHandle = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        if (vertexShaderHandle != 0)
        {
            // Pass in the shader source.
            GLES20.glShaderSource(vertexShaderHandle, mVertexShader);
            // Compile the shader.
            GLES20.glCompileShader(vertexShaderHandle);
            // Get the compilation status.
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(vertexShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
            // If the compilation failed, delete the shader.
            if (compileStatus[0] == 0)
            {
                GLES20.glDeleteShader(vertexShaderHandle);
                vertexShaderHandle = 0;
            }
            else {
                mVertexShaderGLId = vertexShaderHandle;
            }
        }
        if (vertexShaderHandle == 0)
        {
            throw new RuntimeException("Error creating vertex shader.");
        }
        // Load in the fragment shader.
        int fragmentShaderHandle = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        if (fragmentShaderHandle != 0) {
            // Pass in the shader source.
            GLES20.glShaderSource(fragmentShaderHandle, mFragmentShader);
            // Compile the shader.
            GLES20.glCompileShader(fragmentShaderHandle);
            // Get the compilation status.
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(fragmentShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
            // If the compilation failed, delete the shader.
            if (compileStatus[0] == 0) {
                GLES20.glDeleteShader(fragmentShaderHandle);
                fragmentShaderHandle = 0;
            }
        }
        if (fragmentShaderHandle == 0) {
            throw new RuntimeException("Error creating fragment shader.");
        }
        else {
            mFragmentShaderGLId = fragmentShaderHandle;
        }
    }

    public void linkShadersToProgram() {
        // Create a program object and store the handle to it.
        int programHandle = GLES20.glCreateProgram();
        if (programHandle != 0)
        {
            // Bind the vertex shader to the program.
            GLES20.glAttachShader(programHandle, mVertexShaderGLId);
            // Bind the fragment shader to the program.
            GLES20.glAttachShader(programHandle, mFragmentShaderGLId);
            // Bind attributes
           /* GLES20.glBindAttribLocation(programHandle, 0, "a_Position");
            GLES20.glBindAttribLocation(programHandle, 1, "a_Color");*/
            // Link the two shaders together into a program.
            GLES20.glLinkProgram(programHandle);
            // Get the link status.
            final int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);
            // If the link failed, delete the program.
            if (linkStatus[0] == 0)
            {
                GLES20.glDeleteProgram(programHandle);
                programHandle = 0;
            }
        }
        if (programHandle == 0)
        {
            throw new RuntimeException("Error creating program.");
        }
        else {
            mProgramGLId = programHandle;
        }

    }

    public int getVertexShaderGLId() {
        return mVertexShaderGLId;
    }

    public int getFragmentShaderGLId() {
        return mFragmentShaderGLId;
    }

    public int getProgramGLId() {
        return mProgramGLId;
    }
}
