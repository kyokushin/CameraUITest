package jp.dip.firstnote.camerauitest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class DrawSurfaceView extends SurfaceView implements SurfaceView.OnTouchListener{
	
	private static final String TAG = "TouchSurfaceView";
	
	public Rect selectRect;
	
	private Point mStartPos;
	private SurfaceHolder mHolder;
	private Paint mRed;
	private boolean drawable = true;

	public DrawSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		selectRect = new Rect();
		mStartPos = new Point();
		mHolder = getHolder();
		mHolder.setFormat(PixelFormat.TRANSLUCENT);
		
    	mRed = new Paint();
    	mRed.setColor(Color.RED);
    	mRed.setStyle(Style.STROKE);
    	setOnTouchListener(this);
    	
    	
	}

	public boolean onTouch(View v, MotionEvent event) {
		float x = event.getX();
    	float y = event.getY();
    	
    	switch(event.getAction() & MotionEvent.ACTION_MASK){
    	case MotionEvent.ACTION_DOWN:
    		mStartPos.x = (int)x;
    		mStartPos.y = (int)y;
    		selectRect.left = (int) x;
    		selectRect.top = (int) y;
    		selectRect.right = (int) x + 1;
    		selectRect.bottom = (int) y + 1;
    		break;
    	case MotionEvent.ACTION_MOVE:
    	case MotionEvent.ACTION_UP:
    		if( mStartPos.x < x ){
    			selectRect.left = (int) x;
        		selectRect.right = mStartPos.x;
    		}
    		else {
    			selectRect.left = mStartPos.x;
        		selectRect.right = (int) x;
    		}
    		if( mStartPos.y < y ){
        		selectRect.top = (int) y;
        		selectRect.bottom = mStartPos.y;
    		}
    		else {
        		selectRect.top = mStartPos.y;
        		selectRect.bottom = (int) y;
    		}
			Log.d(TAG, "on up or move : " + selectRect);
    		break;
    	case MotionEvent.ACTION_CANCEL:
    		break;
    	default:
    		break;
    	}
    	
    	if( drawable){
    		Canvas canvas = mHolder.lockCanvas();
    		canvas.drawColor( 0, PorterDuff.Mode.CLEAR);
    		canvas.drawRect(selectRect, mRed);
    		mHolder.unlockCanvasAndPost(canvas);
    	}
		return true;
	}
	
	public Canvas getLockCanvas(){
		return mHolder.lockCanvas();
	}
	
	public void unLockCanvasAndPost( Canvas canvas ){
		mHolder.unlockCanvasAndPost(canvas);
	}
	
	public void setNotSelectable(){
		drawable = false;
	}
	public void setSelectable(){
		drawable = true;
	}
	
	

}
