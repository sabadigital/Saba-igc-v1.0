package com.saba.igc.org.models;

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

/**
 * @author Syed Aftab Naqvi
 * @create December, 2014
 * @version 1.0
 */
@Table(name = "PrayerTimes")
public class PrayerTimes extends Model {
	//D-Month-Y	Day	Imsaak	Fajar		Sunrise	Zohar	Sunset	Maghrib/Iftar	Midnight
	
	@Column(name = "city")
	private String mCity;
	
	@Column(name = "date") //MM-DD
	private String mDate;
	
	@Column(name = "imsaak")
	private String mImsaak;
	
	@Column(name = "fajar")
	private String mFajar;
	
	@Column(name = "sunrise")
	private String mSunrise;
	
	@Column(name = "zohar")
	private String mZohar;
	
	@Column(name = "sunset")
	private String mSunset;
	
	@Column(name = "maghrib")
	private String mMaghrib;
	
	@Column(name = "midnight")
	private String mMidnight;
	
	public String getCity() {
		return mCity;
	}

	public void setCity(String city) {
		this.mCity = city;
	}
	
	public String getDate() {
		return mDate;
	}

	public void setDate(String date) {
		this.mDate = date;
	}

	public String getImsaak() {
		return mImsaak;
	}

	public void setImsaak(String imsaak) {
		this.mImsaak = imsaak;
	}

	public String getFajar() {
		return mFajar;
	}

	public void setFajar(String fajar) {
		this.mFajar = fajar;
	}

	public String getSunrise() {
		return mSunrise;
	}

	public void setSunrise(String sunrise) {
		this.mSunrise = sunrise;
	}

	public String getZohar() {
		return mZohar;
	}

	public void setZohar(String zohar) {
		this.mZohar = zohar;
	}

	public String getMaghrib() {
		return mMaghrib;
	}

	public void setMaghrib(String maghrib) {
		this.mMaghrib = maghrib;
	}

	public String getMidnight() {
		return mMidnight;
	}

	public void setMidnight(String midnight) {
		this.mMidnight = midnight;
	}

	// Persistence methods.
    public void saveTime(){
    	this.save();
    }
    
    private void setSunset(String sunset) {
		this.mSunset = sunset;
	}
    
    public String getSunset() {
		// TODO Auto-generated method stub
		return mSunset;
	}

	// Get all times.
    public static List<PrayerTimes> getAll() {
        //return new Select().from(Tweet.class).orderBy("uid DESC").execute();
        return new Select().from(PrayerTimes.class).execute();
    }
    
    
    // Get todays time. date format should be MM-DD only. we don't care about year here..
    public static List<PrayerTimes> getTodayPrayerTimes(String city, String today) {
        return new Select()
        .from(PrayerTimes.class)
        .where("city = ? AND date = ?", city, today)
        .execute();
    }
    
    
//	helper function to create PrayerTime object by passing a line of times and city name:    
//    public static PrayerTimes fromString(String city, String line){
//    	PrayerTimes time = new PrayerTimes();
//    	StringTokenizer token = new StringTokenizer(line, "\t");
// 	
//    	String str_date=token.nextToken();
//		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
//		Date date = null;
//		try {
//			date = (Date)formatter.parse(str_date);
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
//		System.out.println(formatter.format(date));  
//		
//		Calendar calendar = Calendar.getInstance();
//	    calendar.setTime(date);
//	    int day = calendar.get(Calendar.DAY_OF_MONTH); //Day of the month :)
//	    int month = calendar.get(Calendar.MONTH); //number of seconds
//	    
//		System.out.println("MM: " + month +" dd: " + day);
//		
//		time.setDate(""+month+"-"+day);
//		token.nextToken(); // skip this token for day - we are not storing day here...
//	 
//		time.city = city;
//		time.setImsaak(token.nextToken());
//		time.setFajar(token.nextToken());
//		time.setSunrise(token.nextToken());
//		time.setZohar(token.nextToken());
//		time.setSunset(token.nextToken());
//		time.setMaghrib(token.nextToken());
//		time.setMidnight(token.nextToken());
//		          
//    	return time;
//    }
}
