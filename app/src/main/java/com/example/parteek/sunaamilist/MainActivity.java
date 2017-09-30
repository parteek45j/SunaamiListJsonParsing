package com.example.parteek.sunaamilist;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayAdapter<String> adapter;
    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    ArrayList<String > arrayList;
    ProgressDialog progressDialog;
    Handler handler;
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2016-01-01&endtime=2016-01-31&minmagnitude=6&limit=15";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler=new Handler();
        progressDialog=new ProgressDialog(this);
        listView=(ListView)findViewById(R.id.list_item);
        arrayList=new ArrayList<>();
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(adapter);
        MyTask myTask=new MyTask();
        myTask.execute();
    }
    public class MyTask extends AsyncTask<URL,String,ArrayList>{
        ArrayAdapter<String> arrayAdapter;
        ArrayList<String> arrayList2;
        String[] list;
        @Override
        protected void onPreExecute() {
            progressDialog.show();
            arrayAdapter=(ArrayAdapter)listView.getAdapter();
        }

        @Override
        protected void onPostExecute(ArrayList arrayList) {
            progressDialog.dismiss();
        }

        @Override
        protected ArrayList<String> doInBackground(URL... urls) {

                URL url=crateUrl(USGS_REQUEST_URL);
                String response="";
            try {
                response=makeHttpRequest(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            arrayList2=extractFeatureFromJson(response);
            list=arrayList2.toArray(new String[arrayList2.size()]);
            publishProgress(list);
//            MainActivity.this.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    arrayAdapter.add(arrayList);
//                    Log.e("Yaa", String.valueOf(arrayList));
//                }
//            });
            return arrayList;
        }

        @Override
        protected void onProgressUpdate(String[] values) {
            for (int i=0;i<values.length;i++) {
                arrayAdapter.add(values[i]);
            }
        }

        private URL crateUrl(String stringUrl){
            URL url=null;
            try {
                url=new URL(stringUrl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return url;
        }

        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.connect();
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } catch (IOException e) {
                // TODO: Handle the exception
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    // function must handle java.io.IOException here
                    inputStream.close();
                }
            }
            return jsonResponse;
        }
        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }
        private ArrayList<String> extractFeatureFromJson(String earthquakeJSON) {
            ArrayList<String> arrayList1=new ArrayList<>();
            try {
                JSONObject baseJsonResponse = new JSONObject(earthquakeJSON);
                JSONArray featureArray = baseJsonResponse.getJSONArray("features");

                // If there are results in the features array
                for (int i=0;i<featureArray.length();i++) {
                    // Extract out the first feature (which is an earthquake)
                    JSONObject firstFeature = featureArray.getJSONObject(i);
                    JSONObject properties = firstFeature.getJSONObject("properties");

                    // Extract out the title, time, and tsunami values
                    String title = properties.getString("place");
                    arrayList1.add(title);
                    // Create a new {@link Event} object

                }
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
            }
            return arrayList1;
        }
    }
}
