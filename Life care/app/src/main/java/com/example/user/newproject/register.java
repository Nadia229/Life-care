package com.example.user.newproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import java.util.logging.LogRecord;

public class register extends AppCompatActivity {

    // Creating EditText.
    EditText e_FirstName, e_LastName,e_blood,e_Contact_number,e_country,e_present_address,e_password,e_re_password ;

    // Creating button;
    Button InsertButton;

    // Creating Volley RequestQueue.
    RequestQueue requestQueue;

    // Create string variable to hold the EditText Value.
    String FirstNameHolder, LastNameHolder,BloodHolder,phoneHolder,countryHolder,adressHolder,passwordHolder,re_passwordHolder;

    // Creating Progress dialog.
    ProgressDialog progressDialog;

    // Storing server url into String variable.
    String HttpUrl = "http://192.168.0.101/project/insert.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); // for hiding title

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);



        // Assigning ID's to EditText.
        e_FirstName = (EditText) findViewById(R.id.firstname);
        e_LastName = (EditText) findViewById(R.id.lastName);
        e_blood = (EditText) findViewById(R.id.blood);
        e_Contact_number=(EditText) findViewById(R.id.MobileNumber);
        e_country=(EditText) findViewById(R.id.country);
        e_present_address=(EditText) findViewById(R.id.address);
        e_password=(EditText) findViewById(R.id.Password);
        e_re_password=(EditText) findViewById(R.id.RePassword);



        // Assigning ID's to Button.
        InsertButton = (Button) findViewById(R.id.btn_confirm);


        // Creating Volley newRequestQueue .
        requestQueue = Volley.newRequestQueue(register.this);

        progressDialog = new ProgressDialog(register.this);

        // Adding click listener to button.
        InsertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(register.this,login.class));
                final ProgressDialog progress = new ProgressDialog(register.this);
                progress.setTitle("Loading");
              //  progress.setMessage("Welcome");
                progress.show();

                Runnable progressRunnable = new Runnable() {

                    @Override
                    public void run() {
                        progress.cancel();
                    }
                };

                Handler pdCanceller = new Handler();
                pdCanceller.postDelayed(progressRunnable, 10000000);
                // Showing progress dialog at user registration time.
                /*progressDialog.setMessage("Please Wait, We are Inserting Your Data on Server");
                progressDialog.show();
                Handler pdCanceller = new Handler() {
                    @Override
                    public void publish(LogRecord record) {

                    }

                    @Override
                    public void flush() {

                    }

                    @Override
                    public void close() throws SecurityException {

                    }
                };
                pdCanceller.postDelayed(progressDialog, 3000);*/

                // Calling method to get value from EditText.
                //GetValueFromEditText();
                FirstNameHolder = e_FirstName.getText().toString().trim();
                LastNameHolder = e_LastName.getText().toString().trim();
                BloodHolder = e_blood.getText().toString().trim();
                phoneHolder=e_Contact_number.getText().toString().trim();
                countryHolder=e_country.getText().toString().trim();
                adressHolder=e_present_address.getText().toString().trim();
                passwordHolder=e_password.getText().toString().trim();
                re_passwordHolder=e_re_password.getText().toString().trim();



                // Creating string request with post method.
                StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String ServerResponse) {

                                // Hiding the progress dialog after all task complete.
                                progressDialog.dismiss();

                                // Showing response message coming from server.
                                Toast.makeText(register.this, ServerResponse, Toast.LENGTH_LONG).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {

                                // Hiding the progress dialog after all task complete.
                                progressDialog.dismiss();

                                // Showing error message if something goes wrong.
                                Toast.makeText(register.this, volleyError.toString(), Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {

                        // Creating Map String Params.
                        Map<String, String> params = new HashMap<String, String>();

                        // Adding All values to Params.
                        params.put("first_name", FirstNameHolder);
                        params.put("last_name", LastNameHolder);
                        params.put("blood_group", BloodHolder);
                        params.put("contact_number", phoneHolder);
                        params.put("country", countryHolder);
                        params.put("present_address", adressHolder);
                        params.put("password", passwordHolder);
                        params.put("re_password", re_passwordHolder);

                        return params;
                    }

                };

                // Creating RequestQueue.
                RequestQueue requestQueue = Volley.newRequestQueue(register.this);

                // Adding the StringRequest object into requestQueue.
                requestQueue.add(stringRequest);



            }


        });

    }
}
