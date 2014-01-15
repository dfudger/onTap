package fudgestudios.ontap;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
        //Log.w("WineApp","View Bottle View");
        
        /*** Display the GUI ***/
        setContentView(R.layout.view_bottle);
        
        title = (String) getIntent().getExtras().get("title");
        text = (TextView) findViewById(R.id.textView1);
        text.setText(title);
                
        /*** Display the image on the screen ***/
        fname = (String) getIntent().getExtras().get("fileName");
        mImageBitmap = BitmapFactory.decodeFile(fname);
        
        y = (ImageView) findViewById(R.id.imageView2);
 
        y.setImageBitmap(mImageBitmap);
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
