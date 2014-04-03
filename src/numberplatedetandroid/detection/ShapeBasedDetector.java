package numberplatedetandroid.detection;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;


public class ShapeBasedDetector extends Detector
{
	private static final Scalar _shapeColor = new Scalar(255, 0, 0, 255);
	
	int _threshLower;
	int _threshUpper;
	int _threshMethod;

	int _contMode;
	int _contMethod;

	int _approxEpsilon;
	boolean _approxClose;

	int _maxShapeArea;
	int _minShapeArea;

	float _ratio;
	float _rerror;
	
	public ShapeBasedDetector()
	{
		_threshLower = 120;
		_threshUpper = 255;
		_threshMethod = Imgproc.THRESH_BINARY;

		_contMode = Imgproc.RETR_TREE;
		_contMethod = Imgproc.CHAIN_APPROX_SIMPLE;

		_approxEpsilon = 3;
		_approxClose = true;

		_maxShapeArea = 200000;
		_minShapeArea = 400;

		_ratio = 4.5f;
		_rerror = 0.3f;
	}
	
	@Override
	public List<Rect> detect( Mat image )
	{
		Mat fimage;
		
		fimage = filter(image);
	
		return findInterestAreas(fimage);
	}

	@Override
	public void drawInterestAreas( Mat image, List<Rect> shapes )
	{
		for( Rect roi : shapes )
		{
			Core.rectangle( image, roi.tl(), roi.br(), _shapeColor );
		}
	}
	
	private Mat filter( Mat image )
	{
		Mat result = new Mat( image.size(), image.type() );
		
		Imgproc.threshold( image, result, _threshLower, _threshUpper, _threshMethod );

		return result;
	}
	
	private List<Rect> findInterestAreas( Mat image )
	{
		List<Rect> interestAreas = new ArrayList<Rect>();
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Mat hierarchy = new Mat();

		Imgproc.findContours( image, contours, hierarchy, _contMode, _contMethod );

		for( MatOfPoint cont : contours )
		{
			MatOfPoint2f cont2f = new MatOfPoint2f();
			cont.convertTo( cont2f, CvType.CV_32FC2);
			
			MatOfPoint2f approx = new MatOfPoint2f();
			Imgproc.approxPolyDP( cont2f, approx, _approxEpsilon, _approxClose );
			
			approx.convertTo( cont, CvType.CV_32S);

			if( cont.total() >= 4 && cont.total() <= 10 )
			{
				Rect roi = getDims( cont );

				if( validateShape( roi.width, roi.height ) )
				{
					interestAreas.add( roi );
				}
			}
		}
		
		return interestAreas;
	}
	
	private Rect getDims( MatOfPoint cont )
	{
		Rect roi = Imgproc.boundingRect(cont);
		
		return roi;
	}
	
	private boolean validateShape( double width, double height )
	{
		boolean result = false;
		double ratio = 0;
		double area = 0;

		ratio = width / height;
		area = height * width;

		// shapes found have to be approximately rectangular
		result = ( ratio >= _ratio-_ratio*_rerror && ratio <= _ratio+_ratio*_rerror );

		// shape area has to be between a certain threshold
		result = result && ( area < _maxShapeArea  );
		result = result && ( area > _minShapeArea  );

		return result;
	}

}
