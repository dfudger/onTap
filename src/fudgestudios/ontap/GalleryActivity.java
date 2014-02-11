package fudgestudios.ontap;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * @author dfudger
 *
 */

public class GalleryActivity extends Activity
{
	private WineDBAdapter mDbHelper;
    private Cursor mCursor;
    ArrayList<String> mArrayList = new ArrayList<String>();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.gallery);
	  
	    GridView gridview = (GridView) findViewById(R.id.gridview);
	    //gridview.setAdapter(new ImageAdapter(this, mArrayList));
	    gridview.setAdapter(new MyAdapter(this, mArrayList));
	    gridview.setOnItemClickListener(new OnItemClickListener() 
	    {
	        @Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) 
	        {
	            dispatchViewBottleIntent(position);
	        }
	    });
	    
	    mDbHelper = new WineDBAdapter(this);
        mDbHelper.open();
        
        fetchImages();

	}	
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
	
	public void fetchImages()
	{
		System.out.println("Fetch Images");
		mCursor = mDbHelper.fetchAllWines();
		for(mCursor.moveToFirst(); !mCursor.isAfterLast(); mCursor.moveToNext()) 
		{
			Log.w("onTap","In Fetch Images");
			Log.w("onTap", mCursor.getString(2));
			//The Cursor is now set to the right position
			mArrayList.add(mCursor.getString(2));
		}
	} 

	
	
	/**** View Wine Info ****/
    private void dispatchViewBottleIntent(int position)
    {
    	position++;
    	//Log.w("WineApp","In Dispatch");
    	Cursor curs;
    	//Log.w("WineApp", ""+position);
    	curs = mDbHelper.fetchWine(position);
    	
    	Intent bottleIntent = new Intent(this, ViewBottleActivity.class); //Request an image from an existing camera application
    	
    	System.out.println("File Name Before Sent: " + curs.getString(1) + "Image Name: " + curs.getString(2));
    	bottleIntent.putExtra("title", curs.getString(1));
    	bottleIntent.putExtra("fileName", curs.getString(2));
    	startActivity(bottleIntent); 
    }
    
    
    
    /**** Image Adapter Info ****/
    private class MyAdapter extends BaseAdapter
    {
    	
    	
    	private Context mContext;
    	private ArrayList<String> urlArray = new ArrayList<String>();
    	private LayoutInflater inflater;
    	
    	// Constructor
    	public MyAdapter(Context c, ArrayList <String> a)
        {
            inflater = LayoutInflater.from(c);

    		 mContext = c;
    	     urlArray = a;
        }

    	public int getCount() {
            return urlArray.size();
        }

        //public Object getItem(int i)
        //{
        //    return items.get(i);
        //}
    	public Object getItem(int position) {
            return null;
        }
    	
    	
        //public long getItemId(int i)
        //{
        //    return items.get(i).drawableId;
        //}
        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View view, ViewGroup viewGroup)
        {
        	Log.w("onTap", "In Get View!");
        	
            View v = view;
            ImageView picture;
            
            if(v == null)
            {
              v = inflater.inflate(R.layout.gridview_item, viewGroup, false);
              v.setTag(R.id.picture, v.findViewById(R.id.picture));   
            }
            
            picture = (ImageView)v.getTag(R.id.picture);
            
            try {

            	BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                
                Bitmap bm = BitmapFactory.decodeFile(urlArray.get(position), options);
                 
                ExifInterface exif = null;
        		
                try {
        			exif = new ExifInterface(urlArray.get(position));
        		} catch (IOException e) {
        			e.printStackTrace();
        		}
                
        		String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
                 
                int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
                int rotationAngle = 0;
                if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
                if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
                if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
                 
                Matrix matrix = new Matrix();
                matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
                 
                Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, options.outWidth, options.outHeight, matrix, true);
                
            	picture.setImageBitmap(rotatedBitmap);
                
                return v;
                
              } catch(Exception e) {
                Log.e(null, null, e);
              }
            
            
            //picture.setImageResource(item.drawableId);
            

            return null;
        }

    }
}
