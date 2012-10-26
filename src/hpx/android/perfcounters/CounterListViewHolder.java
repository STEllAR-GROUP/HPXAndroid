package hpx.android.perfcounters;

import hpx.android.R;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/* This class will handle the attachment of our views as well as updates etc.. */
/* This is the preferred way of implementing the GetView method */
public class CounterListViewHolder {
	private static final String TAG = "Counter View Holder";
	
	private View _view;
	private int _type;
	private TextView _textView1;
	private TextView _textView2;
	
	public CounterListViewHolder(final View view, int type) {
		_view = view;
		_type = type;
		switch(_type) {
		case Constants.NODE:
			_textView1 = (TextView) _view.findViewById(R.id.node_item_ident_number);
			_textView2 = (TextView) _view.findViewById(R.id.node_item_info);
			break;
		case Constants.LOCALITIES:
			_textView1 = (TextView) _view.findViewById(R.id.localities_list_name);
			_textView2 = (TextView) _view.findViewById(R.id.localities_list_info);
			break;
		case Constants.LOCALITY:
			_textView1 = (TextView) _view.findViewById(R.id.locality_position);
			_textView2 = (TextView) _view.findViewById(R.id.locality_hasGPU);
			
			break;
		default:
			Log.wtf(TAG, "Shouldn't be here."); //We should never be here.
		}
	}
	
	
	/* Will be called to update the views */
	public void updateListItem(Object item, int position) {
		Locality locality;
		switch(_type) {
		case Constants.NODE:
			Node node = (Node) item;
			_textView1.setText("Node: " + (position + 1));
			_textView2.setText("Localities: " + node.localities.size());
			break;
		case Constants.LOCALITIES:
			locality = (Locality) item;
			_textView1.setText("Locality: " + (position + 1));
			_textView2.setText("GPU: " + locality.hasGPU);
			break;
		case Constants.LOCALITY:
			locality = (Locality) item;
			_textView1.setText("Locality: " + (position + 1));
			_textView2.setText("GPU: " + locality.hasGPU);
			break;
		default:
			Log.wtf(TAG, "Not Supposed to Be Here");  //We should never be here.
		}
	}
	
	
}

