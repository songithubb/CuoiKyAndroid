package com.example.androidnc.cuoikyandroid;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

public class firebaseAsyntask extends AsyncTask<String, Boolean, JSONObject> {
    private Map<String, String> map;
    private firebaseAsyntaskView firebaseAsyntaskView;

    public firebaseAsyntask(firebaseAsyntaskView firebaseAsyntaskView, Map<String, String> map) {
        this.map = map;
        this.firebaseAsyntaskView = firebaseAsyntaskView;
    }

    @Override
    protected JSONObject doInBackground(String... strings) {
        try {
            URL url = new URL(strings[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestMethod("POST");
            //them param tham so vao cho method POST//
            JSONObject jsonObject = new JSONObject();
            Iterator iter = map.keySet().iterator();
            while (iter.hasNext()) {
                String key = (String) iter.next();
                String value = map.get(key);
                jsonObject.put(key, value);
            }
            OutputStream outputStream = connection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            bufferedWriter.write(String.valueOf(jsonObject));
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            //-----//
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();
            String result;
            while ((result = bufferedReader.readLine()) != null) {
                stringBuffer.append(result);
            }
            result = stringBuffer.toString();
            JSONObject jsonObject1 = new JSONObject(result);
            return jsonObject1;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        if (jsonObject != null) {
            try {
                int mresult = jsonObject.getInt("result");
                String message = jsonObject.getString("response_message");
                if (mresult > 0) {
                    firebaseAsyntaskView.onSuccess(message);
                } else {
                    firebaseAsyntaskView.onFail(message);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        super.onPostExecute(jsonObject);
    }
}
