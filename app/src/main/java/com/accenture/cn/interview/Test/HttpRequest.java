package com.accenture.cn.interview.Test;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class HttpRequest {

    private Map<String, String> propertys;
    private Map<String, Object> params;
    private String url;
    private HttpURLConnection connection;
    private String token;
    private Object jsonObject;

    public HttpRequest(String url) {
        this.url = url;
    }

    public HttpRequest setProperty(String key, String value) {
        if (propertys == null) {
            propertys = new HashMap<>();
        }
        propertys.put(key, value);
        return this;
    }

    public HttpRequest setToken(String token) {
        this.token = token;
        return this;
    }

    public HttpRequest setParam(String key, String value) {
        if (params == null) {
            params = new HashMap<>();
        }
        params.put(key, value);
        return this;
    }

    public HttpRequest setObject(Object object) {
        this.jsonObject = object;
        return this;
    }

    public String get() throws IOException {
        StringBuffer sbuffer = new StringBuffer("");
        url = url + "?" + splitJointString();
        initConnection();
        // connection.setRequestMethod("GET");
        // connection.setRequestProperty("accept", "*/*");
        // 建立实际的连接
        connection.connect();
        // 读取响应
        if (connection.getResponseCode() == 200) {
            // 从服务器获得一个输入流
            InputStreamReader inputStream = new InputStreamReader(connection.getInputStream());// 调用HttpURLConnection连接对象的getInputStream()函数,
            // 将内存缓冲区中封装好的完整的HTTP请求电文发送到服务端。
            BufferedReader reader = new BufferedReader(inputStream);
            String lines;
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                sbuffer.append(lines);
            }
            reader.close();
        } else {
            //  KLog.i(TAG, "请求失败" + connection.getResponseCode());
            System.out.println("请求失败" + connection.getResponseCode());
        }
        // 断开连接
        connection.disconnect();
        return sbuffer.toString();
    }

    public String post() throws IOException {
        return jsonConnection("POST");
    }

    public String put() throws IOException {
        return jsonConnection("PUT");
    }

    public String delete() throws IOException {
        return jsonConnection("DELETE");
    }

    private String jsonConnection(String method) throws IOException, ProtocolException, UnsupportedEncodingException {
        initConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod(method);
        String result = input(splitJointJson());
        return result;
    }

    private String input(String json) throws IOException {
        System.out.println(json);
        StringBuffer sbuffer = new StringBuffer("");
        connection.setRequestProperty("Content-Type", " application/json");// 设定请求格式json，也可以设定xml格式的
        if (json != null) {
            connection.setRequestProperty("Content-Length", json.toString().getBytes().length + ""); // 设置文件请求的长度
            OutputStream out = connection.getOutputStream();// 向对象输出流写出数据，这些数据将存到内存缓冲区中
            out.write(json.getBytes());
            // 刷新对象输出流，将任何字节都写入潜在的流中
            out.flush();
            // 关闭流对象,此时，不能再向对象输出流写入任何数据，先前写入的数据存在于内存缓冲区中
            out.close();
        }
        connection.connect();
        // 读取响应
        if (connection.getResponseCode() == 200) {
            // 从服务器获得一个输入流
            InputStreamReader inputStream = new InputStreamReader(connection.getInputStream());// 调用HttpURLConnection连接对象的getInputStream()函数,
            // 将内存缓冲区中封装好的完整的HTTP请求电文发送到服务端。
            BufferedReader reader = new BufferedReader(inputStream);
            String lines;
            sbuffer = new StringBuffer("");
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                sbuffer.append(lines);
            }
            reader.close();
        } else {
            //  KLog.i(TAG, "请求失败" + connection.getResponseCode());
            System.out.println("请求失败" + connection.getResponseCode());
        }
        // 断开连接
        connection.disconnect();
        return sbuffer.toString();
    }

    private String splitJointJson() {
        if (jsonObject == null) {
            return null;
        }
        return "";
//        return JSONObject.fromObject(jsonObject).toString();


    }

    private String splitJointString() {
        if (params == null) {
            return "";
        }
        Set<String> set = params.keySet();
        boolean f = true;
        StringBuffer b = new StringBuffer("");
        for (String key : set) {
            if (!f) {
                b.append("&");
            }
            b.append(key).append("=").append(params.get(key).toString());
            f = false;
        }

        return b.toString();
    }

    private void initConnection() throws IOException {
        URL u = new URL(url);
        // 添加 请求内容
        connection = (HttpURLConnection) u.openConnection();
        // connection.setRequestProperty("Accept-Charset", "utf-8"); // 设置编码语言
        // connection.setRequestProperty("X-Auth-Token", "token"); // 设置请求的token
        // connection.setRequestProperty("Connection", "keep-alive"); // 设置连接的状态
        // connection.setRequestProperty("Transfer-Encoding", "chunked");//
        // 设置传输编码
        connection.setRequestProperty("accept", "*/*");
        connection.setRequestProperty("connection", "Keep-Alive");
        connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible;MSIE 6.0; Windows NT 5.1;SV1)");

        if (!TextUtils.isEmpty(token)) {
            connection.setRequestProperty("auz", token);
        }
        if (propertys != null) {
            Set<String> keySet = propertys.keySet();
            for (String key : keySet) {
                connection.setRequestProperty(key, propertys.get(key));
            }
        }
        connection.setReadTimeout(10000);// 设置读取超时时间
        connection.setConnectTimeout(10000);// 设置连接超时时间

    }

    public String upload1() {
        return null;
    }

    public void upload(String path) {
        // List<String> list = new ArrayList<String>(); //
        // 要上传的文件名,如：d:\haha.doc.你要实现自己的业务。我这里就是一个空list.
        try {
            String BOUNDARY = "---------7d4a6d158c9"; // 定义数据分隔线
            // String url = "http://localhost/JobPro/upload.do";
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setRequestProperty("Charsert", "UTF-8");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

            OutputStream out = new DataOutputStream(conn.getOutputStream());
            byte[] end_data = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();// 定义最后数据分隔线
            File file = new File(path);
            StringBuilder sb = new StringBuilder();
            sb.append("--");
            sb.append(BOUNDARY);
            sb.append("\r\n");
            sb.append("Content-Disposition: form-data;name=file;filename=\"" + file.getName() + "\"\r\n");
            sb.append("Content-Type:application/octet-stream\r\n\r\n");

            byte[] data = sb.toString().getBytes();
            out.write(data);
            DataInputStream in = new DataInputStream(new FileInputStream(file));
            int bytes = 0;
            byte[] bufferOut = new byte[1024];
            while ((bytes = in.read(bufferOut)) != -1) {
                out.write(bufferOut, 0, bytes);
            }
            out.write("\r\n".getBytes()); // 多个文件时，二个文件之间加入这个
            in.close();
            out.write(end_data);
            out.flush();
            out.close();

            // 定义BufferedReader输入流来读取URL的响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

        } catch (

                Exception e) {
            System.out.println("发送POST请求出现异常！" + e);
            e.printStackTrace();
        }
    }

    public void download(String path, String fileName) {
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            // 构建图片的url地址
            // url = new URL("http://avatar.csdn.net/C/6/8/1_bz419927089.jpg");
            // 开启连接
            HttpURLConnection conn = (HttpURLConnection) new URL(url + "?fileName=" + fileName).openConnection();
            // 设置超时的时间，5000毫秒即5秒
            conn.setConnectTimeout(20 * 1000);
            // 设置获取图片的方式为GET
            conn.setRequestMethod("GET");
            // 响应码为200，则访问成功
            if (conn.getResponseCode() == 200) {
                // 获取连接的输入流，这个输入流就是图片的输入流
                is = conn.getInputStream();
                // 构建一个file对象用于存储图片
                File file = new File(path);
                if (!file.exists()) {
                    // 文件夹不存 创建文件
                    file.mkdirs();
                }
                file = new File(path, fileName);
                if (!file.exists()) {
                    file.createNewFile();
                }
                fos = new FileOutputStream(file);
                int len = 0;
                byte[] buffer = new byte[1024];
                // 将输入流写入到我们定义好的文件中
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
                // 将缓冲刷入文件
                fos.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 在最后，将各种流关闭
            try {
                if (is != null) {
                    is.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
