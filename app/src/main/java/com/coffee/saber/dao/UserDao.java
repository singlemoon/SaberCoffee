package com.coffee.saber.dao;

import android.content.Context;
import android.database.Cursor;

import com.coffee.saber.App;
import com.coffee.saber.model.User;
import com.coffee.saber.utils.DataBaseOpenHelper;
import com.coffee.saber.utils.SPPrivateUtils;

/**
 * Created by Simo on 2018/12/19.
 */
public class UserDao {

    public static User getUser(Context context) {
        DataBaseOpenHelper db = App.getDb();
        User user = null;
//        "CREATE TABLE IF NOT EXISTS USER (" +
//                "  id INT(11) NOT NULL, " +
//                "  username VARCHAR(50) NOT NULL," +
//                "  PASSWORD VARCHAR(50) NOT NULL," +
//                "  nick VARCHAR(50) DEFAULT NULL," +
//                "  phone VARCHAR(11) DEFAULT NULL," +
//                "  sex INT(1) DEFAULT NULL," +
//                "  PRIMARY KEY (id)" +
//                ")";
        if (db != null) {
            Cursor cursor = db.query("user", "where id=" + SPPrivateUtils.getInt(context, "user_id", 0));
            if (cursor.moveToNext()) {
                user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndex("id")));
                user.setUsername(cursor.getString(cursor.getColumnIndex("username")));
                user.setPassword(cursor.getString(cursor.getColumnIndex("PASSWORD")));
                user.setNick(cursor.getString(cursor.getColumnIndex("nick")));
                user.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
                user.setSex(cursor.getInt(cursor.getColumnIndex("sex")));
            }
        }
        return user;
    }
}
