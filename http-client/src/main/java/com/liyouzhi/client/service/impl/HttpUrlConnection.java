package com.liyouzhi.client.service.impl;


import com.liyouzhi.client.service.HttpClient;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HttpUrlConnection implements HttpClient<String, String, Map<String, Object>, String> {
    @Override
    public String get(String url, Map<String, Object> params, String charset) {
        return null;
    }

    @Override
    public String post(String urlParam, Map<String, Object> params, String charset) {
        StringBuffer resultBuffer = null;

        StringBuffer sbParams = new StringBuffer();

        if(params != null && params.size() > 0){
            for(Map.Entry<String, Object> entry : params.entrySet()){
                sbParams.append(entry.getKey());
                sbParams.append("=");
                sbParams.append(entry.getValue());
                sbParams.append("&");
            }
        }
        HttpURLConnection httpURLConnection = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedReader bufferedReader = null;

        try {
            URL url = new URL(urlParam);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            if (sbParams != null && sbParams.length() > 0) {
                outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream(), charset);
                outputStreamWriter.write(sbParams.substring(0, sbParams.length() - 1));
                outputStreamWriter.flush();
            }
            // 读取返回内容
            resultBuffer = new StringBuffer();
            int contentLength = Integer.parseInt(httpURLConnection.getHeaderField("Content-Length"));
            if (contentLength > 0) {
                bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), charset));
                String temp;
                while ((temp = bufferedReader.readLine()) != null) {
                    resultBuffer.append(temp);
                }
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        } finally {
            if (outputStreamWriter != null) {
                try {
                    outputStreamWriter.close();
                } catch (IOException e) {
                    outputStreamWriter = null;
                    throw new RuntimeException(e);
                } finally {
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                        httpURLConnection = null;
                    }
                }
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    bufferedReader = null;
                    throw new RuntimeException(e);
                } finally {
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                        httpURLConnection = null;
                    }
                }
            }
        }

        return resultBuffer.toString();
    }
}
