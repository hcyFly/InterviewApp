package com.accenture.cn.interview.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.accenture.cn.interview.R;
import com.accenture.cn.interview.adapter.MyFragmentPagerAdp;
import com.accenture.cn.interview.fragment.HaveInterviewFragment;
import com.accenture.cn.interview.fragment.WaitInterviewFragment;
import com.accenture.cn.interview.utils.LoadingDialog;
import com.accenture.cn.interview.widget.SupportMultipleScreensUtil;
import com.socks.library.KLog;
import com.wevey.selector.dialog.DialogInterface;
import com.wevey.selector.dialog.NormalAlertDialog;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ViewPager viewPager;
    private ArrayList<Fragment> mFragmentsList;
    private RadioGroup mRadioGroup;
    private RadioButton rab_recommend;
    private RadioButton rab_map;
    private int mCurrIndex = 0;
    public static LoadingDialog dialogLoad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        View rootView = findViewById(android.R.id.content);
        SupportMultipleScreensUtil.scale(rootView);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        initView();
        initEvent();
    }


    private void initView() {

        mRadioGroup = (RadioGroup) findViewById(R.id.rag_radios);
        rab_recommend = (RadioButton) findViewById(R.id.rab_recommend);
        rab_map = (RadioButton) findViewById(R.id.rab_map);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
    }

    private void initEvent() {
        mFragmentsList = new ArrayList<Fragment>();
        mFragmentsList.add(new WaitInterviewFragment());
        mFragmentsList.add(new HaveInterviewFragment());
        // 适配
        viewPager.setAdapter(new MyFragmentPagerAdp(
                getSupportFragmentManager(), mFragmentsList));
        rab_recommend.setOnClickListener(new MyOnClickListener(0));
        rab_map.setOnClickListener(new MyOnClickListener(1));
        // 默认进入主界面的页面为第一页面
        viewPager.setCurrentItem(0);
        rab_recommend.setChecked(true);
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;// 定义索引值方便根据所引值显示四个功能界面的相应界面

        public MyOnClickListener(int i) {
            // 界面的索引值
            index = i;
             KLog.i(TAG, "界面的索引值" + index);
        }

        @Override
        public void onClick(View v) {
            // 根据索引值显示相应的界面
            viewPager.setCurrentItem(index);
        }
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int arg0) {
            switch (arg0) {
                case 0:
                    rab_recommend.setChecked(true);
                    rab_map.setChecked(false);
                    break;
                case 1:
                    rab_map.setChecked(true);
                    rab_recommend.setChecked(false);
                    break;
                case 2:
                    rab_recommend.setChecked(false);
                    rab_map.setChecked(false);
                    break;
                case 3:
                    rab_recommend.setChecked(false);
                    rab_map.setChecked(false);
                    break;
            }
            mCurrIndex = arg0;
             KLog.i(TAG, "fragment index:" + arg0);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            new NormalAlertDialog.Builder(MainActivity.this).setTitleVisible(true)
                    .setTitleText("温馨提示")
                    .setTitleTextColor(R.color.black_light)
                    .setContentText("是否退出应用？")
                    .setContentTextColor(R.color.black_light)
                    .setLeftButtonText("取消")
                    .setLeftButtonTextColor(R.color.gray)
                    .setRightButtonText("退出")
                    .setRightButtonTextColor(R.color.black_light)
                    .setOnclickListener(new DialogInterface.OnLeftAndRightClickListener<NormalAlertDialog>() {
                        @Override
                        public void clickLeftButton(NormalAlertDialog dialog, View view) {
                            dialog.dismiss();
                        }

                        @Override
                        public void clickRightButton(NormalAlertDialog dialog, View view) {
                            dialog.dismiss();
                            MainActivity.this.finish();
                        }
                    })
                    .setCanceledOnTouchOutside(false)
                    .build()
                    .show();
        }
        return false;
    }

    public static void showLoadingDialog(Context context, String msg) {
        if (dialogLoad == null) {
            dialogLoad = new LoadingDialog(context, msg);
        }
        dialogLoad.show();
    }

    public static void dismissLoadingDialog() {
        if (dialogLoad != null) {
            dialogLoad.close();
            dialogLoad = null;
        }
    }
}
