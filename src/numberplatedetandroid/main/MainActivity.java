package numberplatedetandroid.main;

import java.util.List;

import numberplatedetandroid.detection.Detector;
import numberplatedetandroid.detection.ShapeBasedDetector;
import numberplatedetandroid.recognition.OCRClassifier;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;

public class MainActivity extends Activity implements CvCameraViewListener2
{
	private static final String TAG = "Android License Plate Detector";
	private NumberPlateDetApp app;
	
	private CameraBridgeViewBase _mOpenCvCameraView;
	private Mat _cRgba;
	private Mat _cGray;
	private boolean _setSaveNextFrame;
	
	private Detector _detector;
	private OCRClassifier _classifier;
	
	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this)
	{
		@Override
		public void onManagerConnected(int status)
		{
			switch(status)
			{
				case LoaderCallbackInterface.SUCCESS:
				{
					Log.i( TAG, "OpenCV loaded successfully" );
					_mOpenCvCameraView.enableView();
				} break;
				default:
				{
					super.onManagerConnected(status);
				} break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		Log.i( TAG, "called onCreate" );
		
		super.onCreate(savedInstanceState);
		
		app = (NumberPlateDetApp) getApplication();
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_main);
		
		_mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.HelloOpenCvView);
		_mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
		_mOpenCvCameraView.setCvCameraViewListener(this);
		
		_mOpenCvCameraView.setOnTouchListener(new CustomOnTouchListener(this));
		
		_detector = new ShapeBasedDetector();
		_classifier = new OCRClassifier();
		_setSaveNextFrame = false;
	}

	@Override
	public void onResume()
	{
		super.onResume();
		OpenCVLoader.initAsync( OpenCVLoader.OPENCV_VERSION_2_4_8, this, mLoaderCallback );
	}

	@Override
	public void onPause()
	{
		super.onPause();
		if( _mOpenCvCameraView != null )
		{
			_mOpenCvCameraView.disableView();
		}
	}

	public void onDestroy()
	{
		super.onDestroy();
		if( _mOpenCvCameraView != null )
		{
			_mOpenCvCameraView.disableView();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onCameraViewStarted(int width, int height) {}

	@Override
	public void onCameraViewStopped() {}

	@Override
	public Mat onCameraFrame(CvCameraViewFrame inputFrame)
	{
		if(_setSaveNextFrame)
		{
			_cRgba = inputFrame.rgba();
			_cGray = inputFrame.gray();
			
			runDetect();
			_setSaveNextFrame = false;
		}

		return inputFrame.rgba();
	}

	public void runDetect()
	{		
		app.plates.clear();
		List<Rect> shapes = _detector.detect( _cGray );
		_detector.drawInterestAreas( _cRgba, shapes );
		
		for( Rect roi : shapes )
		{
			Mat pplate = new Mat( _cGray, roi );
			String code = _classifier.classify( pplate );
			if( code != null && code.length() > 0 )
			{
				Bitmap bplate = Bitmap.createBitmap( pplate.cols(), pplate.rows(), Bitmap.Config.ARGB_8888 );
				Utils.matToBitmap( pplate, bplate );
				
				Plate p = new Plate( code, bplate );
				app.plates.add(p);
			}
		}

		Intent intent = new Intent( this, DisplayResults.class );		
		startActivity(intent);
	}
	
	private class CustomOnTouchListener implements OnTouchListener
	{
		MainActivity _act;
		
		public CustomOnTouchListener( MainActivity act )
		{
			this._act = act;
		}

		@Override
		public boolean onTouch( View v, MotionEvent event )
		{
			_act._setSaveNextFrame = true;
			
			return false;
		}
		
	}

}
