package com.example.anxinyang.helloworld;

/**
 * Created by Anxin Yang on 6/20/2016.
 */
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.security.PrivateKey;

import javax.net.ssl.HttpsURLConnection;
public class Connector extends AppCompatActivity {
    JSONObject token;
    JSONObject question;
    String Url;
    RequestQueue newQue;
    StringRequest stringRequest;
    JSONObject Response;
    private Connector(String url){
        this.Url=url;
    }
    private  Connector(Context context){
        token=null;
        question=null;
        newQue=Volley.newRequestQueue(context);

    }
    public static Connector getConnect(Context context){
        //return new Connector(url);
        return new Connector(context);
    }
    public static Connector getConnect(String url){
        return new Connector(url);
        // return new Connector();
    }
    public void requestToken(String user, String pass){
        request("http://sfsuswe.com/413/get_token/?username="+user+"&password="+pass);

    }
    public void requestQuestion(String token) {
        //request("http://sfsuswe.com/413/get_question/?token=" + token);
        request("http://sfsuswe.com/413/get_question/?token=5770eafbb7009");
    }
    private void request(String url){
        stringRequest=null;
        stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            //dToken=response
                            Response=new JSONObject(response);
                            token=null;
                            if(Response.has("token"))token=Response;

                            if(Response.has("question"))question=Response;

                        } catch (JSONException e) {
                            Response=null;
                            token=null;
                            question=null;
                        }
                        //waitResponse(1);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //dToken="Error: Connection";
            }
        });
        newQue.add(stringRequest);
    }
    public String getToken(){
        try{
            return this.token.getString("token");
        }catch (Exception e){
            return "Error:No Token Received.\n";
        }
    }
    public JSONObject getQuestion(){
        try {
            return this.question;
        }catch (Exception e){
            return null;
        }
    }
    public void reset(){

        token=null;
        question=null;
    }
}

