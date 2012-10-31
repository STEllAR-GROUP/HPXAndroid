package hpx.android.adapters;

import java.util.ArrayList;
import java.util.List;

import android.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LocalitiesListAdapter extends BaseAdapter {
	private LayoutInflater _inflater;
	private List<String> items;
	public LocalitiesListAdapter(Context context, int numLoc) {
		_inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		items = new ArrayList<String>();
		
		for(int i = 0; i < numLoc; i++) {
			items.add(new String("Locality " + i));
		}
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
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
		
		holder.itemText.setText(items.get(position));
		
		return convertView;
	}
	
	class ViewHolder {
		TextView itemText;
	}
	


}
