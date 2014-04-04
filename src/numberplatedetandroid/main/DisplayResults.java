package numberplatedetandroid.main;

import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

		ListView list = (ListView) findViewById(R.id.listView1);
		CustomAdapter adapter = new CustomAdapter( this, app.plates );
		list.setAdapter(adapter);
		
		if( !(app.plates.size() > 0) )
		{
			String message = "No possible plates found!";
			
			Toast toast = Toast.makeText( this, message, Toast.LENGTH_SHORT);
			toast.show();
			this.finish();
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

	public class CustomAdapter extends BaseAdapter
	{
		List<Plate> values;
		Context context;
		LayoutInflater mInflater;

		public CustomAdapter( Context context, List<Plate> entries )
		{
			this.context = context;
			this.values = entries;
			this.mInflater  = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount()
		{
			return values.size();
		}

		@Override
		public Object getItem( int position )
		{
			return values.get(position);
		}

		@Override
		public long getItemId( int position )
		{
			return position;
		}

		@Override
		public View getView( int position, View convertView, ViewGroup parent )
		{
			Plate p = (Plate) getItem(position);
			ViewHolder holder = null;
			ImageView imV = null;
			TextView txtV = null;
			
			if( convertView == null )
			{
				convertView = mInflater.inflate(R.layout.image_view_layout, null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			}
			holder = (ViewHolder) convertView.getTag();
			
			imV = holder.getImage();
			imV.setImageBitmap(p.getImage());
			
			txtV = holder.getCode();
			txtV.setText(p.getCode());

			return convertView;
		}

	}
	
	private class ViewHolder
	{      
	    private View mRow;
	    private ImageView image;
	    private TextView code;

		public ViewHolder(View row)
		{
	    	mRow = row;
	    	image = null;
	    	code = null;
		}

		public ImageView getImage()
		{
			if( image == null )
			{
				image = (ImageView) mRow.findViewById(R.id.resultsImageView);
			}
			
			return image;
		}

		public TextView getCode() {
			if( code == null )
			{
				code = (TextView) mRow.findViewById(R.id.textView1);
			}
			
			return code;
		}    	
	}

}
