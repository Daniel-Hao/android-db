package com.daniel.www.greendao.service;

import android.content.Context;
import android.text.TextUtils;

import com.daniel.www.MyApplication;
import com.daniel.www.greendao.dao.DaoSession;
import com.daniel.www.greendao.dao.GDUserInfoMoreDao;
import com.daniel.www.greendao.dao.GDUserLanguagesDao;
import com.daniel.www.greendao.dao.GDUserServicesDao;
import com.daniel.www.greendao.model.GDUserInfoMore;
import com.daniel.www.greendao.model.GDUserLanguages;
import com.daniel.www.greendao.model.GDUserServices;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * @author Daniel
 */
public class GDUserInfoMoreSvr {

    //private static final String TAG = GDUserInfoSvr.class.getSimpleName();
    private static GDUserInfoMoreSvr instance;
    private static Context appContext;
    private DaoSession mDaoSession;
    private GDUserInfoMoreDao userInfoMoreDao;

    private GDUserLanguagesDao userLanguagesDao;
    private GDUserServicesDao userServicesDao;

    private GDUserInfoMoreSvr() {
    }

    public static void disposeSvr() {
        if (instance != null) {
            instance.userLanguagesDao = null;
            instance.userServicesDao = null;
            instance.mDaoSession = null;
            instance.userInfoMoreDao = null;
        }

        appContext = null;
        instance = null;
    }

    public static GDUserInfoMoreSvr getInstance(Context context) {
        if (instance == null) {
            instance = new GDUserInfoMoreSvr();
            if (appContext == null) {
                appContext = context.getApplicationContext();
            }
            instance.mDaoSession = MyApplication.getInstance().getDaoSession(appContext);

            instance.userInfoMoreDao = instance.mDaoSession.getGDUserInfoMoreDao();
            instance.userLanguagesDao = instance.mDaoSession
                    .getGDUserLanguagesDao();
            instance.userServicesDao = instance.mDaoSession
                    .getGDUserServicesDao();

        }
        return instance;
    }

    //查
    public GDUserInfoMore loadUserInfo(String loginUserId, String userId) {
        if (TextUtils.isEmpty(loginUserId) || TextUtils.isEmpty(userId)) {
            return null;
        }
        QueryBuilder<GDUserInfoMore> mqBuilder = userInfoMoreDao.queryBuilder();
        mqBuilder.where(GDUserInfoMoreDao.Properties.Login_user_id.eq(loginUserId),
                GDUserInfoMoreDao.Properties.User_id.eq(userId));
        List<GDUserInfoMore> result = mqBuilder.list();

        if (result == null || result.size() == 0) {
            return null;
        } else {
            int size = result.size();
            if (size > 1) {
                for (int kk = 1; kk < size; kk++) {
                    deleteUserInfo(result.get(kk));
                }
            }
            return result.get(0);
        }
    }

    //查
    public GDUserInfoMore loadUserInfoByUserInfoId(long userInfoId) {
        GDUserInfoMore model = userInfoMoreDao.load(userInfoId);
        return model;
    }

    //查
    public List<GDUserInfoMore> loadAllUserInfo() {
        return userInfoMoreDao.loadAll();
    }

    /**
     * query list with where clause ex: begin_date_time >= ? AND end_date_time
     * <= ?
     *
     * @param where  where clause, include 'where' word
     * @param params query parameters
     * @return
     */

    //查
    public List<GDUserInfoMore> queryUserInfo(String where, String... params) {
        return userInfoMoreDao.queryRaw(where, params);
    }

    // 根据用户id 查询 用户信息并保存在list中
    public List<GDUserInfoMore> queryUserInfoByUserId(int loginUserId, int userId) {
        QueryBuilder<GDUserInfoMore> mqBuilder = userInfoMoreDao.queryBuilder();
        mqBuilder.where(GDUserInfoMoreDao.Properties.Login_user_id.eq(loginUserId),
                GDUserInfoMoreDao.Properties.User_id.eq(userId));
        return mqBuilder.list();
    }

