package th.co.ais.genesis.module.buddy;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;

import th.co.ais.genesis.module.buddy.adapter.ScheduleAdapter;
import th.co.ais.genesis.module.buddy.database.ScheduleDB;

public class Schedule extends AppCompatActivity {

    private TextView textHeader;
    private Button btn_add;
    private ListView showSchedule;
    private ScheduleDB scheduleDB;
    private ScheduleAdapter scheduleAdapter;

    private ArrayList<String> id;
    private ArrayList<String> startHour;
    private ArrayList<String> startMinute;
    private ArrayList<String> stopHour;
    private ArrayList<String> stopMinute;
    private ArrayList<String> title;
    private ArrayList<String> status;

    private String title_sc;
    private String status_sc;

    Calendar mCurrentTimeStart = Calendar.getInstance();
    int hourStart = mCurrentTimeStart.get(Calendar.HOUR_OF_DAY);
    int minuteStart = mCurrentTimeStart.get(Calendar.MINUTE);

    Calendar mCurrentTimeStop = Calendar.getInstance();
    int hourStop = mCurrentTimeStop.get(Calendar.HOUR_OF_DAY);
    int minuteStop = mCurrentTimeStop.get(Calendar.MINUTE);

    int startHourSet = hourStart;
    int startMinuteSet = minuteStart;

    int stopHourSet = hourStop;
    int stopMinuteSet = minuteStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_schedule);

        textHeader = findViewById(R.id.textHeader);
        showSchedule = findViewById(R.id.show_listView);
        btn_add = findViewById(R.id.btn_add);

        initArray();
        scheduleDB = new ScheduleDB(this);

        getDatabase();

        scheduleAdapter = new ScheduleAdapter(getApplicationContext(),id,startHour,startMinute,stopHour,stopMinute,title,status);
        showSchedule.setAdapter(scheduleAdapter);



        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("btn_add");

                textHeader.setText("You adding...");

                final Dialog dialog = new Dialog(Schedule.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.addschedule);
                dialog.setCancelable(true);

                Button btn_ok = dialog.findViewById(R.id.btn_ok);
                Button btn_startTime = dialog.findViewById(R.id.startTime);
                Button btn_stopTime = dialog.findViewById(R.id.stopTime);
                EditText ed_title = dialog.findViewById(R.id.ed_title);

                //do something

                btn_startTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TimePickerDialog mTimePicker;

                        mTimePicker = new TimePickerDialog(Schedule.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                                String starttext = "";

                                startHourSet = selectedHour;
                                startMinuteSet = selectedMinute;

                                if(selectedHour<10){
                                    if(selectedMinute<10){
                                        starttext = "0"+selectedHour+":0"+selectedMinute;
                                    }else{
                                        starttext = "0"+selectedHour+":"+selectedMinute;
                                    }
                                }else{
                                    if(selectedMinute<10){
                                        starttext = selectedHour+":0"+selectedMinute;
                                    }else{
                                        starttext = selectedHour+":"+selectedMinute;
                                    }
                                }

                                btn_startTime.setText(starttext);
                            }
                        }, hourStart, minuteStart, true);//Yes 24 hour time

                        mTimePicker.show();
                    }
                });



                btn_stopTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        TimePickerDialog mTimePicker;
                        mTimePicker = new TimePickerDialog(Schedule.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                                String stoptext = "";

                                stopHourSet = selectedHour;
                                stopMinuteSet = selectedMinute;

                                if(selectedHour<10){
                                    if(selectedMinute<10){
                                        stoptext = "0"+selectedHour+":0"+selectedMinute;
                                    }else{
                                        stoptext = "0"+selectedHour+":"+selectedMinute;
                                    }
                                }else{
                                    if(selectedMinute<10){
                                        stoptext = selectedHour+":0"+selectedMinute;
                                    }else{
                                        stoptext = selectedHour+":"+selectedMinute;
                                    }
                                }

                                btn_stopTime.setText(stoptext);
                            }
                        }, hourStop, minuteStop, true);//Yes 24 hour time

                        mTimePicker.show();

                    }
                });


                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //save data
                        if(ed_title.getText().toString().equals("")){
                            System.out.println("getText");
                            ed_title.setText("please add title!");

                        }else{

                            title_sc = ed_title.getText().toString();
                            status_sc = "Active";

                            addDatabase();

                            //last
                            textHeader.setText("Can I help you?");
                            dialog.cancel();

                        }

                    }
                });


                dialog.show();
            }
        });



    }



    public void getDatabase(){

        SQLiteDatabase scheduleDBWritable = scheduleDB.getWritableDatabase();
        Cursor res = scheduleDBWritable.rawQuery("select * from schedule",null);

        if(res.getCount() == 0){
            return;

        }else{

            while (res.moveToNext()){
                id.add(res.getString(0));
                startHour.add(res.getString(1));
                startMinute.add(res.getString(2));
                stopHour.add(res.getString(3));
                stopMinute.add(res.getString(4));
                title.add(res.getString(5));
                status.add(res.getString(6));
            }

        }

    }

    public void initArray(){
        //create object
        this.id = new ArrayList<>();
        this.startHour = new ArrayList<>();
        this.startMinute = new ArrayList<>();
        this.stopHour = new ArrayList<>();
        this.stopMinute = new ArrayList<>();
        this.title = new ArrayList<>();
        this.status = new ArrayList<>();
    }


    public void addDatabase(){

        SQLiteDatabase addSchedule = scheduleDB.getWritableDatabase();
        ContentValues valueSchedule = new ContentValues();
        valueSchedule.put("hour_start",startHourSet);
        valueSchedule.put("minute_start",startMinuteSet);
        valueSchedule.put("hour_stop",stopHourSet);
        valueSchedule.put("minute_stop",stopMinuteSet);
        valueSchedule.put("title",title_sc);
        valueSchedule.put("status",status_sc);

        addSchedule.insertOrThrow("schedule", null, valueSchedule);

    }

}