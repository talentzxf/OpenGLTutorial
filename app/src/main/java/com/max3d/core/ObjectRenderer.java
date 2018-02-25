package com.max3d.core;

import android.opengl.GLES20;
import android.util.Log;

import com.imooc.opengles.opengllearning.Helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by VincentZhang on 2/25/2018.
 */

public abstract class ObjectRenderer {
    private Object3d mObj = new Object3d();
    private int program = -1;

    private List<Integer> vertexAttributes = new ArrayList<>();

    protected ObjectRenderer(String vertexShaderPath, String fragShaderPath){
        // TODO: Move Helper into package max3d
        program = Helper.compileShadersFromAssets(RenderContext.getInstance().getContext(),
                vertexShaderPath,fragShaderPath);
    }

    protected void init(int[] faces, float[] positions,
                                 float[] colors, float[] normals){
        mObj.init(faces, positions,colors, normals);
    }

    private void bindAttributes(Map<String, AbstractBufferList> shaderObjectMap) {
        if(shaderObjectMap == null){
            return;
        }

        for (String attributeName : shaderObjectMap.keySet()) {
            AbstractBufferList buffer = shaderObjectMap.get(attributeName);
            int newBufferHandle = GLES20.glGetAttribLocation(program, attributeName);

            if (newBufferHandle < 0) {
                Log.e("BindAttribute", "Can's get attribute for buffer:" + attributeName);
            }
            // Enable a handle to the triangle vertices
            GLES20.glEnableVertexAttribArray(newBufferHandle);
            // Prepare the triangle coordinate data
            GLES20.glVertexAttribPointer(
                    newBufferHandle, buffer.getPropertiesPerElement(),
                    GLES20.GL_FLOAT, false,
                    buffer.getBytesPerProperty() * buffer.getPropertiesPerElement(), buffer.buffer().position(0));

            vertexAttributes.add(newBufferHandle);
        }
    }

    private void unBindAttributes() {
        for (Integer bufferHandle : vertexAttributes) {
            GLES20.glDisableVertexAttribArray(bufferHandle);
        }
        vertexAttributes.clear();
    }

    private void bindUniform(Map<String, Object> uniformMap) throws Exception {
        if(uniformMap == null){
            return ;
        }

        for (String key : uniformMap.keySet()) {
            Object valueArrayObj = uniformMap.get(key);
            int uniformLocation = GLES20.glGetUniformLocation(program, key);
            if (uniformLocation < 0) {
                throw new Exception("uniform " + key + " can't be found!");
            }

            if (valueArrayObj instanceof float[]) {
                float[] valueArray = (float[]) valueArrayObj;
                switch (valueArray.length) {
                    case 16: // Matrix
                        GLES20.glUniformMatrix4fv(uniformLocation, 1, false, valueArray, 0);
                        break;
                    case 4: // vec4
                        GLES20.glUniform4fv(uniformLocation, 1, valueArray, 0);
                        break;
                    case 3: // vec3
                        GLES20.glUniform3fv(uniformLocation, 1, valueArray, 0);
                        break;
                    case 2: // vec2
                        GLES20.glUniform2fv(uniformLocation, 1, valueArray, 0);
                        break;
                }
            } else if (valueArrayObj instanceof Integer) {
                GLES20.glUniform1i(uniformLocation, (Integer) valueArrayObj);
            } else if (valueArrayObj instanceof Float){
                GLES20.glUniform1f(uniformLocation, (Float) valueArrayObj);
            }
        }
    }

    public void drawObject(Map<String, Object> uniformMap,
                           Map<String, AbstractBufferList> attribMap){
        try {
            if(mObj == null || !mObj.hasInited()){
                return;
            }

            GLES20.glUseProgram(program);

            mObj.putVariables(uniformMap, attribMap);
            bindUniform(uniformMap);
            bindAttributes(attribMap);

            GLES20.glDrawElements(
                    mObj.getRenderType().glValue(),
                    mObj.getFaceArrayLength(),
                    GLES20.GL_UNSIGNED_INT,
                    mObj.getFaceBuffer());

            unBindAttributes();
        } catch (Exception e) {
            Log.e("Error", "Exception " + e);
        }
    }
}
