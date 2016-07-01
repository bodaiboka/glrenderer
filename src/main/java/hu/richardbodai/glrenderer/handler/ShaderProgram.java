package hu.richardbodai.glrenderer.handler;

import android.opengl.GLES20;

import java.util.ArrayList;

import hu.richardbodai.glrenderer.config.GLConfig;

/**
 * Created by richardbodai on 4/26/16.
 */
public class ShaderProgram {

    private int mProgramId;
    private int mVertexShaderId;
    private int mFragmentShaderId;

    private String mVertexShaderString;
    private String mFragmentShaderString;

    private ArrayList<String> mAttribNames;
    private ArrayList<String> mUniformNames;

    public ShaderProgram() {
        mVertexShaderString = GLConfig.DEFAULT_VERTEX_SHADER;
        mFragmentShaderString = GLConfig.DEFAULT_FRAGMENT_SHADER;
    }

    public ShaderProgram(String vertexShader, String fragmentShader) {
        mVertexShaderString = vertexShader;
        mFragmentShaderString = fragmentShader;
    }

    public ShaderProgram(String vertexShader, String fragmentShader, ArrayList<String> attribNames, ArrayList<String> uniformNames) {
        mVertexShaderString = vertexShader;
        mFragmentShaderString = fragmentShader;
        mAttribNames = attribNames;
        mUniformNames = uniformNames;
    }

    public String getVertexShaderString() {
        return mVertexShaderString;
    }

    public void setVertexShaderString(String mVertexShaderString) {
        this.mVertexShaderString = mVertexShaderString;
    }

    public String getFragmentShaderString() {
        return mFragmentShaderString;
    }

    public void setFragmentShaderString(String mFragmentShaderString) {
        this.mFragmentShaderString = mFragmentShaderString;
    }

    public ArrayList<String> getmAttribNames() {
        return mAttribNames;
    }

    public void setmAttribNames(ArrayList<String> mAttribNames) {
        this.mAttribNames = mAttribNames;
    }

    public int getmProgramId() {
        return mProgramId;
    }

    public void loadVertexShader(String vertexShaderString) {
        mVertexShaderString = vertexShaderString;
        // Load in the vertex shader.
        int vertexShaderHandle = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        if (vertexShaderHandle != 0)
        {
            // Pass in the shader source.
            GLES20.glShaderSource(vertexShaderHandle, mVertexShaderString);
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
                mVertexShaderId = vertexShaderHandle;
            }
        }
        if (vertexShaderHandle == 0)
        {
            throw new RuntimeException("Error creating vertex shader.");
        }
    }

    public void loadFragmentShader(String fragmentShaderString) {
        mFragmentShaderString = fragmentShaderString;
        // Load in the fragment shader.
        int fragmentShaderHandle = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        if (fragmentShaderHandle != 0) {
            // Pass in the shader source.
            GLES20.glShaderSource(fragmentShaderHandle, mFragmentShaderString);
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
            mFragmentShaderId = fragmentShaderHandle;
        }
    }

    public ShaderProgram linkShadersToProgram() {
        // Create a program object and store the handle to it.
        int programHandle = GLES20.glCreateProgram();
        if (programHandle != 0)
        {
            // Bind the vertex shader to the program.
            GLES20.glAttachShader(programHandle, mVertexShaderId);
            // Bind the fragment shader to the program.
            GLES20.glAttachShader(programHandle, mFragmentShaderId);
            // Bind attributes
            for (int i = 0; i < mAttribNames.size(); i++) {
                GLES20.glBindAttribLocation(programHandle, i, mAttribNames.get(i));
            }
            /*GLES20.glBindAttribLocation(programHandle, 0, "a_Position");
            GLES20.glBindAttribLocation(programHandle, 1, "a_TexCoord");
            GLES20.glBindAttribLocation(programHandle, 2, "a_Color");*/
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
            mProgramId = programHandle;
        }
        return this;
    }

    public ShaderProgram loadShaders(String vertexShaderString, String fragmentShaderString) {
        loadVertexShader(vertexShaderString);
        loadFragmentShader(fragmentShaderString);
        return this;
    }

    public ShaderProgram loadShaders() {
        loadVertexShader(mVertexShaderString);
        loadFragmentShader(mFragmentShaderString);
        return this;
    }

}
