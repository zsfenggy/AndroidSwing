package com.kidsdynamic.data.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.kidsdynamic.data.dao.DB_Event;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "t_event".
*/
public class EventDao extends AbstractDao<DB_Event, Long> {

    public static final String TABLENAME = "t_event";

    /**
     * Properties of entity DB_Event.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property EventId = new Property(0, long.class, "eventId", true, "event_id");
        public final static Property KidIds = new Property(1, String.class, "kidIds", false, "kid_ids");
        public final static Property Name = new Property(2, String.class, "name", false, "event_name");
        public final static Property StartDate = new Property(3, String.class, "startDate", false, "start_date");
        public final static Property EndDate = new Property(4, String.class, "endDate", false, "end_date");
        public final static Property Color = new Property(5, String.class, "color", false, "color");
        public final static Property Description = new Property(6, String.class, "description", false, "description");
        public final static Property Alert = new Property(7, Integer.class, "alert", false, "alert");
        public final static Property Repeat = new Property(8, String.class, "repeat", false, "repeat");
        public final static Property TimezoneOffset = new Property(9, Integer.class, "timezoneOffset", false, "timezoneOffset");
    };

    private DaoSession daoSession;


    public EventDao(DaoConfig config) {
        super(config);
    }
    
    public EventDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"t_event\" (" + //
                "\"event_id\" INTEGER PRIMARY KEY NOT NULL ," + // 0: eventId
                "\"kid_ids\" TEXT," + // 1: kidIds
                "\"event_name\" TEXT," + // 2: name
                "\"start_date\" TEXT," + // 3: startDate
                "\"end_date\" TEXT," + // 4: endDate
                "\"color\" TEXT," + // 5: color
                "\"description\" TEXT," + // 6: description
                "\"alert\" INTEGER," + // 7: alert
                "\"repeat\" TEXT," + // 8: repeat
                "\"timezoneOffset\" INTEGER);"); // 9: timezoneOffset
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"t_event\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, DB_Event entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getEventId());
 
        String kidIds = entity.getKidIds();
        if (kidIds != null) {
            stmt.bindString(2, kidIds);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }
 
        String startDate = entity.getStartDate();
        if (startDate != null) {
            stmt.bindString(4, startDate);
        }
 
        String endDate = entity.getEndDate();
        if (endDate != null) {
            stmt.bindString(5, endDate);
        }
 
        String color = entity.getColor();
        if (color != null) {
            stmt.bindString(6, color);
        }
 
        String description = entity.getDescription();
        if (description != null) {
            stmt.bindString(7, description);
        }
 
        Integer alert = entity.getAlert();
        if (alert != null) {
            stmt.bindLong(8, alert);
        }
 
        String repeat = entity.getRepeat();
        if (repeat != null) {
            stmt.bindString(9, repeat);
        }
 
        Integer timezoneOffset = entity.getTimezoneOffset();
        if (timezoneOffset != null) {
            stmt.bindLong(10, timezoneOffset);
        }
    }

    @Override
    protected void attachEntity(DB_Event entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public DB_Event readEntity(Cursor cursor, int offset) {
        DB_Event entity = new DB_Event( //
            cursor.getLong(offset + 0), // eventId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // kidIds
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // name
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // startDate
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // endDate
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // color
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // description
            cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7), // alert
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // repeat
            cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9) // timezoneOffset
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, DB_Event entity, int offset) {
        entity.setEventId(cursor.getLong(offset + 0));
        entity.setKidIds(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setStartDate(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setEndDate(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setColor(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setDescription(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setAlert(cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7));
        entity.setRepeat(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setTimezoneOffset(cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(DB_Event entity, long rowId) {
        entity.setEventId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(DB_Event entity) {
        if(entity != null) {
            return entity.getEventId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}