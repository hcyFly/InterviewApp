package com.accenture.cn.interview.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.accenture.cn.interview.R;
import com.accenture.cn.interview.adapter.WaitInterviewAdapter;
import com.accenture.cn.interview.model.InterviewFindList;
import com.accenture.cn.interview.model.InterviewInfo;
import com.accenture.cn.interview.utils.Constant;
import com.accenture.cn.interview.utils.ImeUtil;
import com.accenture.cn.interview.utils.SPSecurity;
import com.accenture.cn.interview.widget.SExpandableListView;
import com.accenture.cn.interview.widget.SupportMultipleScreensUtil;
import com.bigkoo.pickerview.TimePickerView;
import com.hdl.myhttputils.MyHttpUtils;
import com.hdl.myhttputils.bean.CommCallback;
import com.socks.library.KLog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InterviewerSearchActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "InterviewerDetailsActiv";
    private Context mContext;
    private TextView topTitle, topRightText, tv_search_back;
    //搜索相关
    private RelativeLayout rlSearch;
    private TextView tvStartTime;
    private TextView tvEndTime;
    private EditText etSearchText;
    private Button butCancel, butDetermine;
    //日期选择
    TimePickerView pvTime;
    boolean isStartAndEnd = true;
    private Date startDate;
    private Date endDate;
    private com.accenture.cn.interview.widget.SExpandableListView explv;
    // 一级菜单数据源
    public List<String> groupTitle = new ArrayList<String>() {
    };
    // 二级菜单数据源
    public HashMap<String, List<InterviewInfo>> childMap = new HashMap<String, List<InterviewInfo>>();
    // 适配器
    private WaitInterviewAdapter mAdapter = null;

    public Set<String> setDate = new HashSet<String>();
    public List<InterviewInfo> tempList = new ArrayList<InterviewInfo>();
    private boolean isNowLoadData = false;
    private boolean isGetLoad = false;
    private boolean mIsScrollIng = false;
    //起始数
    private int offsetInt = 0;
    private Map<String, Object> params;
    //每页记录数
    private int sizeShowCount = 50;
    private int mStatus;
    //记录 提交的搜索的参数int status, String name, String startTime, String endTime, int offset, boolean isRefresh
    private String tempName, tempStartTime, tempEndTime;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 111:
                    explv.refreshComplete();
                    if (groupTitle.size() >= 10) {
                        explv.setNoMore(true);//加载没有更多数据设置
                        return;
                    }
                    mAdapter.update(groupTitle, childMap);
                    break;
                case 112:
                    //刷新
                    explv.refreshComplete();
                    explv.setNoMore(false);
                    mAdapter.update(groupTitle, childMap);
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_interviewer_search);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        View rootview = findViewById(android.R.id.content);
        SupportMultipleScreensUtil.init(InterviewerSearchActivity.this);
        SupportMultipleScreensUtil.scale(rootview);
        mContext = this;
        mStatus = getIntent().getIntExtra(Constant.INTERVIEWER_WAIT_OR_ALREADY, 0);
        initView();
    }

    private void initView() {

        topTitle = (TextView) findViewById(R.id.tv_title_search);
        topRightText = (TextView) findViewById(R.id.tv_search_show_text);
        topRightText.setOnClickListener(this);
        tv_search_back = (TextView) findViewById(R.id.tv_search_back);
        tv_search_back.setOnClickListener(this);
        rlSearch = (RelativeLayout) findViewById(R.id.ll_search_conditions);
        tvStartTime = (TextView) findViewById(R.id.tv_start_time);
        tvEndTime = (TextView) findViewById(R.id.tv_end_time);
        tvStartTime.setOnClickListener(this);
        tvEndTime.setOnClickListener(this);
        etSearchText = (EditText) findViewById(R.id.et_search_text);
        butDetermine = (Button) findViewById(R.id.but_determine);
        butDetermine.setOnClickListener(this);
        butCancel = (Button) findViewById(R.id.but_cancel);
        butCancel.setOnClickListener(this);
        pvTime = new TimePickerView(mContext, TimePickerView.Type.YEAR_MONTH_DAY);
        Calendar calendar = Calendar.getInstance();
        pvTime.setRange(calendar.get(Calendar.YEAR) - 10, calendar.get(Calendar.YEAR));
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                if (isStartAndEnd) {
                    startDate = date;
                    tvStartTime.setText(getTime(date));
                } else {
                    endDate = date;
                    tvEndTime.setText(getTime(date));
                }
            }
        });

        mAdapter = new WaitInterviewAdapter(mContext, groupTitle, childMap);
        explv = (SExpandableListView) findViewById(R.id.sel_lsit);
        explv.setLoadingMoreEnabled(true);
        explv.setPullRefreshEnabled(true);
        explv.setAdapter(mAdapter);
        for (int i = 0; i < mAdapter.getGroupCount(); i++) {
            explv.expandGroup(i);
        }
        explv.setVisibility(View.GONE);


        explv.setScrollListener(new SExpandableListView.ScrollListener() {
            @Override
            public void onScrollingStart() {
                KLog.i(TAG, "explv  scroll ing ---start ");
                if (!mIsScrollIng) {
                    mIsScrollIng = true;
                }
            }

            @Override
            public void onScrollingEnd() {
                KLog.i(TAG, "explv  scroll ing ---end");
            }
        });
        explv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                if (!mIsScrollIng) {
                    if (!isNowLoadData) {
                        if (groupTitle.size() > 0 && childMap.size() > 0) {
                            try {
                                InterviewInfo interviewInfo = childMap.get(groupTitle.get(groupPosition)).get(childPosition);
                                Intent intent = new Intent(mContext, InterviewerDetailsActivity.class);
                                intent.putExtra(Constant.INTERVIEWER_WAIT_OR_ALREADY, "" + mStatus);
                                intent.putExtra(Constant.INTERVIEWINFO, interviewInfo);
                                v.setBackgroundColor(getResources().getColor(R.color.color_item_backgroup));
                                startActivity(intent);
                            } catch (Exception e) {
                                KLog.i(TAG, " --onChildClick----Exception");
                                e.printStackTrace();
                            }
                        }
                    }
                }
                mIsScrollIng = false;
                return false;
            }
        });

        explv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
        explv.setmLoadingListener(new SExpandableListView.LoadingListener() {
            @Override
            public void onLoadMore() {

                if (!(rlSearch.getVisibility() == View.VISIBLE)) {
                    isNowLoadData = true;
                    getLoadDate(mStatus, tempName, tempStartTime, tempEndTime, offsetInt, false);
                    explv.refreshComplete();
                    if (isGetLoad) {
                        explv.setNoMore(true);
                    } else {
                        explv.setNoMore(false);
                    }
                } else {
                    explv.refreshComplete();
                    explv.setNoMore(true);
                }

            }

            @Override
            public void onRefresh() {
                if (!(rlSearch.getVisibility() == View.VISIBLE)) {
                    try {
                        isNowLoadData = true;
                        offsetInt = 0;
                        getLoadDate(mStatus, tempName, tempStartTime, tempEndTime, offsetInt, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                        MainActivity.dismissLoadingDialog();
                        KLog.i(TAG, " --onRefresh----exception");
                    }
                }
                explv.refreshComplete();
                explv.setNoMore(false);

            }
        });


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_search_show_text:
                rlSearch.setVisibility(View.VISIBLE);
                explv.setVisibility(View.GONE);
                topRightText.setVisibility(View.GONE);
                topTitle.setText("搜索");
                break;
            case R.id.tv_search_back:
                InterviewerSearchActivity.this.finish();
                break;
            case R.id.tv_start_time:
                ImeUtil.hideSoftKeyboard(etSearchText);
                isStartAndEnd = true;
                pvTime.setTime(startDate);
                pvTime.show();
                break;
            case R.id.tv_end_time:
                ImeUtil.hideSoftKeyboard(etSearchText);
                isStartAndEnd = false;
                pvTime.setTime(endDate);
                pvTime.show();
                break;
            case R.id.but_cancel:
                tvStartTime.setText("");
                tvEndTime.setText("");
                etSearchText.setText("");
                break;

            case R.id.but_determine:
                String startTime = tvStartTime.getText().toString().trim();
                String endTime = tvEndTime.getText().toString().trim();
                String etSearchStr = etSearchText.getText().toString().trim();
                if (etSearchStr.equals("") || etSearchStr == null) {
                    etSearchStr = "";
                }
                if (TextUtils.isEmpty(startTime) && TextUtils.isEmpty(endTime) && TextUtils.isEmpty(etSearchStr)) {
                    Toast.makeText(mContext, "请填写搜索条件", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(startTime) && !TextUtils.isEmpty(endTime)) {
                    Toast.makeText(mContext, "请选择开始时间", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!TextUtils.isEmpty(startTime) && TextUtils.isEmpty(endTime)) {
                    Toast.makeText(mContext, "请选择结束时间", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!compareTwoTime(startTime, endTime)) {
                    Toast.makeText(mContext, "开始时间不能大于结束时间", Toast.LENGTH_SHORT).show();
                    return;
                }
                getLoadDate(mStatus, etSearchStr, startTime, endTime, offsetInt, true);
                break;
            default:
                break;
        }
    }

    /**
     * 获取数据  面试状态：0未面试1已面试
     *
     * @param status
     */
    private void getLoadDate(int status, String name, String startTime, String endTime, int offset, boolean isRefresh) {
        KLog.i(TAG, "getLoadDate---start");
        MainActivity.showLoadingDialog(mContext, "拼命加载中...");
        tempName = name;
        tempStartTime = startTime;
        tempEndTime = endTime;
        if (isRefresh) {
            isGetLoad = false;
            isNowLoadData = false;
            groupTitle.clear();
            childMap.clear();
            setDate.clear();
            tempList.clear();
        }
        if (params == null) {
            params = new HashMap<String, Object>();
        }
        params.put("interviewStatus", status);
        params.put("size", sizeShowCount);
        if (!name.equals("")) {
            params.put("chineseName", name);
        } else {
            params.remove("chineseName");
        }
        if (!startTime.equals("")) {
            params.put("startTime", getStringToDateLong(startTime + " 00:00:00"));
        } else {
            params.remove("startTime");
        }
        if (!endTime.equals("")) {
            params.put("endTime", getStringToDateLong(endTime + " 23:59:59"));
        } else {
            params.remove("endTime");
        }
        if (offset >= 0) {
            params.put("offset", offset);
        }
        MyHttpUtils.build()
                .url(Constant.REQ_HTTP_ROOT + "employee/queryAll")
                .addParams(params)
                .setToken(SPSecurity.getInstance().getString("auz", ""))
                .setJavaBean(InterviewFindList.class)
                .onExecute(new CommCallback<InterviewFindList>() {

                    @Override
                    public void onSucceed(InterviewFindList list) {
                        int size = list.getResult().size();
                        //解析获取的数据
                        if (size > 0) {
                            isGetLoad = false;
                            if (size == sizeShowCount) {
                                offsetInt = size + offsetInt;
                            }
                            tempList.addAll(list.getResult());
                            resolverReturnData(tempList);
                            mAdapter.update(groupTitle, childMap);
                            for (int i = 0; i < mAdapter.getGroupCount(); i++) {
                                explv.expandGroup(i);
                            }
                            if (size < sizeShowCount) {
                                explv.refreshComplete();
                                explv.setNoMore(true);
                            }
                            rlSearch.setVisibility(View.GONE);
                            explv.setVisibility(View.VISIBLE);
                            topRightText.setVisibility(View.VISIBLE);
                            topTitle.setText("搜索结果");

                        } else {
                            isGetLoad = true;
//                            if (offsetInt > 0) {
//                                Toast.makeText(mContext, "没有更多数据！", Toast.LENGTH_SHORT).show();
//                            } else {
                            Toast.makeText(mContext, "没有符合条件的数据！", Toast.LENGTH_SHORT).show();
//                            }
                            rlSearch.setVisibility(View.VISIBLE);
                            explv.setVisibility(View.GONE);
                            explv.refreshComplete();
                            explv.setNoMore(true);
                        }
                        isNowLoadData = false;
                        MainActivity.dismissLoadingDialog();
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        MainActivity.dismissLoadingDialog();
                        KLog.i(TAG, throwable.toString());
                        Toast.makeText(mContext, "请求数据异常，请稍后再试！", Toast.LENGTH_SHORT).show();
                        isNowLoadData = false;
                    }
                });
        KLog.i(TAG, "getLoadDate---end");
    }

    /**
     * 解析数据
     *
     * @param list
     */
    public void resolverReturnData(List<InterviewInfo> list) {
        int size = list.size();
        if (list == null || size < 1) {
            return;
        }
        groupTitle.clear();
        childMap.clear();
        for (int i = 0; i < size; i++) {
            long date = list.get(i).getFirstStartTime();
            setDate.add(getDateToString(date));
        }
        KLog.i(TAG, "setDate.size---" + setDate.size());
        if (groupTitle.size() <= setDate.size()) {
            groupTitle.clear();
            groupTitle.addAll(setDate);
        }
        Collections.sort(groupTitle, Collections.<String>reverseOrder());
        int group = groupTitle.size();
        for (int i = 0; i < group; i++) {
            String groupStrTime = groupTitle.get(i);
            KLog.i(TAG, "-----" + groupStrTime);
            List<InterviewInfo> interviewList = new ArrayList<InterviewInfo>();
            for (int i1 = 0; i1 < size; i1++) {
                String timeStr = getDateToString(list.get(i1).getFirstStartTime());
                String timeStrName = list.get(i1).getChineseName();
                if (groupStrTime.equals(timeStr)) {
                    interviewList.add(list.get(i1));
                    KLog.i(TAG, "----------" + groupStrTime + "--" + timeStrName);
                }
            }
            childMap.put(groupStrTime, interviewList);
        }
    }

    public String getDateToString(long time) {
        try {
            Date d = new Date(time);
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            return sf.format(d);
        } catch (Exception e) {
            return "0";
        }
    }

    public long getStringToDateLong(String time) {
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time));
            // c.getTimeInMillis());
            return c.getTimeInMillis();
        } catch (Exception e) {
            return System.currentTimeMillis();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (groupTitle != null || childMap != null) {
            groupTitle.clear();
            childMap.clear();
            setDate.clear();
            tempList.clear();
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        MainActivity.dismissLoadingDialog();
    }

    /**
     * 比较两个时间
     *
     * @param starTime  开始时间
     * @param endString 结束时间
     * @return 结束时间大于开始时间返回true，否则反之֮
     */
    public boolean compareTwoTime(String starTime, String endString) {
        boolean isDayu = false;
        if (starTime.equals(endString)) {
            isDayu = true;
            return isDayu;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date parse = dateFormat.parse(starTime);
            Date parse1 = dateFormat.parse(endString);

            long diff = parse1.getTime() - parse.getTime();
            if (diff >= 0) {
                isDayu = true;
            } else {
                isDayu = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isDayu;
    }

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }
}
