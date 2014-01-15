package fudgestudios.ontap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity 
{
	//private int mWineNumber = 1;
	//private ImageView mImageView;
	private Bitmap mImageBitmap;
	private String imageFileName;
	private String mCurrentPhotoPath;
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	public static final int MEDIA_TYPE_IMAGE = 1;
	//private Uri fileUri;
	
	/****DB Variables****/
	private WineDBAdapter mDbHelper;
    public static final int INSERT_ID = Menu.FIRST;
	private static final String JPEG_FILE_PREFIX = "onTap";
	private static final String JPEG_FILE_SUFFIX = ".jpg";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mDbHelper = new WineDBAdapter(this);
        mDbHelper.open();     
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    /**** Take a Photo with the Camera App ****/
    private void dispatchTakePictureIntent(int actionCode)
    {
    	//Compose a camera intent
    	Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //Request an image from an existing camera application

    	startActivityForResult(takePictureIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE); //Start the camera intent
    }
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		if (resultCode == RESULT_OK) 
		{
			try 
			{
				handleSmallCameraPhoto(data);
			} 
			catch (FileNotFoundException e) 
			{
				e.printStackTrace();
			}
		}	 
	}

    public void photoButtonResponse(View v) 
    {
    	//Log.w("WineApp","LOL taking photo.");
    	dispatchTakePictureIntent(1);
    }
    
    public static boolean isIntentAvailable(Context context, String action)
    {
    	final PackageManager packageManager = context.getPackageManager();
    	final Intent intent = new Intent(action);
    	
    	List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
    	
    	return list.size() > 0;
    }
    
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = 
            new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName = "onTAp_" + timeStamp;
        File image = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));
        
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.w("onTap", "!!Image Path: " + mCurrentPhotoPath);
        
        return image;
    }
    
    private void galleryAddPic() {
        
    	Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        
        this.sendBroadcast(mediaScanIntent);
    }
    
    private void handleSmallCameraPhoto(Intent intent) throws FileNotFoundException 
    {
    	Log.w("onTap","In the handleSmallCameraPhoto");
    	
    	Bundle extras = intent.getExtras();
		mImageBitmap = (Bitmap) extras.get("data"); //This image is the icon, not good for much else
		
		//Save the photo
		File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "onTapAlbum");    
		
		File f = null;
		try {
			f = createImageFile();
		} catch (IOException e) {
			Log.w("onTap", "OOOOOPS");
			e.printStackTrace();
		}
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
		
		imageFileName = imageFileName + ".jpg";
		
		
		
		/*Log.w("onTap","Spot Two");
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		mImageBitmap.compress(Bitmap.CompressFormat.JPEG, 80, bytes);
		Log.w("onTap","Spot Three");
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		imageFileName = "onTap_" + timeStamp + ".jpg";
		Log.w("onTap","Spot Four");
		imageFileName = (Environment.getExternalStorageDirectory() + File.separator + imageFileName);
		
		try 
		{
			Log.w("onTap","Spot Five");
			f.createNewFile();
		} 
		catch (IOException e) 
		{
			Log.w("onTap","Spot Five - Oops");
			e.printStackTrace();
		}*/
		/*Log.w("onTap","Spot Six");
		//write the bytes in file
		FileOutputStream fo = new FileOutputStream(f);
		try 
		{
			fo.write(bytes.toByteArray());
		} 
		catch (IOException e1) 
		{
			e1.printStackTrace();
		}

		try 
		{
			fo.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}*/
		Log.w("onTap", "I'm done?");
		
		dispatchEditBottleIntent();
		//createWine();
    }

    /**** Edit Wine Info ****/
    private void dispatchEditBottleIntent()
    {
    	Intent bottleIntent = new Intent(this, CreateBottleActivity.class); //Request an image from an existing camera application
    	bottleIntent.putExtra("bitmap", mImageBitmap);
    	System.out.println("File Name Before Sent: " + imageFileName);
    	bottleIntent.putExtra("fileName", imageFileName);
    	startActivity(bottleIntent); 
    }
    	  
    public void getNewPhotoInfo(View v)
    {
    	Intent myIntent = new Intent(v.getContext(), CreateBottleActivity.class);
        startActivityForResult(myIntent, 0);
    }
    
    public void viewGalleryResponse(View v) 
    {
    	Intent myIntent = new Intent(v.getContext(), GalleryActivity.class);
        startActivityForResult(myIntent, 0);
    }
}
