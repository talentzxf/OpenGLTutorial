package com.imooc.opengles.opengllearning;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(new Lesson1SurfaceView(getApplicationContext()));
        setContentView(new Lesson4SurfaceView(getApplicationContext()));
    }
}
