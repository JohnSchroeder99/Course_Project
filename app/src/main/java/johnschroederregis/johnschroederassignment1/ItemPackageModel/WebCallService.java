package johnschroederregis.johnschroederassignment1.ItemPackageModel;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import johnschroederregis.johnschroederassignment1.Master;
import johnschroederregis.johnschroederassignment1.R;

public class WebCallService extends Service {
    public final IBinder mbinder = new MyBinder();
    private ArrayList<String> atempList;
    private ArrayList<String> btempList = new ArrayList<String>();
    public HttpGetAsync httpGetAsync;

    public WebCallService() {
    }

    public class MyBinder extends Binder {
        public WebCallService getService() {
            return WebCallService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("service", "in onBind");
        return mbinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("service", "in onstart service");
        httpGetAsync = new HttpGetAsync();
        httpGetAsync.execute();
        Log.d("service", "after async execute");
        return Service.START_NOT_STICKY;
    }

    public class HttpGetAsync extends AsyncTask<String, Void, String> {
        public HttpGetAsync() {
            Log.d("service", "in ASync");
        }

        @Override
        protected String doInBackground(String... parms) {
            Log.d("service", "in ASync background start");
            String result = "";
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL("https://contacts-course-project.firebaseio.com/.json");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("name", "contacts-course-project.json");
                urlConnection.connect();
                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String s = "";
                while ((s = reader.readLine()) != null) {
                    result += s;
                }

                Log.d("service", "in ASync after connection result is " + result);
            } catch (IOException e) {
                Log.d("service", "result failed");
                e.getMessage();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                Log.d("service", "service getinformation 3 ");
                s = "[" + s + "]";
                JSONArray jsonArray = new JSONArray(s);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                Log.d("service", "service getinformation 4 ");
                atempList = new ArrayList<String>();
                Iterator<String> keys = jsonObject.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    atempList.add(key);
                    btempList.add(key);

                }
                Log.d("service", atempList.toString());
                Log.d("service", btempList.toString());
            } catch (Exception e) {
                Log.d("service", "failed get");
                e.getMessage();
            }
            Log.d("service", "service getinformation end ");
            super.onPostExecute(s);
            fillGroupNamesListAndBroadCast();
        }
    }


    public static class HttpPostAsyncTask extends AsyncTask<String, String, String> {
        String a = "";
        String b = "";

        HttpPostAsyncTask(String a, String b) {
            this.a = a;
            this.b = b;
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d("service", "service onpost Async");
            URL url = null;
            InputStream stream = null;
            HttpURLConnection urlConnection = null;
            try {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put(a, b);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                url = new URL("https://contacts-course-project.firebaseio.com/.json");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.connect();

                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                Log.d("get", jsonObject.toString());
                wr.write(jsonObject.toString());
                wr.flush();
                stream = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"), 8);
                String result = reader.readLine();
                Log.d("service", "service on post after url");

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "";
        }
    }

    public void postInformation(String a, String b) {
        HttpPostAsyncTask httpPostAsyncTask = new HttpPostAsyncTask(a, b);
        httpPostAsyncTask.execute();
    }

    public void fillGroupNamesListAndBroadCast() {
        if (atempList.size() > 0) {
            Intent i = new Intent(this, Master.class);
            i.setAction("android.intent.action.GROUPS_RECEIVED");
            i.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            i.putStringArrayListExtra("GroupList",  atempList);
            i.putStringArrayListExtra("NameList", btempList);
            Log.d("service", " received properly from firebase :" + atempList.size());
            LocalBroadcastManager.getInstance(this.getApplicationContext()).sendBroadcast(i);
        } else {
            Log.d("service", "not received properly from firebase :" + atempList.size());
        }
    }


    @Override
    public void onDestroy() {
        Log.d("service", "service stopped");
        super.onDestroy();
    }
}

