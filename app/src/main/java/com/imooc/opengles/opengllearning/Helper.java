package com.imooc.opengles.opengllearning;

import android.content.Context;
import android.opengl.GLES20;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by VincentZhang on 2/24/2018.
 */

public class Helper {

    public static String getContextFromPath(Context context, String path) {
        try (InputStream is = context.getAssets().open(path)) {
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            return new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static int compileShadersFromAssets(Context context, String fragShaderLocation, String vertShaderLocation) {
        String fragmentShader = getContextFromPath(context, fragShaderLocation);
        String vertexShader = getContextFromPath(context, vertShaderLocation);

        int vertexShaderHandler = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        GLES20.glShaderSource(vertexShaderHandler, vertexShader);
        GLES20.glCompileShader(vertexShaderHandler);

        int fragmentShaderHandler = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource(fragmentShaderHandler, fragmentShader);
        GLES20.glCompileShader(fragmentShaderHandler);

        int program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program,vertexShaderHandler);
        GLES20.glAttachShader(program,fragmentShaderHandler);
        GLES20.glLinkProgram(program);

        return program;
    }
}
