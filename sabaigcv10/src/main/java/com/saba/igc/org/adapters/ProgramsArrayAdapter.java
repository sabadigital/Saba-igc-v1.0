package com.saba.igc.org.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.saba.igc.org.R;
import com.saba.igc.org.models.SabaProgram;

import java.util.List;

class BitmapScaler
{
	// Scale and maintain aspect ratio given a desired width
	// BitmapScaler.scaleToFitWidth(bitmap, 100);
	public static Bitmap scaleToFitWidth(Bitmap b, int width)
	{
		float factor = width / (float) b.getWidth();
		return Bitmap.createScaledBitmap(b, width, (int) (b.getHeight() * factor), true);
	}


	// Scale and maintain aspect ratio given a desired height
	// BitmapScaler.scaleToFitHeight(bitmap, 100);
	public static Bitmap scaleToFitHeight(Bitmap b, int height)
	{
		float factor = height / (float) b.getHeight();
		return Bitmap.createScaledBitmap(b, (int) (b.getWidth() * factor), height, true);
	}
}

/**
 * @author Syed Aftab Naqvi
 * @create December, 2014
 * @version 1.0
 */
public class ProgramsArrayAdapter extends ArrayAdapter<SabaProgram>{

	public ProgramsArrayAdapter(Context context, List<SabaProgram> objects) {
		super(context, R.layout.upcoming_program_item, objects);
	}
	
	public static class ViewHolder{
		private ImageView			ivProgramImage;
		private TextView			tvProgramTitle;
		//private EllipsizingTextView	tvProgramDescription;
		private TextView	tvProgramDescription;
		private TextView			tvUpatedTime;
		private ProgressBar			ivImageProgressBar;
	}

	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
		// Get the data from position.
		final SabaProgram program = getItem(position);
		
		ViewHolder viewHolder =  null;
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.upcoming_program_item, parent, false);
			viewHolder.ivProgramImage = (ImageView)convertView.findViewById(R.id.ivProgram);
			viewHolder.tvProgramTitle = (TextView)convertView.findViewById(R.id.tvProgramTitle);
			//viewHolder.tvProgramDescription = (EllipsizingTextView)convertView.findViewById(R.id.tvProgramDescription);
			viewHolder.tvProgramDescription = (TextView)convertView.findViewById(R.id.tvProgramDescription);
			
//			viewHolder.tvUpatedTime = (TextView)convertView.findViewById(R.id.tvUpatedTime);
//			viewHolder.ivTweetImageProgressBar = (ProgressBar)convertView.findViewById(R.id.imageProgressBar);
			
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
			
		}	
		
		updateTweetVew(viewHolder, program);
//		viewHolder.tvReply.setOnClickListener(new OnClickListener() {
//			 
//		    @Override
//		    public void onClick(View v) {
//		        ((PullToRefreshListView) parent).performItemClick(v, position, 0);
//		    }
//		});
//		
//		viewHolder.tvRetweet.setOnClickListener(new OnClickListener() {
//			 
//		    @Override
//		    public void onClick(View v) {
//		    	((PullToRefreshListView) parent).performItemClick(v, position, 0);
//		    }
//		});
//		
//		viewHolder.tvFavorite.setOnClickListener(new OnClickListener() {
//			 
//		    @Override
//		    public void onClick(View v) {
//		    	((PullToRefreshListView) parent).performItemClick(v, position, 0);
//		    }
//		});
//		
//		viewHolder.ivProfileImage.setOnClickListener(new OnClickListener() {
//			 
//		    @Override
//		    public void onClick(View v) {
//		    	v.setTag(program.getUser().getScreenName());
//		    	((PullToRefreshListView) parent).performItemClick(v, position, 0);
//		    }
//		});
		
		return convertView;
	}
	
	private void updateTweetVew(final ViewHolder viewHolder, SabaProgram program){
		if(viewHolder == null || program == null){
			Log.e("error", "Invalid Arguments");
			return;
		}
	
		if(viewHolder.ivProgramImage != null){
			// loading user profile image via its URL into imageView directly.
			viewHolder.ivProgramImage.setImageResource(android.R.color.transparent);
			//ImageLoader imageLoader = ImageLoader.getInstance();
			//imageLoader.displayImage(program.getImageUrl(), viewHolder.ivProgramImage);
			if(program.getImageUrl() != null){
				display(viewHolder.ivProgramImage, program.getImageUrl(), null);
			}
		}
		
		if(viewHolder.tvProgramTitle != null){
			viewHolder.tvProgramTitle.setText(Html.fromHtml(program.getTitle()));
		}
		
		if(viewHolder.tvProgramDescription != null){
			Log.d("InAdapter: ", program.getDescription());
			viewHolder.tvProgramDescription.setText(Html.fromHtml(program.getDescription()));

            // forgot what following line was doing but lets keep it here...
            // currently, it was disabling the click events.
			//viewHolder.tvProgramDescription.setMovementMethod(LinkMovementMethod.getInstance());
		}	
	}
	
	public void display(ImageView img, String url, final ProgressBar spinner)
	{
		ImageLoader.getInstance().displayImage(url, img, new ImageLoadingListener(){
	
	        @Override
	        public void onLoadingStarted(String imageUri, View view) {
	        	if(spinner != null)
	        		spinner.setVisibility(View.VISIBLE); // set the spinner visible
	        }
	        
	        @Override
	        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
	        	if(spinner != null)
	        		spinner.setVisibility(View.GONE); // set the spinenr visibility to gone
	        }
	        
	        @Override
	        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
	        	if(spinner != null)
	        		spinner.setVisibility(View.GONE); //  loading completed set the spinne m[p 8978 p 5p b84etttttttttyetw4r visibility to gone
	        	
	        	int width=60;
//	        	if( view.getWidth() == 0){
//	        		width = view.getMeasuredWidth();
//	        	}else{
//	        		width = view.getWidth();
//	        	}
//	        	if( width == 0){
//	        		width = loadedImage.getWidth();
//	        	}
	         
	        	int height=60;
//	        	if( view.getHeight() == 0){
//	        		height = view.getMeasuredHeight();
//	        	}else{
//	        		height = view.getHeight();
//	        	}
//	        	if( height == 0 ){
//	        		height = loadedImage.getHeight();
//	        	}
	        	BitmapScaler.scaleToFitHeight(loadedImage, height);
	        	BitmapScaler.scaleToFitWidth(loadedImage, width);
	        }
	        
	        @Override
	        public void onLoadingCancelled(String imageUri, View view) {
	        }
		});
	}
}

// create a separate adapter for weekly programs:
// don't inherit WeeklyFramgment from  SabaFragmentBase and directly inherit from Fragment. 
// implement everything which we have in SabaFragmentBase in Weekly Fragment.
// think, about 2nd approach and use weekly programs to format the data they we have for other Fragment and detials 
// will show a frm the WeeklyModel.

