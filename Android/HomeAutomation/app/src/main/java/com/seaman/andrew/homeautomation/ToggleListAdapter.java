package com.seaman.andrew.homeautomation;

import android.content.Context;
import android.content.Intent;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Andrew on 7/15/2015.
 */
public class ToggleListAdapter<T> extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<Appliance> appls = new ArrayList<Appliance>();
    ToggleButton toggle;
    ActivityLogger log = new ActivityLogger();

    public ToggleListAdapter(Context context, ArrayList<Appliance>nAppls) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.appls = nAppls;
    }

    @Override
    public int getCount() {
        return appls.size();
    }

    @Override
    public Object getItem(int pos) {
        return appls.get(pos).name;
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup viewGroup) {
        final ViewHolder holder;
        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item_with_toggle, null);
            holder = new ViewHolder();
            holder.applianceText = (TextView) convertView.findViewById(R.id.applianceName);
            holder.toggle = (ToggleButton) convertView.findViewById(R.id.applianceToggleButton);
            holder.position = pos;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        // todo: fix all the stuff right below this!

        holder.applianceText.setText(appls.get(pos).name);
        if(appls.get(pos).state == 0) {
            holder.toggle.setChecked(false);
        } else {
            holder.toggle.setChecked(true);
        }


        holder.toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewGroup parent = (ViewGroup)view.getParent();
                String msgOut = holder.applianceText.getText().toString() + " switched ";
                if (appls.get(holder.position).state == 0) {
                    holder.toggle.setChecked(true);
                    appls.get(holder.position).state = 1;
                    msgOut += "on";
                } else {
                    holder.toggle.setChecked(false);
                    appls.get(holder.position).state = 0;
                    msgOut += "off";
                }

                Msg message = new Msg(Main.MAC_ADDRESS,
                        (char)appls.get(holder.position).id,
                        appls.get(holder.position).getTypeChar(),
                        'a', // finalState
                        'e'); // error flags

                System.out.println(message.toSendString());

                Intent msgIntent = new Intent();
                msgIntent.setAction("seaman.andrew.homeautomation.MESSAGE");
                msgIntent.putExtra("MESSAGE", message.toSendString());
                context.sendBroadcast(msgIntent);

                log.addLog(msgOut);
                System.out.println(msgOut);
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        public TextView applianceText;
        public ToggleButton toggle;
        public int position;
    }
}
