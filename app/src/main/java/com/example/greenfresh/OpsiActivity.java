package com.example.greenfresh;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;

public class OpsiActivity extends AppCompatActivity {

    private Button buttonLogin;
    private TextView textRegister;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAuth.getInstance().signOut();
        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_opsi);

        buttonLogin = findViewById(R.id.button_login);
        textRegister = findViewById(R.id.text_register);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OpsiActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        textRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OpsiActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}