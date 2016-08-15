package com.xiekang.king.gift.utils;

import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by King on 2016/8/11.
 * 
 */
public class HttpUtils {

    private static ExecutorService executorService;

    /**
     * 程序的入口
     *
     * @param urlString 请求的url
     */
    public static HttpThread load(String urlString) {
        if (executorService == null) {
            executorService = Executors.newFixedThreadPool(3);
        }
        HttpThread httpThread = new HttpThread();
        httpThread.start(urlString);
        return httpThread;
    }

    public static class HttpThread {
        private ICallBack callBack;
        private int requestCode;
        private boolean isPost;
        private String params;
        /**
         * 出口
         * @param callBack
         * @param requestCode
         */
        public void callBack(ICallBack callBack, int requestCode) {
            this.callBack = callBack;
            this.requestCode = requestCode;
        }

        private Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String result = msg.obj.toString();
                if (callBack != null) {
                    callBack.successJson(result, requestCode);
                }
            }
        };

        /**
         * 若是post请求则必须执行此方法
         * @param params
         * @return
         */
        public HttpThread post(Map<String,Object> params){
            isPost = true;
            this.params = formatParams(params);
            return this;
        }

        private String formatParams(Map<String,Object> params){
            Set<String> strings = params.keySet();
            StringBuilder builder = new StringBuilder();
            for(String key:strings){
                Object value = params.get(key);
                builder.append(key).append("=").append(value).append("&");
            }
            String result = builder.toString();
            int lastIndexOf = result.lastIndexOf("&");
            result = result.substring(0,lastIndexOf);
            return result;
        }


        /**
         * 启动线程
         * @param urlString
         * @return
         */
        private HttpThread start(String urlString) {
            executorService.execute(new HttpRunnable(urlString));
            return this;
        }

        /**
         * 网络请求线程
         */
        class HttpRunnable implements Runnable {
            private String urlString;

            public HttpRunnable(String urlString) {
                this.urlString = urlString;
            }

            @Override
            public void run() {
                try {
                    URL url = new URL(urlString);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    if (isPost){
                        conn.setDoOutput(true);
                        conn.setRequestMethod("POST");
                        conn.getOutputStream().write(params.getBytes());
                        conn.getOutputStream().flush();
                    }
                    conn.connect();
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream inputStream = conn.getInputStream();
                        int len = 0;
                        byte buffer[] = new byte[1024];
                        StringBuilder sb = new StringBuilder();
                        while ((len = inputStream.read(buffer)) != -1) {
                            sb.append(new String(buffer, 0, len));
                        }
                        String result = sb.toString();
                        Message message = mHandler.obtainMessage();
                        message.obj = result;
                        message.sendToTarget();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }


}
