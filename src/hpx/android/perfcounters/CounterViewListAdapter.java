package hpx.android.perfcounters;

import hpx.android.R;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;



public class CounterViewListAdapter extends BaseAdapter {
private static final String TAG = "Counter View List Adapter";
	
	private List<Object> _items;
	private CounterListViewHolder _holder = null;
	private int _type;
	private LayoutInflater _layoutInflater;

	private boolean dataChangeOut;
	
	public CounterViewListAdapter(Context context) {
		dataChangeOut = false;
		_items = new ArrayList<Object>();
		_type = 0;
		_layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public int getCount() {
		return _items.size();
	}
	
	public void setNodeList(List<Node> items) {
		_type = Constants.NODE; //Tell us what were displaying.

		_items.clear(); //Clear all previous items from list
		
		_items.addAll(items); //Add all our new items to list

		dataChangeOut = true; //Hack to make the ViewHolder patter hold up.
		notifyDataSetChanged(); //Called so getView is executed once again with new items.
		
	}
	
	
	public void setLocalitiesList(List<Locality> items) {
		_type = Constants.LOCALITIES; //Tell us what were displaying.
		
		_items.clear();
		
		_items.addAll(items);
		
		dataChangeOut = true; //Hack to make the ViewHolder patter hold up.
	    notifyDataSetChanged(); //Called so getView is executed once again with new items.
	    
	}
	
	public void setLocality(Locality item) {
		_type = Constants.LOCALITY; //Tell us what were displaying.
		
		_items.clear(); 
		
		_items.add(item);
		
		if(_items.size() > 1) Log.wtf(TAG, "Shouldn't have more than one Locality for this step");
		
		dataChangeOut = true; //Hack to make the ViewHolder patter hold up.
		notifyDataSetChanged(); //Called so getView is executed once again with new items.
		
	}
	public Object getItem(int position) { 
		return _items.get(position); //Return Object at the position within the list.
	}

	public long getItemId(int position) { //No need to set ID's
		return position;
	}
	
	public int getItemViewType() { //Will return the current type being displayed.
		return _type;
	}
	
	public void notifyDataChangeOver() {
		dataChangeOut = false;
	}

	/* This is the ViewHolder Pattern style, typical for Android BaseAdapter implementation of getView */
	public View getView(int position, View convertView, ViewGroup parent) {
		if(dataChangeOut) convertView = null;
		if(convertView == null) {
			switch(_type) {
			case Constants.NODE:
				convertView = _layoutInflater.inflate(R.layout.counter_view_list_node_item, null);
				break;
			case Constants.LOCALITIES:
				convertView = _layoutInflater.inflate(R.layout.counter_view_list_localities, null);
				break;
			case Constants.LOCALITY:
				convertView = _layoutInflater.inflate(R.layout.counter_view_list_locality_item, null);
				break;
			default:
				Log.wtf(TAG, "Shouldn't be here"); //We should never be here.
			}
			_holder = new CounterListViewHolder(convertView, _type); //Initialize our ViewHolder
			convertView.setTag(_holder); //Set the tag, in case of screen orientation change we can recycle our holder.
		} else {
			_holder = (CounterListViewHolder) convertView.getTag(); //If the view isn't null, get our holder.
		}
		
		_holder.updateListItem(getItem(position), position); //Send our object to the viewholder to set the views.
		return convertView;
	}
	
}
