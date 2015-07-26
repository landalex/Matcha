package com.sap.matcha;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by AlexLand on 15-07-25.
 */
public class ServerHelper {
    private static String username = "AVC_MatchaDBU";
    private static String password = "Matcha@90";
    private static String link = "http://matcha.avocadostudio.ca/POSTmethod.php";

    public static void serverRequest(final String query) {
        ServerRequestTask task = new ServerRequestTask();
        task.execute(query);
    }

    static class ServerRequestTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(link);
                HttpClient client = new DefaultHttpClient();
                HttpPost request = new HttpPost(link);
                List<NameValuePair> entity = new ArrayList<>();
                entity.add(new BasicNameValuePair("query", params[0]));
                request.setEntity(new UrlEncodedFormEntity(entity));
                HttpResponse response = client.execute(request);
                BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                StringBuffer sb = new StringBuffer("");
                String line = "";

                while ((line = in.readLine()) != null) {
                    sb.append(line);
                    break;
                }
                in.close();
                StatusLine statusLine = response.getStatusLine();
                Log.i("ServerHelper", "" + statusLine.getStatusCode());
                return response.toString();
            }
            catch (Exception e) {
                Log.i("ServerHelper", "Exception" + e.getMessage());
                return "";
            }
        }
    }
}
