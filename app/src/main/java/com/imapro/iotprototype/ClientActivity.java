package com.imapro.iotprototype;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.muddzdev.styleabletoast.StyleableToast;

import java.io.IOException;
import java.util.UUID;

public class ClientActivity extends AppCompatActivity {

    private NumberPicker numberPicker;
    private RadioGroup radioGroup;
    private RadioButton radioButton;

    private RestApi restApi;

    private String users;

    private static final String URL = "http://194.95.20.141/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        radioGroup = findViewById(R.id.radio_group);
        numberPicker = findViewById(R.id.number_picker_rating);

        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(5);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        restApi = retrofit.create(RestApi.class);

        getUsers();
    }

    public void startClient(View v) {
        Mqtt5BlockingClient client = Mqtt5Client.builder()
                .identifier(UUID.randomUUID().toString())
                .serverHost("broker.mqttdashboard.com")
                .buildBlocking();

        client.connect();

        int rating = numberPicker.getValue();
        radioButton = findViewById(radioGroup.getCheckedRadioButtonId());
        int radioValue = radioGroup.indexOfChild(radioButton) + 1;

        if (radioValue == 0) {
            StyleableToast.makeText(this, "Please select an option", R.style.styleToast).show();
            return;
        }

        String message = "";
        message += rating + ";" + radioValue + ";" + users;

        client.publishWith().topic("mensaiot")
                .qos(MqttQos.AT_LEAST_ONCE)
                .payload(message.getBytes()).send();

        client.disconnect();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        StyleableToast.makeText(this, "Thanks for your rating!",
                Toast.LENGTH_LONG, R.style.endToast).show();
    }

    private void getUsers() {
        Call<ResponseBody> responseBodyCall = restApi.getUsers();
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    users = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ClientActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
