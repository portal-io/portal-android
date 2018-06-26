package com.whaley.biz.setting.db;

import android.content.Context;

import com.whaley.biz.setting.constant.SettingConstants;
import com.whaleyvr.core.local.db.DBOpenHelperCreator;

import org.greenrobot.greendao.database.DatabaseOpenHelper;

public class LocalVideoDBOpenHelperCreator implements DBOpenHelperCreator {

    public static LocalVideoOpenHelper myOpenHelper;

    @Override
    public DatabaseOpenHelper onCreateDBOpenHelper(Context context) {
        if(myOpenHelper==null){
            myOpenHelper=new LocalVideoOpenHelper(context, SettingConstants.DB_NAME, null);
        }
        return myOpenHelper;
    }
}
