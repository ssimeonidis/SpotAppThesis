package com.example.sql_connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.sql_connection.MainActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;


public abstract class communicator{
	MainActivity text;
	String returnString;
	public void getString() {
		
	
   InputStream is = null;
    
   String result = "";
    //the year data to send
 //   ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
   // nameValuePairs.add(new BasicNameValuePair("year","1970"));

    //http post
    try{
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpParams params = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(params, 500);
            httpclient.setParams(params);
            HttpPost httppost = new HttpPost("http://192.168.1.2/index.php");
            ResponseHandler<String> handler = new BasicResponseHandler();
            HttpResponse response = httpclient.execute(httppost);
           // String responseString = handler.handleResponse(response);
            //JSONObject json = new JSONObject(responseString);
           if ( response != null ){
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
           }
    } catch (ClientProtocolException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }

    //convert response to string
    try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
            }
            is.close();
            result=sb.toString();
    }catch(Exception e){
            Log.e("log_tag", "Error converting result "+e.toString());
    }
    //parse json data
    try{
            JSONArray jArray = new JSONArray(result);
        
            for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                 /*   Log.i("log_tag","id: "+json_data.getInt("id")+
                            ", name: "+json_data.getString("name")+
                            ", sex: "+json_data.getInt("sex")+
                            ", birthyear: "+json_data.getInt("birthyear")
                    );
                    
                   */ 
                    //Get an output to the screen
                    returnString += "\nevent: " + json_data.getInt("id") + "\n" +
                            "node: "+json_data.getString("node")+ "\n" +
                            "value: "+json_data.getInt("value")+ "\n" +
                            "date: "+json_data.getString("date")+ " \n ---------end of line---------"; 
                    
            }
            
    }catch(JSONException e){
            Log.e("log_tag", "Error parsing data "+e.toString());
    }
    text.txt.append(returnString);
   // return returnString;
	}

}
