package com.example.user.newproject;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.view.Gravity.CENTER;

public class Ambulance_details extends AppCompatActivity {

    TextView Ambulancename;
    TextView Ambulancearea;
    TextView Ambulancecountry;
    TextView Ambulancecatagory;
    TextView Ambulanceprice;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambulance_details);






        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Ambulancename = (TextView) findViewById(R.id.textView);
            Ambulancearea = (TextView) findViewById(R.id.textView2);

            Ambulancecountry = (TextView) findViewById(R.id.textView3);
            Ambulancecatagory = (TextView) findViewById(R.id.textView10);
            Ambulanceprice = (TextView) findViewById(R.id.textView11);




            Ambulancename.setText(extras.getString("ambulance_name"));
            Ambulancearea.setText(extras.getString("ambulance_area"));
            Ambulancecountry.setText(extras.getString("ambulance_country"));
            Ambulancecatagory.setText(extras.getString("ambulance_catagory"));
            Ambulanceprice.setText(extras.getString("ambulance_price"));





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




}
