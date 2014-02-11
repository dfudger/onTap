package fudgestudios.ontap;

import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


public class CreateBottleActivity extends Activity
{
	private int mWineNumber = 1;
	private WineDBAdapter mDbHelper;
	private ImageView y;
	private Bitmap mImageBitmap;
	
	private String bottleTitle;
	EditText mEditTitle;
	Uri myUri;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        /*** Display the GUI ***/
        setContentView(R.layout.new_bottle);
        
        y = (ImageView) findViewById(R.id.imageView2);
        y.setVisibility(View.VISIBLE);
        
        myUri = Uri.parse(getIntent().getExtras().getString("fileUri"));
    	
    	BitmapFactory.Options bounds = new BitmapFactory.Options();
        Bitmap bm = BitmapFactory.decodeFile(myUri.getPath(), bounds);
         
        ExifInterface exif = null;
		try {
			exif = new ExifInterface(myUri.getPath());
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
         
         Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);
         
         y.setImageBitmap(rotatedBitmap);
        
        
        
        /*** Open up DB ***/
        mDbHelper = new WineDBAdapter(this);
        mDbHelper.open();   
        
        Button btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() 
        {
            @Override
			public void onClick(View v) 
            {
            	createWine();
            }
        });
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
	
    private void createWine() 
    {
//    	Uri myUri = Uri.parse(getIntent().getExtras().getString("fileUri"));
    	Log.w("onTap", myUri.getPath());
    	Log.w("onTap", "This worked!");
    	
    	mEditTitle = (EditText) findViewById(R.id.editTitle);
		bottleTitle = mEditTitle.getText().toString();
		
		mDbHelper.createWine(bottleTitle, myUri.getPath());
		mDbHelper.close();
		finish();
    }

}
