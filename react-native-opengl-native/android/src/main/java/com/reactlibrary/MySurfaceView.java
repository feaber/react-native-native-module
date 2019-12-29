package com.reactlibrary;

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
        final String m = message;
        queueEvent(new Runnable() {
            @Override
            public void run() {
                renderer.setMessage(m);
            }}
        );
    }

    public String getMessage() {
        return renderer.getMessage();
    }
}
