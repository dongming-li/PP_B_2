package com.example.myfirstapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.opengl.GLES20;
import android.opengl.GLES31;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.renderscript.Matrix4f;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Kyle on 9/30/2017.
 */

public class HexBoardRenderer implements GLSurfaceView.Renderer {
    public Hex HexBoard[];
    private int numHexsWide;
    private int numHexsTall;
    private Context context;
    public HexBoardRenderer(int width,int height, Context context) {
        super();
        this.context=context;
        numHexsTall=height;
        numHexsWide=width;
        HexBoard=new Hex[height*width];
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0f,0f,1f,1f);

        float width;
        float height;
        DisplayMetrics display=this.context.getResources().getDisplayMetrics();
        width=2f;
        height=2f;
        float j=-1.0f;
        float j1=j;
        float k=1.0f;
        float k1=k;
        float j2=width/ (float) (numHexsWide-0.33f);
        float k2=height/(float) (numHexsTall-1f);
        int row=0;
        for(int i=0;i<numHexsTall*numHexsWide;i++) {
            HexBoard[i] = new Hex((j+(-j/(float) (numHexsWide))), (k+(-k/(float) (numHexsTall))), width/(float) (numHexsWide), height/(float) (numHexsTall), i);
            if(numHexsWide%2==0) {
                if(i%(numHexsWide)==(numHexsWide-1)){
                    k1 -= k2;
                    j = j1;
                    k = k1;
                }

                else if (i % 2 == 0) {
                    j += j2;
                    k -= k2/2;

                } else if (i % 2 == 1) {

                    j += j2;
                    k = k1;
                }
            }
            else{
                if(i%(numHexsWide)==(numHexsWide-1)){
                    k1 -= k2;
                    j = j1;
                    k = k1;
                    row++;
                }
                else if(row%2==0) {
                    if (i % 2 == 0) {
                        j += j2;
                        k -= k2/2;
                    } else if (i % 2 == 1) {
                        j += j2;
                        k = k1;
                    }
                }
                else{
                    if(i%2==0){
                        j+=j2;
                        k=k1;
                    }
                    else{
                        j+=j2;
                        k-=k2/2;
                    }
                }
            }
        }
    }

    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT
                | GLES20.GL_DEPTH_BUFFER_BIT);
        for(int i=0;i<HexBoard.length;i++){
            HexBoard[i].draw();
        }

    }
}
