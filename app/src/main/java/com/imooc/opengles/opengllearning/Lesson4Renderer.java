package com.imooc.opengles.opengllearning;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by VincentZhang on 2/24/2018.
 */

class Lesson4Renderer implements GLSurfaceView.Renderer {
    private float[] mViewMatrix = new float[16];
    private float[] mProjectionMatrix = new float[16];
    private long startTime = -1;
    private int mProgram = -1;
    private Context context;

    private float[] lightPos = {
            0.0f, 0.0f, 5.0f, 1.0f
    };

    private float[] vertices = {
            1.0f, 0.0f, 0.0f,
            -1.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f,

            2.0f, 0.0f, -1.0f,
            -2.0f, 0.0f, -1.0f,
            0.0f, 2.0f, -1.0f
    };

    private float[] colors = {
            1.0f, 0.0f, 0.0f,
            0.1f, 0.8f, 0.2f,
            0.3f, 0.5f, 0.6f,

            0.0f, 1.0f, 0.0f,
            0.5f, 0.1f, 0.0f,
            0.0f, 1.0f, 0.0f,
    };

    private float[] normals = {
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,

            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
    };

    private FloatBuffer mPositionBuffer;
    private FloatBuffer mColorBuffer;
    private FloatBuffer mNormalBuffer;

    Lesson4Renderer(Context context) {
        this.context = context;

        mPositionBuffer = ByteBuffer.allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mPositionBuffer.put(vertices).position(0);

        mColorBuffer = ByteBuffer.allocateDirect(colors.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mColorBuffer.put(colors).position(0);

        mNormalBuffer = ByteBuffer.allocateDirect(normals.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mNormalBuffer.put(normals).position(0);
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

        mProgram = Helper.compileShadersFromAssets(this.context, "shaders/lambert.frag", "shaders/lambert.vert");
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

        GLES20.glUseProgram(mProgram);

        // model matrix
        float[] modelMatrix = new float[16];
        Matrix.setIdentityM(modelMatrix,0);
        long elapsedTime = System.currentTimeMillis() - startTime;
        Matrix.rotateM(modelMatrix,0,elapsedTime/10.0f, 1.0f,1.0f,0.0f);

        // 把modelMatrix 传给u_MMatrix
        int modelMatrixHandler = GLES20.glGetUniformLocation(mProgram, "u_MMatrix");
        GLES20.glUniformMatrix4fv(modelMatrixHandler,1,false,modelMatrix,0);

        int viewMatrixHandler = GLES20.glGetUniformLocation(mProgram, "u_VMatrix");
        GLES20.glUniformMatrix4fv(viewMatrixHandler,1,false,mViewMatrix,0);

        int projectMatrixHandler = GLES20.glGetUniformLocation(mProgram, "u_PMatrix");
        GLES20.glUniformMatrix4fv(projectMatrixHandler,1,false,mProjectionMatrix,0);

        int lightPosHandler = GLES20.glGetUniformLocation(mProgram,"u_lightPos");
        GLES20.glUniform4fv(lightPosHandler,1,lightPos,0);

        // 把定点数组赋给a_Position
        int positionHandler = GLES20.glGetAttribLocation(mProgram,"a_Position");

        GLES20.glVertexAttribPointer(positionHandler,3,GLES20.GL_FLOAT,false,3*4,
                mPositionBuffer);
        GLES20.glEnableVertexAttribArray(positionHandler);

        int colorHandler = GLES20.glGetAttribLocation(mProgram,"a_Color");
        GLES20.glVertexAttribPointer(colorHandler,3,GLES20.GL_FLOAT,false,3*4,
                mColorBuffer);
        GLES20.glEnableVertexAttribArray(colorHandler);

        int normalHandler = GLES20.glGetAttribLocation(mProgram,"a_Normal");
        GLES20.glVertexAttribPointer(normalHandler,3,GLES20.GL_FLOAT,false,3*4,
                mNormalBuffer);
        GLES20.glEnableVertexAttribArray(normalHandler);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,vertices.length/3);

        GLES20.glDisableVertexAttribArray(positionHandler);
        GLES20.glDisableVertexAttribArray(colorHandler);
        GLES20.glDisableVertexAttribArray(normalHandler);
    }
}
