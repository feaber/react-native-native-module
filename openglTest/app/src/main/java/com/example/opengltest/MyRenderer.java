package com.example.opengltest;

import android.opengl.Matrix;
import android.opengl.GLSurfaceView;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLES20;
import android.os.SystemClock;
import java.lang.Math;


public class MyRenderer implements GLSurfaceView.Renderer {
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mRotationMatrixX = new float[16];
    private final float[] mRotationMatrixY = new float[16];
    private final float[] mRotationMatrixZ = new float[16];

    private final float speedRatio = 1000.0f;
    private TextBox mTextBox = null;
    private String message;


    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        GLES20.glClearColor(0,1,1,1);
        mTextBox = new TextBox();
        mTextBox.setMessage(message);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        float[] scratch = new float[16];

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        // Calculate nice bounce against 3 axis
        float xRad = ((float)SystemClock.uptimeMillis() / speedRatio) % (float)(2 * Math.PI);
        float yRad = ((float)SystemClock.uptimeMillis() / speedRatio) % (float)(2 * Math.PI);
        float zRad = ((float)SystemClock.uptimeMillis() / speedRatio) % (float)(2 * Math.PI);

        float xAngle = (float)Math.sin(xRad) * 10;
        float yAngle = (float)Math.sin(yRad) * 15;
        float zAngle = (float)Math.sin(zRad) * 8;

        Matrix.setRotateM(mRotationMatrixX, 0, xAngle, 1.0f, 0, 0);
        Matrix.setRotateM(mRotationMatrixY, 0, yAngle, 0, 1.0f, 0);
        Matrix.setRotateM(mRotationMatrixZ, 0, zAngle, 0, 0, 1.0f);

        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrixX, 0);
        Matrix.multiplyMM(scratch, 0, scratch, 0, mRotationMatrixY, 0);
        Matrix.multiplyMM(scratch, 0, scratch, 0, mRotationMatrixZ, 0);

        mTextBox.draw(scratch);
    }

    @Override
    public  void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 2, 7);
    }

    public static int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);

        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    public void setMessage(String message) {
        this.message = message;
        if (this.mTextBox != null) {
            this.mTextBox.setMessage(message);
        }
    }

    public String getMessage() {
        return mTextBox.getMessage();
    }
}
