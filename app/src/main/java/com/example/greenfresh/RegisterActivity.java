
package com.example.greenfresh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    // 1. Deklarasi variabel untuk komponen UI dan Firebase Authentication
    private EditText etEmail, etPassword, etConfirmPassword;
    private Button btnRegister;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // 2. Inisialisasi Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // 3. Hubungkan variabel dengan komponen di layout (XML)
        etEmail = findViewById(R.id.email_edit_text_register);
        etPassword = findViewById(R.id.password_edit_text_register);
        etConfirmPassword = findViewById(R.id.confirm_password_edit_text_register);
        btnRegister = findViewById(R.id.button_register_submit);

        // 4. Set OnClickListener untuk tombol register
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Panggil method untuk mendaftarkan pengguna
                registerUser();
            }
        });
    }

    private void registerUser() {
        // 5. Ambil data dari EditText
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // 6. Lakukan validasi input
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Masukkan alamat email!", Toast.LENGTH_SHORT).show();
            return; // Hentikan eksekusi jika email kosong
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Masukkan password!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password terlalu pendek, minimal 6 karakter!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(getApplicationContext(), "Password konfirmasi tidak cocok!", Toast.LENGTH_SHORT).show();
            return;
        }

        // 7. Jika semua validasi lolos, buat pengguna baru di Firebase
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // 8. Cek apakah proses register berhasil
                        if (task.isSuccessful()) {
                            // Jika berhasil
                            Toast.makeText(getApplicationContext(), "Registrasi berhasil!", Toast.LENGTH_SHORT).show();

                            // Pindah ke halaman Login
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);

                            // Tutup RegisterActivity agar pengguna tidak bisa kembali dengan tombol back
                            finish();
                        } else {
                            // Jika gagal, tampilkan pesan error dari Firebase
                            Toast.makeText(getApplicationContext(), "Registrasi Gagal: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}