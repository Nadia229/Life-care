package com.example.user.newproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class login extends AppCompatActivity {
    Button _loginButton;
    EditText _phone,_password;
    TextView sign1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        requestWindowFeature(Window.FEATURE_NO_TITLE); // for hiding title

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);



        sign1 = (TextView) findViewById(R.id.sign_up1);
        _phone=(EditText)findViewById(R.id.numberLogin);
        _password=(EditText)findViewById(R.id.passwordLogin);
        sign1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(login.this,register.class);
                startActivity(intent);
            }
        });
        _loginButton = (Button) findViewById(R.id.login);

        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getJSON("http://192.168.0.101/project/login.php");


               //startActivity(new Intent(login.this,MapsActivity.class));



            }
        });




    }
    private void getJSON(final String urlWebService) {

        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


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



                    String user_phone=_phone.getText().toString();
                    String user_pass=_password.getText().toString();


                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        pass=obj.getString("password");
                        phone=obj.getString("Contact_number");

                      //  Toast.makeText(login.this, phone, Toast.LENGTH_SHORT).show();


                        if (phone.equals(user_phone)&&pass.equals(user_pass))
                        {

                            startActivity(new Intent(login.this,MapsActivity.class));
                        }



                      // stringBuffer.append("First Name: " + name + "  " + "Password: " + pass + "\n\n");


                    }
                    if (user_phone.isEmpty()&&user_pass.isEmpty())
                    {
                        Toast.makeText(getApplicationContext(), "Give valid information", Toast.LENGTH_SHORT).show();

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
