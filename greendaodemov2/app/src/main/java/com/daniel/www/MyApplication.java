package com.daniel.www;

import android.app.Application;
import android.content.Context;

import com.daniel.www.greendao.DaoMaster;
import com.daniel.www.greendao.DaoSession;

/**
 * Created by Daniel on 2016/12/30.
 */

public class MyApplication extends Application {

    private static MyApplication _instance;

    public static MyApplication getInstance() {
        return _instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        _instance = this;
    }

    //region GreenDAO的初始化操作
    private DaoMaster daoMaster;
    private DaoSession daoSession;

    /**
     * 取得DaoMaster
     *
     * @param context 上下文
     * @return DaoMaster
     */
    public DaoMaster getDaoMaster(Context context) {
        if (daoMaster == null) {
            DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(context,
                    "daniel_test_db", null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }

    /**
     * 取得DaoSession
     *
     * @param context 上下文
     * @return DaoSession
     */
    public DaoSession getDaoSession(Context context) {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster(context);
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }
    //endregion
}
