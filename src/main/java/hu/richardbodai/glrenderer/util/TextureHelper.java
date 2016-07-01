package hu.richardbodai.glrenderer.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by richardbodai on 4/6/16.
 */
public class TextureHelper
{
    static HashMap<String, Integer> resourceMap;
    static HashMap<String, Integer> textureMap;
    static int[] textureHandle;


    public static int getTextureId(String key) {
        return textureMap.get(key);
    }

    public static void loadTextures(final Context context, final HashMap<String, Integer> resourceMap) {
        int mapSize = resourceMap.size();
        textureHandle = new int[mapSize];
        textureMap = new HashMap<>();
        GLES20.glGenTextures(mapSize, textureHandle, 0);
        if (textureHandle[0] != 0) {
            Iterator it = resourceMap.entrySet().iterator();
            int itNum = 0;
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                int resourceId = (Integer)pair.getValue();
                String resourceKey = (String)pair.getKey();

                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inScaled = false;	// No pre-scaling
                // Read in the resource
                final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);

                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[itNum]);

                // Set filtering
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

                // Load the bitmap into the bound texture.
                GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
                // Recycle the bitmap, since its data has been loaded into OpenGL.
                bitmap.recycle();
                textureMap.put(resourceKey, textureHandle[itNum]);
                itNum++;
            }
        }
    }

    public static int loadTexture(final Context context, final int resourceId)
    {
        final int[] textureHandle = new int[1];

        GLES20.glGenTextures(1, textureHandle, 0);

        if (textureHandle[0] != 0)
        {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;	// No pre-scaling

            // Read in the resource
            final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);

            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

            // Set filtering
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

            // Load the bitmap into the bound texture.
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

            // Recycle the bitmap, since its data has been loaded into OpenGL.
            bitmap.recycle();
        }

        if (textureHandle[0] == 0)
        {
            throw new RuntimeException("Error loading texture.");
        }

        return textureHandle[0];
    }
}
