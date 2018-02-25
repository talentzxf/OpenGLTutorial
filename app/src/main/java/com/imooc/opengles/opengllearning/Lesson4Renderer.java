package com.imooc.opengles.opengllearning;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.max3d.core.AbstractBufferList;
import com.max3d.core.RenderContext;
import com.max3d.primitives.Plane;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by VincentZhang on 2/24/2018.
 */

class Lesson4Renderer implements GLSurfaceView.Renderer {
    private float[] mViewMatrix = new float[16];
    private float[] mProjectionMatrix = new float[16];
    private long startTime = -1;

    private Plane plane;

    private float[] lightPos = {
            0.0f, 0.0f, 5.0f, 1.0f
    };

    Lesson4Renderer(Context context) {
        RenderContext.getInstance().setContext(context);
        plane = new Plane(1.0f, 1.0f, 5);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.f, 0f, 1.0f, 1.0f);

        float eyeX = 0.0f;
        float eyeY = 0.0f;
        float eyeZ = 5.0f;

        float lookX = 0.0f;
        float lookY = 0.0f;
        float lookZ = 0.0f;

        float upX = 0.0f;
        float upY = 1.0f;
        float upZ = 0.0f;

        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ,
                lookX, lookY, lookZ, upX, upY, upZ);

        startTime = System.currentTimeMillis();
    }


    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0,0,width,height);

        // 宽高比
        float ratio = (float)width/height;
        float left = -ratio;
        float right = ratio;
        float bottom = -1.0f;
        float top = 1.0f;
        float near = 1.0f;
        float far = 10.0f;

        // 正交投影
        Matrix.frustumM(mProjectionMatrix,0,left,right,bottom,top,near,far);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        // model matrix
        float[] modelMatrix = new float[16];
        Matrix.setIdentityM(modelMatrix,0);
        long elapsedTime = System.currentTimeMillis() - startTime;
        Matrix.rotateM(modelMatrix,0,elapsedTime/10.0f, 1.0f,1.0f,0.0f);


        Map<String, Object> uniformMap = new HashMap<>();
        Map<String, AbstractBufferList> attribMap = new HashMap<>();
        uniformMap.put("u_MMatrix", modelMatrix);
        uniformMap.put("u_VMatrix", mViewMatrix);
        uniformMap.put("u_PMatrix", mProjectionMatrix);
        uniformMap.put("u_lightPos", lightPos);
        plane.drawObject(uniformMap, attribMap);
    }
}
