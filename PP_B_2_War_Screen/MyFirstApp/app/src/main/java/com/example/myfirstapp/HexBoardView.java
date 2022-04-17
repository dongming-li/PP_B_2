package com.example.myfirstapp;

import android.app.ActionBar;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.PopupWindow;

import java.text.AttributedCharacterIterator;

/**
 * Created by Kyle on 10/9/2017.
 */

public class HexBoardView extends GLSurfaceView {
    private final HexBoardRenderer renderer;
    public hexboardcontextinfo contextInfo;
    PopupWindow hexOptions;


    public HexBoardView(Context context, AttributeSet attrs) {
        super(context,attrs);
        setEGLContextClientVersion(2);
        renderer = new HexBoardRenderer(3,3,context);
        setRenderer(renderer);
        contextInfo=new hexboardcontextinfo(null,false);
        //inHex=false;
        //touchedHex=null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e){
        contextInfo.notInHex();
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
            ax = (x - viewSizex/2) / (viewSizex/2);
            ay=1-(y/(viewSizey/2));

        if(e.getAction()==MotionEvent.ACTION_DOWN) {
            for (int i = 0; i < renderer.HexBoard.length; i++) {
                if (contextInfo.inHex==true) {
                    break;
                }
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
               if(pointChecker(hexPoints,hcx,hcy,ax,ay)){
                   contextInfo.inHex=true;
                   contextInfo.setHex(renderer.HexBoard[i]);
               }
            }
        }
        return contextInfo.inHex;
    }
    @Override
    public ContextMenu.ContextMenuInfo getContextMenuInfo()
    {
        return contextInfo;
    }
    private boolean pointChecker(float[] points, float cx, float cy, float x,float y){
        boolean Triangle1;
        boolean triangle2;
        boolean triangle3;
        boolean triangle4;
        boolean triangle5;
        boolean triangle6;
        //Using Barycentric co-ordinates to determine if x,y is in the Hexagon by checking its composite triangles.
        //Could be simplified to three Triangles, but this is proof of concept.
        Triangle1=barycentricChecker(x,y,cx,cy,points[0],points[1],points[2],points[3]);
        triangle2=barycentricChecker(x,y,cx,cy,points[2],points[3],points[4],points[5]);
        triangle3=barycentricChecker(x,y,cx,cy,points[4],points[5],points[6],points[7]);
        triangle4=barycentricChecker(x,y,cx,cy,points[6],points[7],points[8],points[9]);
        triangle5=barycentricChecker(x,y,cx,cy,points[8],points[9],points[10],points[11]);
        triangle6=barycentricChecker(x,y,cx,cy,points[10],points[11],points[0],points[1]);
        if(Triangle1||triangle2||triangle3||triangle4||triangle5||triangle6){
            return true;
        }
        else{
            return false;
        }
    }
    private boolean barycentricChecker(float x, float y, float px1,float py1, float px2, float py2, float px3, float py3){
        float alpha = ((py2 - py3)*(x - px3) + (px3 - px2)*(y - py3)) /
                ((py2 - py3)*(px1 - px3) + (px3 - px2)*(py1 - py3));
        float beta = ((py3 - py1)*(x - px3) + (px1 - px3)*(y - py3)) /
                ((py2 - py3)*(px1 - px3) + (px3 - px2)*(py1 - py3));
        float gamma = 1.0f - alpha - beta;
        if(alpha>0&&beta>0&&gamma>0){
            return true;
        }
        else{
            return false;
        }
    }
    public HexBoardRenderer getRenderer(){
        return this.renderer;
    }
}
class hexboardcontextinfo implements ContextMenu.ContextMenuInfo{
    public Hex touchedHex;
    public boolean inHex;
    public hexboardcontextinfo(Hex touchedHex, boolean inHex){
        this.touchedHex=touchedHex;
        this.inHex=inHex;
    }
    public void setHex(Hex touchedHex) {
        this.touchedHex = touchedHex;
        this.inHex = true;
    }
    public void notInHex(){
        inHex=false;
    }
}

