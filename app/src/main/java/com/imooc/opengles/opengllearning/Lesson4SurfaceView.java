package com.imooc.opengles.opengllearning;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Created by VincentZhang on 2/24/2018.
 */

public class Lesson4SurfaceView extends GLSurfaceView{
    public Lesson4SurfaceView(Context context) {
        super(context);

        setEGLContextClientVersion(2);
        setEGLConfigChooser(8,8,8,8,16,0);
        setRenderer(new Lesson4Renderer(context));
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }
}
