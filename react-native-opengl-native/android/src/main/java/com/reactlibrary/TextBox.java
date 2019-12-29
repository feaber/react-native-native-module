package com.reactlibrary;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.PorterDuff.Mode;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.opengl.GLUtils;

import android.opengl.GLES20;

public class TextBox {
    private final String vertexShaderCode =
        "attribute vec2 aTexCoordinate;" +
        "varying vec2 vTexCoordinate;" +
        "uniform mat4 uMVPMatrix;" +
        "attribute vec4 vPosition;" +
        "void main() {" +
        "  gl_Position = uMVPMatrix * vPosition;" +
        "  vTexCoordinate = aTexCoordinate;" +
        "}";

    private final String fragmentShaderCode =
        "precision mediump float;" +
        "uniform vec4 vColor;" +
        "uniform sampler2D uTexture;" +
        "varying vec2 vTexCoordinate;" +
        "void main() {" +
        "  gl_FragColor = (texture2D(uTexture, vTexCoordinate));" +
        "}";

    private final FloatBuffer vertexBuffer;
    private final FloatBuffer texCoordsBuffer;
    private final ShortBuffer drawListBuffer;
    private final int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    private int mTextureHandle;
    private int mCoordsHandle;
    private int mMVPMatrixHandle;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    static final int TEX_COORDS_SIZE = 2;

    static float squareCoords[] = {
        -0.8f,  0.5f, 0.0f,  // top left
        -0.8f, -0.5f, 0.0f,  // bottom left
        0.8f, -0.5f, 0.0f,   // bottom right
        0.8f,  0.5f, 0.0f }; // top right

    static final float textureCoords[] = {
        1.0f, 0.0f,
        1.0f, 1.0f,
        0.0f, 1.0f,
        0.0f, 0.0f
    };

    private final float boxProportions = 0.5f / 0.8f;
    private final short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices

    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
    private final int texCoordsStride = TEX_COORDS_SIZE * 4;

    float color[] = { 0.2f, 0.709803922f, 0.898039216f, 1.0f };
    private int[] textureId = new int[1];
    private String message = "message not set";


    public TextBox() {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
            // (# of coordinate values * 4 bytes per float)
            squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(
            // (# of coordinate values * 2 bytes per short)
            drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

        // initialize texture coords buffer
        ByteBuffer tbb = ByteBuffer.allocateDirect(textureCoords.length * 4);
        tbb.order(ByteOrder.nativeOrder());
        texCoordsBuffer = tbb.asFloatBuffer();
        texCoordsBuffer.put(textureCoords);
        texCoordsBuffer.position(0);

        // prepare shaders and OpenGL program
        int vertexShader = MyRenderer.loadShader(
            GLES20.GL_VERTEX_SHADER,
            vertexShaderCode);
        int fragmentShader = MyRenderer.loadShader(
            GLES20.GL_FRAGMENT_SHADER,
            fragmentShaderCode);

        mProgram = GLES20.glCreateProgram(); // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, vertexShader); // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glBindAttribLocation(mProgram, 5, "aTexCoordinate");
        GLES20.glLinkProgram(mProgram);  // create OpenGL program executables

        prepareTexture(message);
    }

    public void draw(float[] mvpMatrix) {
        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(
            mPositionHandle, COORDS_PER_VERTEX,
            GLES20.GL_FLOAT, false,
            vertexStride, vertexBuffer);

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        //Set Texture Handles and bind Texture
        mTextureHandle = GLES20.glGetUniformLocation(mProgram, "uTexture");
        mCoordsHandle = GLES20.glGetAttribLocation(mProgram, "aTexCoordinate");

        //Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

        //Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId[0]);

        //Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(mTextureHandle, 0);

        //Pass in the texture coordinate information
        GLES20.glEnableVertexAttribArray(mCoordsHandle);
        GLES20.glVertexAttribPointer(mCoordsHandle, 2, GLES20.GL_FLOAT, false, texCoordsStride, texCoordsBuffer);

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        // Draw the square
        GLES20.glDrawElements(
            GLES20.GL_TRIANGLES, drawOrder.length,
            GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mCoordsHandle);
    }

    private void prepareTexture(String aText) {
        Paint textPaint = new Paint();
        int fontSize = 128;
        textPaint.setTextSize(fontSize);
        textPaint.setFakeBoldText(false);
        textPaint.setAntiAlias(true);
        textPaint.setARGB(255, 255, 255, 255);

        textPaint.setSubpixelText(true);
        textPaint.setXfermode(new PorterDuffXfermode(Mode.SCREEN));

        GLES20.glGenTextures(1, textureId, 0);

        float realTextWidth = textPaint.measureText(aText);

        int bitmapWidth = realTextWidth > (int)((fontSize * 2) - ((fontSize * 2) * 0.2f)) ? (int)(realTextWidth + (realTextWidth * 0.2f)) : (fontSize * 2);
        int bitmapHeight = (int)(bitmapWidth * boxProportions);

        // Calculate offsets to center text
        int bitmapOffsetX = (int) ((bitmapWidth - realTextWidth) / 2.0);
        int bitmapOffsetY = (int) (bitmapHeight / 2.0) + (int)((fontSize * 0.75) / 2.0);

        Bitmap textBitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        textBitmap.eraseColor(Color.argb(230, 30, 155, 200));

        Canvas bitmapCanvas = new Canvas(textBitmap);

        bitmapCanvas.drawText(aText, bitmapOffsetX, bitmapOffsetY, textPaint);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId[0]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

        // Assigns the OpenGL texture with the Bitmap
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, textBitmap, 0);

        // Free memory resources associated with this texture
        textBitmap.recycle();

        // After the image has been subloaded to texture, regenerate mipmaps
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);

        if (textureId[0] == 0)
        {
            throw new RuntimeException("Error loading texture.");
        }
    }

    public void setMessage(String message) {
        this.message = message;
        GLES20.glDeleteTextures(0, textureId, 1);
        prepareTexture(message);
    }

    public String getMessage() {
        return message;
    }
}
