package com.chenjensen.graphictest.java_layer.egl_layer;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by chenjensen on 17/7/26.
 */

public class BackDraw {

    private int width = 480;
    private int height = 800;
    private EGL10 mEgl;
    private int[] version = new int[2];
    EGLConfig[] configs = new EGLConfig[1];
    int[] num_config = new int[1];
    //EglchooseConfig used this config

    //如果你要创建的是PbufferSurface（后台显示）类型，就要说明这个，
    // 如果要创建WindowSurface(前台显示)，需要EGL10.EGL_WINDOW_BIT类型，

    int[] configSpec = {
            EGL10.EGL_SURFACE_TYPE, EGL10.EGL_PBUFFER_BIT,
            EGL10.EGL_RED_SIZE, 8,
            EGL10.EGL_GREEN_SIZE, 8,
            EGL10.EGL_BLUE_SIZE, 8,
            EGL10.EGL_ALPHA_SIZE, 8,
            EGL10.EGL_NONE
    };
    //eglCreatePbufferSurface used this config
    int attribListPbuffer[] = {
            EGL10.EGL_WIDTH, width,
            EGL10.EGL_HEIGHT, height,
            EGL10.EGL_NONE
    };

    public BackDraw() {
        initEGL();
    }

    private void initEGL() {
        mEgl = (EGL10) EGLContext.getEGL();

        EGLDisplay mEglDisplay = mEgl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);

        mEgl.eglInitialize(mEglDisplay, version);

        mEgl.eglChooseConfig(mEglDisplay, configSpec, configs, 1, num_config);

        EGLConfig mEglConfig = configs[0];

        EGLContext mEglContext = mEgl.eglCreateContext(mEglDisplay, mEglConfig,
                EGL10.EGL_NO_CONTEXT,
                null);
        if (mEglContext == EGL10.EGL_NO_CONTEXT) {
            //mEgl.eglDestroySurface(mEglDisplay, mEglSurface);
            Log.d("ERROR:", "no CONTEXT");
        }

        //注意这个attribListPbuffer，属性表
        EGLSurface mEglPBSurface = mEgl.eglCreatePbufferSurface(mEglDisplay, mEglConfig, attribListPbuffer);
        if (mEglPBSurface == EGL10.EGL_NO_SURFACE) {
            //mEgl.eglDestroySurface(mEglDisplay, mEglPBSurface);
            int ec = mEgl.eglGetError();
            if (ec == EGL10.EGL_BAD_DISPLAY) {
                Log.d("ERROR:", "EGL_BAD_DISPLAY");
            }
            if (ec == EGL10.EGL_BAD_DISPLAY) {
                Log.d("ERROR:", "EGL_BAD_DISPLAY");
            }
            if (ec == EGL10.EGL_NOT_INITIALIZED) {
                Log.d("ERROR:", "EGL_NOT_INITIALIZED");
            }
            if (ec == EGL10.EGL_BAD_CONFIG) {
                Log.d("ERROR:", "EGL_BAD_CONFIG");
            }
            if (ec == EGL10.EGL_BAD_ATTRIBUTE) {
                Log.d("ERROR:", "EGL_BAD_ATTRIBUTE");
            }
            if (ec == EGL10.EGL_BAD_ALLOC) {
                Log.d("ERROR:", "EGL_BAD_ALLOC");
            }
            if (ec == EGL10.EGL_BAD_MATCH) {
                Log.d("ERROR:", "EGL_BAD_MATCH");
            }
        }


        if (!mEgl.eglMakeCurrent(mEglDisplay, mEglPBSurface, mEglPBSurface, mEglContext))//这里mEglPBSurface，意思是画图和读图都是从mEglPbSurface开始
        {

            Log.d("ERROR:", "bind failed ECODE:" + mEgl.eglGetError());
        }

        GL10 gl = (GL10) mEglContext.getGL();
        gl.glClearColor(1, 0 , 0 ,0);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        createBitmp(gl);
    }

    private void createBitmp(GL10 gl) {
        IntBuffer PixelBuffer = IntBuffer.allocate(width * height);
        PixelBuffer.position(0);
        gl.glReadPixels(0, 0, width, height, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, PixelBuffer);

        PixelBuffer.position(0);//这里要把读写位置重置下
        int pix[] = new int[width * height];
        PixelBuffer.get(pix);//这是将intbuffer中的数据赋值到pix数组中

        Bitmap bmp = Bitmap.createBitmap(pix, width, height, Bitmap.Config.ARGB_8888);//pix是上面读到的像素
        FileOutputStream fos = null;
        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath();

            Log.i("Path", path);
            fos = new FileOutputStream(path + "/screen.png");//注意app的sdcard读写权限问题
        } catch (FileNotFoundException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }

        bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);//压缩成png,100%显示效果
        try {
            fos.flush();
            Log.e("success", "文件创建成功!");
        } catch (IOException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    native private static void _nativeClassInit();

    static {_nativeClassInit();}

}
