package com.saba.igc.org.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

// first version - coming data
//{
//    "id": "https://spreadsheets.google.com/feeds/list/0Av-PwTFDtwEYdEdnYllyYUxjbnd2VzkwaG9oUExLS0E/2/public/values/wxl8y",
//    "updated": "2014-11-19T22:27:44.790Z",
//    "category": {
//        "@attributes": {
//            "scheme": "http://schemas.google.com/spreadsheets/2006",
//            "term": "http://schemas.google.com/spreadsheets/2006#list"
//        }
//    },
//    "title": "Nadeem Sarwar at SABA",
//    "content": "description: Guest Nauha reciter Nadeem Sarwar will be at SABA on Saturday, November 29 2014</b> to recite Nauha and Marsiya., imageurl: http://www.saba-igc.org/data/weeklyemail/muharram_flag.jpg, imagewidth: 60, imageheight: 60",
//    "link": {
//        "@attributes": {
//            "rel": "self",
//            "type": "application/atom+xml",
//            "href": "http://spreadsheets.google.com/feeds/list/0Av-PwTFDtwEYdEdnYllyYUxjbnd2VzkwaG9oUExLS0E/2/public/values/wxl8y"
//        }
//    }
//}

// 2nd version - coming data
//{
//    "title": "School Timings",
//    "description": "\nBell schedule 2 has now being implemented in Sunday school:\n\nBell Schedule 2 (Approx. November to March)</b>\n\nFirst Period: 11:00 am – 11:50 am\n\nSecond Period:11:50 am – 12:40 pm\n\nSalaat Period:12:40 pm -1:00 pm\n\nThird Period:1:10 pm – 2:00 pm\n\nLUNCH/Pick-up time: 2:00 pm</font>\n",
//    "urltitle": "",
//    "url": "",
//    "imageurl": "http://www.saba-igc.org/data/announcements/email/school.jpeg",
//    "imagewidth": "",
//    "imageheight": ""
//}

/**
 * @author Syed Aftab Naqvi
 * @create December, 2014
 * @version 1.0
 */
@Table (name="SabaProgram")
public class SabaProgram extends Model implements Parcelable{

	// lastRequestedTime, last 
	@Column(name = "lastUpdated")
	private String mLastUpdated;
	
	//@Column(name = "updated")
	//private String mUpdated;
	
	@Column(name = "programName")
	private String mProgramName;
	
	@Column(name = "title")
	private String mTitle;
	
	@Column(name = "description")
	private String mDescription;
	
	@Column(name = "imageUrl")
	private String mImageUrl;
	
	@Column(name = "imageHeight")
	private int mImageHeight;
	
	@Column(name = "imageWidth")
	private int mImageWidth;
	
	
	public String getLastUpdated() {
		return mLastUpdated;
	}
	
	public String getImageUrl() {
		return mImageUrl;
	}

	public String getProgramName() {
		return mProgramName;
	}
	
	public String getTitle() {
		return mTitle;
	}
	
	public String  getDescription() {
		return mDescription;
	}
	
	public void setProgramName(String programName) {
		this.mProgramName = programName;
	}

	public void setTitle(String title) {
		this.mTitle = title;
	}
	
	public int getImageHeight() {
		return mImageHeight;
	}
	
	public int getImageWidth() {
		return mImageWidth;
	}
	
	public void setLastUpdated(String lastUpdated) {
		mLastUpdated = lastUpdated;
	}
	
	public String toString(){
		return "LastUpdated: " + mLastUpdated + "\nImageUrl: " + mImageUrl + "\nProgramName: " + mProgramName + "\nTitle: " + mTitle + "\nDescription: " + mDescription;
	}
	
