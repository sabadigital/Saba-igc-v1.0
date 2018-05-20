package com.saba.igc.org.models;

import java.util.StringTokenizer;

import android.util.Log;
// "content": 
//"description: Guest Nauha reciter Nadeem Sarwar will be at SABA on Saturday, November 29 2014</b> to recite Nauha and Marsiya., 
//imageurl: http://www.saba-igc.org/data/weeklyemail/muharram_flag.jpg, 
//imagewidth: 60, 
// imageheight: 60",

// Content are comma separated. 
/**
 * @author Syed Aftab Naqvi
 * @create December, 2014
 * @version 1.0
 */
public class Content {
	private String mDescription;
	private String mImageUrl;
	private int mWidth;
	private int mHeight;
	
	public static Content contentFromString(final String strContent){
		
		Content content = new Content();
		StringTokenizer tokenizer = new StringTokenizer(strContent, ":");
		if(tokenizer.hasMoreTokens()){
			tokenizer.nextToken(":");
			content.mDescription = tokenizer.nextToken(":");
//			content.mDescription += tokenizer.nextToken(","); // hack...
		}
		
		if(tokenizer.hasMoreTokens()){
			content.mImageUrl = tokenizer.nextToken(",");
		}
		
		if(tokenizer.hasMoreTokens()){
			tokenizer.nextToken(":");
			content.mWidth = Integer.parseInt(tokenizer.nextToken(":, "));
		}
		
		if(tokenizer.hasMoreTokens()){
			tokenizer.nextToken(":");
			content.mHeight = Integer.parseInt(tokenizer.nextToken(":, "));
			
		}
		Log.d("Content: ",  content.toString());
		return content;
	}

	public String getDescription() {
		return mDescription;
	}

	public String getImageUrl() {
		return mImageUrl;
	}

	public int getWidth() {
		return mWidth;
	}

	public int getHeight() {
		return mHeight;
	}
	
	public String toString(){
		return "Desc: " + mDescription + "\n ImageUrl: " + mImageUrl + 
				"\n Width: " + mWidth + "\n Height: " + mHeight + "\n";
	}
}
