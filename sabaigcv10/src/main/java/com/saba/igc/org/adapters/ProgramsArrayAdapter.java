package com.saba.igc.org.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.text.method.LinkMovementMethod;
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
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
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
		super(context, R.layout.program_item, objects);
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
			if(program.getProgramName().equalsIgnoreCase("Weekly Programs")==true){
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.weekly_program_item, parent, false);
			} else if(program.getProgramName().equalsIgnoreCase("Community Announcements")==true){
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.community_announcements_item, parent, false);
			} else {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.program_item, parent, false);
			}

			viewHolder.ivProgramImage = (ImageView)convertView.findViewById(R.id.ivProgram);
			viewHolder.tvProgramTitle = (TextView)convertView.findViewById(R.id.tvProgramTitle);
			//viewHolder.tvProgramDescription = (EllipsizingTextView)convertView.findViewById(R.id.tvProgramDescription);
			viewHolder.tvProgramDescription = (TextView)convertView.findViewById(R.id.tvProgramDescription);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
			
		}	
		
		updateProgramsView(viewHolder, program);
		return convertView;
	}
	
	private void updateProgramsView(final ViewHolder viewHolder, SabaProgram program){
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

		// if we are in weekly programs then we want to display the week day images.
		if(program.getImageUrl()==null && (program.getProgramName().equalsIgnoreCase("Weekly Programs")==true) ){
			int index = program.getTitle().indexOf(',');
			if(index != -1){
				String day = program.getTitle().substring(0, index);
				if("saturday".compareToIgnoreCase(day)==0){
					viewHolder.ivProgramImage.setImageResource(R.drawable.ic_saturday);
				} else if("sunday".compareToIgnoreCase(day)==0){
					viewHolder.ivProgramImage.setImageResource(R.drawable.ic_sunday);
				} else if("monday".compareToIgnoreCase(day)==0){
					viewHolder.ivProgramImage.setImageResource(R.drawable.ic_monday);
				} else if("tuesday".compareToIgnoreCase(day)==0){
					viewHolder.ivProgramImage.setImageResource(R.drawable.ic_tuesday);
				} else if("wednesday".compareToIgnoreCase(day)==0){
					viewHolder.ivProgramImage.setImageResource(R.drawable.ic_wednesday);
				} else if("thursday".compareToIgnoreCase(day)==0){
					viewHolder.ivProgramImage.setImageResource(R.drawable.ic_thursday);
				} else if("friday".compareToIgnoreCase(day)==0){
					viewHolder.ivProgramImage.setImageResource(R.drawable.ic_friday);
				}
			}
		}

		if(viewHolder.tvProgramTitle != null){
			viewHolder.tvProgramTitle.setText(Html.fromHtml(program.getTitle()));
		}

		if(viewHolder.tvProgramDescription != null){
			viewHolder.tvProgramDescription.setText(Html.fromHtml(program.getDescription()));

			// disable tap on Weekly Programs on links. Tap will work on Weekly programs to show daily programs.
            if(program.getProgramName().equalsIgnoreCase("Weekly Programs")==false)
            	viewHolder.tvProgramDescription.setMovementMethod(LinkMovementMethod.getInstance());
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
	        	int height=60;
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

