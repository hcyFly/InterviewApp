package com.accenture.cn.interview.fragment;

import android.content.Context;
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
import android.widget.Toast;

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
 * Created by ytx on 2016/6/22.
 */
public class HaveInterviewFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "HaveInterviewFragment";
    private Context mContext;
    private View mView;
    private SExpandableListView explv;
    private TextView tvSearchHave, tv_log_out;

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
    private boolean isNotDataShow = false;
    private boolean mIsScrollIng = false;
    //起始数
    private int offsetInt = 0;
    private Map<String, Object> params;
    //每页记录数
    private int sizeShowCount = 20;
    private int isShowDetails = 0;

    Handler mHandlerHave = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case 1000:
                    KLog.i(TAG, " --handleMessage  81  onRefresh----have");
                    getLoadDate(1, 0, true);
                    break;
                case 2000:
                    KLog.i(TAG, " --handleMessage  84  onLoadMore----have");
                    getLoadDate(1, offsetInt, false);
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
        mView = inflater.inflate(R.layout.fragment_have, container, false);
        mContext = getActivity();
        return mView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            KLog.i(TAG, "--setUserVisibleHint-->相当于Fragment的onResume");
            if (isShowDetails == 0) {
                if (!isNotDataShow) {
                    getLoadDate(1, offsetInt, true);
                    isShowDetails++;
                }
            }
        } else {
            KLog.i(TAG, "--setUserVisibleHint-->相当于Fragment的onPause");
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
        KLog.i(TAG, " --onResume-->start");
        if (mView != null) {
            if (explv == null) {
                initView();
                mAdapter = new WaitInterviewAdapter(mContext, groupTitle, childMap);
                explv.setPullRefreshEnabled(true);
                explv.setLoadingMoreEnabled(true);
                explv.setAdapter(mAdapter);

                explv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                    @Override
                    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                        //屏蔽组点击事件  false为可点击
                        return true;
                    }
                });
                explv.setmLoadingListener(new SExpandableListView.LoadingListener() {
                    @Override
                    public void onLoadMore() {
                        KLog.i(TAG, " --onLoadMore----");
                        isNowLoadData = true;
                        mHandlerHave.sendEmptyMessage(2000);
                    }

                    @Override
                    public void onRefresh() {
                        KLog.i(TAG, " --onRefresh----have");
                        isNowLoadData = true;
                        mHandlerHave.sendEmptyMessage(1000);
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
                        KLog.i(TAG, " --onChildClick----");
                        if (groupTitle.size() > 0 && childMap.size() > 0) {
                            try {
                                InterviewInfo interviewInfo = childMap.get(groupTitle.get(groupPosition)).get(childPosition);
                                Intent intent = new Intent(getActivity(), InterviewerDetailsActivity.class);
                                intent.putExtra(Constant.INTERVIEWER_WAIT_OR_ALREADY, "1");
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
        KLog.i(TAG, " --onResume-->end");
    }

    private void initView() {
        explv = (SExpandableListView) mView.findViewById(R.id.elsitview_have);
        tvSearchHave = (TextView) mView.findViewById(R.id.tv_search_have);
        tvSearchHave.setOnClickListener(this);
        tv_log_out = (TextView) mView.findViewById(R.id.tv_log_out);
        tv_log_out.setOnClickListener(this);
    }

    /**
     * 获取数据  面试状态：0未面试1已面试
     *
     * @param status
     */
    private void getLoadDate(int status, int offset, boolean isRefresh) {

        MainActivity.showLoadingDialog(getActivity(), "拼命加载中...");
        if (isRefresh) {
            isNowLoadData = true;
            isNotDataShow = false;
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
                            offsetInt = size + offsetInt;
                            tempList.addAll(list.getResult());
                            resolverReturnData(tempList);
                            mAdapter.update(groupTitle, childMap);
                            for (int i = 0; i < mAdapter.getGroupCount(); i++) {
                                explv.expandGroup(i);
                            }

                        } else {
                            if (offsetInt > 0) {
                                KLog.i(TAG, "getLoadDate---没有更多数据");
                                Toast.makeText(getActivity(), "没有更多数据！", Toast.LENGTH_SHORT).show();
                            } else {
                                KLog.i(TAG, "getLoadDate---无数据");
                                Toast.makeText(getActivity(), "暂没有符合条件的数据！", Toast.LENGTH_SHORT).show();
                                isNotDataShow = true;
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
                    }
                });

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
//        Collections.sort(this.arrayList);
//        Collections.sort(this.arrayList,Collections.reverseOrder());//降序
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_search_have:
                Intent intent = new Intent(getActivity(), InterviewerSearchActivity.class);
                intent.putExtra(Constant.INTERVIEWER_WAIT_OR_ALREADY, 1);
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
    }

}