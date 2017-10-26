package com.kidsdynamic.data.dao;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

import com.kidsdynamic.data.dao.DB_Kids;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "t_kids".
*/
public class KidsDao extends AbstractDao<DB_Kids, Integer> {

    public static final String TABLENAME = "t_kids";

    /**
     * Properties of entity DB_Kids.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property KidsId = new Property(0, int.class, "kidsId", true, "kids_id");
        public final static Property Name = new Property(1, String.class, "name", false, "name");
        public final static Property DateCreated = new Property(2, String.class, "dateCreated", false, "dateCreated");
        public final static Property MacId = new Property(3, String.class, "macId", false, "macId");
        public final static Property FirmwareVersion = new Property(4, String.class, "firmwareVersion", false, "firmwareVersion");
        public final static Property Profile = new Property(5, String.class, "profile", false, "profile");
        public final static Property State = new Property(6, String.class, "state", false, "state");
        public final static Property ParentId = new Property(7, Integer.class, "parentId", false, "parent_id");
    };

    private DaoSession daoSession;

    private Query<DB_Kids> dB_User_KidsListQuery;

    public KidsDao(DaoConfig config) {
        super(config);
    }
    
    public KidsDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"t_kids\" (" + //
                "\"kids_id\" INTEGER PRIMARY KEY NOT NULL ," + // 0: kidsId
                "\"name\" TEXT," + // 1: name
                "\"dateCreated\" TEXT," + // 2: dateCreated
                "\"macId\" TEXT," + // 3: macId
                "\"firmwareVersion\" TEXT," + // 4: firmwareVersion
                "\"profile\" TEXT," + // 5: profile
                "\"state\" TEXT," + // 6: state
                "\"parent_id\" INTEGER);"); // 7: parentId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"t_kids\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, DB_Kids entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getKidsId());
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String dateCreated = entity.getDateCreated();
        if (dateCreated != null) {
            stmt.bindString(3, dateCreated);
        }
 
        String macId = entity.getMacId();
        if (macId != null) {
            stmt.bindString(4, macId);
        }
 
        String firmwareVersion = entity.getFirmwareVersion();
        if (firmwareVersion != null) {
            stmt.bindString(5, firmwareVersion);
        }
 
        String profile = entity.getProfile();
        if (profile != null) {
            stmt.bindString(6, profile);
        }
 
        String state = entity.getState();
        if (state != null) {
            stmt.bindString(7, state);
        }
 
        Integer parentId = entity.getParentId();
        if (parentId != null) {
            stmt.bindLong(8, parentId);
        }
    }

    @Override
    protected void attachEntity(DB_Kids entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Integer readKey(Cursor cursor, int offset) {
        return cursor.getInt(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public DB_Kids readEntity(Cursor cursor, int offset) {
        DB_Kids entity = new DB_Kids( //
            cursor.getInt(offset + 0), // kidsId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // dateCreated
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // macId
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // firmwareVersion
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // profile
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // state
            cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7) // parentId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, DB_Kids entity, int offset) {
        entity.setKidsId(cursor.getInt(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setDateCreated(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setMacId(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setFirmwareVersion(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setProfile(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setState(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setParentId(cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7));
     }
    
    /** @inheritdoc */
    @Override
    protected Integer updateKeyAfterInsert(DB_Kids entity, long rowId) {
        return entity.getKidsId();
    }
    
    /** @inheritdoc */
    @Override
    public Integer getKey(DB_Kids entity) {
        if(entity != null) {
            return entity.getKidsId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "kidsList" to-many relationship of DB_User. */
    public List<DB_Kids> _queryDB_User_KidsList(Integer parentId) {
        synchronized (this) {
            if (dB_User_KidsListQuery == null) {
                QueryBuilder<DB_Kids> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.ParentId.eq(null));
                dB_User_KidsListQuery = queryBuilder.build();
            }
        }
        Query<DB_Kids> query = dB_User_KidsListQuery.forCurrentThread();
        query.setParameter(0, parentId);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getUserDao().getAllColumns());
            builder.append(" FROM t_kids T");
            builder.append(" LEFT JOIN t_user T0 ON T.\"parent_id\"=T0.\"users_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected DB_Kids loadCurrentDeep(Cursor cursor, boolean lock) {
        DB_Kids entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        DB_User dB_User = loadCurrentOther(daoSession.getUserDao(), cursor, offset);
        entity.setDB_User(dB_User);

        return entity;    
    }

    public DB_Kids loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<DB_Kids> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<DB_Kids> list = new ArrayList<DB_Kids>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<DB_Kids> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<DB_Kids> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}