package hu.richardbodai.glrenderer.handler;

import android.content.Intent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import hu.richardbodai.glrenderer.config.GLConfig;
import hu.richardbodai.glrenderer.exceptions.NoProgramForKeyException;

/**
 * Created by richardbodai on 4/26/16.
 */
public class ShaderManager {

    static HashMap<Integer, ShaderProgram> programs = new HashMap<>();

    static int shaderCount = 0;

    public static ShaderProgram createShaderProgram(String vertexShader, String fragmentShader, ArrayList<String> attribs, ArrayList<String> uniforms) {
        if (vertexShader == null) {
            vertexShader = GLConfig.DEFAULT_VERTEX_SHADER;
        }
        if (fragmentShader == null) {
            fragmentShader = GLConfig.DEFAULT_FRAGMENT_SHADER;
        }
        ShaderProgram shaderProgram = new ShaderProgram(vertexShader, fragmentShader, attribs, uniforms);
        addShaderProgram(shaderCount, shaderProgram);
        return shaderProgram;
    }

    public static void addShaderProgram(int key, ShaderProgram shaderProgram) {
        programs.put(key, shaderProgram);
        shaderCount++;
    }

    public static int getProgramId(String key) throws NoProgramForKeyException {
        ShaderProgram shaderProgram = programs.get(key);
        if (shaderProgram == null) {
            throw new NoProgramForKeyException("Can't find ShaderProgram for the key: " + key);
        }
        return shaderProgram.getmProgramId();
    }

    public static ShaderProgram getShaderProgram(String key) throws NoProgramForKeyException {
        ShaderProgram shaderProgram = programs.get(key);
        if (shaderProgram == null) {
            throw new NoProgramForKeyException("Can't find ShaderProgram for the key: " + key);
        }
        return shaderProgram;
    }

    public static void linkAllShader() {
        Iterator it = programs.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            ((ShaderProgram)pair.getValue()).loadShaders().linkShadersToProgram();
        }
    }


}
