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

public class ThreadsListAdapter extends BaseAdapter{

	private List<String> items;
	private LayoutInflater _inflater;
	public ThreadsListAdapter(Context context, int numWorkerThreads) {
		_inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		items = new ArrayList<String>();
		
		items.add(new String("Threads Executed"));
		items.add(new String("Threads State Count"));
		items.add(new String("Threads Average Idle Rate"));
		items.add(new String("Threads Queue Length"));
		items.add(new String("Thread Unbind operations Performed"));
		items.add(new String("Thread Recycling operations performed"));
		
		for(int i = 0; i < numWorkerThreads; i++) {
			items.add(new String("Worker Thread " + i));
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
