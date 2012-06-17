package jp.dip.firstnote.camerauitest;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CameraUITestActivity extends Activity {
	
	private static final String TAG = "CameraUITest";
	private DrawSurfaceView mOverrayView;
	private SurfaceHolder mOverrayHolder;
	private Rect mTouchRect;
	private Point mStartPos;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //�J�����pSurfaceView
        CameraView camCallback = new CameraView();
        SurfaceView camView = (SurfaceView)findViewById(R.id.cam_view);
        SurfaceHolder holder = camView.getHolder();
        holder.setType( SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS );
        holder.addCallback( camCallback );
        
        //�I�[�o�[���C�\���p
        mOverrayView = (DrawSurfaceView)findViewById(R.id.overray);
        mOverrayHolder = mOverrayView.getHolder();
       
        //UI�p
        Button button = (Button)findViewById(R.id.start_button);
        button.setOnClickListener(new OnClickListener(){
        	//�I�[�o�[���C�\���Ƃ̘A�g
        	//�{�^���������ɃL�����o�X�N���A�Ɨ̈�ێ�
        	private boolean doing = false;
        	
			public void onClick(View v) {
				Button button = (Button)v;
				
				if( doing){
					button.setText("Start");
					mOverrayView.setSelectable();
					
				}
				else {
					button.setText("Stop");
					mOverrayView.setNotSelectable();
					
					Canvas canvas = mOverrayHolder.lockCanvas();
					canvas.drawColor(0, PorterDuff.Mode.CLEAR);
					mOverrayHolder.unlockCanvasAndPost(canvas);
				
					mTouchRect = new Rect(mOverrayView.selectRect);
				}
				doing = !doing;
				
			}
        	
        });
        
    }
        
}