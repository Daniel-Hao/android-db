package com.daniel.www;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daniel.www.greendao.model.GDUserInfo;
import com.daniel.www.greendao.service.GDUserInfoMoreSvr;
import com.daniel.www.greendao.service.GDUserInfoSvr;

import java.util.List;

/**
 * creat by Daniel
 * 思考：多个线程同时操作同一张表，如何处理
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private EditText mEdt1;
    private EditText mEdt2;
    private EditText mEdt3;
    private Button mBtn1;
    private Button mBtn2;
    private TextView mTv;

    //不要这样直接初始化，会报this的引用空指针
    //GDUserInfoSvr userInfoSvr = GDUserInfoSvr.getInstance(this);
    GDUserInfoSvr userInfoSvr;
    GDUserInfoMoreSvr userInfoMoreSvr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userInfoSvr = GDUserInfoSvr.getInstance(this);
        userInfoMoreSvr = GDUserInfoMoreSvr.getInstance(this);

        mEdt1 = (EditText) findViewById(R.id.edt_1);
        mEdt2 = (EditText) findViewById(R.id.edt_2);
        mEdt3 = (EditText) findViewById(R.id.edt_3);

        mBtn1 = (Button) findViewById(R.id.btn_1);
        mBtn2 = (Button) findViewById(R.id.btn_2);
        mTv = (TextView) findViewById(R.id.tv);

        Log.e("","");

        mBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //子线程建议 try catch捕获异常
                        try {
                            savaUserInfo();
                        } catch (Exception e) {
                        }
                    }
                }).start();
            }
        });
        mBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUserInfo();
            }
        });

        mTv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("title删除所有数据")
                        .setMessage("message删除所有数据")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteUserInfo();
                                dialog.dismiss();
                            }
                        }).create().show();
                return false;
            }
        });


    }


    //数据库操作，建议放入子线程操作
    private void savaUserInfo() {

        GDUserInfo userInfo = userInfoSvr.loadUserInfo(mEdt1.getText().toString().trim());
        if (userInfo == null) {
            userInfo = new GDUserInfo();
        }
        userInfo.setUser_id(mEdt1.getText().toString().trim());
        userInfo.setUser_name(mEdt2.getText().toString().trim());
        if (TextUtils.isEmpty(mEdt3.getText().toString().trim())) {
            Log.i(TAG,"mEdt3getText() == null");
            //ps:子线程Toast不显示
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "字段3为必填项目", Toast.LENGTH_SHORT).show();
                }
            });
            return;
        }
        //表中标明该字段为 notnull,测试 == null 会发生什么
        userInfo.setLogin_user_id(mEdt3.getText().toString().trim());
        userInfoSvr.saveUserInfo(userInfo);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showUserInfo() {
        List<GDUserInfo> userInfos = userInfoSvr.loadAllUserInfo();
        StringBuffer str = new StringBuffer();
        if (userInfos != null && userInfos.size() > 0) {
            for (GDUserInfo userInfo : userInfos) {
                str.append("id = " + userInfo.getId());
                str.append("\n");
                str.append("userid = " + userInfo.getUser_id());
                str.append("\n");
                str.append("username = " + userInfo.getUser_name());
                str.append("\n");
                str.append("loginid = " + userInfo.getLogin_user_id());
                str.append("\n");
                str.append("\n");
            }
        }
        mTv.setText(str);
    }

    private void deleteUserInfo() {
        userInfoSvr.deleteAllUserInfo();
        Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
        mTv.setText("test");
    }
}
