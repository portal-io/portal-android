package com.whaleyvr.biz.download.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "DOWNLOAD_BEAN".
*/
public class DownloadBeanDao extends AbstractDao<DownloadBean, String> {

    public static final String TABLENAME = "DOWNLOAD_BEAN";

    /**
     * Properties of entity DownloadBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Itemid = new Property(0, String.class, "itemid", true, "ITEMID");
        public final static Property Status = new Property(1, int.class, "status", false, "STATUS");
        public final static Property Progress = new Property(2, float.class, "progress", false, "PROGRESS");
        public final static Property DownloadUrl = new Property(3, String.class, "downloadUrl", false, "DOWNLOAD_URL");
        public final static Property SavePath = new Property(4, String.class, "savePath", false, "SAVE_PATH");
        public final static Property Name = new Property(5, String.class, "name", false, "NAME");
        public final static Property Pic = new Property(6, String.class, "pic", false, "PIC");
        public final static Property TotalSize = new Property(7, long.class, "totalSize", false, "TOTAL_SIZE");
        public final static Property CurrentSize = new Property(8, long.class, "currentSize", false, "CURRENT_SIZE");
        public final static Property Type = new Property(9, int.class, "type", false, "TYPE");
        public final static Property DownloadDate = new Property(10, long.class, "downloadDate", false, "DOWNLOAD_DATE");
        public final static Property Intro = new Property(11, String.class, "intro", false, "INTRO");
        public final static Property RenderType = new Property(12, String.class, "renderType", false, "RENDER_TYPE");
    }


    public DownloadBeanDao(DaoConfig config) {
        super(config);
    }
    
    public DownloadBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"DOWNLOAD_BEAN\" (" + //
                "\"ITEMID\" TEXT PRIMARY KEY NOT NULL ," + // 0: itemid
                "\"STATUS\" INTEGER NOT NULL ," + // 1: status
                "\"PROGRESS\" REAL NOT NULL ," + // 2: progress
                "\"DOWNLOAD_URL\" TEXT," + // 3: downloadUrl
                "\"SAVE_PATH\" TEXT," + // 4: savePath
                "\"NAME\" TEXT," + // 5: name
                "\"PIC\" TEXT," + // 6: pic
                "\"TOTAL_SIZE\" INTEGER NOT NULL ," + // 7: totalSize
                "\"CURRENT_SIZE\" INTEGER NOT NULL ," + // 8: currentSize
                "\"TYPE\" INTEGER NOT NULL ," + // 9: type
                "\"DOWNLOAD_DATE\" INTEGER NOT NULL ," + // 10: downloadDate
                "\"INTRO\" TEXT," + // 11: intro
                "\"RENDER_TYPE\" TEXT);"); // 12: renderType
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"DOWNLOAD_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, DownloadBean entity) {
        stmt.clearBindings();
 
        String itemid = entity.getItemid();
        if (itemid != null) {
            stmt.bindString(1, itemid);
        }
        stmt.bindLong(2, entity.getStatus());
        stmt.bindDouble(3, entity.getProgress());
 
        String downloadUrl = entity.getDownloadUrl();
        if (downloadUrl != null) {
            stmt.bindString(4, downloadUrl);
        }
 
        String savePath = entity.getSavePath();
        if (savePath != null) {
            stmt.bindString(5, savePath);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(6, name);
        }
 
        String pic = entity.getPic();
        if (pic != null) {
            stmt.bindString(7, pic);
        }
        stmt.bindLong(8, entity.getTotalSize());
        stmt.bindLong(9, entity.getCurrentSize());
        stmt.bindLong(10, entity.getType());
        stmt.bindLong(11, entity.getDownloadDate());
 
        String intro = entity.getIntro();
        if (intro != null) {
            stmt.bindString(12, intro);
        }
 
        String renderType = entity.getRenderType();
        if (renderType != null) {
            stmt.bindString(13, renderType);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, DownloadBean entity) {
        stmt.clearBindings();
 
        String itemid = entity.getItemid();
        if (itemid != null) {
            stmt.bindString(1, itemid);
        }
        stmt.bindLong(2, entity.getStatus());
        stmt.bindDouble(3, entity.getProgress());
 
        String downloadUrl = entity.getDownloadUrl();
        if (downloadUrl != null) {
            stmt.bindString(4, downloadUrl);
        }
 
        String savePath = entity.getSavePath();
        if (savePath != null) {
            stmt.bindString(5, savePath);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(6, name);
        }
 
        String pic = entity.getPic();
        if (pic != null) {
            stmt.bindString(7, pic);
        }
        stmt.bindLong(8, entity.getTotalSize());
        stmt.bindLong(9, entity.getCurrentSize());
        stmt.bindLong(10, entity.getType());
        stmt.bindLong(11, entity.getDownloadDate());
 
        String intro = entity.getIntro();
        if (intro != null) {
            stmt.bindString(12, intro);
        }
 
        String renderType = entity.getRenderType();
        if (renderType != null) {
            stmt.bindString(13, renderType);
        }
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public DownloadBean readEntity(Cursor cursor, int offset) {
        DownloadBean entity = new DownloadBean( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // itemid
            cursor.getInt(offset + 1), // status
            cursor.getFloat(offset + 2), // progress
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // downloadUrl
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // savePath
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // name
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // pic
            cursor.getLong(offset + 7), // totalSize
            cursor.getLong(offset + 8), // currentSize
            cursor.getInt(offset + 9), // type
            cursor.getLong(offset + 10), // downloadDate
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // intro
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12) // renderType
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, DownloadBean entity, int offset) {
        entity.setItemid(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setStatus(cursor.getInt(offset + 1));
        entity.setProgress(cursor.getFloat(offset + 2));
        entity.setDownloadUrl(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setSavePath(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setName(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setPic(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setTotalSize(cursor.getLong(offset + 7));
        entity.setCurrentSize(cursor.getLong(offset + 8));
        entity.setType(cursor.getInt(offset + 9));
        entity.setDownloadDate(cursor.getLong(offset + 10));
        entity.setIntro(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setRenderType(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
     }
    
    @Override
    protected final String updateKeyAfterInsert(DownloadBean entity, long rowId) {
        return entity.getItemid();
    }
    
    @Override
    public String getKey(DownloadBean entity) {
        if(entity != null) {
            return entity.getItemid();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(DownloadBean entity) {
        return entity.getItemid() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
