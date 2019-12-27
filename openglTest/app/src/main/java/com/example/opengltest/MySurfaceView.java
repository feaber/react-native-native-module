package com.example.opengltest;

import android.opengl.GLSurfaceView;
import android.content.Context;
import com.example.opengltest.MyRenderer;

public class MySurfaceView extends GLSurfaceView {
    public MySurfaceView(Context context) {
        super(context);
        renderer = new MyRenderer();
        setRenderer(renderer);
    }

    private MyRenderer renderer;
}
