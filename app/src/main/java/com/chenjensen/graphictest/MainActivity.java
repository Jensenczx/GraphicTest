package com.chenjensen.graphictest;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.chenjensen.graphictest.java_layer.surfaceview.FirstOpenRenderer;

public class MainActivity extends AppCompatActivity {


    private GLSurfaceView glSurfaceView;
    private boolean renderSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initGL();
        setContentView(glSurfaceView);
    }

    private void initGL() {
        glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        glSurfaceView.setRenderer(new FirstOpenRenderer(this));
        renderSet = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(renderSet) {
            glSurfaceView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(renderSet) {
            glSurfaceView.onPause();
        }
    }
}
