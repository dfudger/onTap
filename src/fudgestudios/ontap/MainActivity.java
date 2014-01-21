package fudgestudios.ontap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import fudgestudios.ontap.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity 
{
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
	
    private static final String IMAGE_DIRECTORY_NAME = "onTap Photos";
    private Uri fileUri;
    
    private Button btnCapturePicture;
    
    private WineDBAdapter mDbHelper;
    public static final int INSERT_ID = Menu.FIRST;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mDbHelper = new WineDBAdapter(this);
        mDbHelper.open();
        
        btnCapturePicture = (Button) findViewById(R.id.button2);
        
        btnCapturePicture.setOnClickListener(new View.OnClickListener() 
    	{
            @Override
            public void onClick(View v) 
            {
        		captureImage();
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
    
    /*
     * Capturing Camera Image will lauch camera app requrest image capture
     */
    private void captureImage() 
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
     
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
     
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
     
        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		// if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) 
        {
            if (resultCode == RESULT_OK) 
               dispatchCreateBottleIntent();
            
            else if (resultCode == RESULT_CANCELED)
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            else
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
        }
	}

	/*
    * Display image from a path to ImageView
    */
//   private void previewCapturedImage() {
//       try {
//           //imgPreview.setVisibility(View.VISIBLE);
//
//           // bimatp factory
//           //BitmapFactory.Options options = new BitmapFactory.Options();
//
//           // downsizing image as it throws OutOfMemory Exception for larger
//           // images
//           //options.inSampleSize = 8;
//
//           //IS THIS NECESSARY
//           //final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
//             //      options);
//           
//           //IS THIS NECESSARY
//           //imgPreview.setImageBitmap(bitmap);
//           //dispatchEditBottleIntent();
//           
//       } catch (NullPointerException e) {
//           e.printStackTrace();
//       }
//       
//       //startActivity(bottleIntent); 
//   }

   /**
    * Here we store the file url as it will be null after returning from camera
    * app
    */
   @Override
   protected void onSaveInstanceState(Bundle outState) {
       super.onSaveInstanceState(outState);
    
       // save file url in bundle as it will be null on scren orientation
       // changes
       outState.putParcelable("file_uri", fileUri);
   }
    
   /*
    * Here we restore the fileUri again
    */
   @Override
   protected void onRestoreInstanceState(Bundle savedInstanceState) {
       super.onRestoreInstanceState(savedInstanceState);
    
       // get the file url
       fileUri = savedInstanceState.getParcelable("file_uri");
   }
   
   /**
    * Creating file uri to store image/video
    */
   public Uri getOutputMediaFileUri(int type) {
       return Uri.fromFile(getOutputMediaFile(type));
   }
    
   /*
    * returning image / video
    */
   private static File getOutputMediaFile(int type) {
    
       // External sdcard location
       File mediaStorageDir = new File(
               Environment
                       .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
               IMAGE_DIRECTORY_NAME);
    
       // Create the storage directory if it does not exist
       if (!mediaStorageDir.exists()) {
           if (!mediaStorageDir.mkdirs()) {
               Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                       + IMAGE_DIRECTORY_NAME + " directory");
               return null;
           }
       }
    
       // Create a media file name
       String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
               Locale.getDefault()).format(new Date());
       File mediaFile;
       if (type == MEDIA_TYPE_IMAGE) {
           mediaFile = new File(mediaStorageDir.getPath() + File.separator
                   + "IMG_" + timeStamp + ".jpg");
       } else {
           return null;
       }
    
       return mediaFile;
   }
   

    /**** Create Wine Intent for Activity ****/
    private void dispatchCreateBottleIntent()
    {
    	Intent bottleIntent = new Intent(this, CreateBottleActivity.class); //Request an image from an existing camera application
    	
    	//fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        
    	bottleIntent.putExtra("fileUri", fileUri.toString());
    	//intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
     
    	
    	//bottleIntent.putExtra("bitmap",bitmap);
    	
    	//bottleIntent.putExtra("fileName", ***);
    	startActivity(bottleIntent); 
    }
    	  
    //public void getNewPhotoInfo(View v)
    //{
    	//Intent myIntent = new Intent(v.getContext(), CreateBottleActivity.class);
        //startActivityForResult(myIntent, 0);
    //}
    
    public void viewGalleryResponse(View v) 
    {
    	Intent myIntent = new Intent(v.getContext(), GalleryActivity.class);
        startActivityForResult(myIntent, 0);
    }
}
