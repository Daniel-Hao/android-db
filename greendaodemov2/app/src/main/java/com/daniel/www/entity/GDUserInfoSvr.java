package com.daniel.www.entity;

import android.content.Context;

import com.daniel.www.MyApplication;
import com.daniel.www.greendao.DaoSession;
import com.daniel.www.greendao.UserInfoDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by Daniel on 2016/12/30.
 */

public class GDUserInfoSvr {

    private static GDUserInfoSvr instance;
    private static Context appContext;
    private DaoSession mDaoSession;
    private UserInfoDao userInfoDao;

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
            instance.userInfoDao = instance.mDaoSession.getUserInfoDao();
        }
        return instance;
    }

    //查
    public UserInfo loadUserInfo(String userid) {

        QueryBuilder<UserInfo> mqBuilder = userInfoDao.queryBuilder();
        mqBuilder.where(UserInfoDao.Properties.User_id.eq(userid));
        List<UserInfo> result = mqBuilder.list();
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
    public List<UserInfo> loadAllUserInfo() {
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

    public List<UserInfo> queryUserInfo(String where, String... params) {
        return userInfoDao.queryRaw(where, params);
    }

    /**
     * 增，改
     * insert or update request
     *
     * @param request
     * @return insert or update request id
     */
    public long saveUserInfo(UserInfo request) {
        return userInfoDao.insertOrReplace(request);
    }

    /**
     * 增，改
     * insert or update requestList use transaction
     *
     * @param list
     */
    public void saveUserInfoLists(final List<UserInfo> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        userInfoDao.getSession().runInTx(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < list.size(); i++) {
                    UserInfo request = list.get(i);
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
        QueryBuilder<UserInfo> mqBuilder = userInfoDao.queryBuilder();
        mqBuilder.where(UserInfoDao.Properties.User_id.eq(userid));
        mqBuilder.buildDelete().executeDeleteWithoutDetachingEntities();
    }

    //刪
    public void deleteRequest(UserInfo request) {
        userInfoDao.delete(request);
    }
}
