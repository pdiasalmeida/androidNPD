package numberplatedetandroid.main;

import java.util.ArrayList;
import java.util.List;
import android.app.Application;
import android.os.Environment;

public class NumberPlateDetApp extends Application
{
	public static final String EXT_STR_DTPATH = Environment.getExternalStorageDirectory().getPath();
	public static final String TESS_DATA_PATH = EXT_STR_DTPATH + "/tesseract";
	public static final String TESS_LANG = "eng";
	
	public List< Plate > plates = new ArrayList< Plate >();
}
