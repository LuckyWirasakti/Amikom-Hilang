package com.temukangen.amikomhilang;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private EditText edtNim, edtPassword;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFirebaseAuth = FirebaseAuth.getInstance();
        edtNim = findViewById(R.id.edtNim);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nim = edtNim.getText().toString();
                String password = edtPassword.getText().toString();
                if(nim.isEmpty() && password.isEmpty()){
                    Toast.makeText(LoginActivity.this,"Field are empty", Toast.LENGTH_SHORT).show();
                } else {
                    mFirebaseAuth.signInWithEmailAndPassword(nim, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(LoginActivity.this, "Credentials doesn't match!", Toast.LENGTH_SHORT).show();
                            } else {
                                startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                                finish();
                            }
                        }
                    });

                }
            }
        });
    }

}
