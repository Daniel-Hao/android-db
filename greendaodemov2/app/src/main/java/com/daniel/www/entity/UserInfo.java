package com.daniel.www.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Daniel on 2016/12/30.
 */

@Entity
public class UserInfo {

    @Id(autoincrement = true)
    private Long id;
    @NotNull
    private String user_id;
    private String user_name;
    public String getUser_name() {
        return this.user_name;
    }
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
    public String getUser_id() {
        return this.user_id;
    }
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 1726585432)
    public UserInfo(Long id, @NotNull String user_id, String user_name) {
        this.id = id;
        this.user_id = user_id;
        this.user_name = user_name;
    }
    @Generated(hash = 1279772520)
    public UserInfo() {
    }
}
