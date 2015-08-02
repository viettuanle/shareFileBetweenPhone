package npu.support.adapter;

import npu.s2p.activity.R;

import java.util.ArrayList;
import npu.database.SingleData;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @author Tuan Le The purpose of this file are: create adapter to display
 *         content of some fields in table npucontact. Note: next version will
 *         add code for control image
 */

public class listviewAdapter extends ArrayAdapter<SingleData> {

	private ArrayList<SingleData> list;

	public listviewAdapter(Context context, int textViewResourceId,
			ArrayList<SingleData> list) {
		super(context, textViewResourceId, list);
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.row, null);
		}

		SingleData singlerow = list.get(position);
		if (singlerow != null) {
			// ImageView image = (ImageView)
			// v.findViewById(R.id.img_row_viewcontact);
			TextView firstname = (TextView) v
					.findViewById(R.id.txt_firstmiddlelastname_row_viewcontact);

			TextView email = (TextView) v
					.findViewById(R.id.txt_email_row_viewcontact);

			// if (image != null) {
			// //handle later
			// }
			if (firstname != null) {
				firstname.setText(singlerow.getGivenname() + " "
						+ singlerow.getMiddlename() + " "
						+ singlerow.getFamilyname());
			}

			if (email != null) {
				email.setText(singlerow.getEmail());
			}
		}
		return v;
	}

	// @Override
	//
	// public View getView(int position, View convertView, ViewGroup parent) {
	//
	//
	// ViewHolder holder;
	//
	// LayoutInflater inflater = activity.getLayoutInflater();
	//
	//
	//
	// if (convertView == null)
	//
	// {
	//
	// convertView = inflater.inflate(R.layout.row, null);
	//
	// holder = new ViewHolder();
	//
	// holder.txtFirst = (TextView) convertView.findViewById(R.id.FirstText);
	//
	// holder.txtSecond = (TextView) convertView.findViewById(R.id.SecondText);
	//
	// holder.txtThird = (TextView) convertView.findViewById(R.id.ThirdText);
	// holder.txtFourth = (TextView) convertView.findViewById(R.id.FourthText);
	// convertView.setTag(holder);
	// }
	// else
	// {
	// holder = (ViewHolder) convertView.getTag();
	// }
	// HashMap<String, String> map = list.get(position);
	// holder.txtFirst.setText(map.get(KEY_ROWID));//row id
	// holder.txtSecond.setText(map.get(KEY_GIVENNAME));
	// holder.txtThird.setText(map.get(KEY_PHONE));
	// holder.txtFourth.setText(map.get(KEY_EMAIL));
	//
	// return convertView;
	// }

}
