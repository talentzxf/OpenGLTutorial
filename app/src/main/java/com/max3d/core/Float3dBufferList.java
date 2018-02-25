package com.max3d.core;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Float3dBufferList extends AbstractBufferList<FloatBuffer>
{
	public static final int PROPERTIES_PER_ELEMENT = 3;
	public static final int BYTES_PER_PROPERTY = 4;

	public Float3dBufferList(float[] data)
	{
		ByteBuffer bb = ByteBuffer.allocateDirect(data.length * BYTES_PER_PROPERTY);
		bb.order(ByteOrder.nativeOrder());
		mBuffer = bb.asFloatBuffer();
		mBuffer.put(data);

		mNumElements = data.length;
	}
	@Override
	public int getPropertiesPerElement() {
		return Float3dBufferList.PROPERTIES_PER_ELEMENT;
	}

	@Override
	public int getBytesPerProperty() {
		return Float3dBufferList.BYTES_PER_PROPERTY;
	}
}
