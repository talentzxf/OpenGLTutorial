package com.max3d.primitives;

import com.max3d.core.AbstractBufferList;
import com.max3d.core.Object3d;
import com.max3d.core.ObjectRenderer;

import java.util.Map;

/**
 * Created by VincentZhang on 2/25/2018.
 * <p>
 * Plane in x-y plane
 */

public class Plane extends ObjectRenderer {
    public Plane(float width, float height, int segsPerEdge) {
        super("shaders/lambert.vert", "shaders/lambert.frag");

        int verticeCount = (segsPerEdge + 1) * (segsPerEdge + 1);
        float[] positions = new float[verticeCount * 3];
        int[] faces = new int[segsPerEdge * segsPerEdge * 3 * 2];
        float[] colors = new float[verticeCount * 3];
        float[] normals = new float[verticeCount * 3];

        float segWidth = width / segsPerEdge;
        float segHeight = height / segsPerEdge;

        float[] color = {1.0f, 1.0f, 0.0f};
        float[] normal = {0.0f, 0.0f, 1.0f};

        for (int row = 0; row < segsPerEdge; row++) {
            for (int col = 0; col < segsPerEdge; col++) {
                int startIdx = (row * segsPerEdge + col) * 3;
                positions[startIdx] = -width / 2 + col * segWidth;
                positions[startIdx + 1] = -height / 2 + row * segHeight;
                positions[startIdx + 2] = 0;

                colors[startIdx] = color[0];
                colors[startIdx + 1] = color[1];
                colors[startIdx + 2] = color[2];

                normals[startIdx] = normal[0];
                normals[startIdx + 1] = normal[1];
                normals[startIdx + 2] = normal[2];
            }
        }

        for (int row = 0; row < segsPerEdge; row++) {
            for (int col = 0; col < segsPerEdge; col++) {
                int faceIdx = row * col;
                faces[faceIdx] = row * segsPerEdge + col;
                faces[faceIdx + 1] = row * segsPerEdge + col + 1;
                faces[faceIdx + 2] = (row + 1) * segsPerEdge + col + 1;

                faces[faceIdx + 3] = (row + 1) * segsPerEdge + col + 1;
                faces[faceIdx + 4] = (row + 1) * segsPerEdge + col;
                faces[faceIdx + 5] = row * segsPerEdge + col;
            }
        }
        init(faces,positions,colors,normals);
    }

}
