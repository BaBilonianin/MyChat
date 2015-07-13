package com.android.chatproject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketHandler;


/**
 * A placeholder fragment containing a simple view.
 */
public class ChatFragment extends Fragment {

    WebSocketConnection mConnection2;
    String channel_id;
    String[] messagesHistory={"1","2","3"};
    ArrayList<String> data;

    ArrayAdapter<String> myListAdapter2;
    String user_name, user_message;
    ListView listView;

    public ChatFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);

        channel_id =(String)getActivity().getIntent().getSerializableExtra("channel_id");
        connect();

        data = new ArrayList<String>(Arrays.asList(messagesHistory));

        myListAdapter2 = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_layout,
                R.id.list_item_textview_id,
                data);

        listView=(ListView)rootView.findViewById(R.id.listview_for_chat_id);
        listView.setAdapter(myListAdapter2);

        return rootView;

    }

    public void connect() {
        try {
            mConnection2 = new WebSocketConnection();
            mConnection2.connect("ws://chat.goodgame.ru:8081/chat/websocket", new WebSocketHandler() {
                @Override
                public void onOpen() {
                    System.out.println("--open");
                    myListAdapter2.clear();
                    try {
                        mConnection2.sendTextMessage(connectToChannel().toString());
                        Log.v("connetcToChannel", connectToChannel().toString());
                        mConnection2.sendTextMessage(getMessagesHistory().toString());
                        Log.v("gethistorystring", getMessagesHistory().toString());
                    }catch (JSONException e){
                        Log.v("123", e.toString());
                    }
                }

                @Override
                public void onTextMessage(String message) {
                    System.out.println("--received message: " + message);
                    try {
                        myParseJSON(message);
                        //myListAdapter2.notifyDataSetChanged();

                    }catch(JSONException e){
                        Log.v("JS excepcion", e.toString());
                    }

                }
                @Override
                public void onClose(int code, String reason) {

                    System.out.println("--close");
                    mConnection2.disconnect();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private JSONObject connectToChannel() throws JSONException {

        JSONObject newstringJSON = new JSONObject();
        JSONArray myArray1 = new JSONArray();
        JSONArray myArray2 = new JSONArray();

        JSONObject newJSObj1 = new JSONObject();
        JSONObject newJSObj2 = new JSONObject();

        newJSObj1.put("channel_id",channel_id);
        newJSObj1.put("hidden","false");

        newstringJSON.put("type","join");
        newstringJSON.put("data",newJSObj1);

        return newstringJSON;
    }
    private JSONObject getMessagesHistory() throws JSONException{
        JSONObject newstringJSON = new JSONObject();
        JSONArray myArray1 = new JSONArray();
        JSONArray myArray2 = new JSONArray();

        JSONObject newJSObj1 = new JSONObject();
        JSONObject newJSObj2 = new JSONObject();

        newJSObj1.put("channel_id",channel_id);
        //newJSObj1.put("hidden","false");

        newstringJSON.put("type","get_channel_history");
        newstringJSON.put("data", newJSObj1);
        Log.v("get_channels",newstringJSON.toString());
        return newstringJSON;
    }
    private void myParseJSON(String s) throws JSONException{
        JSONObject MyJsonObject = new JSONObject(s);

        String stringOfFate = MyJsonObject.getString("type");

        //если сервер прислал список каналов
        if(stringOfFate.equals("channel_history")){
            Log.v("stringOfFate", stringOfFate);
            Log.v("Это история сообщений", "сообщения:");

            JSONObject newJsObject = MyJsonObject.getJSONObject("data");
            JSONArray newJsArray = newJsObject.getJSONArray("messages");

            //myListAdapter2.clear();

            for(int i=0; i < newJsArray.length(); i++) {
                JSONObject newJsObj2 = newJsArray.getJSONObject(i);

                user_name= newJsObj2.getString("user_name");
                user_message =newJsObj2.getString("text");
                Log.v(user_name, user_message);


                myListAdapter2.add(user_name + " : " + user_message);
                //myListAdapter2.

            }
        }else if(stringOfFate.equals("motd")){

        }else if(stringOfFate.equals("message")){

            //Log.v("stringOfFate", stringOfFate + "новое сообщение");
            //Log.v("Это новое сообщение", "сообщение:");

            JSONObject newJsObject = MyJsonObject.getJSONObject("data");
            //JSONArray newJsArray = newJsObject.getJSONArray("messages");

            //myListAdapter2.clear();

            //for(int i=0; i < newJsArray.length(); i++) {
                //JSONObject newJsObj2 = newJsObject.getJSONObject(i);

            user_name = newJsObject.getString("user_name");
            user_message = newJsObject.getString("text");

            Log.v(user_name, user_message);

            myListAdapter2.add(user_name + " : " + user_message);
            listView.smoothScrollToPosition(listView.getCount());
            if (listView.getCount() >5) {
                //myListAdapter2.re
            }

            //}
        }

    }
}
