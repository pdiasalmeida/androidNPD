package numberplatedetandroid.main;

import android.app.Application;
import android.graphics.Bitmap;
import android.os.Environment;

public class NumberPlateDetApp extends Application
{
	public static final String EXT_STR_DTPATH = Environment.getExternalStorageDirectory().getPath();
	public static final String TESS_DATA_PATH = EXT_STR_DTPATH + "/tesseract";
	public static final String TESS_LANG = "eng";

	public Bitmap results;
	public Bitmap plate;
	public String textResult;
	
	//public Map<String, Bitmap> plates = new HashMap<String, Bitmap>();
}
