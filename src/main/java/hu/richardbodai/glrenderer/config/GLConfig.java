package hu.richardbodai.glrenderer.config;

import android.content.Context;

import hu.richardbodai.glrenderer.renderer.OnClicklistener;
import hu.richardbodai.glrenderer.util.text.TextManager;

/**
 * Created by richardbodai on 4/4/16.
 */
public class GLConfig {

    public static int defaultProgramHandle;
    public static int textureProgramHandle;
    public int getTextureImageId;
    public int getTextureTextId;

    public enum GL_VERSION {
        ES_1, ES_2
    }

    public enum PROJECTION_MODE {
        PERSPECTIVE, ORTHOGONAL
    }

    final String U_MVPMATRIX = "u_MVPMatrix";
    final String A_POSITION = "a_Position";
    final String A_COLOR = "a_Color";

    public final static String DEFAULT_VERTEX_SHADER = "uniform mat4 u_MVPMatrix;      \n"     // A constant representing the combined model/view/projection matrix.

    + "attribute vec4 a_Position;     \n"     // Per-vertex position information we will pass in.
            + "attribute vec4 a_Color;        \n"     // Per-vertex color information we will pass in.

            + "varying vec4 v_Color;          \n"     // This will be passed into the fragment shader.

            + "void main()                    \n"     // The entry point for our vertex shader.
            + "{                              \n"
            + "   v_Color = a_Color;          \n"     // Pass the color through to the fragment shader.
            // It will be interpolated across the triangle.
            + "   gl_Position = u_MVPMatrix   \n"     // gl_Position is a special variable used to store the final position.
            + "               * a_Position;   \n"     // Multiply the vertex by the matrix to get the final point in
            + "}                              \n";    // normalized screen coordinates.

    public final static String DEFAULT_FRAGMENT_SHADER = "precision mediump float;       \n"     // Set the default precision to medium. We don't need as high of a
            // precision in the fragment shader.
            + "varying vec4 v_Color;          \n"     // This is the color from the vertex shader interpolated across the
            // triangle per fragment.
            + "void main()                    \n"     // The entry point for our fragment shader.
            + "{                              \n"
            + "   gl_FragColor = v_Color;     \n"     // Pass the color directly through the pipeline.
            + "}                              \n";


    public static final String vs_Image =
            "uniform mat4 u_MVPMatrix;" +
                    "attribute vec4 a_Position;" +
                    "attribute vec4 a_Color;" +
                    "attribute vec2 a_TexCoord;" +
                    "varying vec4 v_Color;" +
                    "varying vec2 v_TexCoord;" +
                    "void main() {" +
                    "  gl_Position = u_MVPMatrix * a_Position;" +
                    "  v_Color = a_Color;" +
                    "  v_TexCoord = a_TexCoord;" +
                    "}";
    public static final String fs_Image =
            "precision mediump float;" +
                    "varying vec4 v_Color;" +
                    "varying vec2 v_TexCoord;" +
                    "uniform sampler2D s_Texture;" +
                    "void main() {" +
                    "  gl_FragColor = v_Color * texture2D( s_Texture, v_TexCoord );" +
                   /* " if ((gl_FragColor.r == 0) && (gl_FragColor.g == 0) && (gl_FragColor.b == 0) ) gl_FragColor = v_Color;" +*/
                    "}";

    public static int sp_Text;

    /* SHADER Text
     *
     * This shader is for rendering 2D text textures straight from a texture
     * Color and alpha blended.
     *
     */
    public static final String vs_Text =
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "attribute vec4 a_Color;" +
                    "attribute vec2 a_texCoord;" +
                    "varying vec4 v_Color;" +
                    "varying vec2 v_texCoord;" +
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "  v_texCoord = a_texCoord;" +
                    "  v_Color = a_Color;" +
                    "}";
    public static final String fs_Text =
            "precision mediump float;" +
                    "varying vec4 v_Color;" +
                    "varying vec2 v_texCoord;" +
                    "uniform sampler2D s_texture;" +
                    "void main() {" +
                    "  gl_FragColor = texture2D( s_texture, v_texCoord ) * v_Color;" +
                    "  gl_FragColor.rgb *= v_Color.a;" +
                    "}";

    public GL_VERSION gl_version;
    public PROJECTION_MODE projection_mode;
    public String vertex_shader;
    public String fragment_shader;
    public TextManager textManager;
    private OnClicklistener onClicklistener;

    public OnClicklistener getOnClicklistener() {
        return onClicklistener;
    }

    public void setOnClicklistener(OnClicklistener onClicklistener) {
        this.onClicklistener = onClicklistener;
    }

    public Context context;
    public GLConfig() {
        gl_version = GL_VERSION.ES_2;
        projection_mode = PROJECTION_MODE.PERSPECTIVE;
        vertex_shader = vs_Image;
        fragment_shader = fs_Image;
        textManager = new TextManager();
    }

}
