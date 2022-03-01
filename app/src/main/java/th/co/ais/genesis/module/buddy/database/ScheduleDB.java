package th.co.ais.genesis.module.buddy.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class ScheduleDB extends SQLiteOpenHelper {

    String TABLE_NAME = "schedule";
    String HOUR_START = "hour_start";
    String MINUTE_START = "minute_start";
    String HOUR_STOP = "hour_stop";
    String MINUTE_STOP = "minute_stop";
    String TITLE = "title";
    String STATUS = "status";


    private static final String DATABASE_NAME = "schedule.db";
    private static final int DATABASE_VERSION = 1;

    public ScheduleDB(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + HOUR_START + " TEXT NOT NULL, " +
                MINUTE_START + " TEXT NOT NULL, " + HOUR_STOP + " TEXT NOT NULL, " + MINUTE_STOP + " TEXT NOT NULL, " + TITLE + " TEXT NOT NULL, " + STATUS + " TEXT NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
