package com.example.user.newproject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import androidbangladesh.bengali.support.BengaliUnicodeString;

import static android.view.Gravity.CENTER;
import static android.view.View.GONE;
import static com.example.user.newproject.MapsActivity.URL_m;

public class MainActivity extends AppCompatActivity {


    TextView hospitalname;
    TextView hospitalarea;
    TextView hospitalcountry;
    TextView hospitalcatagory;
    String text;
    TextView textView,test_textview;
    ArrayAdapter<String> itemsAdapter;
    List<String> list;
    Button button;
    public  String information="";
    public  String test_information="";
    public  String cabin_information="";
    public  String bed_information="";

    Animation animFadein;
  //  public static final String URL_m = "http://192.168.0.101/project/doctor.php";
    private JSONArray result;
    int test_count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // for hiding title

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);


        //  getJSON("http://192.168.0.101/project/doctor.php");

        // button=(Button)findViewById(R.id.btnShowDialog);

       /* button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getJSON("http://192.168.0.101/project/doctor.php");
              //  Toast.makeText(getApplicationContext(), "hello", Toast.LENGTH_SHORT).show();

            }
        });*/


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            hospitalname = (TextView) findViewById(R.id.textView);
            hospitalarea = (TextView) findViewById(R.id.textView2);

            hospitalcountry = (TextView) findViewById(R.id.textView3);
            hospitalcatagory = (TextView) findViewById(R.id.textView5);


            hospitalname.setText(extras.getString("hospital_name"));
            hospitalarea.setText(extras.getString("hospital_area"));
            hospitalcountry.setText(extras.getString("hospital_country"));
            hospitalcatagory.setText(extras.getString("hospital_catagory"));
            getJSON1("http://192.168.0.101/project/doctor.php");

             getJSON2("http://192.168.0.101/project/test.php");


            getJSON3("http://192.168.0.101/project/cabin.php");
            getJSON4("http://192.168.0.101/project/bed.php");




        }
     //   Toast.makeText(getApplicationContext(), hospitalarea.getText(), Toast.LENGTH_SHORT).show();

        /*Button mShowDialog = (Button) findViewById(R.id.btnShowDialog);
        mShowDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.popup_window, null);

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();

                dialog.show();


            }
        });*/
    }


    public void onButtonShowPopupWindowClick(View view) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup, null);

        TextView textView=popupView.findViewById(R.id.doctor);
        textView.setText("");


        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }


    // ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, heroes);
    //listView.setAdapter(arrayAdapter);



    private void getJSON1(final String urlWebService)
    {

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

                     StringBuffer stringBuffer=new StringBuffer();
                     String hospital_name="";
                     String d_firstname="";
                     String d_lastname="";
                     String d_contact_number="";
                     String catagory="";
                     String d_details="";
                     String d_email="";





                    for (int i = 0; i < jsonArray.length(); i++) {
                        // JSONObject obj = jsonArray.getJSONObject(i);
                        //   pass=obj.getString("password");
                        hospital_name = jsonArray.getJSONObject(i).getString("hospital_name");
                        d_firstname = jsonArray.getJSONObject(i).getString("first_name");
                        d_lastname = jsonArray.getJSONObject(i).getString("last_name");
                        d_contact_number = jsonArray.getJSONObject(i).getString("d_contact_number");
                        catagory = jsonArray.getJSONObject(i).getString("d_catagory");
                        d_details = jsonArray.getJSONObject(i).getString("details");
                        d_email = jsonArray.getJSONObject(i).getString("email");


                        //  Toast.makeText(getApplicationContext(),hospital_name, Toast.LENGTH_SHORT).show();


                        if (hospital_name.equals(hospitalname.getText())) {

                            information +=" "+d_firstname+d_lastname+"\n"+catagory+"\n"+d_contact_number+"\n"+d_email+"\n"+d_details+"\n\n";
                            //Toast.makeText(getApplicationContext(),d_firstname+d_lastname+"\n"+catagory+"\n"+d_contact_number+"\n"+d_email+"\n"+d_details, Toast.LENGTH_SHORT).show();



                        }

                    }   ImageView img = (ImageView) findViewById(R.id.imageView5);
                            img.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    LayoutInflater inflater = (LayoutInflater)
                                            getSystemService(LAYOUT_INFLATER_SERVICE);
                                    View popupView = inflater.inflate(R.layout.popup, null);

                                    textView=(TextView)popupView.findViewById(R.id.doctor);
                                    textView.setText(information);



                                    int width = LinearLayout.LayoutParams.MATCH_PARENT;
                                    int height = LinearLayout.LayoutParams.MATCH_PARENT;
                                    boolean focusable = true; // lets taps outside the popup also dismiss it
                                    final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
                                    //

                                    // show the popup window
                                    // which view you pass in doesn't matter, it is only used for the window tolken
                                    popupWindow.showAtLocation(popupView, Gravity.CENTER,0,0);

                                    // dismiss the popup window when touched
                                    popupView.setOnTouchListener(new View.OnTouchListener() {
                                        @Override
                                        public boolean onTouch(View v, MotionEvent event) {
                                            textView.setText("");
                                            popupWindow.dismiss();


                                            return true;
                                        }
                                    });


                                    //onButtonShowPopupWindowClick(view);

                                }
                            });


                            //  textView.setText(information + "\t");
                                // textView.setMovementMethod(new ScrollingMovementMethod());
                              /*  animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                                        R.anim.slide_down);
                                textView.clearAnimation();
                                textView.startAnimation(animFadein);
                                count++;
*/


                           // textView.getAdapter(d_firstname);











                        // stringBuffer.append("First Name: " + name + "  " + "Password: " + pass + "\n\n");
                  /*  Button mShowDialog = (Button) findViewById(R.id.btnShowDialog);
                    mShowDialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                         if (count==0) {


                             textView.setText(information + "\t");
                             // textView.setMovementMethod(new ScrollingMovementMethod());
                             animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                                     R.anim.slide_down);
                             textView.clearAnimation();
                             textView.startAnimation(animFadein);
                             count++;
                         }
                         else
                             {
                                 textView.setText("");
                                 count=0;

                             }
                        }
                    });*/










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



    private void getJSON2(final String urlWebService)
    {

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

                    StringBuffer stringBuffer=new StringBuffer();
                    String hospital_name="";
                    String h_test_name="";
                    String h_test_price="";
                    String d_test_duration="";






                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        // JSONObject obj = jsonArray.getJSONObject(i);
                        //   pass=obj.getString("password");
                        hospital_name=jsonArray.getJSONObject(i).getString("hospital_name");
                        h_test_name=jsonArray.getJSONObject(i).getString("test_name");
                        h_test_price=jsonArray.getJSONObject(i).getString("price");
                        d_test_duration=jsonArray.getJSONObject(i).getString("Test_duration");





                       // Toast.makeText(getApplicationContext(),hospital_name, Toast.LENGTH_SHORT).show();





                        if (hospital_name.equals(hospitalname.getText()))
                        {
                           // Toast.makeText(getApplicationContext(),h_test_name+h_test_price+"\n"+d_test_duration+"\n", Toast.LENGTH_SHORT).show();



                            test_textview=(TextView) findViewById(R.id.textView7);
                            test_information +="Test name: "+h_test_name+"\n"+"Price: "+h_test_price+"Tk"+"\n"+"Time duration: "+d_test_duration+" days"+"\n\n";



                            // textView.getAdapter(d_firstname);





                        }






                        // stringBuffer.append("First Name: " + name + "  " + "Password: " + pass + "\n\n");


                    }
                    ImageView test = (ImageView) findViewById(R.id.imageView6);

                    test.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            LayoutInflater inflater = (LayoutInflater)
                                    getSystemService(LAYOUT_INFLATER_SERVICE);
                            View popupView = inflater.inflate(R.layout.popup, null);

                            textView=(TextView)popupView.findViewById(R.id.doctor);
                            textView.setText(test_information);



                            int width = LinearLayout.LayoutParams.MATCH_PARENT;
                            int height = LinearLayout.LayoutParams.MATCH_PARENT;
                            boolean focusable = true; // lets taps outside the popup also dismiss it
                            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                            // show the popup window
                            // which view you pass in doesn't matter, it is only used for the window tolken
                            popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

                            // dismiss the popup window when touched
                            popupView.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    textView.setText("");
                                    popupWindow.dismiss();


                                    return true;
                                }
                            });




                        }
                    });










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
    private void getJSON3(final String urlWebService)
    {

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

                    StringBuffer stringBuffer = new StringBuffer();
                    String hospital_name = "";
                    String totalcabin = "";
                    String c_price = "";
                    String c_catagory = "";


                    for (int i = 0; i < jsonArray.length(); i++) {
                        // JSONObject obj = jsonArray.getJSONObject(i);
                        //   pass=obj.getString("password");
                        hospital_name = jsonArray.getJSONObject(i).getString("Hospital_name");
                        totalcabin = jsonArray.getJSONObject(i).getString("Total_cabin");
                        c_catagory = jsonArray.getJSONObject(i).getString("c_Catagory");
                        c_price = jsonArray.getJSONObject(i).getString("price");


                        //  Toast.makeText(getApplicationContext(),hospital_name, Toast.LENGTH_SHORT).show();


                        if (hospital_name.equals(hospitalname.getText())) {

                            cabin_information += "  Total cabin:" + totalcabin+"  "+"catagory: "+ c_catagory + "  " +"Price: "+ c_price + "\n";
                            //  Toast.makeText(getApplicationContext(),d_firstname+d_lastname+"\n"+catagory+"\n"+d_contact_number+"\n"+d_email+"\n"+d_details, Toast.LENGTH_SHORT).show();


                        }

                    }
                    TextView cabin_text=(TextView)findViewById(R.id.textView10);
                    cabin_text.setText(cabin_information);




                    //onButtonShowPopupWindowClick(view);








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
    private void getJSON4(final String urlWebService)
    {

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

                    StringBuffer stringBuffer = new StringBuffer();
                    String hospital_name = "";
                    String totalbed = "";
                    String b_price = "";
                    //Toast.makeText(getApplicationContext(),bed_information, Toast.LENGTH_SHORT).show();




                    for (int i = 0; i < jsonArray.length(); i++) {
                        // JSONObject obj = jsonArray.getJSONObject(i);
                        //   pass=obj.getString("password");
                        hospital_name = jsonArray.getJSONObject(i).getString("Hospital_name");
                        totalbed = jsonArray.getJSONObject(i).getString("total_bed");
                        b_price = jsonArray.getJSONObject(i).getString("b_price");


                        //  Toast.makeText(getApplicationContext(),hospital_name, Toast.LENGTH_SHORT).show();


                        if (hospital_name.equals(hospitalname.getText())) {

                            bed_information += "  Total bed:" + totalbed+"  "+"Price: "+ b_price +"per bed"+ "\n";
                            //  Toast.makeText(getApplicationContext(),d_firstname+d_lastname+"\n"+catagory+"\n"+d_contact_number+"\n"+d_email+"\n"+d_details, Toast.LENGTH_SHORT).show();
                          //  Toast.makeText(getApplicationContext(),bed_information, Toast.LENGTH_SHORT).show();


                        }

                    }
                    TextView bed_text=(TextView)findViewById(R.id.textView11);
                    bed_text.setText(bed_information);
                    Toast.makeText(getApplicationContext(),bed_information, Toast.LENGTH_SHORT).show();





                    //onButtonShowPopupWindowClick(view);








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
