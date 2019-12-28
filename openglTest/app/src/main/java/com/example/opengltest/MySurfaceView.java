package com.example.opengltest;

import android.opengl.GLSurfaceView;
import android.content.Context;


public class MySurfaceView extends GLSurfaceView {
    private MyRenderer renderer;

    public MySurfaceView(Context context) {
        super(context);

        setEGLContextClientVersion(2);
        super.setEGLConfigChooser(8 , 8, 8, 8, 16, 0);

        renderer = new MyRenderer();
        renderer.setMessage("Hello World!!");
        setRenderer(renderer);
    }

    public void setMessage(String message) {
        this.renderer.setMessage(message);
    }

    public String getMessage() {
        return renderer.getMessage();
    }
}
