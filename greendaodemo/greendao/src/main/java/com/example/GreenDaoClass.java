package com.example;


import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

/**
 * Created by Daniel
 * 使用手册：
 *  1、main()绑定schema
 * 注意事项：
 *  1、每运行一次，DB版本号升级
 *  2、new DaoGenerator().generateAll，查看项目路径是否正确
 */
public class GreenDaoClass {

    public static void main(String[] args) throws Exception {
        System.out.println("Hello DaoGenerator");

        // first parameter for version, <span></span> second for default
        // generate package
        // Schema对象接受2个参数，第一个参数是DB的版本号，通过更新版本号来更新数据库。
        // 第二个参数是自动生成代码的包路径
        Schema schema = new Schema(2, "com.daniel.www.greendao.model");

        addUserInfo(schema);
        addUserInfoMore(schema);

        // set dao class generate package
        schema.setDefaultJavaPackageDao("com.daniel.www.greendao.dao");
        // keep custom code block
        schema.enableKeepSectionsByDefault();
        new DaoGenerator().generateAll(schema, "../greendaodemo/app/src/main/java");
    }

    //单表
    private static void addUserInfo(Schema schema) {
        // begin:add countDownTime
        Entity countDownTime = schema.addEntity("GDUserInfo");
        //自增id
        countDownTime.addIdProperty().autoincrement().primaryKey();
        countDownTime.addStringProperty("user_id");
        countDownTime.addStringProperty("user_name");
        countDownTime.addStringProperty("login_user_id").notNull();//非空
        // end:add countDownTime
    }

    //多表
    private static void addUserInfoMore(Schema schema) {
        Entity userInfo = schema.addEntity("GDUserInfoMore");
        userInfo.addIdProperty().autoincrement().primaryKey();
        userInfo.addStringProperty("user_id").notNull();
        userInfo.addStringProperty("login_user_id").notNull();
        userInfo.addStringProperty("nick_name");
        userInfo.addStringProperty("sex_code");
        userInfo.addIntProperty("age");
        // begin:add languages
        Entity userLanguages = schema.addEntity("GDUserLanguages");
        userLanguages.addIdProperty().autoincrement().primaryKey();
        userLanguages.addStringProperty("user_id");
        userLanguages.addStringProperty("language_code");

        Property user_info_id_languages = userLanguages
                .addLongProperty("user_info_id").notNull().getProperty();
        userLanguages.addToOne(userInfo, user_info_id_languages);
        ToMany userInfoToLanguages = userInfo.addToMany(userLanguages,
                user_info_id_languages, "rp_UserInfoToLanguages");
        // end:add languages

        // begin:add services
        Entity userServices = schema.addEntity("GDUserServices");
        userServices.addIdProperty().autoincrement().primaryKey();
        userServices.addStringProperty("user_id");
        userServices.addStringProperty("service_code");

        Property user_info_id_services = userServices
                .addLongProperty("user_info_id").notNull().getProperty();
        userServices.addToOne(userInfo, user_info_id_services);
        ToMany userInfoToServices = userInfo.addToMany(userServices,
                user_info_id_services, "rp_UserInfo_Services");
        userInfoToServices.setName("rp_UserInfoToServices"); //Optional
        // end:add services
    }

    /**
     * 自动生成得GDUserInfoMore中：
     * // KEEP METHODS - put your custom methods here
     * 表示，数据库升级，再次自动生成不会覆盖或重写的部分
     * // KEEP METHODS END
     */
}
