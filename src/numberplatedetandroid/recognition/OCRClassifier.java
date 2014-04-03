package numberplatedetandroid.recognition;

import numberplatedetandroid.main.NumberPlateDetApp;

import org.opencv.android.Utils;
import org.opencv.core.Size;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import android.graphics.Bitmap;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.googlecode.tesseract.android.TessBaseAPI.PageSegMode;

public class OCRClassifier
{
	private TessBaseAPI _baseApi;
	
	public OCRClassifier()
	{
		_baseApi = new TessBaseAPI();
		_baseApi.init( NumberPlateDetApp.TESS_DATA_PATH, NumberPlateDetApp.TESS_LANG );
		_baseApi.setPageSegMode( PageSegMode.PSM_SINGLE_BLOCK );
	}
	
	public Mat filter( Mat image )
	{
		Mat fimage = new Mat( image.size(), image.type() );
		
		Imgproc.blur( image, fimage, new Size(3,3) );
		Imgproc.threshold( fimage, fimage, 0, 255, Imgproc.THRESH_OTSU+Imgproc.THRESH_BINARY_INV );
		
		return fimage;
	}

	public Mat mopOpen( Mat image )
	{
		Mat rimage = new Mat( image.size(), image.type() );
		
		Mat element = Imgproc.getStructuringElement( Imgproc.MORPH_RECT, new Size( 2, 2 ) );
		Imgproc.morphologyEx( image, rimage, Imgproc.MORPH_ERODE, element );
		Imgproc.morphologyEx( rimage, rimage, Imgproc.MORPH_DILATE, element );
		
		return rimage;
	}

	public Mat mopClose( Mat image )
	{
		Mat rimage = new Mat( image.size(), image.type() );
		
		Mat element = Imgproc.getStructuringElement( Imgproc.MORPH_RECT, new Size( 2, 2 ) );
		Imgproc.morphologyEx( image, rimage, Imgproc.MORPH_DILATE, element );
		Imgproc.morphologyEx( rimage, rimage, Imgproc.MORPH_ERODE, element );
		
		return rimage;
	}

	public String classify( Mat image )
	{
		Mat fimage = filter( image );
		Mat cimage = mopClose( fimage );
		
		Bitmap img = Bitmap.createBitmap( cimage.cols(), cimage.rows(), Bitmap.Config.ARGB_8888 );
		Utils.matToBitmap( cimage, img );
		
		_baseApi.setImage( img );

		String recognizedText = _baseApi.getUTF8Text();

		return recognizedText;
	}
	
}
