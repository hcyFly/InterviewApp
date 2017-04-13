package com.accenture.cn.interview.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.accenture.cn.interview.R;
import com.accenture.cn.interview.activity.InterviewerDetailsActivity;
import com.accenture.cn.interview.activity.InterviewerSearchActivity;
import com.accenture.cn.interview.activity.LoginActivity;
import com.accenture.cn.interview.activity.MainActivity;
import com.accenture.cn.interview.adapter.WaitInterviewAdapter;
import com.accenture.cn.interview.model.InterviewFindList;
import com.accenture.cn.interview.model.InterviewInfo;
import com.accenture.cn.interview.utils.Constant;
import com.accenture.cn.interview.utils.SPSecurity;
import com.accenture.cn.interview.widget.SExpandableListView;
import com.accenture.cn.interview.widget.SupportMultipleScreensUtil;
import com.hdl.myhttputils.MyHttpUtils;
import com.hdl.myhttputils.bean.CommCallback;
import com.socks.library.KLog;
import com.wevey.selector.dialog.DialogInterface;
import com.wevey.selector.dialog.NormalAlertDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 等待面试者
 * Created by ytx on 2016/6/22.
 */
public class WaitInterviewFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "WaitInterviewFragment";
    private View mView;
    private SExpandableListView explv;
    private TextView tvSearchWait, tv_log_out;
    // 一级菜单数据源
    public List<String> groupTitle = new ArrayList<String>() {
    };
    // 二级菜单数据源
    public HashMap<String, List<InterviewInfo>> childMap = new HashMap<String, List<InterviewInfo>>();
    public Set<String> setDate = new HashSet<String>();
    public List<InterviewInfo> tempList = new ArrayList<InterviewInfo>();
    // 适配器
    private WaitInterviewAdapter mAdapter = null;
    private boolean isNowLoadData = false;
    private boolean mIsScrollIng = false;
    //起始数
    private int offsetInt = 0;
    private Map<String, Object> params;
    //每页记录数
    private int sizeShowCount = 20;
    private int isShowDetails = 0;
    private String tokenStr;

    public Handler mHandlerWait = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case 1000:
                    getLoadDate(0, 0, true);
                    break;
                case 2000:
                    getLoadDate(0, offsetInt, false);
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView != null) {
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null) {
                parent.removeView(mView);
            }
            return mView;
        }
        mView = inflater.inflate(R.layout.fragment_wait, container, false);
        SupportMultipleScreensUtil.scale(mView);
        tokenStr = SPSecurity.getInstance().getString("auz", "");
        return mView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            KLog.i(TAG, "setUserVisibleHint-->相当于Fragment的onResume");
        } else {
            KLog.i(TAG, "setUserVisibleHint-->相当于Fragment的onPause");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mView != null) {
            if (explv == null) {
                initView();
                explv.setPullRefreshEnabled(true);
                explv.setLoadingMoreEnabled(true);
                mAdapter = new WaitInterviewAdapter(getActivity(), groupTitle, childMap);
                explv.setAdapter(mAdapter);

                explv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                    @Override
                    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                        return true;
                    }
                });
                explv.setmLoadingListener(new SExpandableListView.LoadingListener()

                {
                    @Override
                    public void onLoadMore() {
                        isNowLoadData = true;
                        mHandlerWait.sendEmptyMessage(2000);

                    }

                    @Override
                    public void onRefresh() {
                        isNowLoadData = true;
                        mHandlerWait.sendEmptyMessage(1000);
                    }
                });
            }
        }
        //test hcy
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
                KLog.i(TAG, "explv  scroll ing ---child click ing");
                if (!mIsScrollIng) {

                    if (!isNowLoadData) {
                        if (groupTitle.size() > 0 && childMap.size() > 0) {
                            try {
                                InterviewInfo interviewInfo = childMap.get(groupTitle.get(groupPosition)).get(childPosition);
                                Intent intent = new Intent(getActivity(), InterviewerDetailsActivity.class);
                                intent.putExtra(Constant.INTERVIEWER_WAIT_OR_ALREADY, "0");
                                intent.putExtra(Constant.INTERVIEWINFO, interviewInfo);
                                isShowDetails++;
                                v.setBackgroundColor(getResources().getColor(R.color.color_item_backgroup));
                                getActivity().startActivity(intent);
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
        //test hcy
        if (isShowDetails == 0) {
            getLoadDate(0, offsetInt, true);
            isShowDetails++;
        }
        // 面试官 对面试者提交成功返回时刷新
        if (SPSecurity.getInstance().getBoolean(Constant.IS_COMMIT_UPDATE_SUCCEED, false)) {
            SPSecurity.getInstance().mSecurityEditor.putBoolean(Constant.IS_COMMIT_UPDATE_SUCCEED, false);
            getLoadDate(0, 0, true);
        }
    }

    private void initView() {
        explv = (SExpandableListView) mView.findViewById(R.id.elsitview);
        tvSearchWait = (TextView) mView.findViewById(R.id.tv_search_wait);
        tvSearchWait.setOnClickListener(this);
        tv_log_out = (TextView) mView.findViewById(R.id.tv_log_out);
        tv_log_out.setOnClickListener(this);
    }


    /**
     * 获取数据  面试状态：0未面试1已面试
     *
     * @param status
     */
    private void getLoadDate(int status, int offset, boolean isRefresh) {

        try {
//            MainActivity.showLoadingDialog(getActivity(), "拼命加载中...");
            if (isRefresh) {
                isNowLoadData = true;
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
            params.put("offset", offset);

            MyHttpUtils.build()
                    .url(Constant.REQ_HTTP_ROOT + "employee/queryAll")
                    .addParams(params)
                    .setToken(tokenStr)
                    .setJavaBean(InterviewFindList.class)
                    .onExecute(new CommCallback<InterviewFindList>() {

                        @Override
                        public void onSucceed(InterviewFindList list) {
                            KLog.i(TAG, "--------------------------onSucceed");
                            int size = list.getResult().size();
                            //解析获取的数据
                            if (size > 0) {
                                offsetInt = size + offsetInt;
                                tempList.addAll(list.getResult());
                                resolverReturnData(tempList);
                                mAdapter.update(groupTitle, childMap);
                                for (int i = 0; i < mAdapter.getGroupCount(); i++) {
                                    explv.expandGroup(i);
                                }

                            }
                            isNowLoadData = false;
                            explv.refreshComplete();
                            if (size < sizeShowCount) {
                                explv.setNoMore(true);
                            } else {
                                explv.setNoMore(false);
                            }
                            MainActivity.dismissLoadingDialog();
                        }

                        @Override
                        public void onFailed(Throwable throwable) {
                            MainActivity.dismissLoadingDialog();
                            KLog.i(TAG, throwable.toString());
                            isNowLoadData = false;
                            explv.refreshComplete();
                            explv.setNoMore(true);
                        }
                    });
        } catch (Exception e) {
            KLog.i(TAG, "getLoadDate---exception");
            e.printStackTrace();
        } finally {
            MainActivity.dismissLoadingDialog();

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_search_wait:
                Intent intent = new Intent(getActivity(), InterviewerSearchActivity.class);
                intent.putExtra(Constant.INTERVIEWER_WAIT_OR_ALREADY, 0);
                isShowDetails++;
                getActivity().startActivity(intent);
                break;

            case R.id.tv_log_out:
                logOutAccount();
                break;
            default:
                break;
        }
    }

    private void logOutAccount() {
        new NormalAlertDialog.Builder(getActivity()).setTitleVisible(true)
                .setTitleText("温馨提示")
                .setTitleTextColor(R.color.black_light)
                .setContentText("确定登出此账号？")
                .setContentTextColor(R.color.black_light)
                .setLeftButtonText("取消")
                .setLeftButtonTextColor(R.color.gray)
                .setRightButtonText("登出")
                .setRightButtonTextColor(R.color.black_light)
                .setOnclickListener(new DialogInterface.OnLeftAndRightClickListener<NormalAlertDialog>() {
                    @Override
                    public void clickLeftButton(NormalAlertDialog dialog, View view) {
                        dialog.dismiss();
                    }

                    @Override
                    public void clickRightButton(NormalAlertDialog dialog, View view) {
                        dialog.dismiss();
                        SPSecurity.getInstance().mSecurityEditor.putString("SeaveLoginPwd", "");
                        SPSecurity.getInstance().mSecurityEditor.putString("auz", "");
                        getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                        ((MainActivity) getActivity()).finish();
                    }
                })
                .setCanceledOnTouchOutside(false)
                .build()
                .show();
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
        if (groupTitle.size() <= setDate.size()) {
            groupTitle.clear();
            groupTitle.addAll(setDate);
        }

        Collections.sort(groupTitle, Collections.<String>reverseOrder());
        int group = groupTitle.size();
        for (int i = 0; i < group; i++) {
            String groupStrTime = groupTitle.get(i);
            List<InterviewInfo> interviewList = new ArrayList<InterviewInfo>();
            for (int i1 = 0; i1 < size; i1++) {
                String timeStr = getDateToString(list.get(i1).getFirstStartTime());
                String timeStrName = list.get(i1).getChineseName();
                if (groupStrTime.equals(timeStr)) {
                    interviewList.add(list.get(i1));
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


    @Override
    public void onPause() {
        super.onPause();
        MainActivity.dismissLoadingDialog();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandlerWait.removeCallbacksAndMessages(null);
    }
}
