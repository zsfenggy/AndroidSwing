package com.kidsdynamic.data.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.kidsdynamic.data.dao.DB_User;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "t_user".
*/
public class UserDao extends AbstractDao<DB_User, Long> {

    public static final String TABLENAME = "t_user";

    /**
     * Properties of entity DB_User.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property UserId = new Property(0, long.class, "userId", true, "users_id");
        public final static Property Email = new Property(1, String.class, "email", false, "email");
        public final static Property FirstName = new Property(2, String.class, "firstName", false, "first_name");
        public final static Property LastName = new Property(3, String.class, "lastName", false, "last_name");
        public final static Property LastUpdate = new Property(4, Long.class, "lastUpdate", false, "last_update");
        public final static Property DataCreate = new Property(5, Long.class, "dataCreate", false, "data_create");
        public final static Property ZipCode = new Property(6, String.class, "zipCode", false, "zip_code");
        public final static Property PhoneNum = new Property(7, String.class, "phoneNum", false, "phone_number");
        public final static Property Profile = new Property(8, String.class, "profile", false, "profile");
        public final static Property FocusID = new Property(9, Integer.class, "focusID", false, "focus_id");
        public final static Property FocusPID = new Property(10, Integer.class, "focusPID", false, "focus_pid");
        public final static Property RegistrationId = new Property(11, String.class, "registrationId", false, "registrationId");
    };

    private DaoSession daoSession;


    public UserDao(DaoConfig config) {
        super(config);
    }
    
    public UserDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"t_user\" (" + //
                "\"users_id\" INTEGER PRIMARY KEY NOT NULL ," + // 0: userId
                "\"email\" TEXT NOT NULL ," + // 1: email
                "\"first_name\" TEXT," + // 2: firstName
                "\"last_name\" TEXT," + // 3: lastName
                "\"last_update\" INTEGER," + // 4: lastUpdate
                "\"data_create\" INTEGER," + // 5: dataCreate
                "\"zip_code\" TEXT," + // 6: zipCode
                "\"phone_number\" TEXT," + // 7: phoneNum
                "\"profile\" TEXT," + // 8: profile
                "\"focus_id\" INTEGER," + // 9: focusID
                "\"focus_pid\" INTEGER," + // 10: focusPID
                "\"registrationId\" TEXT);"); // 11: registrationId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"t_user\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, DB_User entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getUserId());
        stmt.bindString(2, entity.getEmail());
 
        String firstName = entity.getFirstName();
        if (firstName != null) {
            stmt.bindString(3, firstName);
        }
 
        String lastName = entity.getLastName();
        if (lastName != null) {
            stmt.bindString(4, lastName);
        }
 
        Long lastUpdate = entity.getLastUpdate();
        if (lastUpdate != null) {
            stmt.bindLong(5, lastUpdate);
        }
 
        Long dataCreate = entity.getDataCreate();
        if (dataCreate != null) {
            stmt.bindLong(6, dataCreate);
        }
 
        String zipCode = entity.getZipCode();
        if (zipCode != null) {
            stmt.bindString(7, zipCode);
        }
 
        String phoneNum = entity.getPhoneNum();
        if (phoneNum != null) {
            stmt.bindString(8, phoneNum);
        }
 
        String profile = entity.getProfile();
        if (profile != null) {
            stmt.bindString(9, profile);
        }
 
        Integer focusID = entity.getFocusID();
        if (focusID != null) {
            stmt.bindLong(10, focusID);
        }
 
        Integer focusPID = entity.getFocusPID();
        if (focusPID != null) {
            stmt.bindLong(11, focusPID);
        }
 
        String registrationId = entity.getRegistrationId();
        if (registrationId != null) {
            stmt.bindString(12, registrationId);
        }
    }

    @Override
    protected void attachEntity(DB_User entity) {
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
    public DB_User readEntity(Cursor cursor, int offset) {
        DB_User entity = new DB_User( //
            cursor.getLong(offset + 0), // userId
            cursor.getString(offset + 1), // email
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // firstName
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // lastName
            cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4), // lastUpdate
            cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5), // dataCreate
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // zipCode
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // phoneNum
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // profile
            cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9), // focusID
            cursor.isNull(offset + 10) ? null : cursor.getInt(offset + 10), // focusPID
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11) // registrationId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, DB_User entity, int offset) {
        entity.setUserId(cursor.getLong(offset + 0));
        entity.setEmail(cursor.getString(offset + 1));
        entity.setFirstName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setLastName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setLastUpdate(cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4));
        entity.setDataCreate(cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5));
        entity.setZipCode(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setPhoneNum(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setProfile(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setFocusID(cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9));
        entity.setFocusPID(cursor.isNull(offset + 10) ? null : cursor.getInt(offset + 10));
        entity.setRegistrationId(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(DB_User entity, long rowId) {
        entity.setUserId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(DB_User entity) {
        if(entity != null) {
            return entity.getUserId();
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
