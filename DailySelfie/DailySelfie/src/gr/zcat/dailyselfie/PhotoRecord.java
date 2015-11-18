package gr.zcat.dailyselfie;

import android.graphics.Bitmap;
import android.location.Location;

public class PhotoRecord {
	private String fileName;
	private Bitmap photoBitmap;

	public PhotoRecord(String fileName) {
		this.fileName = fileName;
	}

	public PhotoRecord() {	
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Bitmap getFlagBitmap() {
		return photoBitmap;
	}

	public void setFlagBitmap(Bitmap photoBitmap) {
		this.photoBitmap = photoBitmap;
	}

	@Override
	public String toString(){
		return fileName;
		
	}
}
