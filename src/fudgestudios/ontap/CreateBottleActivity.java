package fudgestudios.ontap;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
        Log.w("WineApp","Edit Bottle View");
        
        /*** Display the GUI ***/
        setContentView(R.layout.new_bottle);
        
        y = (ImageView) findViewById(R.id.imageView2);
        y.setVisibility(View.VISIBLE);
        
        myUri = Uri.parse(getIntent().getExtras().getString("fileUri"));
    	Log.w("onTap", myUri.getPath());
    	Log.w("onTap", "This worked!");
        BitmapFactory.Options options = new BitmapFactory.Options();

        // downsizing image as it throws OutOfMemory Exception for larger images
        //options.inSampleSize = 10;

        final Bitmap bitmap = BitmapFactory.decodeFile(myUri.getPath(),
                options);

        y.setImageBitmap(bitmap);

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