	public static SabaProgram fromProgramJSON(JSONObject json){
		SabaProgram upcomingProgram = new SabaProgram();
		
		try {
			System.out.println("JSON: " + json.toString());
			upcomingProgram.mLastUpdated = new Date().toString();
			upcomingProgram.mTitle = json.getString("title");
			upcomingProgram.mDescription = json.getString("description");
			
			if(!json.isNull("imageurl")){
				upcomingProgram.mImageUrl = json.getString("imageurl");
			}
			
			if(!json.isNull("imageheight") && !json.getString("imageheight").trim().isEmpty()){
				upcomingProgram.mImageHeight = Integer.parseInt(json.getString("imageheight"));
			}
			
			if(!json.isNull("imagewidth") && !json.getString("imagewidth").trim().isEmpty()){
				upcomingProgram.mImageWidth = Integer.parseInt(json.getString("imagewidth"));
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return upcomingProgram;
	}
	
	public static ArrayList<SabaProgram> fromJSONArray(String programName, JSONArray jsonArray){
		ArrayList<SabaProgram> programs = new ArrayList<SabaProgram>();
		
		for(int i=0; i<jsonArray.length(); i++){
			JSONObject programJson = null;
			try{
				programJson = jsonArray.getJSONObject(i);
			} catch(JSONException e){
				e.printStackTrace();
				continue;
			}
			
			SabaProgram upcomingProgram = SabaProgram.fromProgramJSON(programJson);
			upcomingProgram.setProgramName(programName);
			
			if(upcomingProgram != null){
				//Log.d("Program: ", upcomingProgram.toString());
				programs.add(upcomingProgram);
			}
		}
		
		return programs;
	}
	
	public static ArrayList<SabaProgram> fromWeeklyPrograms(String programName, List<List<DailyProgram>> weeklyPrograms){
		ArrayList<SabaProgram> programs = new ArrayList<SabaProgram>();
		//ArrayList<WeeklyProgram> weeklyPrograms = WeeklyProgram.fromJSONArray(programName, weeklyPrograms);
		
		int length = weeklyPrograms.size();
		// Weekly programs are coming in in different way as compare to other programs. Every day we may have different sub-programs
		// like time based. e.g. at 6:30 PM - Maghrib prayers, 7:00 PM Dua e kumael etc... and everday we might have different number 
		//	of programs. e.g. on Ashora day, we have all day programs. on 21st Ramadan, all night programs from iftaar to sehri etc.
		
		// Outer loop is navigating for one whole day program. It might have many sub-programs   
		for(int index=0; index < length; index++){
			SabaProgram sabaProgram = new SabaProgram();
			sabaProgram.setProgramName(programName);
			List<DailyProgram> dailyPrograms = weeklyPrograms.get(index); 
			if(dailyPrograms != null && dailyPrograms.get(0)!=null){
				StringBuilder sb = new StringBuilder();
					//Log.d("SabaProgram: ", );
					sb.append(dailyPrograms.get(0).getDay());
				
					sb.append("/");
					sb.append(dailyPrograms.get(0).getEnglishDate());
					sb.append("/");
					sb.append(dailyPrograms.get(0).getHijriDate());
					sabaProgram.mTitle = sb.toString();
					
				// Inner loop is navigating through sub-programs. 
				// Formatting note: we can get the max number of lines from TextView and combined those lines 
				// and make a block. we should ignore other lines.. 
				
				//int maxLinesToShow = 0;  // currently, we are displaying ... after two lines. we can modify here 
				// if we want to display after 3 lines.
				StringBuilder description = new StringBuilder();
				for(final DailyProgram program : dailyPrograms){
					if(program != null && !program.getTime().trim().isEmpty() ){
						description.append(program.getTime());
						description.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
					}
						
					// make this <br> business better.. improve the code. :(	
					if(program.getProgram().startsWith("<br>")){
						description.append(program.getProgram().replace("<br>", ""));
						description.append("<br>");
					} else if(!program.getProgram().isEmpty()){
						description.append(program.getProgram());
						description.append("<br>");
					}
					//maxLinesToShow++;
				}

				sabaProgram.mDescription = description.toString();
				sabaProgram.setLastUpdated(new Date().toString());
				Log.d("Weekly - Program: ", sabaProgram.mDescription);
				programs.add(sabaProgram);
			}
		}
		return programs;
	}
	
	public static ArrayList<SabaProgram> fromJSON(JSONObject completeJson){
		return null;
	}
	
	// Persistence methods.
    public void saveProgram(){
    	this.save();
    }
    
    public static void deleteSabaPrograms(String programName){
    	new Delete()
    	.from(SabaProgram.class)
    	.where("programName = ?", programName)
    	.execute();
    }
    
	// Get all times.
    public static List<SabaProgram> getAll() {
        return new Select().from(SabaProgram.class).execute();
    }
    
    // get recent SABA programs by given program name from the database.
    public static List<SabaProgram> getSabaPrograms(String programName) {
        return new Select()
        .from(SabaProgram.class)
        .where("programName = ?", programName)
        .execute();
        // where lastUpdate = "today"
    }
 
    // ----------------- Parcelable implementation ----------------------------
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(this.mTitle);
        dest.writeString(this.mDescription);
	}
	
	public SabaProgram() {
    	super();
    }

    private SabaProgram(Parcel in) {
    	this();
        this.mTitle = in.readString();
        this.mDescription = in.readString();
    }
    
    public static final Parcelable.Creator<SabaProgram> CREATOR = new Parcelable.Creator<SabaProgram>() {
        public SabaProgram createFromParcel(Parcel source) {
            return new SabaProgram(source);
        }

        public SabaProgram[] newArray(int size) {
            return new SabaProgram[size];
        }
    };
}
