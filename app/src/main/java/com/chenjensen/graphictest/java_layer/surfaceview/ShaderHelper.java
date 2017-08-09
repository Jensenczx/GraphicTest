package com.chenjensen.graphictest.java_layer.surfaceview;

import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteProgram;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glGetProgramiv;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;

/**
 * Created by chenjensen on 17/7/20.
 */


public class ShaderHelper {

    public ShaderHelper(GL10 gl) {

    }

    public static int compileVertexShader(String shaderCode) {
        return compileShader(GL_VERTEX_SHADER, shaderCode);
    }

    public static int compileFragmentShader(String shaderCode) {
        return compileShader(GL_FRAGMENT_SHADER, shaderCode);
    }

    private static int compileShader(int type, String shaderCode) {
        final int objectID = glCreateShader(type);
        if (objectID == 0) {
            return 0;
        }
        glShaderSource(objectID, shaderCode);
        glCompileShader(objectID);
        int[] compileStatus = new int[1];
        glGetShaderiv(objectID, GL_COMPILE_STATUS, compileStatus, 0);

        if(compileStatus[0] == 0) {
            glDeleteShader(objectID);
            return 0;
        }

        return objectID;
    }

    public static int linkProgram(int vertexShader, int fragmentShader) {
        final int programID = glCreateProgram();
        if(programID == 0) {
            return 0;
        }

        glAttachShader(programID, vertexShader);
        glAttachShader(programID, fragmentShader);

        glLinkProgram(programID);

        final int[] linkStatus = new int[1];
        glGetProgramiv(programID, GL_LINK_STATUS, linkStatus, 0);

        if (linkStatus[0] == 0) {
            glDeleteProgram(programID);
            return 0;
        }
        return programID;
    }



}