    /**
     * 排序
     * list UserInfo order by acs or desc
     *
     * @param orderType
     * @return List<UserInfo>
     */
    public List<GDUserInfoMore> loadUserInfoWithOrder(OrderType orderType) {
        List<GDUserInfoMore> list = null;
        QueryBuilder<GDUserInfoMore> qb = userInfoMoreDao.queryBuilder();

        switch (orderType) {
            case DESC:
                list = qb.orderDesc(GDUserInfoMoreDao.Properties.User_id).list();
                break;
            case ASC:
                list = qb.orderAsc(GDUserInfoMoreDao.Properties.User_id).list();
                break;
            default:
                list = qb.orderDesc(GDUserInfoMoreDao.Properties.User_id).list();
                break;
        }

        return list;
    }

    public enum OrderType {
        DESC, ASC
    }

    /**
     * 增，改
     * insert or update userInfo
     *
     * @param userInfo
     * @return insert or update userInfo id
     */
    public long saveuserInfo(GDUserInfoMore userInfo) {
        long id = userInfoMoreDao.insertOrReplace(userInfo);
        return id;
    }

    /**
     * 多表
     * 增，改
     * insert or update userInfo
     *
     * @param userInfo
     * @return insert or update userInfo id
     */
    public long saveuserInfoAndToMany(GDUserInfoMore userInfo,
                                      boolean isUpdateLanguage,
                                      boolean isUpdateService) {
        final long relationId = userInfoMoreDao.insertOrReplace(userInfo);

        if (isUpdateLanguage) {
            QueryBuilder<GDUserLanguages> mqBuilder = userLanguagesDao
                    .queryBuilder();
            mqBuilder.where(GDUserLanguagesDao.Properties.User_info_id
                    .eq(relationId));
            mqBuilder.buildDelete().executeDeleteWithoutDetachingEntities();
            final List<GDUserLanguages> listUserLanguage = userInfo
                    .getRp_UserInfoToLanguages();
            if (listUserLanguage != null && listUserLanguage.size() > 0) {
                userLanguagesDao.getSession().runInTx(new Runnable() {

                    @Override
                    public void run() {
                        for (int i = 0; i < listUserLanguage.size(); i++) {
                            GDUserLanguages gDUserLanguages = listUserLanguage
                                    .get(i);
                            gDUserLanguages.setUser_info_id(relationId);
                            userLanguagesDao.insertOrReplace(gDUserLanguages);
                        }
                    }
                });
            }
        }
        if (isUpdateService) {
            QueryBuilder<GDUserServices> mqBuilder = userServicesDao
                    .queryBuilder();
            mqBuilder.where(GDUserServicesDao.Properties.User_info_id
                    .eq(relationId));
            mqBuilder.buildDelete().executeDeleteWithoutDetachingEntities();
            final List<GDUserServices> listUserService = userInfo
                    .getRp_UserInfoToServices();
            if (listUserService != null && listUserService.size() > 0) {
                userServicesDao.getSession().runInTx(new Runnable() {

                    @Override
                    public void run() {
                        for (int i = 0; i < listUserService.size(); i++) {
                            GDUserServices gDUserServices = listUserService
                                    .get(i);
                            gDUserServices.setUser_info_id(relationId);
                            userServicesDao.insertOrReplace(gDUserServices);
                        }
                    }
                });
            }
        }
        return relationId;
    }

    public long saveUserLanguage(GDUserLanguages userLanguages) {
        return userLanguagesDao.insert(userLanguages);
    }

    public void saveUserLanguageList(List<GDUserLanguages> userLanguagesList) {
        userLanguagesDao.insertInTx(userLanguagesList);
    }

