package com.kele.opengldemo;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyRender implements GLSurfaceView.Renderer {

    private static final String TAG = "MyRender";

    private Triangle mTriangle;


    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private float[] mRotationMatrix = new float[16];

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        Log.w(TAG, "onSurfaceCreated");
        mTriangle = new Triangle();
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {
        Log.w(TAG, "onSurfaceChanged：" + "width：" + "   height：" + i1);
        GLES20.glViewport(0, 0, i, i1);

        float ratio = (float) i / i1;

        // 这个投影矩阵被应用于对象坐标在onDrawFrame（）方法中
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        Log.w(TAG, "onDrawFrame");

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        float[] scratch = new float[16];
        long time = SystemClock.uptimeMillis() % 4000L;
        float angle = 0.09f * ((int) time);
        Matrix.setRotateM(mRotationMatrix, 0, angle, 0, 0, -1.0f);

        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);

        // Draw shape
        mTriangle.draw(scratch);
    }
}
