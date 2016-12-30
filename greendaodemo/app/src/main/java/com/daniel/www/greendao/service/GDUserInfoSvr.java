package com.daniel.www.greendao.service;

import android.content.Context;

import com.daniel.www.MyApplication;
import com.daniel.www.greendao.dao.DaoSession;
import com.daniel.www.greendao.dao.GDUserInfoDao;
import com.daniel.www.greendao.model.GDUserInfo;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;


/**
 * @author Daniel
 */
public class GDUserInfoSvr {

    // private static final String TAG = GDUserInfoSvr.class.getSimpleName();
    private static GDUserInfoSvr instance;
    private static Context appContext;
    private DaoSession mDaoSession;
    private GDUserInfoDao userInfoDao;

    private GDUserInfoSvr() {
    }

    public static void disposeSvr() {
        if (instance != null) {
            instance.mDaoSession = null;
            instance.userInfoDao = null;
        }
        appContext = null;
        instance = null;
    }

    public static GDUserInfoSvr getInstance(Context context) {
        if (instance == null) {
            instance = new GDUserInfoSvr();
            if (appContext == null) {
                appContext = context.getApplicationContext();
            }
            instance.mDaoSession = MyApplication.getInstance().getDaoSession(context);
            instance.userInfoDao = instance.mDaoSession.getGDUserInfoDao();
        }
        return instance;
    }

    //查
    public GDUserInfo loadUserInfo(String userid) {

        QueryBuilder<GDUserInfo> mqBuilder = userInfoDao.queryBuilder();
        mqBuilder.where(GDUserInfoDao.Properties.User_id.eq(userid));
        List<GDUserInfo> result = mqBuilder.list();
        if (result == null || result.size() == 0) {
            return null;
        } else {
            int size = result.size();
            if (size > 1) {
                for (int kk = 1; kk < size; kk++) {
                    deleteRequest(result.get(kk));
                }
            }
            return result.get(0);
        }
    }
    //查
    public List<GDUserInfo> loadAllUserInfo() {
        return userInfoDao.loadAll();
    }

    /**
     * query list with where clause ex: begin_date_time >= ? AND end_date_time
     * <= ?
     *
     * @param where  where clause, include 'where' word
     * @param params query parameters
     * @return
     */

    public List<GDUserInfo> queryUserInfo(String where, String... params) {
        return userInfoDao.queryRaw(where, params);
    }

    /**
     * 增，改
     * insert or update request
     *
     * @param request
     * @return insert or update request id
     */
    public long saveUserInfo(GDUserInfo request) {
        return userInfoDao.insertOrReplace(request);
    }

    /**
     * 增，改
     * insert or update requestList use transaction
     *
     * @param list
     */
    public void saveUserInfoLists(final List<GDUserInfo> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        userInfoDao.getSession().runInTx(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < list.size(); i++) {
                    GDUserInfo request = list.get(i);
                    userInfoDao.insertOrReplace(request);
                }
            }
        });

    }

    /**
     * 刪
     * delete all request
     */
    public void deleteAllUserInfo() {
        userInfoDao.deleteAll();
    }

    //刪
    public void deleteAllUserInfo(String userid) {
        QueryBuilder<GDUserInfo> mqBuilder = userInfoDao.queryBuilder();
        mqBuilder.where(GDUserInfoDao.Properties.User_id.eq(userid));
        mqBuilder.buildDelete().executeDeleteWithoutDetachingEntities();
    }

    //刪
    public void deleteRequest(GDUserInfo request) {
        userInfoDao.delete(request);
    }
}
