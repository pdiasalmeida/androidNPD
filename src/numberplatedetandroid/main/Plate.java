package numberplatedetandroid.main;

import android.graphics.Bitmap;

public class Plate
{
	private String code;
	private Bitmap image;
	
	public Plate( String code, Bitmap image)
	{
		this.code = code;
		this.image = image;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public Bitmap getImage()
	{
		return image;
	}

	public void setImage(Bitmap image)
	{
		this.image = image;
	}
	
	
}
