package hpx.android.adapters;

import android.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ThreadListAdapter extends BaseAdapter {
	private String items[] = { "Worker Thread States", 
							  "Avg Idle Rate"};
	private LayoutInflater _inflater;
	
	public ThreadListAdapter(Context context) {
		_inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		
		return items.length;
	}

	@Override
	public Object getItem(int position) {
		return items[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null) {
			convertView = _inflater.inflate(R.layout.simple_list_item_activated_1, null);
			holder = new ViewHolder();
			holder.itemText = (TextView) convertView.findViewById(R.id.text1);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.itemText.setText(items[position]);
		
		return convertView;
	}
	
	class ViewHolder {
		TextView itemText;
	}

}
