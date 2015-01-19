package com.saba.igc.org.models;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

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
				prayTimes.add(time);
			}
		
			if(response.getString("Fajr") != null){
				PrayTime time = new PrayTime("Fajr", response.getString("Fajr"));
				prayTimes.add(time);
			}
			
			if(response.getString("Sunrise") != null){
				PrayTime time = new PrayTime("Sunrise", response.getString("Sunrise"));
				prayTimes.add(time);
			}
			
			if(response.getString("Dhuhr") != null){
				PrayTime time = new PrayTime("Dhuhr", response.getString("Dhuhr"));
				prayTimes.add(time);
			}
			
			if(response.getString("Asr") != null){
				PrayTime time = new PrayTime("Asr", response.getString("Asr"));
				prayTimes.add(time);
			}
			
			if(response.getString("Sunset") != null){
				PrayTime time = new PrayTime("Sunset", response.getString("Sunset"));
				prayTimes.add(time);
			}
			
			if(response.getString("Maghrib") != null){
				PrayTime time = new PrayTime("Maghrib", response.getString("Maghrib"));
				prayTimes.add(time);
			}
			
			if(response.getString("Isha") != null){
				PrayTime time = new PrayTime("Isha", response.getString("Isha"));
				prayTimes.add(time);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return prayTimes;
	}
}

