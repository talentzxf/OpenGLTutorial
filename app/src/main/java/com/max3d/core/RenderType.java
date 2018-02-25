package com.max3d.core;

import android.opengl.GLES20;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by VincentZhang on 2/25/2018.
 */

public enum RenderType
{
    POINTS (GLES20.GL_POINTS),
    LINES (GLES20.GL_LINES),
    LINE_LOOP (GLES20.GL_LINE_LOOP),
    LINE_STRIP (GLES20.GL_LINE_STRIP),
    TRIANGLES (GLES20.GL_TRIANGLES),
    TRIANGLE_STRIP (GLES20.GL_TRIANGLE_STRIP),
    TRIANGLE_FAN (GLES20.GL_TRIANGLE_FAN);

    private final int _glValue;

    private RenderType(int $glValue)
    {
        _glValue = $glValue;
    }

    public int glValue()
    {
        return _glValue;
    }
}
