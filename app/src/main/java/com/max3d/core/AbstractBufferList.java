package com.max3d.core;

import java.nio.Buffer;
import java.nio.FloatBuffer;

/**
 * Created by VincentZhang on 2/25/2018.
 */

public abstract class AbstractBufferList<T extends Buffer> {
    protected T mBuffer;
    protected int mNumElements = 0;
    public T buffer()
    {
        return mBuffer;
    }

    public abstract int getPropertiesPerElement();
    public abstract int getBytesPerProperty();

    public int size(){
        return mNumElements;
    }
}
