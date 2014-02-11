package fudgestudios.ontap;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter 
{
    
//EXAMPLE
	private Context mContext;
	private ArrayList<String> urlArray = new ArrayList<String>();
	
    // Constructor
    public ImageAdapter(Context c, ArrayList <String> a) {
        mContext = c;
        urlArray = a;
    }
 
    @Override
	public int getCount() {
        return urlArray.size();
    }

    @Override
	public Object getItem(int position) {
        return null;
    }
 
    @Override
	public long getItemId(int position) {
        return 0;
    }
 
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(mContext);
        
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
        imageView.setPadding(8, 8, 8, 8);
        
        //Bitmap myBitmap = BitmapFactory.decodeFile(urlArray.get(position));
        
        try {
        	Log.w("onTap", "IN THE TRY!");
        	BitmapFactory.Options options = new BitmapFactory.Options();
            
        	options.inSampleSize = 4;
            //options.inJustDecodeBounds = true;
            Bitmap myBitmap = BitmapFactory.decodeFile(urlArray.get(position), options);
            
            int imageHeight = options.outHeight;
            int imageWidth = options.outWidth;
            String imageType = options.outMimeType;
            
            Log.w("onTap", "Image Height: " + imageHeight);
            Log.w("onTap", "Image Width: " + imageWidth);
            Log.w("onTap", "Image Type: " + imageType);
            
            imageView.setImageBitmap(myBitmap);
            
            return imageView;
            
          } catch(Exception e) {
            Log.e(null, null, e);
          }
        
        return null;
    }
    
    
    public static int calculateInSampleSize(
        BitmapFactory.Options options, int reqWidth, int reqHeight) {
	    // Raw height and width of image
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;
	
	    if (height > reqHeight || width > reqWidth) {
	
	        final int halfHeight = height / 2;
	        final int halfWidth = width / 2;
	
	        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
	        // height and width larger than the requested height and width.
	        while ((halfHeight / inSampleSize) > reqHeight
	                && (halfWidth / inSampleSize) > reqWidth) {
	            inSampleSize *= 2;
	        }
	    }
	
	    return inSampleSize;
    }
    
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
            int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

}