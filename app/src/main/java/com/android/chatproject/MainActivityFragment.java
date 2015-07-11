package com.android.chatproject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketHandler;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private String myResultStr;
    private TextView textView;
    private HttpURLConnection urlConnection = null;
    WebSocketConnection mConnection;

    public MainActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //MyNetworkClass myNetworkClass = new MyNetworkClass();
        //myNetworkClass.execute();



        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        //textView = (TextView)rootView.findViewById(R.id.test_textview);

        //textView.setText(myResultStr);
        //connect();

        String[] arrayForList= {"Fake channel 1 : 51",
                "Fake channel 2 : 51",
                "Fake channel 3 : 57",
                "Fake channel 4 : 52",
                "Fake channel 5 : 53"

        };

        List<String> data = new ArrayList<String>(Arrays.asList(arrayForList));

        ArrayAdapter<String> myListAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_layout,
                R.id.list_item_textview_id,
                data);


        ListView listView =(ListView) rootView.findViewById(R.id.listview_for_channels_id);
        listView.setAdapter(myListAdapter);

        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.action_stop_connection) {
           return true;
        }
        if (id == R.id.action_getchannels) {
            connect();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    public void connect() {
        try {
            mConnection = new WebSocketConnection();
            mConnection.connect("ws://chat.goodgame.ru:8081/chat/websocket", new WebSocketHandler() {
                @Override
                public void onOpen() {

                    System.out.println("--open");


                    try {
                        mConnection.sendTextMessage(getChannels().toString());// ("123");//getChannels().toString());

                        //mConnection.disconnect();

                        textView.setText(getChannels().toString());
                    }catch (JSONException e){
                        Log.v("123", e.toString());
                    }
                }

                @Override
                public void onTextMessage(String message) {
                    System.out.println("--received message: " + message);
                    try {
                        myParseJSON(message);

                    }catch(JSONException e){

                    }
                }

                @Override
                public void onClose(int code, String reason) {

                    System.out.println("--close");
                    mConnection.disconnect();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JSONObject getChannels() throws JSONException {
        //JSONObject completeJSON = new JSONObject();

        JSONObject newstringJSON = new JSONObject();


        JSONArray myArray1 = new JSONArray();
        JSONArray myArray2 = new JSONArray();

        JSONObject newJSObj1 = new JSONObject();
        JSONObject newJSObj2 = new JSONObject();

        newJSObj1.put("start","0");
        newJSObj2.put("count","300");
        //JSONObject newJSObj3 = new JSONObject();

        //newJSObj.put("start","0");
        myArray1.put(newJSObj1);
        myArray1.put(newJSObj2);

        newstringJSON.put("type","get_channels_list");
        newstringJSON.put("data",myArray1);

        //myArray2.put(newstringJSON);
        //myArray2.put(myArray1);

        return newstringJSON;
    }
    private void myParseJSON(String s) throws JSONException{
        JSONObject MyJsonObject = new JSONObject(s);

        String stringOfFate=MyJsonObject.getString("type");

        if(stringOfFate=="channels_list"){
            Log.v("Это список каналов", "каналы:");
        }else {

        }
    }

    /*//req_to_server
{
    type: "get_channels_list",
    data: {
        start: 0, // стартовая позиция (отсчет с 0)
        count: 50 // количество каналов на страницу (max - 50)
    }
}
    * */
}
