package com.devsar.android.rxstoragesample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.lblHello);
        DevsarMyStorage storage = new DevsarMyStorage(this);
            storage.saveAccessToken("ejoqbf1ofu3h9247gbkuhi7wg3of83fo83g8g23gojgho")
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show())
                .andThen(storage.accessToken())
                .subscribe(
                        v -> textView.setText(v),
                        e -> textView.setText("Error! " + e.getMessage()));
    }
}
