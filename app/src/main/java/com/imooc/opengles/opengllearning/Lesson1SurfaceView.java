package com.imooc.opengles.opengllearning;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.View;

/**
 * Created by VincentZhang on 1/28/2018.
 */

class Lesson1SurfaceView extends GLSurfaceView {
    public Lesson1SurfaceView(Context context) {
        super(context);

        setEGLContextClientVersion(2);
        setEGLConfigChooser(8,8,8,8,16,0);
        setRenderer(new Lesson1Renderer());
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
}
