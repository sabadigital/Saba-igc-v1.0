package com.saba.igc.org.models;

/**
 * @author Syed Aftab Naqvi
 * @create December, 2014
 * @version 1.0
 */
public class NavDrawerItem {    
    private String mTitle;
    private int mIcon;
     
    public NavDrawerItem(){
    	
    }
 
    public NavDrawerItem(String title, int icon){
        this.mTitle = title;
        this.mIcon = icon;
    }
     
    public String getTitle(){
        return this.mTitle;
    }
     
    public int getIcon(){
        return this.mIcon;
    }
     
    public void setTitle(String title){
        this.mTitle = title;
    }
     
    public void setIcon(int icon){
        this.mIcon = icon;
    }     
}