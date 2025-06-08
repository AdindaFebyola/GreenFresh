package com.example.greenfresh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        etEmail = findViewById(R.id.email_edit_text_login);
        etPassword = findViewById(R.id.password_edit_text_login);
        btnLogin = findViewById(R.id.button_login_submit);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Masukkan alamat email!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Masukkan password!", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Login Berhasil!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        } else {
                            // ---> INI BAGIAN YANG KITA UBAH <---
                            // Jika login gagal, kita cek jenis errornya
                            try {
                                // Lemparkan exception untuk ditangkap dan diidentifikasi
                                throw task.getException();
                            } catch (FirebaseAuthInvalidUserException e) {
                                // Error jika email tidak terdaftar
                                Toast.makeText(LoginActivity.this, "Email tidak terdaftar.", Toast.LENGTH_LONG).show();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                // Error jika password salah
                                Toast.makeText(LoginActivity.this, "Password salah.", Toast.LENGTH_LONG).show();
                            } catch (FirebaseNetworkException e) {
                                // Error jika tidak ada koneksi internet
                                Toast.makeText(LoginActivity.this, "Tidak ada koneksi internet.", Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                // Error umum lainnya
                                Toast.makeText(LoginActivity.this, "Login Gagal: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                // Log untuk developer melihat error asli
                                Log.e("LoginActivity", e.getMessage());
                            }
                        }
                    }
                });
    }
}