package numberplatedetandroid.detection;

import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.Rect;

public abstract class Detector
{
	public abstract List< Rect > detect( Mat image );
	public abstract void drawInterestAreas( Mat image, List< Rect > shapes );
}
