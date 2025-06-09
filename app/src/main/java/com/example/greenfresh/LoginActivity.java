package com.example.greenfresh;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private FirebaseAuth mAuth;

    private GoogleSignInClient mGoogleSignInClient;
    private ActivityResultLauncher<Intent> mGoogleSignInLauncher;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        etEmail = findViewById(R.id.email_edit_text_login);
        etPassword = findViewById(R.id.password_edit_text_login);
        btnLogin = findViewById(R.id.button_login_submit);
        Button btnGoogleSignIn = findViewById(R.id.button_google_sign_in);

        createGoogleRequest();

        mGoogleSignInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        try {
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            firebaseAuthWithGoogle(account.getIdToken());
                        } catch (ApiException e) {
                            Log.w(TAG, "Google sign in failed", e);
                            Toast.makeText(LoginActivity.this, "Google Sign-In Gagal", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        btnGoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });
    }

    private void createGoogleRequest() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signInWithGoogle() {
        mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> {
            // Setelah sign out selesai, baru mulai intent untuk sign in
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            mGoogleSignInLauncher.launch(signInIntent);
        });
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            Toast.makeText(getApplicationContext(), "Login Google Berhasil!", Toast.LENGTH_SHORT).show();
                            navigateToHome();
                        } else {
                            // If sign in fails
                            Toast.makeText(LoginActivity.this, "Autentikasi Firebase Gagal.", Toast.LENGTH_SHORT).show();
                        }
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
                            navigateToHome();
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidUserException e) {
                                Toast.makeText(LoginActivity.this, "Email tidak terdaftar.", Toast.LENGTH_LONG).show();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                Toast.makeText(LoginActivity.this, "Password salah.", Toast.LENGTH_LONG).show();
                            } catch (FirebaseNetworkException e) {
                                Toast.makeText(LoginActivity.this, "Tidak ada koneksi internet.", Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(LoginActivity.this, "Login Gagal: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                Log.e(TAG, e.getMessage());
                            }
                        }
                    }
                });
    }

    private void navigateToHome() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finishAffinity();
    }
}