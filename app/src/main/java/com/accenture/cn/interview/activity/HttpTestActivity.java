package com.accenture.cn.interview.activity;


import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.accenture.cn.interview.R;
import com.accenture.cn.interview.base.MyApplication;
import com.accenture.cn.interview.model.Weather;
import com.accenture.cn.interview.utils.DrawableTextView;
import com.accenture.cn.interview.utils.FileOpenUtils;
import com.accenture.cn.interview.utils.FileUtil;
import com.accenture.cn.interview.utils.LoadingDialog;
import com.accenture.cn.interview.utils.MarqueeTextView;
import com.accenture.cn.interview.utils.MarqueeTextViewClickListener;
import com.accenture.cn.interview.widget.SupportMultipleScreensUtil;
import com.hdl.myhttputils.MyHttpUtils;
import com.hdl.myhttputils.bean.CommCallback;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.socks.library.KLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpTestActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "HttpTestActivity";
    private Context mContext;
    private TextView tv;
    private Button but, but11, butdocx, buttxt, butppt, butpdf, butimg, but_but_hook;
    private LoadingDialog dialog;

    Map map = new HashMap();//test hook
    private MarqueeTextView mtv;
    //text
    private DrawableTextView drawableTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_texxt);
        View rootview = findViewById(android.R.id.content);
        SupportMultipleScreensUtil.scale(rootview);

        mContext = this;
        tv = (TextView) findViewById(R.id.tv_tv_http);
        but = (Button) findViewById(R.id.but_but_http);
        but.setOnClickListener(this);
        but11 = (Button) findViewById(R.id.but_but_http11);
        but11.setOnClickListener(this);
        butdocx = (Button) findViewById(R.id.but_but_docx);
        butdocx.setOnClickListener(this);
        buttxt = (Button) findViewById(R.id.but_but_txt);
        buttxt.setOnClickListener(this);
        butppt = (Button) findViewById(R.id.but_but_ppt);
        butppt.setOnClickListener(this);
        butpdf = (Button) findViewById(R.id.but_but_pdf);
        butpdf.setOnClickListener(this);
        butimg = (Button) findViewById(R.id.but_but_img);
        butimg.setOnClickListener(this);

        but_but_hook = (Button) findViewById(R.id.but_but_hook);
        hook(but_but_hook);
        tv.setOnClickListener(this);
        dialog = new LoadingDialog(mContext, "玩命加载中...");

        mtv = (MarqueeTextView) findViewById(R.id.mtv_text);
        mtv.setTextArraysAndClickListener(new String[]{"heheheda 001", "HEHEHEDAO 002", "呵呵哒呵呵哒003"}, new MarqueeTextViewClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "click one", Toast.LENGTH_SHORT).show();
            }
        });

        //test
        drawableTextView = (DrawableTextView) findViewById(R.id.drawableTextView);
//        Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.ic_launcher);
//        drawableTextView.setDrawable(DrawableTextView.LEFT, drawable, 56, 56);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_tv_http:
                break;
            case R.id.but_but_http:
                showLoadingDialog("2玩命加载中...");
