package numberplatedetandroid.main;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class DisplayResults extends Activity
{

	private NumberPlateDetApp app;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		app = (NumberPlateDetApp) getApplication();
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_display_results);
		
		ImageView lview = (ImageView) findViewById(R.id.resultsImageView);
		TextView tview = (TextView) findViewById(R.id.textView1);
		if( lview != null && tview != null )
		{	
			tview.setText(app.textResult);
			lview.setImageBitmap(app.plate);
		}
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
	}

	@Override
	public void onPause()
	{
		super.onPause();
	}

	public void onDestroy()
	{
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.display_results, menu);
		return true;
	}

}
