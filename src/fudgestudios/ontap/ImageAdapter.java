package fudgestudios.ontap;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
 
    //@Override MINE
    //public Object getItem(int position) {
    //   return mThumbIds[position];
    //}
    
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
        //imageView.setImageResource(mThumbIds[position]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(70, 70));
        
        Bitmap myBitmap = BitmapFactory.decodeFile(urlArray.get(position));
        imageView.setImageBitmap(myBitmap);
        
        return imageView;
    }
    
    
 // create a new ImageView for each item referenced by the Adapter
  /*  @Override
	public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            //imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        Bitmap myBitmap = BitmapFactory.decodeFile(urlArray.get(position));
        imageView.setImageBitmap(myBitmap);
        
        return imageView;
    }*/
    //END EXAMPLE

}