package com.example.myfirstapp;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.text.AttributedCharacterIterator;

/**
 * Created by Kyle on 10/9/2017.
 */

public class HexBoardView extends GLSurfaceView {
    private final HexBoardRenderer renderer;
    boolean inHex;
    Hex touchedHex;
    public HexBoardView(Context context, AttributeSet attrs) {
        super(context,attrs);
        setEGLContextClientVersion(2);
        renderer = new HexBoardRenderer(15,12,context);
        setRenderer(renderer);
        inHex=false;
        touchedHex=null;

    }
    @Override
    public boolean onTouchEvent(MotionEvent e){
        float x;
        float y;
        float hcx=0.0f;
        float hcy=0.0f;
        float hexPoints[]=new float[12];
        x=e.getX();
        y=e.getY();
        float ax;
        float ay;
        float viewSizey;
        viewSizey=this.getHeight();
        float viewSizex;
        viewSizex=this.getWidth();
        if(x<viewSizex/2) {
            ax = (x - viewSizex) / (viewSizex/2);
        }
        else{
            ax = (((x)/(viewSizex))-(viewSizex/2))/(viewSizex/2);
        }
        if(y<viewSizey/2){
            ay=(y-viewSizey)/viewSizey;
        }
        if(e.getAction()==MotionEvent.ACTION_DOWN) {
            for (int i = 0; i < renderer.HexBoard.length; i++) {
                hcx = renderer.HexBoard[i].hexCoordinates[0];
                hcy = renderer.HexBoard[i].hexCoordinates[1]; //center
                hexPoints[0] = renderer.HexBoard[i].hexCoordinates[3];
                hexPoints[1] = renderer.HexBoard[i].hexCoordinates[4]; //center right
                hexPoints[2] = renderer.HexBoard[i].hexCoordinates[6];
                hexPoints[3] = renderer.HexBoard[i].hexCoordinates[7]; //upper right
                hexPoints[4] = renderer.HexBoard[i].hexCoordinates[9];
                hexPoints[5] = renderer.HexBoard[i].hexCoordinates[10]; //upper left
                hexPoints[6] = renderer.HexBoard[i].hexCoordinates[12];
                hexPoints[7] = renderer.HexBoard[i].hexCoordinates[13]; //center left
                hexPoints[8] = renderer.HexBoard[i].hexCoordinates[15];
                hexPoints[9] = renderer.HexBoard[i].hexCoordinates[16]; //lower left
                hexPoints[10] = renderer.HexBoard[i].hexCoordinates[18];
                hexPoints[11] = renderer.HexBoard[i].hexCoordinates[19]; //lower right
                if (x == hcx && y == hcy) {
                    inHex = true;
                    touchedHex = renderer.HexBoard[i];
                    break;
                }
                for (int j = 0; j < hexPoints.length - 1; j++) {
                    if (x == hexPoints[j] && y == hexPoints[j + 1]) {
                        inHex = true;
                        touchedHex = renderer.HexBoard[i];
                        break;
                    }
                }
                for (int j = 0; j < hexPoints.length; j++) {
                    if (j == 0) {
                        if (x > hexPoints[j]) {
                            inHex = false;
                            break;
                        }
                    }
                    if (j == 2) {
                        if (y > hexPoints[j]) {
                            inHex = false;
                            break;
                        }
                    }
                    if (j == 6) {
                        if (x < hexPoints[j]) {
                            inHex = false;
                            break;
                        }
                    }
                    if (j == 8) {
                        if (y < hexPoints[j]) {
                            inHex = false;
                            break;
                        }
                    }
                }

                if (inHex == true) {
                    break;
                }
            }
        }
        return inHex;
    }
}
