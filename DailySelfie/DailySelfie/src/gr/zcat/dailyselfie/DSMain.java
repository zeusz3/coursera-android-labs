package gr.zcat.dailyselfie;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class DSMain extends ListActivity {

	static final int REQUEST_TAKE_PHOTO = 1;
	static final int REQUEST_IMAGE_CAPTURE = 1;
	private String mCurrentPhotoPath;
	private PhotoViewAdapter mAdapter;
	private AlarmManager mAlarmManager;
	private Intent mNotificationReceiverIntent;
	private PendingIntent mNotificationReceiverPendingIntent;
	private static final long INITIAL_ALARM_DELAY = 2*60*1000L;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAdapter = new PhotoViewAdapter(getApplicationContext());
		setListAdapter(mAdapter);
		Iterator<File> picsIterator = getCameraImages().iterator();
		while (picsIterator.hasNext()) {
			File nextFile = picsIterator.next();
			PhotoRecord pr = new PhotoRecord(nextFile.getName());
			pr.setFlagBitmap(setPic(nextFile.getAbsolutePath()));
			mAdapter.add(pr);
		}
		// Get the AlarmManager Service
		mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

		// Create an Intent to broadcast to the AlarmNotificationReceiver
		mNotificationReceiverIntent = new Intent(DSMain.this,
				AlarmNotificationReceiver.class);

		// Create an PendingIntent that holds the NotificationReceiverIntent
		mNotificationReceiverPendingIntent = PendingIntent.getBroadcast(
				DSMain.this, 0, mNotificationReceiverIntent, 0);
		
		/*mAlarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
				SystemClock.elapsedRealtime() + INITIAL_ALARM_DELAY,
				INITIAL_ALARM_DELAY,
				mNotificationReceiverPendingIntent);
		Toast.makeText(getApplicationContext(), "Repeating Alarm Set",
				Toast.LENGTH_LONG).show();*/
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dsmain, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.snap) {
			dispatchTakePictureIntent();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
	        galleryAddPic();
	        PhotoRecord pr = new PhotoRecord(mCurrentPhotoPath.substring(mCurrentPhotoPath.lastIndexOf("/")));
	        pr.setFlagBitmap(setPic(mCurrentPhotoPath));
			mAdapter.add(pr);
	    }
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
	  String uri = Environment.getExternalStoragePublicDirectory(
	            Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+mAdapter.getItem(position).getFileName();
	  Toast.makeText(this, uri, Toast.LENGTH_LONG).show();
	  
	  Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri)).setType("image/jpg");
	  if(intent.resolveActivity(getPackageManager())!= null) {
		  Log.i("TAG", "FOUND INTENT FOUND");
		  startActivity(Intent.createChooser(intent, "CHOOSE"));
	  } else {
		  Log.i("TAG", "NO INTENT FOUND");
	  }
	  super.onListItemClick(l, v, position, id);
	}
	
	private void galleryAddPic() {
	    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
	    File f = new File(mCurrentPhotoPath);
	    Uri contentUri = Uri.fromFile(f);
	    mediaScanIntent.setData(contentUri);
	    this.sendBroadcast(mediaScanIntent);
	}
	
	public static List<File> getCameraImages() {
		List<File> ls = null;
		File storageDir = Environment.getExternalStoragePublicDirectory(
	            Environment.DIRECTORY_PICTURES);
		ls = Arrays.asList(storageDir.listFiles());
		return ls;
	}
	
	private Bitmap setPic(String path) {
	    // Get the dimensions of the View
	    int targetW = 120;
	    int targetH = 120;

	    // Get the dimensions of the bitmap
	    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
	    bmOptions.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(path, bmOptions);
	    int photoW = bmOptions.outWidth;
	    int photoH = bmOptions.outHeight;

	    // Determine how much to scale down the image
	    int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

	    // Decode the image file into a Bitmap sized to fill the View
	    bmOptions.inJustDecodeBounds = false;
	    bmOptions.inSampleSize = scaleFactor;
	    bmOptions.inPurgeable = true;

	    Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
	    return bitmap;
	}
	
	private File createImageFile() throws IOException {
	    // Create an image file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    String imageFileName = "DS_" + timeStamp;
	    File storageDir = Environment.getExternalStoragePublicDirectory(
	            Environment.DIRECTORY_PICTURES);
	    boolean d = storageDir.mkdirs();
	    File image = File.createTempFile(
	        imageFileName,  /* prefix */
	        ".jpg",         /* suffix */
	        storageDir      /* directory */
	    );
	    // Save a file: path for use with ACTION_VIEW intents
	    mCurrentPhotoPath = image.getAbsolutePath();
	    return image;
	}
	
	private void dispatchTakePictureIntent() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    // Ensure that there's a camera activity to handle the intent
	    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
	        // Create the File where the photo should go
	        File photoFile = null;
	        try {
	            photoFile = createImageFile();
	        } catch (IOException ex) {
	            // Error occurred while creating the File
	        	Log.e("TAG", "exception", ex);
	        }
	        // Continue only if the File was successfully created
	        if (photoFile != null) {
	            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
	                    Uri.fromFile(photoFile));
	            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
	        }
	    }
	}
}