//                String url = "http://guolin.tech/api/weather?cityid=深圳&key=3799d79d735340ac9accef410a7f5316";
                String url = "http://guolin.tech/api/weather?key=3799d79d735340ac9accef410a7f5316&cityid=";
                try {
                    String cityt = URLEncoder.encode("深圳", "UTF-8");
                    url = url + cityt;
                } catch (UnsupportedEncodingException e) {
                    url = "http://guolin.tech/api/weather?cityid=shenzhen&key=3799d79d735340ac9accef410a7f5316";
                    e.printStackTrace();
                }
                MyHttpUtils.build()
                        .url(url)
                        .setJavaBean(Weather.class)
                        .onExecute(new CommCallback<Weather>() {

                            @Override
                            public void onSucceed(Weather weather) {
                                dismissLoadingDialog();
                                if (weather != null) {
                                    final Weather.HeWeatherUserHH wuh = weather.getHeWeather().get(0);
                                    String temp = tv.getText().toString();
                                    tv.setText(temp + "\n--成功：" + wuh.getBasic().getCity() + "--id:" + wuh.getBasic().getId());
                                }
                            }

                            @Override
                            public void onFailed(Throwable throwable) {
                                dismissLoadingDialog();
                                tv.setText("error:" + throwable.toString());
                            }
                        });
                break;

            case R.id.but_but_http11:
                showLoadingDialog("玩命加载中...11");
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            Thread.sleep(2000);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dismissLoadingDialog();
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;

            case R.id.but_but_docx:

                createFileToCache(MyApplication.saveDriPath + "/text_word.docx", R.raw.test_word, "text_word.docx----文件写入缓存目录成功", "text_word.docx---文件已存在");
                FileOpenUtils.openFilePath(mContext, MyApplication.saveDriPath + "/text_word.docx");
                break;
            case R.id.but_but_txt:

                // TODO: 2017/1/13  测试打开附件
                createFileToCache(MyApplication.saveDriPath + "/text_txt_utf8.txt", R.raw.test_txt_utf8, "text_txt_utf8.txt----文件写入缓存目录成功", "text_txt_utf8.txt---文件已存在");
                FileOpenUtils.openFilePath(mContext, MyApplication.saveDriPath + "/text_txt_utf8.txt");
                break;

            case R.id.but_but_ppt:

                createFileToCache(MyApplication.saveDriPath + "/text_ppt.pptx", R.raw.test_ppt, "text_ppt.pptx----文件写入缓存目录成功", "text_ppt.pptx----文件已存在");
                FileOpenUtils.openFile(mContext, new File(MyApplication.saveDriPath + "/text_ppt.pptx"));
                break;
            case R.id.but_but_pdf:

                createFileToCache(MyApplication.saveDriPath + "/text_pdf.pdf", R.raw.test_pdf, "text_pdf.pdf----文件写入缓存目录成功", "text_pdf.pdf----文件已存在");
                FileOpenUtils.openFile(mContext, new File(MyApplication.saveDriPath + "/text_pdf.pdf"));
                break;
            case R.id.but_but_img:

                createFileToCache(MyApplication.saveDriPath + "/test_img.jpg", R.raw.test_img, "test_img.jpg----文件写入缓存目录成功", "test_img.jpg----文件已存在");
                FileOpenUtils.openFile(mContext, new File(MyApplication.saveDriPath + "/test_img.jpg"));
                break;
            default:
                break;
        }
    }

    private void createFileToCache(final String path, final int img, final String msg, final String msg2) {
        Acp.getInstance(mContext).request(new AcpOptions.Builder()
                        .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .build(),
                new AcpListener() {
                    @Override
                    public void onGranted() {
                        if (!FileUtil.isFileExists(path)) {//文件是否已经存在？
                            File pptxfile = new File(path);
                            InputStream is = null;
                            FileOutputStream fos = null;
                            try {
                                is = getResources().openRawResource(img);
                                int len = 0;
                                byte[] buf = new byte[1024 * 1024];
                                fos = new FileOutputStream(pptxfile);
                                while ((len = is.read(buf)) != -1) {
                                    fos.write(buf, 0, len);
                                }
                                 KLog.i(TAG, msg);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                try {
                                    if (is != null) {
                                        is.close();
                                    }
                                    if (fos != null) {
                                        fos.close();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                        } else {
                             KLog.i(TAG, msg2);
                        }
                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        Toast.makeText(mContext, "你拒绝了权限申请，请到‘设置’中开启，才能使用此功能。", Toast.LENGTH_SHORT).show();

                    }
                });

    }

    private void showLoadingDialog(String msg) {
        if (dialog == null) {
            dialog = new LoadingDialog(mContext, msg);
        }
        dialog.show();
    }

    private void dismissLoadingDialog() {
        if (dialog != null) {
            dialog.close();
            dialog = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissLoadingDialog();
    }

    // TODO: 2017/1/18  test hook

    private void hook(View view) {
        try {
            Class classview = Class.forName("android.view.View");
            Method method = classview.getDeclaredMethod("getListenerInfo");
            method.setAccessible(true);
            Object listenerInfo = method.invoke(view);
            Class classInfo = Class.forName("android.view.View$ListenerInfo");
            Field field = classInfo.getDeclaredField("mOnClickListener");
            field.set(listenerInfo, new HookListener((View.OnClickListener) field.get(listenerInfo)));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void hook(View... views) {
        for (View view : views) {
            hook(view);
        }
    }

//    case R.id.but_but_hook:
//            // TODO: 2017/1/18   test hook
//            map.put("hehe", "呵呵");
//    map.put("好", "好好的");
//    v.setTag(R.id.but_but_hook, map);
//    break;
//    public void onClick(View view) {
//        Map map = new HashMap();
//        switch (view.getId()) {
//            case R.id.but_but_hook:
//                map.put("巴", "掌");
//                map.put("菜", "比");
//                break;
//            case R.id.but_but_hook:
//                map.put("TF-Boys", "嘿嘿嘿");
//                map.put("id", "111");
//                break;
//        }
//        view.setTag(R.id.but_but_hook, map);
//    }

    public static class HookListener implements View.OnClickListener {

        private View.OnClickListener mOriginalListener;

        public HookListener(View.OnClickListener originalListener) {
            mOriginalListener = originalListener;
        }

        @Override
        public void onClick(View v) {
            if (mOriginalListener != null) {
                mOriginalListener.onClick(v);
            }
            StringBuffer sb = new StringBuffer();
            sb.append("hook succeed.\n");
            Object obj = v.getTag(R.id.but_but_hook);
            if (obj instanceof HashMap && !((Map) obj).isEmpty()) {
                for (Map.Entry<String, String> entry : ((Map<String, String>) obj).entrySet()) {
                    sb.append("key=> ").append(entry.getKey())
                            .append("  ")
                            .append("value=> ")
                            .append(entry.getValue())
                            .append("\n");
                }
            } else {
                sb.append("params => null \n");
            }
            Toast.makeText(v.getContext(), sb.toString(), Toast.LENGTH_SHORT).show();
        }
    }

}
