package com.accenture.cn.interview.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.accenture.cn.interview.R;
import com.accenture.cn.interview.base.MyApplication;
import com.accenture.cn.interview.model.InterviewDteailsResult;
import com.accenture.cn.interview.model.InterviewInfo;
import com.accenture.cn.interview.model.UpdateInterviewInfo;
import com.accenture.cn.interview.utils.Constant;
import com.accenture.cn.interview.utils.FileOpenUtils;
import com.accenture.cn.interview.utils.FileUtil;
import com.accenture.cn.interview.utils.ImeUtil;
import com.accenture.cn.interview.utils.LoadingDialog;
import com.accenture.cn.interview.utils.SPSecurity;
import com.accenture.cn.interview.widget.SupportMultipleScreensUtil;
import com.bigkoo.pickerview.OptionsPickerView;
import com.hdl.myhttputils.MyHttpUtils;
import com.hdl.myhttputils.bean.CommCallback;
import com.hdl.myhttputils.utils.FailedMsgUtils;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.socks.library.KLog;
import com.wevey.selector.dialog.DialogInterface;
import com.wevey.selector.dialog.NormalAlertDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InterviewerDetailsActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = InterviewerDetailsActivity.class.getSimpleName();
    private String isThisWait;
    private boolean isThisHR;
    private Context mContext;
    //show content
    private ExpandableTextView expTv1;
    private TextView tv_title_interviewer, et_inerviewer_skilled, et_inerviewer_level, tv_et_Eventually_tel, tv_et_first_tel,
            et_inerviewer_job_role, tv_search_back;
    private RelativeLayout rl_call_tel;
    private LinearLayout ll_first_interviewer, ll_feventually_interviewer;
    //function
    private Button but_attachment_interviewer_one, but_attachment_interviewer_two, but_commit_back;
    private CheckBox cb_isFirst_interviewer, cb_isFirst_through_yes, cb_isFirst_through_no, cb_eventually_interviewer,
            cb_isEventually_through_yes, cb_isEventually_through_no;
    private EditText et_first_eid, et_first_tel, et_inerviewer_skills, et_inerviewer_first_details,
            et_Eventually_eid, et_Eventually_tel, et_inerviewer_skills_ysears_eventually,
            et_inerviewer_eventually_details;
    private ImageView img_is_pass_eventually, img_is_pass_first, but_call_tel;
    private List<EditText> ets = new ArrayList<EditText>();

    //选择level
    private OptionsPickerView pvOptions;
    private InterviewInfo mInterviewInfo;

    public LoadingDialog dialogLoadDetails;
    private Map<String, Object> mParams;//提交面试反馈参数
    private Map<String, Object> params;//获取详细信息
    private int isFirstShowPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_interviewer_details);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        View rootview = findViewById(android.R.id.content);
        SupportMultipleScreensUtil.init(InterviewerDetailsActivity.this);
        SupportMultipleScreensUtil.scale(rootview);
        mContext = this;

        isThisHR = SPSecurity.getInstance().getString(Constant.ROLE_TYPE, "").equals("0") ? true : false;
        isThisWait = getIntent().getStringExtra(Constant.INTERVIEWER_WAIT_OR_ALREADY);
        mInterviewInfo = getIntent().getParcelableExtra(Constant.INTERVIEWINFO);

