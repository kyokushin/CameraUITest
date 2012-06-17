package jp.dip.firstnote.camerauitest;

import java.io.IOException;
import java.util.List;

import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.SurfaceHolder;

public class CameraView implements SurfaceHolder.Callback{
	
	private static final String TAG = "CameraViewHandler";

	private Camera mCam;
	private List<Size> supportedPreviewSize ;
	private byte[] mFrame;
	private Size mOptimalSize;
	
	public CameraView(){
		try{
			mCam = Camera.open();
		}
		catch ( Exception e ){
			Log.d(TAG, "Error: failed to open Camera > " + e.getMessage());
		}
		
		mCam.setPreviewCallback(new PreviewCallback(){

			public void onPreviewFrame(byte[] data, Camera camera) {
				
				if( mFrame == null
						|| mFrame.length != data.length ){
					mFrame = new byte[data.length];
				}
				for( int i=0; i<data.length; i++){
					mFrame[i] = data[i];
				}
			}
			
		});
		Camera.Parameters params = mCam.getParameters();
		supportedPreviewSize = params.getSupportedPreviewSizes();
		
	}
	
	public void  getFrame( byte[] frame ){
		synchronized( mFrame ){
			for( int i=0; i<mFrame.length; i++){
				frame[i] = mFrame[i];
			}
		}
		
	}
	public int getWidth(){
		return mOptimalSize.width;
	}
	public int getHeight(){
		return mOptimalSize.height;
	}
	
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			mCam.setPreviewDisplay(holder);
			mCam.startPreview();
		}
		catch( IOException e ){
			Log.d(TAG, "Error: setting Camera preview > " + e.getMessage());
		}
		
	}
		
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
		mOptimalSize = getOptimalPreviewSize( supportedPreviewSize, width, height );
		
		if( holder.getSurface() == null){
			return;
		}
		
		try {
			mCam.stopPreview();
		}
		catch( Exception e){
			Log.d(TAG, "Error: failed to stop preview > " + e.getMessage());
		}
		
		try {
			Camera.Parameters parameters = mCam.getParameters();
			parameters.setPreviewSize(mOptimalSize.width, mOptimalSize.height);
			
			mCam.setParameters(parameters);
			mCam.setPreviewDisplay(holder);
			mCam.startPreview();
		}
		catch( Exception e){
			Log.d(TAG, "Error: failed to start camera preview > " + e.getMessage());
		}
	}



	public void surfaceDestroyed(SurfaceHolder holder) {
		mCam.stopPreview();
		mCam.release();
	}

	private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null) return null;

        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Try to find an size match aspect ratio and size
        for (Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

}
