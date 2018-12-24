package com.coffee.saber;

import android.app.Application;

import com.coffee.saber.utils.DataBaseOpenHelper;
import com.coffee.saber.utils.Global;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simo on 2018/12/19.
 */
public class App extends Application {
    private static DataBaseOpenHelper mDbHelper = null;

    @Override
    public void onCreate() {
        super.onCreate();
        List<String> sqlList = new ArrayList<>();
        sqlList.add(Global.CREATE_USER_TABLE);
        mDbHelper = DataBaseOpenHelper.getInstance(this, "sabercoffee", 1, sqlList);
    }

    public static DataBaseOpenHelper getDb() {
        return mDbHelper;
    }
}
