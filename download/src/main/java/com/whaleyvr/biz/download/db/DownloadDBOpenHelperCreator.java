package com.whaleyvr.biz.download.db;

import android.content.Context;

import com.whaleyvr.biz.download.util.DownloadConstants;
import com.whaleyvr.core.local.db.DBOpenHelperCreator;

import org.greenrobot.greendao.database.DatabaseOpenHelper;

public class DownloadDBOpenHelperCreator implements DBOpenHelperCreator {

    public static DownloadOpenHelper myOpenHelper;

    @Override
    public DatabaseOpenHelper onCreateDBOpenHelper(Context context) {
        if(myOpenHelper==null){
            myOpenHelper=new DownloadOpenHelper(context, DownloadConstants.DB_NAME, null);
        }
        return myOpenHelper;
    }
}
