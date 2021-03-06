package com.imooc.opengles.opengllearning;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by VincentZhang on 1/28/2018.
 */

class Lesson1Renderer implements GLSurfaceView.Renderer {

    private float[] mViewMatrix = new float[16];
    private float[] mProjectionMatrix = new float[16];
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

    // Vertex Shader作用在顶点上
    private String vertexShader ="precision mediump float;" +
            "uniform mat4 u_MMatrix;" + //从外部输入，对所有顶点一样的矩阵
            "uniform mat4 u_VMatrix;" + //从外部输入，对所有顶点一样的矩阵
            "uniform mat4 u_PMatrix;" + //从外部输入，对所有顶点一样的矩阵
            "attribute vec4 a_Position;" + //从外部输入，对每个定点不一样，每个定点的位置
            "attribute vec4 a_Color;" + //从外部输入，对每个顶点不一样，每个顶点的颜色
            "attribute vec4 a_Normal;" + //从外部输入，对每个顶点不一样，每个顶点的法向量

            "varying vec4 v_Color;"+ // 在VertexShader和Fragment Shader之间传值
            "varying vec4 v_Normal;"+
            "varying vec4 v_Position;" +
            "void main(){" +
            "gl_Position = u_PMatrix * u_VMatrix * u_MMatrix * a_Position;" + // 输出，在屏幕上的坐标
            "v_Color = a_Color;" + // 不是简单的赋值，插值
            "v_Normal = u_MMatrix * a_Normal;" + // 把法向量转到世界坐标系
            "v_Position = u_MMatrix * a_Position;" +
            "}";

    // Fragment Shader作用在每个像素点上的
    private String fragmentShader = "precision mediump float;" +
            "uniform vec4 u_lightPos;" +
            "varying vec4 v_Normal;" +
            "varying vec4 v_Position;"+
            "varying vec4 v_Color;" +
            "void main(){" +
            "float I = abs(dot(normalize(u_lightPos - v_Position), normalize(v_Normal)));" +
            "gl_FragColor = I * v_Color;" + //输出，每个定点在屏幕上的颜色
            "}";
    private int mProgramHandler = -1;
    private FloatBuffer mPositionBuffer;
    private FloatBuffer mColorBuffer;
    private FloatBuffer mNormalBuffer;
    private long startTime = -1;

    Lesson1Renderer(){
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

    void compileShaders(String vertexShader, String fragmentShader){
        int vertexShaderHandler = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        GLES20.glShaderSource(vertexShaderHandler, vertexShader);
        GLES20.glCompileShader(vertexShaderHandler);

        int fragmentShaderHandler = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource(fragmentShaderHandler, fragmentShader);
        GLES20.glCompileShader(fragmentShaderHandler);

        mProgramHandler = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgramHandler,vertexShaderHandler);
        GLES20.glAttachShader(mProgramHandler,fragmentShaderHandler);
        GLES20.glLinkProgram(mProgramHandler);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.f,0f,1.0f,1.0f);

        float eyeX = 0.0f;
        float eyeY = 0.0f;
        float eyeZ = 5.0f;

        float lookX = 0.0f;
        float lookY = 0.0f;
        float lookZ = 0.0f;

        float upX = 0.0f;
        float upY = 1.0f;
        float upZ = 0.0f;

        Matrix.setLookAtM(mViewMatrix,0,eyeX,eyeY,eyeZ,
                lookX,lookY,lookZ,upX,upY,upZ);

        compileShaders(vertexShader, fragmentShader);
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

        // 透视投影
        // Matrix.perspectiveM();
    }

    @Override
    public void onDrawFrame(GL10 gl) {

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        GLES20.glUseProgram(mProgramHandler);

        // model matrix
        float[] modelMatrix = new float[16];
        Matrix.setIdentityM(modelMatrix,0);
        long elapsedTime = System.currentTimeMillis() - startTime;
        Matrix.rotateM(modelMatrix,0,elapsedTime/10.0f, 1.0f,1.0f,0.0f);

//        // mvpMatrix = p*v*m
//        float[] mvpMatrix = new float[16];
//        Matrix.multiplyMM(mvpMatrix,0,mViewMatrix,0,modelMatrix,0) ;  // v*m
//        Matrix.multiplyMM(mvpMatrix,0,mProjectionMatrix, 0,mvpMatrix,0); // p*v*m

//        // 把mvpMatrix 传给u_MVPMatrix
//        int mvpMatrixHandler = GLES20.glGetUniformLocation(mProgramHandler, "u_MVPMatrix");
//        GLES20.glUniformMatrix4fv(mvpMatrixHandler,1,false,mvpMatrix,0);

        // 把modelMatrix 传给u_MMatrix
        int modelMatrixHandler = GLES20.glGetUniformLocation(mProgramHandler, "u_MMatrix");
        GLES20.glUniformMatrix4fv(modelMatrixHandler,1,false,modelMatrix,0);

        int viewMatrixHandler = GLES20.glGetUniformLocation(mProgramHandler, "u_VMatrix");
        GLES20.glUniformMatrix4fv(viewMatrixHandler,1,false,mViewMatrix,0);

        int projectMatrixHandler = GLES20.glGetUniformLocation(mProgramHandler, "u_PMatrix");
        GLES20.glUniformMatrix4fv(projectMatrixHandler,1,false,mProjectionMatrix,0);

        int lightPosHandler = GLES20.glGetUniformLocation(mProgramHandler,"u_lightPos");
        GLES20.glUniform4fv(lightPosHandler,1,lightPos,0);

        // 把定点数组赋给a_Position
        int positionHandler = GLES20.glGetAttribLocation(mProgramHandler,"a_Position");

        GLES20.glVertexAttribPointer(positionHandler,3,GLES20.GL_FLOAT,false,3*4,
                mPositionBuffer);
        GLES20.glEnableVertexAttribArray(positionHandler);

        int colorHandler = GLES20.glGetAttribLocation(mProgramHandler,"a_Color");
        GLES20.glVertexAttribPointer(colorHandler,3,GLES20.GL_FLOAT,false,3*4,
                mColorBuffer);
        GLES20.glEnableVertexAttribArray(colorHandler);

        int normalHandler = GLES20.glGetAttribLocation(mProgramHandler,"a_Normal");
        GLES20.glVertexAttribPointer(normalHandler,3,GLES20.GL_FLOAT,false,3*4,
                mNormalBuffer);
        GLES20.glEnableVertexAttribArray(normalHandler);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,vertices.length/3);

        GLES20.glDisableVertexAttribArray(positionHandler);
        GLES20.glDisableVertexAttribArray(colorHandler);
        GLES20.glDisableVertexAttribArray(normalHandler);
    }
}
