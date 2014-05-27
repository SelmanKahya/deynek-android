package com.deynek.app.activity.find;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.deynek.app.R;
import com.deynek.app.activity.MainActivity;
import com.deynek.app.model.MyActivity;
import com.deynek.app.util.PlaceJSONParser;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindActivity_NEW extends MyActivity {

    AutoCompleteTextView atvPlaces;
    PlacesTask placesTask;
    ParserTask parserTask;
    List<Map<String, String>> planetsList;

    private HashMap<String, String> createPlanet(String key, String name) {
        HashMap<String, String> planet = new HashMap<String, String>();
        planet.put(key, name);
        return planet;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, this);
        setContentView(R.layout.activity_find_activity__new);

        planetsList = new ArrayList<Map<String,String>>();
        ListView lv = (ListView) findViewById(R.id.listView);
        SimpleAdapter simpleAdpt = new SimpleAdapter(this, planetsList, android.R.layout.simple_list_item_1, new String[] {"planet"}, new int[] {android.R.id.text1});
        lv.setAdapter(simpleAdpt);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onDestinationButtonClick(parent, position);
            }
        });

        atvPlaces = (AutoCompleteTextView) findViewById(R.id.atv_places);
        atvPlaces.setThreshold(1);
        atvPlaces.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                placesTask = new PlacesTask();
                placesTask.execute(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Find Parking");
        actionBar.show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void onDestinationButtonClick(AdapterView<?> parent, int position) {
        Map<String,String> o = (Map<String,String>) parent.getItemAtPosition(position);

        String address = null;
        if(o != null)
            address = o.get("description");

        Intent i = new Intent(getApplicationContext(), PickLocationActivity.class);
        i.putExtra("USER_ADDRESS", address);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String walk_time = settings.getString("example_list", "10");
        i.putExtra("USER_MINS", walk_time);
        startActivity(i);
    }

    public void onCurrentLocationButtonClick(View v) {
        Intent i = new Intent(getApplicationContext(), PickLocationActivity.class);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String walk_time = settings.getString("example_list", "10");
        i.putExtra("USER_MINS", walk_time);
        i.putExtra("USER_ADDRESS", false);
        startActivity(i);
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches all places from GooglePlaces AutoComplete Web Service
    private class PlacesTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... place) {
            // For storing data from web service
            String data = "";

            // Obtain browser key from https://code.google.com/apis/console
            String key = "key=AIzaSyBrLBJyn5VdouHvH1HbvZnUNdL0PyPDDIc";

            String input="";

            try {
                input = "input=" + URLEncoder.encode(place[0], "utf-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }

            // place type to be searched
            String types = "types=geocode";

            // Sensor enabled
            String sensor = "sensor=true";

            // Building the parameters to the web service
            String parameters = input+"&"+types+"&"+sensor+"&"+key;

            // Output format
            String output = "json";

            // Building the url to the web service
            String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"+output+"?"+parameters;

            Log.d("url Task", url);

            try{
                // Fetching the data from we service
                data = downloadUrl(url);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Creating ParserTask
            parserTask = new ParserTask();

            // Starting Parsing the JSON string returned by Web Service
            parserTask.execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{

        JSONObject jObject;

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;

            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try{
                jObject = new JSONObject(jsonData[0]);

                // Getting the parsed data as a List construct
                places = placeJsonParser.parse(jObject);

            }catch(Exception e){
                Log.d("Exception",e.toString());
            }
            return places;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {
            String[] from = new String[] { "description"};
            int[] to = new int[] { android.R.id.text1 };
            ListView lv = (ListView) findViewById(R.id.listView);
            SimpleAdapter simpleAdpt = new SimpleAdapter(getBaseContext(), result, android.R.layout.simple_list_item_1, from, to);
            lv.setAdapter(simpleAdpt);
        }
    }
}
