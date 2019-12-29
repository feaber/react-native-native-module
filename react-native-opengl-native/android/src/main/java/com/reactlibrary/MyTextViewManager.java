package com.reactlibrary;

import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;


public class MyTextViewManager extends SimpleViewManager<MySurfaceView> {
    public static final String REACT_CLASS = "MyGLBox";

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    public MySurfaceView createViewInstance(ThemedReactContext context) {
        return new MySurfaceView(context);
    }

    @ReactProp(name = "text")
    public void setText(MySurfaceView view, String text) {
        view.setMessage(text);
    }
}
