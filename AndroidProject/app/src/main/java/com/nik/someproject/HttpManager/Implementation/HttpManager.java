package com.nik.someproject.HttpManager.Implementation;

import android.util.Log;

import com.nik.someproject.HttpManager.Services.IRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HttpManager implements IRequest {

    @Override
    public Map<String, String> request(String urlString, Map<String, String> params, Map<String, String> properties, String method) {
        Map<String, String> response = new HashMap<String, String>();
        try {
            // Setting up request
            URL url = new URL(urlString);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            if (!method.equals("GET")) {
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
            }
            httpURLConnection.setRequestMethod(method);
            httpURLConnection.setConnectTimeout(3000);
            httpURLConnection.setReadTimeout(3000);

            String param = parseParameters(params);
            byte[] postData = param.getBytes( StandardCharsets.UTF_8 );
            int postDataLength = postData.length;
            httpURLConnection.setRequestProperty("Content-Length", Integer.toString(postDataLength));

            // Writing properties of request
            for (Map.Entry<String,String> i : properties.entrySet()) {
                httpURLConnection.setRequestProperty(i.getKey(), i.getValue());
            }

            if (!method.equals("GET")) {
                // Writings params of request
                OutputStream os = httpURLConnection.getOutputStream();
                OutputStreamWriter writer = new OutputStreamWriter(os, StandardCharsets.UTF_8);
                writer.write(param);
                writer.flush();
                writer.close();
                os.close();
            }


            httpURLConnection.connect();
            response.put("Message", httpURLConnection.getResponseMessage());
            response.put("Status", Integer.toString(httpURLConnection.getResponseCode()));
            if (httpURLConnection.getResponseCode() != 200)
                return response;


            // Reading response resources
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            StringBuilder source = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                source.append(line+"\n");
            }
            reader.close();

            // Parse resources into String -> JSON -> Map
            JSONObject object = new JSONObject(source.toString());
            for (Iterator<String> it = object.keys(); it.hasNext(); ) {
                String key = it.next();
                Object sub_object = object.get(key);
                if (sub_object instanceof JSONArray) {
                    StringBuilder arrayString = new StringBuilder();
                    for (int i = 0; i < ((JSONArray)sub_object).length(); i++) {
                        String temp = ((JSONArray) sub_object).get(i).toString();
                        temp = temp.replaceAll(",", ";");
                        arrayString.append(temp).append(",");
                    }
                    if (arrayString.length() > 0)
                        arrayString = new StringBuilder(arrayString.substring(0, arrayString.length() - 1));
                    response.put(key, arrayString.toString());
                } else {
                    response.put(key, String.valueOf(object.get(key)));
                }
            }

        } catch (Exception e) {
            response.put("Message", e.toString());
            response.put("Status", "-1");
            return response;
        }
        return response;
    }

    private String parseParameters(Map<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = false;

        for (Map.Entry<String,String> i : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(i.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(i.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}
