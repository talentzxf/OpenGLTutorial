package com.max3d.core;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Map;

/**
 * Created by VincentZhang on 2/25/2018.
 */

public class Object3d {
    private String mName;
    private RenderType mRenderType = RenderType.TRIANGLES;
    private IntBuffer mFaceBuffer;
    private Float3dBufferList mPositionBuffer;
    private Float3dBufferList mColorBuffer;
    private Float3dBufferList mNormalBuffer;

    private boolean mInited = false;
    private int faceArrayLength = -1;

    public RenderType getRenderType() {
        return mRenderType;
    }

    void init(int[] faces, float[] positions, float[] colors, float[] normals){
        mInited = true;

        mPositionBuffer = new Float3dBufferList(positions);
        mColorBuffer = new Float3dBufferList(colors);
        mNormalBuffer = new Float3dBufferList(normals);

        mFaceBuffer = ByteBuffer.allocateDirect(faces.length*4)
                .order(ByteOrder.nativeOrder()).asIntBuffer();
        mFaceBuffer.put(faces).position(0);
        faceArrayLength = faces.length;
    }

    public int getFaceArrayLength(){
        return faceArrayLength;
    }

    public IntBuffer getFaceBuffer() {
        return mFaceBuffer;
    }

    public void putVariables(Map<String, Object> uniformMap,
                              Map<String, AbstractBufferList> attribMap){
        attribMap.put("a_Position", mPositionBuffer);
        attribMap.put("a_Color", mColorBuffer);
        attribMap.put("a_Normal", mNormalBuffer);
    }

    public boolean hasInited() {
        return mInited;
    }
}
