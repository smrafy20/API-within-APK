package com.example.apitest;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    TextView apiResponseTextView;
    Button fetchDataButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiResponseTextView = findViewById(R.id.apiResponseTextView);
        fetchDataButton = findViewById(R.id.fetchDataButton);

        fetchDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchApiData();
            }
        });
    }

    private void fetchApiData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url("https://jsonplaceholder.typicode.com/users/1")  //test API
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    final String responseData = response.body().string();

                    JSONObject jsonObject = new JSONObject(responseData);

                    final String formattedData = "Name: " + jsonObject.getString("name") +
                            "\nUsername: " + jsonObject.getString("username") +
                            "\nEmail: " + jsonObject.getString("email") +
                            "\nCity: " + jsonObject.getJSONObject("address").getString("city");

                    // Update the UI on the main thread
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            apiResponseTextView.setText(formattedData);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            apiResponseTextView.setText("Failed to fetch data");
                        }
                    });
                }
            }
        }).start();
    }
}
