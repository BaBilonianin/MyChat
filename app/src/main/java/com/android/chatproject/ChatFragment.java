package com.android.chatproject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketHandler;


/**
 * A placeholder fragment containing a simple view.
 */
public class ChatFragment extends Fragment {

    WebSocketConnection mConnection2;
    String channel_id;

    public ChatFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);

        channel_id =(String)getActivity().getIntent().getSerializableExtra("channel_id");
        connect();

        return rootView;

    }

    public void connect() {
        try {
            mConnection2 = new WebSocketConnection();
            mConnection2.connect("ws://chat.goodgame.ru:8081/chat/websocket", new WebSocketHandler() {
                @Override
                public void onOpen() {

                    System.out.println("--open");


                    try {
                        mConnection2.sendTextMessage(connetcToChannel().toString());
                        Log.v("connetcToChannel", connetcToChannel().toString());
                    }catch (JSONException e){
                        Log.v("123", e.toString());
                    }
                }

                @Override
                public void onTextMessage(String message) {
                    System.out.println("--received message: " + message);
//                    try {
//
//                        myParseJSON(message);
//
//                        myListAdapter.notifyDataSetChanged();
//
//
//
//                    }catch(JSONException e){
//                        Log.v("JS exepciot", e.toString());
//                    }

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
    private JSONObject connetcToChannel() throws JSONException {

        JSONObject newstringJSON = new JSONObject();
        JSONArray myArray1 = new JSONArray();
        JSONArray myArray2 = new JSONArray();

        JSONObject newJSObj1 = new JSONObject();
        JSONObject newJSObj2 = new JSONObject();

        newJSObj1.put("channel_id",channel_id);
        newJSObj1.put("hidden","false");


        //myArray1.put("channel_id",channel_id);
        //myArray1.put(newJSObj2);

        newstringJSON.put("type","join");
        newstringJSON.put("data",newJSObj1);

        return newstringJSON;
    }
//    private void myParseJSON(String s) throws JSONException{
//        JSONObject MyJsonObject = new JSONObject(s);
//
//        String stringOfFate = MyJsonObject.getString("type");
//
//        //если сервер прислал список каналов
//        if(stringOfFate.equals("channels_list")){
//            Log.v("stringOfFate", stringOfFate);
//            Log.v("Это список каналов", "каналы:");
//
//
//            JSONObject newJsObject = MyJsonObject.getJSONObject("data");
//            JSONArray newJsArray = newJsObject.getJSONArray("channels");
//            arrayForChannelId = new String[newJsArray.length()];
//
//            myListAdapter.clear();
//
//            for(int i=0; i < newJsArray.length(); i++) {
//                JSONObject newJsObj2 = newJsArray.getJSONObject(i);
//
//                channel_id = newJsObj2.getString("channel_id");
//                channel_name =newJsObj2.getString("channel_name");
//                clients_in_channel=newJsObj2.getString("clients_in_channel");
//                users_in_channel = newJsObj2.getDouble("users_in_channel");
//
//                arrayForChannelId[i]= channel_id;
//
//                myListAdapter.add("ID -" + channel_id  + "; "+ "Name -" + channel_name + "; "+"Clients -"+ "; "
//                        + clients_in_channel + "; "+"Users -" + users_in_channel);
//
//            }
//        }
//
//    }
}
