package com.example.sql_connection;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;



public class MainActivity extends Activity {
/** Called when the activity is first created. */
  
   TextView txt;
@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
   
    txt = (TextView) findViewById(R.id.txt);
   Button bt = (Button) findViewById(R.id.refresh);
bt.setOnClickListener(myhandler);
Button bt2 = (Button) findViewById(R.id.exit);
bt2.setOnClickListener(myhandler2);
txt.append("Press connect to start quering \n\n"); 
   
   
  
    // Create a crude view - this should really be set via the layout resources  
    // but since its an example saves declaring them in the XML.  
   // LinearLayout rootLayout = new LinearLayout(getApplicationContext());
  //  rootLayout.addView(txt);  
  // setContentView(rootLayout); 
    // Set the text and call the connect function.  
  //call the method to run the data retreival
    //getServerData(KEY_121);
   

   
   
}
View.OnClickListener myhandler2 = new View.OnClickListener() {
    public void onClick(View v) {
    	finish();
    }
};
View.OnClickListener myhandler = new View.OnClickListener() {
    public void onClick(View v) {
    	data();
    }
};
public void data(){
	 txt.setText("");
	   txt.append(getServerData(KEY_121));
}
public void finish(){
	super.finish();
}

public static final String KEY_121 = "http://83.212.107.16/values.php"; //i use my real ip here




private String getServerData(String returnString) {
	
   InputStream is = null;
    
   String result = "";
    //the year data to send
 //   ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
   // nameValuePairs.add(new BasicNameValuePair("year","1970"));

    //http post
   
    try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(KEY_121);
     //       httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();

    }catch(Exception e){
            Log.e("log_tag", "Error in http connection "+e.toString());
            txt.append("\n\n\nMust be connected to the Internet!\n\n");
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
            sb.setLength(0);
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
                   if (json_data.getInt("value")<40){
                	     returnString += "\nevent: " + json_data.getInt("id") + "\n" +
                                 "node: "+json_data.getString("node")+ "\n" +
                                 "value: "+json_data.getInt("value")+ "\n" +
                                 "date: "+json_data.getString("date")+ " \nWARNING!!!! ---------end of line---------"; 
                       
                                    }else{
                 
                   
                    //Get an output to the screen
                    returnString += "\nevent: " + json_data.getInt("id") + "\n" +
                            "node: "+json_data.getString("node")+ "\n" +
                            "value: "+json_data.getInt("value")+ "\n" +
                            "date: "+json_data.getString("date")+ " \n ---------end of line---------"; 
                  
                               }}
            
    }catch(JSONException e){
            Log.e("log_tag", "Error parsing data "+e.toString());
    }
    
   return returnString;
   
   }
}   