//        if (sDemo == null) {
//            sDemo = new Demo();
//        }
//        //获取单例对象，退出Activity即可模拟出内存泄露
//        TestManager testManager = TestManager.getInstance(this);

        initView();
    }

    private void initView() {
        tv_title_interviewer = (TextView) findViewById(R.id.tv_title_interviewer);
        expTv1 = (ExpandableTextView) findViewById(R.id.expand_text_view);
        but_attachment_interviewer_one = (Button) findViewById(R.id.but_attachment_interviewer_one);
        but_attachment_interviewer_two = (Button) findViewById(R.id.but_attachment_interviewer_two);
        but_call_tel = (ImageView) findViewById(R.id.but_call_tel);
        but_commit_back = (Button) findViewById(R.id.but_commit_back);
        but_attachment_interviewer_one.setOnClickListener(this);
        but_attachment_interviewer_two.setOnClickListener(this);
        but_call_tel.setOnClickListener(this);
        but_commit_back.setOnClickListener(this);
        tv_et_first_tel = (TextView) findViewById(R.id.tv_et_first_tel);
        tv_et_Eventually_tel = (TextView) findViewById(R.id.tv_et_Eventually_tel);
        tv_et_first_tel.setOnClickListener(this);
        tv_et_Eventually_tel.setOnClickListener(this);
        //块
        rl_call_tel = (RelativeLayout) findViewById(R.id.rl_call_tel);
        ll_first_interviewer = (LinearLayout) findViewById(R.id.ll_first_interviewer);
        ll_feventually_interviewer = (LinearLayout) findViewById(R.id.ll_feventually_interviewer);
        cb_isFirst_interviewer = (CheckBox) findViewById(R.id.cb_isFirst_interviewer);
        cb_eventually_interviewer = (CheckBox) findViewById(R.id.cb_eventually_interviewer);
        cb_isFirst_through_yes = (CheckBox) findViewById(R.id.cb_isFirst_through_yes);
        cb_isFirst_through_no = (CheckBox) findViewById(R.id.cb_isFirst_through_no);
        cb_isFirst_through_yes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cb_isFirst_through_no.setChecked(false);
                }
            }
        });
        cb_isFirst_through_no.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cb_isFirst_through_yes.setChecked(false);
                }
            }
        });
        cb_isEventually_through_yes = (CheckBox) findViewById(R.id.cb_isEventually_through_yes);
        cb_isEventually_through_no = (CheckBox) findViewById(R.id.cb_isEventually_through_no);
        cb_isEventually_through_yes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cb_isEventually_through_no.setChecked(false);
                }
            }
        });
        cb_isEventually_through_no.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cb_isEventually_through_yes.setChecked(false);
                }
            }
        });
        img_is_pass_eventually = (ImageView) findViewById(R.id.img_is_pass_eventually);
        img_is_pass_first = (ImageView) findViewById(R.id.img_is_pass_first);
        et_first_eid = (EditText) findViewById(R.id.et_first_eid);
        ets.add(et_first_eid);
        et_first_tel = (EditText) findViewById(R.id.et_first_tel);
        ets.add(et_first_tel);
        et_inerviewer_skills = (EditText) findViewById(R.id.et_inerviewer_skills);
        ets.add(et_inerviewer_skills);
        et_inerviewer_first_details = (EditText) findViewById(R.id.et_inerviewer_first_details);
        ets.add(et_inerviewer_first_details);
        et_inerviewer_skilled = (TextView) findViewById(R.id.et_inerviewer_skilled);
        et_inerviewer_skilled.setOnClickListener(this);
        et_Eventually_eid = (EditText) findViewById(R.id.et_Eventually_eid);
        ets.add(et_Eventually_eid);
        et_Eventually_tel = (EditText) findViewById(R.id.et_Eventually_tel);
        ets.add(et_Eventually_tel);
        et_inerviewer_skills_ysears_eventually = (EditText) findViewById(R.id.et_inerviewer_skills_ysears_eventually);
        ets.add(et_inerviewer_skills_ysears_eventually);

        et_inerviewer_eventually_details = (EditText) findViewById(R.id.et_inerviewer_eventually_details);
        ets.add(et_inerviewer_eventually_details);
        et_inerviewer_level = (TextView) findViewById(R.id.et_inerviewer_level);
        et_inerviewer_level.setOnClickListener(this);
        et_inerviewer_job_role = (TextView) findViewById(R.id.et_inerviewer_job_role);
        et_inerviewer_job_role.setOnClickListener(this);
        tv_search_back = (TextView) findViewById(R.id.tv_search_back);
        tv_search_back.setOnClickListener(this);
        if (isThisHR) {
            rl_call_tel.setVisibility(View.INVISIBLE);
            but_commit_back.setVisibility(View.INVISIBLE);
            if (isThisWait.equals("0")) {
                ll_first_interviewer.setVisibility(View.INVISIBLE);
                ll_feventually_interviewer.setVisibility(View.INVISIBLE);
            } else {
                ll_first_interviewer.setVisibility(View.VISIBLE);
                ll_feventually_interviewer.setVisibility(View.VISIBLE);
                cb_isFirst_interviewer.setEnabled(false);
                cb_eventually_interviewer.setEnabled(false);
                cb_isFirst_through_yes.setEnabled(false);
                cb_isFirst_through_no.setEnabled(false);
                cb_isEventually_through_yes.setEnabled(false);
                cb_isEventually_through_no.setEnabled(false);
            }
            et_inerviewer_skilled.setEnabled(false);
            et_inerviewer_level.setEnabled(false);

        } else {
            rl_call_tel.setVisibility(View.VISIBLE);
            ll_first_interviewer.setVisibility(View.VISIBLE);
            ll_feventually_interviewer.setVisibility(View.VISIBLE);
            but_commit_back.setVisibility(View.VISIBLE);
            for (EditText et : ets) {
                et.setEnabled(false);
            }
        }
        if (mInterviewInfo != null) {
            tv_title_interviewer.setText(mInterviewInfo.getChineseName() + "(" + mInterviewInfo.getEnglishName() + ")");
            expTv1.setText(mInterviewInfo.getInterviewSkill());
            et_first_eid.setText(mInterviewInfo.getFirstInterviewer());
            et_Eventually_eid.setText(mInterviewInfo.getFinalInterviewer());
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (isFirstShowPage == 0) {
            if (!mInterviewInfo.getId().equals("")) {
                showLoadingDialog(mContext, "玩命加载中...");
                if (params == null) {
                    params = new HashMap<String, Object>();
                }
                params.put("id", mInterviewInfo.getId());
                MyHttpUtils.build()
                        .url(Constant.REQ_HTTP_ROOT + "employee/queryById")
                        .addParams(params)
                        .setToken(SPSecurity.getInstance().getString("auz", ""))
                        .setJavaBean(InterviewDteailsResult.class)
                        .onExecute(new CommCallback<InterviewDteailsResult>() {

                            @Override
                            public void onSucceed(InterviewDteailsResult result) {

                                if (result.getCode() == 0) {
                                    if (result.getResult() != null) {
                                        showDetailsInfo(result.getResult());
                                    }
                                } else {
                                    Toast.makeText(mContext, "请求失败，请稍后再试试", Toast.LENGTH_SHORT).show();
                                }

                                dismissLoadingDialog();
                            }

                            @Override
                            public void onFailed(Throwable throwable) {
                                KLog.i(TAG, throwable.toString());
                                dismissLoadingDialog();
                            }

                        });
            }

            if (!isThisHR) {
                if (mInterviewInfo != null) {
                    String nameFirst = mInterviewInfo.getFirstInterviewer();
                    String nameFind = mInterviewInfo.getFinalInterviewer();
                    if (!nameFirst.equals("")) {
                        cb_isFirst_interviewer.setChecked(true);
                        cb_isFirst_interviewer.setClickable(false);
                        et_inerviewer_skilled.setEnabled(true);
                        int size = ets.size();
                        if (size > 0) {
                            for (int i = 0; i < size; i++) {
                                if (i < 6) {
                                    ets.get(i).setEnabled(true);
                                }
                            }
                        }
                    } else {
                        et_inerviewer_skilled.setEnabled(false);
                        cb_isFirst_through_yes.setEnabled(false);
                        cb_isFirst_through_no.setEnabled(false);
                    }
                    if (!nameFind.equals("")) {
                        cb_eventually_interviewer.setChecked(true);
                        cb_eventually_interviewer.setClickable(false);
                        et_inerviewer_level.setClickable(true);
                        int size = ets.size();
                        if (size > 0) {
                            for (int i = 0; i < size; i++) {
                                if (i > 5) {
                                    ets.get(i).setEnabled(true);
                                }
                            }
                        }
                    } else {
                        et_inerviewer_level.setEnabled(false);
                        cb_isEventually_through_no.setEnabled(false);
                        cb_isEventually_through_yes.setEnabled(false);
                    }
                }
            }
        }


    }


    @Override
    protected void onPause() {
        super.onPause();
        isFirstShowPage++;
    }

    /**
     * 展示面试者详情信息
     *
     * @param mEmployeeVO
     */
    private void showDetailsInfo(InterviewDteailsResult.ResultUserHH mEmployeeVO) {

        //初面
        if (mEmployeeVO.getFirstResult().equals("1")) {
            cb_isFirst_through_yes.setChecked(true);
            img_is_pass_first.setVisibility(View.VISIBLE);
        }
        if (mEmployeeVO.getFirstResult().equals("0")) {
            cb_isFirst_through_no.setChecked(true);
            img_is_pass_first.setVisibility(View.VISIBLE);
            img_is_pass_first.setBackgroundResource(R.drawable.interviewer_details_page_no_pass_seal_icon);
        }
        if (cb_isFirst_through_yes.isChecked() || cb_isFirst_through_no.isChecked()) {
            cb_isFirst_interviewer.setChecked(true);
        }
        et_first_eid.setText(mEmployeeVO.getFirstInterviewer().equals("") ? "" : mEmployeeVO.getFirstInterviewer());
        et_first_tel.setText(mEmployeeVO.getFirstInterviewerPhone().equals("") ? "" : mEmployeeVO.getFirstInterviewerPhone());
        et_inerviewer_skills.setText(mEmployeeVO.getInterviewSkill().equals("") ? "" : mEmployeeVO.getInterviewSkill());
        et_inerviewer_skilled.setText(mEmployeeVO.getSkillLevel().equals("") ? "" : mEmployeeVO.getSkillLevel());
        et_inerviewer_first_details.setText(mEmployeeVO.getSkillFeedback().equals("") ? "" : mEmployeeVO.getSkillFeedback());
        if (isThisHR) {
            if (!et_first_tel.getText().toString().equals("")) {
                tv_et_first_tel.setVisibility(View.VISIBLE);
                tv_et_first_tel.setText(et_first_tel.getText().toString());
                et_first_tel.setVisibility(View.GONE);
            }
        }
        //终面
        if (mEmployeeVO.getFinalResult().equals("1")) {
            cb_isEventually_through_yes.setChecked(true);
            img_is_pass_eventually.setVisibility(View.VISIBLE);
        }
        if (mEmployeeVO.getFinalResult().equals("0")) {
            cb_isEventually_through_no.setChecked(true);
            img_is_pass_eventually.setVisibility(View.VISIBLE);
            img_is_pass_eventually.setBackgroundResource(R.drawable.interviewer_details_page_no_pass_seal_icon);
        }
        if (isThisWait.equals("0")) {
            img_is_pass_first.setVisibility(View.GONE);
            img_is_pass_eventually.setVisibility(View.GONE);
        }
        if (cb_isEventually_through_yes.isChecked() || cb_isEventually_through_no.isChecked()) {
            cb_eventually_interviewer.setChecked(true);
        }
        if (mEmployeeVO.getFinalInterviewer().equals("")) {
            cb_eventually_interviewer.setChecked(false);
        } else {
            cb_eventually_interviewer.setChecked(true);
        }
        cb_eventually_interviewer.setClickable(false);
        if (!cb_eventually_interviewer.isChecked()) {
            et_Eventually_eid.setEnabled(false);
            et_Eventually_tel.setEnabled(false);
            et_inerviewer_skills_ysears_eventually.setEnabled(false);
            et_inerviewer_level.setEnabled(false);
            et_inerviewer_job_role.setEnabled(false);
            et_inerviewer_eventually_details.setEnabled(false);
        }
        et_Eventually_eid.setText(mEmployeeVO.getFinalInterviewer().equals("") ? "" : mEmployeeVO.getFinalInterviewer());
        et_Eventually_tel.setText(mEmployeeVO.getFinalInterviewerPhone().equals("") ? "" : mEmployeeVO.getFinalInterviewerPhone());
        et_inerviewer_skills_ysears_eventually.setText(mEmployeeVO.getItYears().equals("") ? "" : mEmployeeVO.getItYears());
        et_inerviewer_level.setText(mEmployeeVO.getMal().equals("") ? "" : mEmployeeVO.getMal());
        et_inerviewer_job_role.setText(mEmployeeVO.getInterviewLevel().equals("") ? "" : mEmployeeVO.getInterviewLevel());
        et_inerviewer_eventually_details.setText(mEmployeeVO.getFinalInterviewFeedback().equals("") ? "" : mEmployeeVO.getFinalInterviewFeedback());
        if (isThisHR) {
            if (!et_Eventually_tel.getText().toString().equals("")) {
                tv_et_Eventually_tel.setVisibility(View.VISIBLE);
                tv_et_Eventually_tel.setText(et_first_tel.getText().toString());
                et_Eventually_tel.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.but_attachment_interviewer_one:
                if (!mInterviewInfo.getVcURL().equals("")) {
                    createFileToCache(mInterviewInfo.getVcURL());
//                    downloadResume(mInterviewInfo.getVcURL());
                }
                break;
            case R.id.but_attachment_interviewer_two:

                break;
            case R.id.but_call_tel:
                //6.0及以上时权限申请
                callPhone("400334242535");
                break;

            case R.id.tv_et_first_tel:

                if (!tv_et_first_tel.getText().toString().equals("")) {
                    callPhone(tv_et_first_tel.getText().toString().trim());
                }

                break;
            case R.id.tv_et_Eventually_tel:

                if (!tv_et_Eventually_tel.getText().toString().equals("")) {
                    callPhone(tv_et_Eventually_tel.getText().toString().trim());
                }


                break;
            case R.id.but_commit_back:
                commitInfo();
                break;

            case R.id.et_inerviewer_skilled:
                if (skillLevelList.size() < 1) {
                    skillLevelList.add("P0");
                    skillLevelList.add("P1");
                    skillLevelList.add("P2");
                    skillLevelList.add("P3");
                    skillLevelList.add("P4");
                }
                initOptionPicker(skillLevelList, "skillLevelList", et_inerviewer_skilled);
                pvOptions.show();
                break;
            case R.id.et_inerviewer_level:
                if (malList.size() < 1) {
                    for (int i = 0; i < 36; i++) {
                        malList.add(i + "");
                    }
                }
                initOptionPicker(malList, "malList", et_inerviewer_level);
                pvOptions.show();
                break;
            case R.id.et_inerviewer_job_role:

                if (LevelRecommendedList.size() < 1) {
                    LevelRecommendedList.add("SE");
                    LevelRecommendedList.add("SSE");
                    LevelRecommendedList.add("TL");
                    LevelRecommendedList.add("AM");
                    LevelRecommendedList.add("M");
                    LevelRecommendedList.add("ASE");
                }
                initOptionPicker(LevelRecommendedList, "LevelRecommendedList", et_inerviewer_job_role);
                pvOptions.show();

                break;

            case R.id.tv_search_back:
                InterviewerDetailsActivity.this.finish();
                break;

            default:
                break;
        }
    }

    private void callPhone(final String tel) {
        Acp.getInstance(this).request(new AcpOptions.Builder()
                        .setPermissions(Manifest.permission.CALL_PHONE)
                        .build(),
                new AcpListener() {
                    @Override
                    public void onGranted() {
                        // TODO: 2017/4/10 diglog show ok?
                        new NormalAlertDialog.Builder(mContext).setTitleVisible(true)
                                .setTitleText("拨号")
                                .setTitleTextColor(R.color.black_light)
                                .setContentText(tel)
                                .setContentTextColor(R.color.black_light)
                                .setLeftButtonText("取消")
                                .setLeftButtonTextColor(R.color.gray)
                                .setRightButtonText("确定")
                                .setRightButtonTextColor(R.color.black_light)
                                .setOnclickListener(new DialogInterface.OnLeftAndRightClickListener<NormalAlertDialog>() {
                                                        @Override
                                                        public void clickLeftButton(NormalAlertDialog dialog, View view) {
                                                            dialog.dismiss();
                                                        }

                                                        @Override
                                                        public void clickRightButton(NormalAlertDialog dialog, View view) {
                                                            Intent intent = new Intent();
                                                            intent.setAction(Intent.ACTION_CALL);
                                                            intent.setData(Uri.parse("tel:" + tel));
                                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(intent);
                                                            dialog.dismiss();
                                                        }
                                                    }
                                )
                                .setCanceledOnTouchOutside(true)
                                .build()
                                .show();


                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        Toast.makeText(mContext, "你拒绝了拨打电话的权限申请，请到‘设置’中开启，才能使用此功能。", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 下载简历
     */
    private void downloadResume(final String urlFileName) {

        // TODO: 2017/3/13  检查是否已经有下载
        if (!FileUtil.isFileExists(MyApplication.saveDriPath + "/" + urlFileName)) {
            showLoadingDialog(mContext, "拼命下载中...");
            MyHttpUtils.build()
                    .url(Constant.REQ_HTTP_ROOT_DOWNLOAD_RESUME + urlFileName)
                    .setConnTimeOut(6000)
                    .setReadTimeOut(5 * 60 * 1000)
                    .setToken(SPSecurity.getInstance().getString("auz", ""))
                    .onExecuteDwonload(new CommCallback() {
                        @Override
                        public void onSucceed(Object o) {
                            dismissLoadingDialog();
                            FileOpenUtils.openFilePath(mContext, MyApplication.saveDriPath + "/" + urlFileName);
                        }

                        @Override
                        public void onFailed(Throwable throwable) {
                            KLog.i(TAG, FailedMsgUtils.getErrMsgStr(throwable));
                            dismissLoadingDialog();
                            Toast.makeText(mContext, "服务异常，下载失败！", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onDownloading(long total, long current) {
                            KLog.i(TAG, "--load jingdu--:" + total + "-------" + current);
                        }
                    });
        } else {
            FileOpenUtils.openFilePath(mContext, MyApplication.saveDriPath + "/" + urlFileName);
        }


    }

    /**
     * 提交结果
     */
    private void commitInfo() {

        if (mParams == null) {
            mParams = new HashMap<>();
        }
        mParams.put("id", mInterviewInfo.getId());
        mParams.put("chineseName", mInterviewInfo.getChineseName());
        mParams.put("englishName", mInterviewInfo.getEnglishName());
        mParams.put("interviewStatus", "1");

        //须弹框提示  对面试提交的内容 进行必要判断
        if (cb_isFirst_interviewer.isChecked() || cb_eventually_interviewer.isChecked()) {
            String commintStr = "";
            if (cb_isFirst_interviewer.isChecked()) {
                commintStr = "‘初面’";
                //是否通过
                if (cb_isFirst_through_yes.isChecked()) {
                    checkCommintInfo("1", "firstResult");
                    if (!checkCommintInfo(et_first_eid.getText().toString().trim(), "firstInterviewer")) {
                        Toast.makeText(mContext, "请填写你的EID信息", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!checkCommintInfo(et_first_tel.getText().toString().trim(), "firstInterviewerPhone")) {
                        Toast.makeText(mContext, "请填写你的手机", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!checkCommintInfo(et_inerviewer_skills.getText().toString().trim(), "interviewSkill")) {
                        Toast.makeText(mContext, "请填写面试者的技能", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!checkCommintInfo(et_inerviewer_skilled.getText().toString().trim(), "skillLevel")) {
                        Toast.makeText(mContext, "请选择技能等级", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!checkCommintInfo(et_inerviewer_first_details.getText().toString().trim(), "skillFeedback")) {
                        Toast.makeText(mContext, "请对面试者进行简要的描述", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (cb_isFirst_through_no.isChecked()) {
                    checkCommintInfo("0", "firstResult");
                    checkCommintInfo(et_first_eid.getText().toString().trim(), "firstInterviewer");
                    checkCommintInfo(et_first_tel.getText().toString().trim(), "firstInterviewerPhone");
                    checkCommintInfo(et_inerviewer_skills.getText().toString().trim(), "interviewSkill");
                    checkCommintInfo(et_inerviewer_skilled.getText().toString().trim(), "skillLevel");
                    checkCommintInfo(et_inerviewer_first_details.getText().toString().trim(), "skillFeedback");
                }
                if (!cb_isFirst_through_no.isChecked() && !cb_isFirst_through_yes.isChecked()) {
                    Toast.makeText(mContext, "请勾选面试者是否通过", Toast.LENGTH_SHORT).show();
                    return;
                }

                //提交时间
                mParams.put("firstEndTime", System.currentTimeMillis());
            }
            if (cb_eventually_interviewer.isChecked()) {
                commintStr = "‘终面’";
                //是否通过
                if (cb_isEventually_through_yes.isChecked()) {
                    checkCommintInfo("1", "finalResult");
                    if (!checkCommintInfo(et_Eventually_eid.getText().toString().trim(), "finalInterviewer")) {
                        Toast.makeText(mContext, "请填写你的EID信息", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!checkCommintInfo(et_Eventually_tel.getText().toString().trim(), "finalInterviewerPhone")) {
                        Toast.makeText(mContext, "请填写你的手机", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!checkCommintInfo(et_inerviewer_skills_ysears_eventually.getText().toString().trim(), "itYears")) {
                        Toast.makeText(mContext, "请填写面试者技能年限", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!checkCommintInfo(et_inerviewer_level.getText().toString().trim(), "mal")) {
                        Toast.makeText(mContext, "请选择面试者技术级别", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!checkCommintInfo(et_inerviewer_job_role.getText().toString().trim(), "InterviewLevel")) {
                        Toast.makeText(mContext, "请填写面试者岗位角色", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!checkCommintInfo(et_inerviewer_eventually_details.getText().toString().trim(), "finalInterviewFeedback")) {
                        Toast.makeText(mContext, "请填写对面试者的一些描述", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (cb_isEventually_through_no.isChecked()) {
                    checkCommintInfo("0", "finalResult");
                    checkCommintInfo(et_Eventually_eid.getText().toString().trim(), "finalInterviewer");
                    checkCommintInfo(et_Eventually_tel.getText().toString().trim(), "finalInterviewerPhone");
                    checkCommintInfo(et_inerviewer_skills_ysears_eventually.getText().toString().trim(), "itYears");
                    checkCommintInfo(et_inerviewer_level.getText().toString().trim(), "mal");
                    checkCommintInfo(et_inerviewer_job_role.getText().toString().trim(), "InterviewLevel");
                    checkCommintInfo(et_inerviewer_eventually_details.getText().toString().trim(), "finalInterviewFeedback");
                }
                if (!cb_isEventually_through_yes.isChecked() & !cb_isEventually_through_no.isChecked()) {
                    Toast.makeText(mContext, "请勾选面试者是否通过", Toast.LENGTH_SHORT).show();
                    return;
                }
                //提交时间
                mParams.put("finalEndTime", System.currentTimeMillis());
            }
            if (cb_isFirst_interviewer.isChecked() & cb_eventually_interviewer.isChecked()) {
                commintStr = "‘初面和终面’";
            }
            new NormalAlertDialog.Builder(mContext).setTitleVisible(true)
                    .setTitleText("提示")
                    .setTitleTextColor(R.color.black_light)
                    .setContentText("选择的是：" + commintStr + " 提交")
                    .setContentTextColor(R.color.black_light)
                    .setLeftButtonText("取消")
                    .setLeftButtonTextColor(R.color.gray)
                    .setRightButtonText("提交")
                    .setRightButtonTextColor(R.color.black_light)
                    .setOnclickListener(new DialogInterface.OnLeftAndRightClickListener<NormalAlertDialog>() {
                        @Override
                        public void clickLeftButton(NormalAlertDialog dialog, View view) {
                            dialog.dismiss();
                        }

                        @Override
                        public void clickRightButton(NormalAlertDialog dialog, View view) {
                            dialog.dismiss();
                            showLoadingDialog(mContext, "正在提交中...");
                            MyHttpUtils.build()
                                    .url(Constant.REQ_HTTP_ROOT + "employee/update")
                                    .setToken(SPSecurity.getInstance().getString("auz", ""))
                                    .setJavaBean(UpdateInterviewInfo.class)
                                    .addParams(mParams)
                                    .onExecuteJsonByPost(new CommCallback<UpdateInterviewInfo>() {

                                                             @Override
                                                             public void onSucceed(UpdateInterviewInfo updaate) {
                                                                 if (updaate.getCode() == 0) {
                                                                     //更新成功
                                                                     SPSecurity.getInstance().mSecurityEditor.putBoolean(Constant.IS_COMMIT_UPDATE_SUCCEED, true);
                                                                     Toast.makeText(mContext, "面试反馈提交成功", Toast.LENGTH_SHORT).show();
                                                                 } else {
                                                                     Toast.makeText(mContext, "" + updaate.getMessage(), Toast.LENGTH_SHORT).show();
                                                                 }
                                                                 dismissLoadingDialog();

                                                             }

                                                             @Override
                                                             public void onFailed(Throwable throwable) {
                                                                 dismissLoadingDialog();
                                                                 Toast.makeText(mContext, "提交失败，请稍后再试!" + throwable.toString(), Toast.LENGTH_SHORT).show();
                                                             }
                                                         }
                                    );

                        }
                    })
                    .setCanceledOnTouchOutside(false)
                    .build()
                    .show();
        } else {
            Toast.makeText(mContext, "数据有误，请联系管理员或相关HR", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkCommintInfo(String commintStr, String keyStr) {
        if (commintStr.equals("") || commintStr == null || commintStr.length() < 1) {
            return false;
        } else {
            mParams.put(keyStr, commintStr);
            return true;
        }
    }

    // levle 选择
    private ArrayList<String> skillLevelList = new ArrayList<>();//初面级别
    private ArrayList<String> malList = new ArrayList<>();//终面评级
    private ArrayList<String> LevelRecommendedList = new ArrayList<>();//终面 岗位角色水平

    private void initOptionPicker(ArrayList<String> listA, String tepyStr, View view) {//条件选择器初始化

        if (ImeUtil.isKeyboardShown(view.getRootView())) {
            ImeUtil.hideSoftKeyboard(view);
        }

        if (pvOptions == null) {
            pvOptions = new OptionsPickerView(this);
        }
        pvOptions.setPicker(listA);
        pvOptions.setCyclic(false);
        pvOptions.setCancelable(true);
        if ("skillLevelList".equals(tepyStr)) {
            pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int option2, int options3) {
                    String ttt = skillLevelList.get(options1);
                    et_inerviewer_skilled.setText(ttt);
                }
            });
        } else if ("malList".equals(tepyStr)) {
            pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int option2, int options3) {
                    String ttt = malList.get(options1);
                    et_inerviewer_level.setText(ttt);
                }
            });
        } else if ("LevelRecommendedList".equals(tepyStr)) {
            pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int option2, int options3) {
                    String ttt = LevelRecommendedList.get(options1);
                    et_inerviewer_job_role.setText(ttt);
                }
            });
        }
    }

    /**
     * 申请权限（下载文件）
     *
     * @param pathFileName
     */
    private void createFileToCache(final String pathFileName) {
        Acp.getInstance(mContext).request(new AcpOptions.Builder()
                        .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .build(),
                new AcpListener() {
                    @Override
                    public void onGranted() {
                        downloadResume(pathFileName);
                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        Toast.makeText(mContext, "你拒绝了权限申请，请到‘设置’中开启，才能使用此功能。", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void showLoadingDialog(Context context, String msg) {
        if (dialogLoadDetails == null) {
            dialogLoadDetails = new LoadingDialog(context, msg);
        }
        dialogLoadDetails.show();
    }

    public void dismissLoadingDialog() {
        if (dialogLoadDetails != null) {
            dialogLoadDetails.close();
            dialogLoadDetails = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissLoadingDialog();
        if (params != null) {
            params.clear();
            params = null;
        }
        if (mParams != null) {
            mParams.clear();
            mParams = null;
        }
        if (ets != null) {
            ets.clear();
            ets = null;
        }
        if (skillLevelList != null) {
            skillLevelList.clear();
            skillLevelList = null;
        }
        if (malList != null) {
            malList.clear();
            malList = null;
        }
        if (LevelRecommendedList != null) {
            LevelRecommendedList.clear();
            LevelRecommendedList = null;
        }

    }
}
