package th.co.ais.genesis.module.buddy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import th.co.ais.genesis.module.buddy.R;

public class ScheduleAdapter extends BaseAdapter {


    private Context mContext;
    private ArrayList<String> id;
    private ArrayList<String> startHour;
    private ArrayList<String> startMinute;
    private ArrayList<String> stopHour;
    private ArrayList<String> stopMinute;
    private ArrayList<String> title;
    private ArrayList<String> status;


    private TextView datetimeView;
    private TextView titleView;
    private Switch switchStatus;



    public ScheduleAdapter(Context mContext,ArrayList<String> id,ArrayList<String> startHour,ArrayList<String> startMinute,ArrayList<String> stopHour,
                           ArrayList<String> stopMinute,ArrayList<String> title,ArrayList<String> status){

        this.mContext = mContext;

        //create object
        this.id = new ArrayList<>();
        this.startHour = new ArrayList<>();
        this.startMinute = new ArrayList<>();
        this.stopHour = new ArrayList<>();
        this.stopMinute = new ArrayList<>();
        this.title = new ArrayList<>();
        this.status = new ArrayList<>();

        this.id = id;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.stopHour = stopHour;
        this.stopMinute = stopMinute;
        this.title = title;
        this.status = status;

    }


    @Override
    public int getCount() {
        return id.size();
    }

    @Override
    public Object getItem(int i) {
        return title.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        if(view == null) {
            LayoutInflater mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = mInflater.inflate(R.layout.listview_schedule, viewGroup, false);
        }

        titleView = view.findViewById(R.id.showTitle);
        datetimeView = view.findViewById(R.id.showDatetime);
        switchStatus = view.findViewById(R.id.switchStatus);


        titleView.setText(title.get(position));

        return view;
    }
}
