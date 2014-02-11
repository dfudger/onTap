package fudgestudios.ontap;

import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewBottleActivity extends Activity
{
	private WineDBAdapter mDbHelper;
	private ImageView y;
	private Bitmap mImageBitmap;
	private TextView text;
	private String title;
	private String fname;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        Log.w("WineApp","View Bottle View");
        
        /*** Display the GUI ***/
        setContentView(R.layout.view_bottle);
        
        title = (String) getIntent().getExtras().get("title");
        text = (TextView) findViewById(R.id.textView1);
        text.setText(title);
                
        /*** Display the image on the screen ***/
        fname = (String) getIntent().getExtras().get("fileName");
        
        
        
        
        
    	BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        
        Bitmap bm = BitmapFactory.decodeFile(fname, options);
         
        ExifInterface exif = null;
		
        try {
			exif = new ExifInterface(fname);
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
       
        
        
        
        
        
        y = (ImageView) findViewById(R.id.imageView2);
 
        y.setImageBitmap(rotatedBitmap);
        y.setVisibility(View.VISIBLE);
        
        /*** Open up DB ***/
        mDbHelper = new WineDBAdapter(this);
        mDbHelper.open();   
        
        mDbHelper.close(); 
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

}
