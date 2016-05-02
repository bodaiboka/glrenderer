package hu.richardbodai.glrenderer.handler;

import java.util.ArrayList;
import java.util.HashMap;

import hu.richardbodai.glrenderer.config.GLConfig;
import hu.richardbodai.glrenderer.exceptions.NoProgramForKeyException;

/**
 * Created by richardbodai on 4/26/16.
 */
public class ShaderManager {

    static HashMap<String, ShaderProgram> programs;

    public void createShaderProgram(String key, String vertexShader, String fragmentShader, ArrayList<String> attribs) {
        if (vertexShader == null) {
            vertexShader = GLConfig.DEFAULT_VERTEX_SHADER;
        }
        if (fragmentShader == null) {
            fragmentShader = GLConfig.DEFAULT_FRAGMENT_SHADER;
        }
        ShaderProgram shaderProgram = new ShaderProgram(vertexShader, fragmentShader, attribs);
        addShaderProgram(key, shaderProgram);
    }

    public void addShaderProgram(String key, ShaderProgram shaderProgram) {
        programs.put(key, shaderProgram);
    }

    public static int getProgramId(String key) throws NoProgramForKeyException {
        ShaderProgram shaderProgram = programs.get(key);
        if (shaderProgram == null) {
            throw new NoProgramForKeyException("Can't find ShaderProgram for the key: " + key);
        }
        return shaderProgram.getmProgramId();
    }


}
