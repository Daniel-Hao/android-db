package com.daniel.www.greendao.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.daniel.www.greendao.model.GDUserInfoMore;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table GDUSER_INFO_MORE.
*/
public class GDUserInfoMoreDao extends AbstractDao<GDUserInfoMore, Long> {

    public static final String TABLENAME = "GDUSER_INFO_MORE";

    /**
     * Properties of entity GDUserInfoMore.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property User_id = new Property(1, String.class, "user_id", false, "USER_ID");
        public final static Property Login_user_id = new Property(2, String.class, "login_user_id", false, "LOGIN_USER_ID");
        public final static Property Nick_name = new Property(3, String.class, "nick_name", false, "NICK_NAME");
        public final static Property Sex_code = new Property(4, String.class, "sex_code", false, "SEX_CODE");
        public final static Property Age = new Property(5, Integer.class, "age", false, "AGE");
    };

    private DaoSession daoSession;


    public GDUserInfoMoreDao(DaoConfig config) {
        super(config);
    }
    
    public GDUserInfoMoreDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'GDUSER_INFO_MORE' (" + //
                "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'USER_ID' TEXT NOT NULL ," + // 1: user_id
                "'LOGIN_USER_ID' TEXT NOT NULL ," + // 2: login_user_id
                "'NICK_NAME' TEXT," + // 3: nick_name
                "'SEX_CODE' TEXT," + // 4: sex_code
                "'AGE' INTEGER);"); // 5: age
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'GDUSER_INFO_MORE'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, GDUserInfoMore entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getUser_id());
        stmt.bindString(3, entity.getLogin_user_id());
 
        String nick_name = entity.getNick_name();
        if (nick_name != null) {
            stmt.bindString(4, nick_name);
        }
 
        String sex_code = entity.getSex_code();
        if (sex_code != null) {
            stmt.bindString(5, sex_code);
        }
 
        Integer age = entity.getAge();
        if (age != null) {
            stmt.bindLong(6, age);
        }
    }

    @Override
    protected void attachEntity(GDUserInfoMore entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public GDUserInfoMore readEntity(Cursor cursor, int offset) {
        GDUserInfoMore entity = new GDUserInfoMore( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // user_id
            cursor.getString(offset + 2), // login_user_id
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // nick_name
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // sex_code
            cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5) // age
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, GDUserInfoMore entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUser_id(cursor.getString(offset + 1));
        entity.setLogin_user_id(cursor.getString(offset + 2));
        entity.setNick_name(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setSex_code(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setAge(cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(GDUserInfoMore entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(GDUserInfoMore entity) {
        if(entity != null) {
            return entity.getId();
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
