package com.example.user.newproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main2Activity extends AppCompatActivity {


    //public static final String URL_m = "http://192.168.0.101/project/doctor_list.php";
   // private JSONArray result;
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);



    }


    private void getJSON(final String urlWebService) {

        class GetJSON extends AsyncTask<Void, Void, String> {



            @Override
            protected void onPostExecute(final String s) {
                super.onPostExecute(s);
                //textView.setText(s);

                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();

                try {
                    JSONArray jsonArray = new JSONArray(s);
                    final String[] heroes = new String[jsonArray.length()];

                    final StringBuffer stringBuffer=new StringBuffer();
                    String phone="";
                    String pass="";

                    Toast.makeText(Main2Activity.this,"hello", Toast.LENGTH_SHORT).show();





                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        pass=obj.getString("password");
                        phone=obj.getString("hospital_name");

                        Toast.makeText(Main2Activity.this, phone, Toast.LENGTH_SHORT).show();


                        /*if (phone.equals(user_phone)&&pass.equals(user_pass))
                        {

                            startActivity(new Intent(login.this,MapsActivity.class));
                        }*/



                        // stringBuffer.append("First Name: " + name + "  " + "Password: " + pass + "\n\n");


                    }

                    //loadIntoListView(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }


}