    /**
     * insert or update userInfoList use transaction
     *
     * @param list
     */
    public void saveUserInfoLists(final List<GDUserInfoMore> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        userInfoMoreDao.getSession().runInTx(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < list.size(); i++) {
                    GDUserInfoMore userInfo = list.get(i);
                    userInfoMoreDao.insertOrReplace(userInfo);
                }
            }
        });

    }

    /**
     * 删除服务器已删除数据，更新服务器已更新数据，与服务器同步
     **/
    public void saveUserInfoLists(String loginUserId, final List<GDUserInfoMore> list,
                                  List<String> listDelIds, String type, final boolean isUpdateService) {
        if (listDelIds != null && listDelIds.size() > 0) {
            deleteByKeyInTx(loginUserId, listDelIds);
        }
        if ("all".equalsIgnoreCase(type)) {
            deleteAllFriend(loginUserId);
        }

        if (list != null && list.size() > 0) {
            userInfoMoreDao.getSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < list.size(); i++) {
                        final GDUserInfoMore userInfo = list.get(i);
                        userInfoMoreDao.insertOrReplace(userInfo);

                        if (isUpdateService) {
                            QueryBuilder<GDUserServices> mqBuilder = userServicesDao
                                    .queryBuilder();
                            mqBuilder
                                    .where(GDUserServicesDao.Properties.User_info_id
                                            .eq(userInfo.getId()));
                            mqBuilder.buildDelete()
                                    .executeDeleteWithoutDetachingEntities();

                            final List<GDUserServices> listUserService = userInfo
                                    .getRp_UserInfoToServices();
                            if (listUserService != null
                                    && listUserService.size() > 0) {
                                userServicesDao.getSession().runInTx(
                                        new Runnable() {

                                            @Override
                                            public void run() {
                                                for (int i = 0; i < listUserService
                                                        .size(); i++) {
                                                    GDUserServices gDUserServices = listUserService
                                                            .get(i);
                                                    gDUserServices
                                                            .setUser_info_id(userInfo
                                                                    .getId());
                                                    userServicesDao
                                                            .insertOrReplace(gDUserServices);
                                                }
                                            }
                                        });
                            }
                        }
                    }
                }
            });
        }
    }

    /**
     * delete all userInfo
     */
    public void deleteAllUserInfo() {
        userInfoMoreDao.deleteAll();
    }

    /**
     * delete userInfo by id
     *
     * @param id
     */
    public void deleteUserInfo(long id) {
        userInfoMoreDao.deleteByKey(id);
    }

    public void deleteAllFriend(String loginUserId) {
        QueryBuilder<GDUserInfoMore> mqBuilder = userInfoMoreDao.queryBuilder();
        mqBuilder.where(GDUserInfoMoreDao.Properties.Login_user_id.eq(loginUserId),
                GDUserInfoMoreDao.Properties.Login_user_id.eq(UserFriendType.Friend.value()));
        mqBuilder.buildDelete().executeDeleteWithoutDetachingEntities();
    }

    public void deleteUserInfo(GDUserInfoMore userInfo) {
        userInfoMoreDao.delete(userInfo);
    }

    public void deleteByKeyInTx(String loginUserId, List<String> listDelUserIds) {
        if (listDelUserIds == null || listDelUserIds.size() <= 0) {
            return;
        }

        int size = listDelUserIds.size();

        for (int kk = 0; kk < size; kk++) {
            QueryBuilder<GDUserInfoMore> mqBuilder = userInfoMoreDao.queryBuilder();

            mqBuilder.where(GDUserInfoMoreDao.Properties.Login_user_id.eq(loginUserId),
                    GDUserInfoMoreDao.Properties.User_id.eq(listDelUserIds.get(kk)));

            mqBuilder.buildDelete().executeDeleteWithoutDetachingEntities();
        }
    }

    /**
     * 是否是好友枚举
     **/
    public enum UserFriendType {
        Friend((short) 1, "Friend"), CommonUser((short) 2, "CommonUser");

        private short _value;
        private String _code;

        UserFriendType(short value, String code) {
            this._value = value;
            this._code = code;
        }

        public short value() {
            return _value;
        }

        public String code() {
            return _code;
        }
    }
}
