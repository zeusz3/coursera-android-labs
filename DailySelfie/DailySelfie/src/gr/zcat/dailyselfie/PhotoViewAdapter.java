package gr.zcat.dailyselfie;

import java.util.ArrayList;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PhotoViewAdapter extends BaseAdapter {

	private ArrayList<PhotoRecord> list = new ArrayList<PhotoRecord>();
	private static LayoutInflater inflater = null;
	private Context mContext;

	public PhotoViewAdapter(Context context) {
		mContext = context;
		inflater = LayoutInflater.from(mContext);
	}

	public int getCount() {
		return list.size();
	}

	public PhotoRecord getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		View newView = convertView;
		ViewHolder holder;

		PhotoRecord curr = list.get(position);

		if (null == convertView) {
			holder = new ViewHolder();
			newView = inflater
					.inflate(R.layout.photo_view, parent, false);
			holder.photo = (ImageView) newView.findViewById(R.id.photo);
			holder.fileName = (TextView) newView.findViewById(R.id.fileName);
			newView.setTag(holder);

		} else {
			holder = (ViewHolder) newView.getTag();
		}

		holder.photo.setImageBitmap(curr.getFlagBitmap());
		holder.fileName.setText("File: " + curr.getFileName());

		return newView;
	}

	static class ViewHolder {
		ImageView photo;
		TextView fileName;
		
		public String getText() {
			return fileName.getText().toString();
		}
	}

	public void add(PhotoRecord listItem) {
		list.add(listItem);
		notifyDataSetChanged();
	}

	public ArrayList<PhotoRecord> getList() {
		return list;
	}

	public void removeAllViews() {
		list.clear();
		this.notifyDataSetChanged();
	}
}
