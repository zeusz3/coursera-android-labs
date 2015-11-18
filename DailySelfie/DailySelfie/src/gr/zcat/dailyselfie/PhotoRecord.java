package gr.zcat.dailyselfie;

import android.graphics.Bitmap;
import android.location.Location;

public class PhotoRecord {
	private String mFlagUrl;
	private String mCountryName;
	private String mPlaceName;
	private Bitmap mFlagBitmap;
	private Location mLocation;

	public PhotoRecord(String flagUrl, String country, String place) {
		this.mFlagUrl = flagUrl;
		this.mCountryName = country;
		this.mPlaceName = place;
	}

	public PhotoRecord(Location location) {
		mLocation = location;
	}

	public PhotoRecord() {	
	}
	
	public String getFlagUrl() {
		return mFlagUrl;
	}

	public void setFlagUrl(String flagUrl) {
		this.mFlagUrl = flagUrl;
	}

	public String getCountryName() {
		return mCountryName;
	}

	public void setCountryName(String country) {
		this.mCountryName = country;
	}

	public String getPlace() {
		return mPlaceName;
	}

	public void setPlace(String place) {
		this.mPlaceName = place;
	}

	public Bitmap getFlagBitmap() {
		return mFlagBitmap;
	}

	public void setFlagBitmap(Bitmap mFlagBitmap) {
		this.mFlagBitmap = mFlagBitmap;
	}

	public void setLocation(Location location) {
		mLocation = location;
	}

	public Location getLocation() {
		return mLocation;
	}
	
	boolean intersects(Location location) {
		double tolerance = 1000;
		return (mLocation.distanceTo(location) <= tolerance);
	}

	@Override
	public String toString(){
		return "Place: " + mPlaceName + " Country: " + mCountryName;
		
	}
}
