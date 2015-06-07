package com.saba.igc.org.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;

public class PrayTime {
	private String mName;
	private String mTime;
	
	PrayTime(){
		
	}
	
	PrayTime(String name, String time){
		mName = name;
		mTime = time;
	}

	public String getName() {
		return mName;
	}

	public String getTime() {
		return mTime;
	}

	public static List<PrayTime> fromJSON(JSONObject response) {
		if(response == null)
			return null;
		
		//{"Fajr":"05:59","Isha":"18:18","Asr":"14:43","Dhuhr":"12:11","Sunset":"17:01","Sunrise":"07:21","Maghrib":"17:19","Imsaak":"05:48"}
		List<PrayTime> prayTimes = new ArrayList<PrayTime>();
		
		try {
			if(response.getString("Imsaak") != null){
				PrayTime time = new PrayTime("Imsaak", response.getString("Imsaak"));
				time.mTime = get12HrFormatTime(time.mTime);
				prayTimes.add(time);
			}
		
			if(response.getString("Fajr") != null){
				PrayTime time = new PrayTime("Fajr", response.getString("Fajr"));
				time.mTime = get12HrFormatTime(time.mTime);
				prayTimes.add(time);
			}
			
			if(response.getString("Sunrise") != null){
				PrayTime time = new PrayTime("Sunrise", response.getString("Sunrise"));
				time.mTime = get12HrFormatTime(time.mTime);
				prayTimes.add(time);
			}
			
			if(response.getString("Dhuhr") != null){
				PrayTime time = new PrayTime("Dhuhr", response.getString("Dhuhr"));
				time.mTime = get12HrFormatTime(time.mTime);
				prayTimes.add(time);
			}
			
			if(response.getString("Asr") != null){
				PrayTime time = new PrayTime("Asr", response.getString("Asr"));
				time.mTime = get12HrFormatTime(time.mTime);
				prayTimes.add(time);
			}
			
			if(response.getString("Sunset") != null){
				PrayTime time = new PrayTime("Sunset", response.getString("Sunset"));
				time.mTime = get12HrFormatTime(time.mTime);
				prayTimes.add(time);
			}
			
			if(response.getString("Maghrib") != null){
				PrayTime time = new PrayTime("Maghrib", response.getString("Maghrib"));
				time.mTime = get12HrFormatTime(time.mTime);
				prayTimes.add(time);
			}
			
			if(response.getString("Isha") != null){
				PrayTime time = new PrayTime("Isha", response.getString("Isha"));
				get12HrFormatTime(time.mTime);
				time.mTime = get12HrFormatTime(time.mTime);
				prayTimes.add(time);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return prayTimes;
	}

	public static String get12HrFormatTime(String time){
		Date date24Hours = null;
		SimpleDateFormat simpleDateFormat12Hours = null;
		try {
			SimpleDateFormat simpleDateFormat24Hours = new SimpleDateFormat("HH:mm");
			simpleDateFormat12Hours = new SimpleDateFormat("hh:mm a");
			date24Hours = simpleDateFormat24Hours.parse(time);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return simpleDateFormat12Hours.format(date24Hours);
	}
}

