package com.accenture.cn.interview.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.accenture.cn.interview.R;
import com.accenture.cn.interview.base.MyApplication;
import com.accenture.cn.interview.model.UpdateInterviewInfo;
import com.accenture.cn.interview.model.User;
import com.accenture.cn.interview.utils.Constant;
import com.accenture.cn.interview.utils.StringUtils;
import com.accenture.cn.interview.widget.SupportMultipleScreensUtil;
import com.hdl.myhttputils.MyHttpUtils;
import com.hdl.myhttputils.bean.CommCallback;
import com.socks.library.KLog;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static String TAG = RegisterActivity.class.getSimpleName();
    private Context mContext;
    private boolean isRegister;
    private TextView tv_title_search, tv_search_back;
    private Button but_register_or_update_pw;
    private Button but_get_auth_code;
    private EditText et_rgs_account, et_auth_code, et_first_pw, et_confirm_pw;
    Handler mHandler = new Handler();
    private int countTime = 11;
    private int recLen = countTime;
    private Map<String, Object> mParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        View rootview = findViewById(android.R.id.content);
        SupportMultipleScreensUtil.scale(rootview);
        setContentView(R.layout.activity_register);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        mContext = this;
        isRegister = getIntent().getBooleanExtra(Constant.IS_REGISTER_TRUE, false);
        initView();
    }

    private void initView() {

        tv_title_search = (TextView) findViewById(R.id.tv_title_search);
        tv_search_back = (TextView) findViewById(R.id.tv_search_back);
        tv_search_back.setOnClickListener(this);
        but_get_auth_code = (Button) findViewById(R.id.but_get_auth_code);
        but_get_auth_code.setOnClickListener(this);
        but_register_or_update_pw = (Button) findViewById(R.id.but_register_or_update_pw);
        but_register_or_update_pw.setOnClickListener(this);
        et_rgs_account = (EditText) findViewById(R.id.et_rgs_account);
        et_auth_code = (EditText) findViewById(R.id.et_auth_code);
        et_first_pw = (EditText) findViewById(R.id.et_first_pw);
        et_confirm_pw = (EditText) findViewById(R.id.et_confirm_pw);
        if (isRegister) {
            tv_title_search.setText("注 册");
            but_register_or_update_pw.setText("注 册");
        } else {
            tv_title_search.setText("忘记密码");
            but_register_or_update_pw.setText("提 交");
            et_first_pw.setHint("请设置新的密码");
            et_confirm_pw.setHint("请确认新设密码");

        }
        et_rgs_account.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //获取焦点
                    String eid = et_rgs_account.getText().toString().trim();
                    if (eid.length() > 0) {
                        if (eid.contains("@accenture.com")) {
                            eid = eid.substring(0, eid.indexOf("@accenture.com"));
                            et_rgs_account.setText(eid);
                        }
                    }

                } else {
                    //失去焦点
                    String eid = et_rgs_account.getText().toString().trim();
                    if (!eid.equals("")) {
                        eid = eid + "@accenture.com";
                    }
                    et_rgs_account.setText(eid);
                }
            }
        });
        // TODO: 2017/3/30 email check


    }


    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            recLen--;
            if (recLen < 0) {
                but_get_auth_code.setText("获取验证码");
                but_get_auth_code.setEnabled(true);
                recLen = countTime;
            } else {
                but_get_auth_code.setText(recLen + "秒");
                but_get_auth_code.setEnabled(false);
                mHandler.postDelayed(this, 1000);
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_search_back:
                RegisterActivity.this.finish();
                break;
            case R.id.but_get_auth_code:
                //获取验证码
                getAutCode();
                break;
            case R.id.but_register_or_update_pw:
                String account = et_rgs_account.getText().toString().trim();
                String code = et_auth_code.getText().toString().trim();
                String firstpw = et_first_pw.getText().toString().trim();
                String confirmpw = et_confirm_pw.getText().toString().trim();
                checkCommitInfo(account, code, firstpw, confirmpw);
                if (isRegister) {
                    registerCommit(account, code, firstpw);
                } else {
                    updatePwCommit(account, code, firstpw);
                }

                break;

        }
    }

    private void getAutCode() {

        MainActivity.showLoadingDialog(MyApplication.CONTEXT, "拼命加载中...");
        if (recLen == countTime) {
            if (mParams == null) {
                mParams = new HashMap<>();
            } else {
                mParams.clear();
            }
            String accountCode = et_rgs_account.getText().toString().trim();
            //验证 eid
            if (StringUtils.isEmpty(accountCode) || accountCode.length() < 4 || !accountCode.contains(".") || accountCode.indexOf(".") < 1) {
                Toast.makeText(mContext, "请你填写有效的EID", Toast.LENGTH_SHORT).show();
                return;
            }
            mParams.put("usereid", accountCode);
            if (isRegister) {
                mParams.put("action", 0);
            } else {
                mParams.put("action", 1);
            }
            MyHttpUtils.build()
                    .url(Constant.REQ_HTTP_ROOT + "user/code")
                    .setJavaBean(UpdateInterviewInfo.class)
                    .addParams(mParams)
                    .onExecuteJsonByPost(new CommCallback<UpdateInterviewInfo>() {
                        @Override
                        public void onSucceed(UpdateInterviewInfo result) {

                            MainActivity.dismissLoadingDialog();
                            if ((int) result.getResult() == 0) {
                                //success
                                if (recLen == countTime) {
                                    mHandler.postDelayed(mRunnable, 1000);
                                }
                            } else {
                                //failed
                                Toast.makeText(mContext, "验证码发送失败，请检查填写的EID是否有误？", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailed(Throwable throwable) {
                            KLog.i(TAG, throwable.toString());
                            MainActivity.dismissLoadingDialog();
                        }
                    });
        }
    }

    private void registerCommit(String account, String code, String firstpw) {
        MainActivity.showLoadingDialog(MyApplication.CONTEXT, "拼命加载中...");
        if (mParams == null) {
            mParams = new HashMap<>();
        } else {
            mParams.clear();
        }
        mParams.put("usereid", account);
        try {
            mParams.put("password", StringUtils.getMD5(firstpw));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        mParams.put("type", 1);
        mParams.put("code", code);

        MyHttpUtils.build()
                .url(Constant.REQ_HTTP_ROOT + "user/register")
                .setJavaBean(UpdateInterviewInfo.class)
                .addParams(mParams)
                .onExecuteJsonByPost(new CommCallback<UpdateInterviewInfo>() {
                    @Override
                    public void onSucceed(UpdateInterviewInfo result) {
                        MainActivity.dismissLoadingDialog();
                        if ((Boolean) result.getResult() == true) {
                            //register  success
                            Toast.makeText(mContext, "注册成功", Toast.LENGTH_SHORT).show();
                            RegisterActivity.this.finish();
                        } else {
                            //register  failed
                            Toast.makeText(mContext, "注册失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        KLog.i(TAG, throwable.toString());
                        MainActivity.dismissLoadingDialog();
                    }
                });

    }


    private void updatePwCommit(String account, String code, String firstpw) {

        MainActivity.showLoadingDialog(MyApplication.CONTEXT, "拼命加载中...");
        if (mParams == null) {
            mParams = new HashMap<>();
        } else {
            mParams.clear();
        }
        mParams.put("usereid", account);
        try {
            mParams.put("password", StringUtils.getMD5(firstpw));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        mParams.put("code", code);

        MyHttpUtils.build()
                .url(Constant.REQ_HTTP_ROOT + "user/updatepwd")
                .setJavaBean(User.class)
                .addParams(mParams)
                .onExecuteJsonByPost(new CommCallback<User>() {
                    @Override
                    public void onSucceed(User result) {
                        MainActivity.dismissLoadingDialog();
                        if (result.getCode() == 0 && result.getMessage().equals("成功")) {
                            //register  success
                            Toast.makeText(mContext, "新密码设置成功", Toast.LENGTH_SHORT).show();
                            RegisterActivity.this.finish();
                        } else {
                            //register  failed
                            Toast.makeText(mContext, "密码重置失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        KLog.i(TAG, throwable.toString());
                        MainActivity.dismissLoadingDialog();
                    }
                });

    }

    private void checkCommitInfo(String account, String code, String firstpw, String confirmpw) {

        //验证 eid
        if (StringUtils.isEmpty(account) || account.length() < 4 || !account.contains(".") || account.indexOf(".") < 1) {
            Toast.makeText(mContext, "请你填写有效的EID", Toast.LENGTH_SHORT).show();
            return;
        }
//        else {
//            account = account + "@accenture.com";
//        }
        //验证 验证码
        if (StringUtils.isEmpty(code) || code.length() < 4) {
            Toast.makeText(mContext, "请填写有效的验证码", Toast.LENGTH_SHORT).show();
            return;
        }
        //验证密码
        if (StringUtils.isEmpty(firstpw) || firstpw.length() < 6) {
            Toast.makeText(mContext, "密码不能小于6位数", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!StringUtils.equals(firstpw, confirmpw)) {
            Toast.makeText(mContext, "两次密码填写不一致", Toast.LENGTH_SHORT).show();
            return;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mHandler != null) {
            KLog.i(TAG, "---removeCallbacksAndMessages");
            mHandler.removeCallbacksAndMessages(null);
        }
        MainActivity.dismissLoadingDialog();
    }


}
