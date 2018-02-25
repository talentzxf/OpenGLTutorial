package com.max3d.core;

import android.content.Context;

/**
 * Created by VincentZhang on 2/25/2018.
 */

public class RenderContext {
    static private RenderContext _inst;
    private RenderContext(){

    }

    public static RenderContext getInstance(){
        if(_inst == null){
            _inst = new RenderContext();
        }
        return _inst;
    }

    private Context mContext;
    public void setContext(Context context){
        mContext = context;
    }

    public Context getContext(){
        return mContext;
    }
}
