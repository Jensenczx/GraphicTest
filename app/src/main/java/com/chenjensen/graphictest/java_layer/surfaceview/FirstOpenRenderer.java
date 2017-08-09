package com.chenjensen.graphictest.java_layer.surfaceview;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.chenjensen.graphictest.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

/**
 * Created by chenjensen on 17/7/20.
 */

public class FirstOpenRenderer implements GLSurfaceView.Renderer {

    private static final String U_COLOR = "u_Color";
    private static final String A_POSITION = "a_Position";
    private static final String A_COLOR = "a_Color";
    private static final String U_MATRIX = "u_Matrix";

    private static final int POSITION_COMPONENT_COUNT = 4;
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static  final int BYTES_PER_FLOAT = 4;

    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    private final float[] projectionMatrix = new float[16];
    private final float[] modelMatrix = new float[16];

    private int uMatrixLocation;

    private int uColorLocation;
    private int aPositionLocation;
    private int aColorLocation;


//    float[] tableVertices = {0f, 0f,
//                            -0.5f, -0.5f,
//                             0.5f, -0.5f,
//                             0.5f, 0.5f,
//                            -0.5f, 0.5f,
//                            -0.5f, -0.5f,
//
//                            -0.5f, 0f,
//                            0.5f, 0f,
//
//                            0f, -0.25f,
//                            0f, 0.25f
//    };

    float[] tableVertices = {
            //Tri
            0f, 0f, 0f,  1.5f,     1f, 1f, 1f,
            -0.5f, -0.8f, 0f, 1f,  0.7f, 0.7f, 0.7f,
            0.5f, -0.8f, 0f, 1f,   0.7f, 0.7f, 0.7f,
            0.5f, 0.8f, 0f, 2f,    0.7f, 0.7f, 0.7f,
            -0.5f, 0.8f, 0f, 2f,   0.7f, 0.7f, 0.7f,
            -0.5f, -0.8f,0f, 1f,   0.7f, 0.7f, 0.7f,

            //Line1
            -0.5f, 0f, 0f, 1.5f, 1f, 0f, 0f,
            0.5f, 0f,  0f, 1.5f,  1f, 0f, 0f,

            //mallets
            0f, -0.4f, 0f, 1.25f, 0f, 0f, 1f,
            0f, 0.4f, 0f, 1.75f, 1f, 0f, 0f

    };


    private final FloatBuffer data;
    private int programe;


    {
        data = ByteBuffer
                .allocateDirect(tableVertices.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        data.put(tableVertices);
    }


    private Context mContext;

    public FirstOpenRenderer(Context context) {
        mContext = context;
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        String vertexShader = ResouceReader.readTextFromResource(mContext, R.raw.simple_shader);
        String fragmentShader = ResouceReader.readTextFromResource(mContext, R.raw.simple_fragment_shader);

        int vertexID = ShaderHelper.compileVertexShader(vertexShader);
        int fragmentID = ShaderHelper.compileFragmentShader(fragmentShader);

        Log.i("vId", vertexID+"");
        Log.i("fId", fragmentID+"");

        programe = ShaderHelper.linkProgram(vertexID, fragmentID);

        gl.glClearColor(0.0f, 0f, 0f, 0f);

        glUseProgram(programe);
        uColorLocation = glGetUniformLocation(programe, U_COLOR);
        aPositionLocation = glGetAttribLocation(programe, A_POSITION);
        aColorLocation = glGetAttribLocation(programe, A_COLOR);
        uMatrixLocation = glGetUniformLocation(programe, U_MATRIX);

        data.position(0);
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, gl.GL_FLOAT, false, STRIDE, data);
        glEnableVertexAttribArray(aPositionLocation);

        data.position(POSITION_COMPONENT_COUNT);
        glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, gl.GL_FLOAT, false, STRIDE, data);
        glEnableVertexAttribArray(aColorLocation);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
        MatrixHelper.perspectiveM(projectionMatrix, 45, (float)width/(float)height, 1f, 10f);
        setIdentityM(modelMatrix, 0);
        translateM(modelMatrix, 0, 0f, 0f, -2f);

        final float[] temp = new float[16];
        multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0);
        System.arraycopy(temp, 0, projectionMatrix, 0, temp.length);

        translateM(modelMatrix, 0, 0f, 0f, -2.5f);
        rotateM(modelMatrix, 0, -60f, 1f, 0f, 0f);
//
//        final float aspectRatio = width > height ? (float) width / (float) height:
//                (float) height / (float) width;
//
//        if (width > height) {
//            orthoM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f);
//        } else {
//            orthoM(projectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f);
//        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(gl.GL_COLOR_BUFFER_BIT);

        glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0);

//        glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f);
        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);

//        glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        glDrawArrays(GL_LINES, 6, 2);

//        glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        glDrawArrays(GL_POINTS, 8, 1);

//        glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
        glDrawArrays(GL_POINTS, 9, 1);
    }


}
