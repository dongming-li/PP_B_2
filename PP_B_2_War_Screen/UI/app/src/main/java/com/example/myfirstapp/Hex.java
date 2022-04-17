package com.example.myfirstapp;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Kyle on 10/9/2017.
 */

public class Hex {
        FloatBuffer vertexBuffer;
        ShortBuffer drawListBuffer;
        public float hexCoordinates[]=new float[24];
        private int mProgram;
        float Color[]={0.0f,1.0f,0.0f,1.0f};
        private short drawOrder[]={0,1,2,3,4,5,6,7};

        static final int COORDS_PER_VERTEX=3;
        public Hex(float x1, float y1, float height, float width, float offsetX, float offsetY) {
            hexCoordinates=new float[]{
                    x1,y1,0.0f, //center
                    (width)/2.0f+x1,0.0f+y1,0.0f, //center right
                    (width)/4+x1,height/2+y1,0.0f, //upper right
                    (-width)/4+x1,height/2+y1,0.0f, //upper left
                    (-width)/2.0f+x1,0.0f+y1,0.0f, // center left
                    (-width)/4.0f+x1,-height/2.0f+y1,0.0f, //lower left
                    (width)/4.0f+x1,-height/2.0f+y1,0.0f, //lower right
                    (width)/2+x1,0.0f+y1,0.0f //center right

            };
            ByteBuffer bb = ByteBuffer.allocateDirect(hexCoordinates.length * 4);
            bb.order(ByteOrder.nativeOrder());
            vertexBuffer = bb.asFloatBuffer();
            vertexBuffer.put(hexCoordinates);
            vertexBuffer.position(0);

            ByteBuffer bdl = ByteBuffer.allocateDirect(drawOrder.length * 2);
            bdl.order(ByteOrder.nativeOrder());
            drawListBuffer = bdl.asShortBuffer();
            drawListBuffer.put(drawOrder);
            drawListBuffer.position(0);

            int vertexShader = HexBoardRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                    vertexShaderCode);
            int fragmentShader = HexBoardRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                    fragmentShaderCode);

            // create empty OpenGL ES Program
            mProgram = GLES20.glCreateProgram();

            // add the vertex shader to program
            GLES20.glAttachShader(mProgram, vertexShader);

            // add the fragment shader to program
            GLES20.glAttachShader(mProgram, fragmentShader);

            // creates OpenGL ES program executables
            GLES20.glLinkProgram(mProgram);
        }
    private int mPositionHandle;
    private int mColorHandle;

    private final int vertexCount = hexCoordinates.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    public void draw() {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, Color, 0);

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, vertexCount);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
        private final String vertexShaderCode =
                "attribute vec4 vPosition;" +
                        "void main() {" +
                        "  gl_Position = vPosition;" +
                        "}";

        private final String fragmentShaderCode =
                "precision mediump float;" +
                        "uniform vec4 vColor;" +
                        "void main() {" +
                        "  gl_FragColor = vColor;" +
                        "}";


}


